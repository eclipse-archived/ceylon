package com.sun.tools.javac.comp;

import static com.sun.tools.javac.code.TypeTags.CLASS;

import com.sun.tools.javac.ceylon.ExtensionFinder.Route;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type.ArrayType;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticPosition;

public class LowerCeylon extends TreeTranslator {
    protected static final Context.Key<LowerCeylon> lowerKey =
        new Context.Key<LowerCeylon>();

    public static LowerCeylon instance(Context context) {
        LowerCeylon instance = context.get(lowerKey);
        if (instance == null)
            instance = new LowerCeylon(context);
        return instance;
    }

    private Name.Table names;
    private Log log;
    private Symtab syms;
    private Resolve rs;
    private Check chk;
    private Attr attr;
    private TreeMaker make;
    private DiagnosticPosition make_pos;
    private ClassWriter writer;
    private ClassReader reader;
    private ConstFold cfolder;
    private Target target;
    private Source source;
    private boolean allowEnums;
    private final Name dollarAssertionsDisabled;
    private final Name classDollar;
    private Types types;
    private boolean debugLower;

    protected LowerCeylon(Context context) {
        context.put(lowerKey, this);
        names = Name.Table.instance(context);
        log = Log.instance(context);
        syms = Symtab.instance(context);
        rs = Resolve.instance(context);
        chk = Check.instance(context);
        attr = Attr.instance(context);
        make = TreeMaker.instance(context);
        writer = ClassWriter.instance(context);
        reader = ClassReader.instance(context);
        cfolder = ConstFold.instance(context);
        target = Target.instance(context);
        source = Source.instance(context);
        allowEnums = source.allowEnums();
        dollarAssertionsDisabled = names.
            fromString(target.syntheticNameChar() + "assertionsDisabled");
        classDollar = names.
            fromString("class" + target.syntheticNameChar());

        types = Types.instance(context);
        Options options = Options.instance(context);
        debugLower = options.get("debuglower") != null;
    }

    public void visitVarDef(JCVariableDecl tree) {
        tree.mods = translate(tree.mods);
        tree.vartype = translate(tree.vartype);
        tree.init = translate(tree.init, tree.type);
        result = tree;
    }

    public void visitAssign(JCAssign tree) {
        tree.lhs = translate(tree.lhs);
        tree.rhs = translate(tree.rhs, tree.type);
        result = tree;
    }

    public void visitApply(JCMethodInvocation tree) {
        tree.meth = translate(tree.meth);
        tree.args = translate(tree.args);

        Symbol meth = TreeInfo.symbol(tree.meth);
        List<Type> argtypes = meth.type.getParameterTypes();
        if (allowEnums &&
            meth.name==names.init &&
            meth.owner == syms.enumSym)
            argtypes = argtypes.tail.tail;
        tree.args = lowerArgs(argtypes, tree.args, tree.varargsElement);

        if (meth.name.toString().equals("$internalErasedExists")) {
            if (tree.meth.getTag() == JCTree.SELECT) {
                JCExpression selected = ((JCFieldAccess) tree.meth).selected;
                if (tree.args.length() == 0) {
                    result = selected;
                    return;
                }
            }
        }

        result = tree;
    }

    public JCTree translateTopLevelClass(JCTree tree, TreeMaker localMake) {
        return translate(tree);
    }

    public <T extends JCTree> T translate(T tree, Type type) {
        tree = translate(tree);
        if (tree != null) tree =
            (T)ceylonExtensionIfNeeded((JCExpression)tree, type);
        return tree;
    }

    public void visitTypeCast(JCTypeCast tree) {
        // Convert (Nothing)null to just null
        tree.clazz = translate(tree.clazz);
        tree.expr = translate(tree.expr);
        if (tree.expr.type.tag == TypeTags.BOT
                && tree.type == syms.ceylonNothingType)
            result = tree.expr;
        else
            result = tree;
    }

    /** Convert using a Ceylon extension if needed */
    JCExpression ceylonExtensionIfNeeded(JCExpression tree, Type dstType) {
        Type srcType = tree.type;
        if (srcType.isPrimitive())
            return tree;

        srcType = nonOptionalTypeFor(srcType);
        dstType = nonOptionalTypeFor(dstType);

        Route route = types.getCeylonExtension(srcType, dstType);
        if (route != null) {
            make.at(tree.pos());
            tree = route.apply(tree, make) ;
            return ceylonExtensionIfNeeded(tree, dstType);
        }

        // Handle conversions to and from Mutable.
        // First we strip Mutable from the source by applying get().

        // Mutable -> immutable conversion
        if (srcType.tsym == syms.ceylonMutableType.tsym &&
                dstType.tsym != syms.ceylonMutableType.tsym) {
            // Immutable -> mutable conversion
            List<Type> l = srcType.getTypeArguments();
            if (l.length() == 1) {
                Type t1 = l.last();
                if (t1.tag == CLASS) {
                    Scope scope = ((ClassSymbol) srcType.tsym).members();
                    Scope.Entry entry = scope.lookup(names.fromString("get"));
                    Symbol sym = entry.sym;
                    MethodSymbol msym = (MethodSymbol) sym;
                    tree = make.Apply(null,
                            make.Select(tree, msym), List.<JCExpression>nil()).setType(t1);
                    return ceylonExtensionIfNeeded(tree, dstType);
                }
            }
        }

        // Immutable -> mutable conversion
        if (dstType.tsym == syms.ceylonMutableType.tsym &&
                srcType.tsym != syms.ceylonMutableType.tsym) {
            List<Type> l = dstType.getTypeArguments();
            if (l.length() == 1) {
                Type t1 = l.last();
                if (t1.tag == CLASS) {
                    Scope scope = ((ClassSymbol) dstType.tsym).members();
                    Scope.Entry entry = scope.lookup(names.fromString("of"));
                    Symbol sym = entry.sym;
                    tree = make.Apply(null, make.Select(make.Type(types.erasure(dstType)), sym),
                            List.of(tree));
                    Type newType = new ClassType(Type.noType, List.of(srcType), dstType.tsym);
                    tree.setType(newType);
                    return ceylonExtensionIfNeeded(tree, dstType);
                }
            }
        }

        return tree;
    }

    private Type nonOptionalTypeFor(Type t) {
        // We need to compare symbols (tsym) here rather
        // than directly comparing types because t has type
        // parameters and syms.ceylonOptionalType does not.
        while (t.tsym == syms.ceylonOptionalType.tsym) {
            List<Type> l = t.getTypeArguments();
            assert l.length() == 1;
            t = l.last();
        }
        return t;
    }

    List<JCExpression> lowerArgs(List<Type> parameters, List<JCExpression> _args, Type varargsElement) {
        List<JCExpression> args = _args;
        if (parameters.isEmpty()) return args;
        boolean anyChanges = false;
        ListBuffer<JCExpression> result = new ListBuffer<JCExpression>();
        while (parameters.tail.nonEmpty()) {
            JCExpression arg = translate(args.head, parameters.head);
            anyChanges |= (arg != args.head);
            result.append(arg);
            args = args.tail;
            parameters = parameters.tail;
        }
        Type parameter = parameters.head;
        if (varargsElement != null) {
            anyChanges = true;
            ListBuffer<JCExpression> elems = new ListBuffer<JCExpression>();
            while (args.nonEmpty()) {
                JCExpression arg = translate(args.head, varargsElement);
                elems.append(arg);
                args = args.tail;
            }
            JCNewArray boxedArgs = make.NewArray(make.Type(varargsElement),
                                               List.<JCExpression>nil(),
                                               elems.toList());
            boxedArgs.type = new ArrayType(varargsElement, syms.arrayClass);
            result.append(boxedArgs);
        } else {
            if (args.length() != 1) throw new AssertionError(args);
            JCExpression arg = translate(args.head, parameter);
            anyChanges |= (arg != args.head);
            result.append(arg);
            if (!anyChanges) return _args;
        }
        return result.toList();
    }

}

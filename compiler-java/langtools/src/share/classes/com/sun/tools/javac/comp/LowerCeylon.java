package com.sun.tools.javac.comp;

import static com.sun.tools.javac.code.TypeTags.CLASS;

import java.util.ArrayList;

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
import com.sun.tools.javac.code.Type.MethodType;
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
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
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
    private Symtab syms;
    private TreeMaker make;
    private Source source;
    private boolean allowEnums;
    private Types types;

    private ArrayList<JCMethodDecl> methodStack = new ArrayList<JCMethodDecl>();

    protected LowerCeylon(Context context) {
        context.put(lowerKey, this);
        names = Name.Table.instance(context);
        syms = Symtab.instance(context);
        make = TreeMaker.instance(context);
        source = Source.instance(context);
        allowEnums = source.allowEnums();
        types = Types.instance(context);
    }

    private JCMethodDecl getCurrentMethod() {
        return methodStack.get(methodStack.size() - 1);
    }

    private void pushMethod(JCMethodDecl m) {
        methodStack.add(m);
    }

    public JCMethodDecl popMethod() {
        return methodStack.remove(methodStack.size() - 1);
    }


    public void visitVarDef(JCVariableDecl tree) {
        tree.mods = translate(tree.mods);
        tree.vartype = translate(tree.vartype);
        if (tree.name.toString().startsWith("$ceylontmpDeleted"))
            // We don't want the initializer expression: we were only using it
            // to provide the type of the variable.
            tree.init = null;
        else
            tree.init = translate(tree.init, tree.type);
        result = tree;
    }

     public void visitMethodDef(JCMethodDecl tree) {
         try {
             pushMethod(tree);
             if (tree.type.toString().contains("Optional"))
                 System.err.print("");
             tree.mods = translate(tree.mods);
             tree.restype = translate(tree.restype);
             tree.typarams = translateTypeParams(tree.typarams);
             tree.params = translateVarDefs(tree.params);
             tree.thrown = translate(tree.thrown);
             tree.body = translate(tree.body);
         } finally {
             popMethod();
         }
         result = tree;
    }

    public void visitTypeApply(JCTypeApply tree) {
        tree.clazz = translate(tree.clazz);
        tree.arguments = translate(tree.arguments);
        result = tree;
    }

    public void visitAssign(JCAssign tree) {
        tree.lhs = translate(tree.lhs);
        tree.rhs = translate(tree.rhs, tree.type);
        result = tree;
    }

    public void visitIf(JCIf tree) {
        tree.cond = translate(tree.cond, syms.booleanType);
        tree.thenpart = translate(tree.thenpart);
        tree.elsepart = translate(tree.elsepart);
        result = tree;
    }

    public void visitConditional(JCConditional tree) {
        tree.cond = translate(tree.cond, syms.booleanType);
        tree.truepart = translate(tree.truepart);
        tree.falsepart = translate(tree.falsepart);
        result = tree;
    }

    public void visitForLoop(JCForLoop tree) {
        tree.init = translate(tree.init);
        tree.cond = translate(tree.cond, syms.booleanType);
        tree.step = translate(tree.step);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitWhileLoop(JCWhileLoop tree) {
        tree.cond = translate(tree.cond, syms.booleanType);
        tree.body = translate(tree.body);
        result = tree;
    }

    public void visitApply(JCMethodInvocation tree) {
        tree.meth = translate(tree.meth);
        tree.args = translate(tree.args);

        Symbol meth = TreeInfo.symbol(tree.meth);
        List<Type> argtypes = tree.meth.type.getParameterTypes();
        if (allowEnums &&
            meth.name==names.init &&
            meth.owner == syms.enumSym)
            argtypes = argtypes.tail.tail;
        tree.args = lowerArgs(argtypes, tree.args, tree.varargsElement);
        // Now that we have lowered all varargs, we must set varargsElement null
        // or Lower will do it again.
        tree.varargsElement = null;

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

    public void visitReturn(JCReturn tree) {
        JCMethodDecl currentMethod = getCurrentMethod();
        tree.expr = translate(tree.expr, ((MethodType)currentMethod.type).restype);
        result = tree;
    }

    public JCTree translateTopLevelClass(JCTree tree, TreeMaker localMake) {
        return translate(tree);
    }

    public void visitNewClass(JCNewClass tree) {
        tree.encl = translate(tree.encl);
        tree.clazz = translate(tree.clazz);

        Symbol meth = tree.constructor;
        List<Type> argtypes = meth.type.getParameterTypes();

        tree.args = translate(tree.args);
        tree.def = translate(tree.def);
        tree.args = lowerArgs(argtypes, tree.args, tree.varargsElement);
        // Now that we have lowered all varargs, we must set varargsElement null
        // or Lower will do it again.
        tree.varargsElement = null;

        result = tree;
    }

    public <T extends JCTree> T translate(T tree, Type type) {
        tree = translate(tree);
/* Disabled (stef) because it threw an NPE when @Name was on a param for some reason
        if (tree != null) tree =
            (T)ceylonExtensionIfNeeded((JCExpression)tree, type);
*/
        return tree;
    }

    public void visitTypeCast(JCTypeCast tree) {
        // Convert (Nothing)null to just null
        tree.clazz = translate(tree.clazz);
        tree.expr = translate(tree.expr);
        tree.expr = ceylonExtensionIfNeeded(tree.expr, tree.clazz.type);

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

        Route route = types.getCeylonExtension(srcType, dstType);
        if (route != null) {
            make.at(tree.pos());
            tree = route.apply(tree, make) ;
            return ceylonExtensionIfNeeded(tree, dstType);
        }

        return tree;
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

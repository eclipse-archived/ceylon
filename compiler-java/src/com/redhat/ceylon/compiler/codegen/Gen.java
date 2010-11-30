package com.redhat.ceylon.compiler.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.*;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.TypeTags.*;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTool;
import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.CeylonTree.*;

public class Gen {
    Context context;
    TreeMaker make;
    Name.Table names;
    ClassReader reader;
    Resolve resolve;
    JavaCompiler compiler;
    DiagnosticCollector<JavaFileObject> diagnostics;
    CeyloncFileManager fileManager;
    JavacTaskImpl task;
    Options options;
    private LineMap map;
    Symtab syms;

    JCCompilationUnit jcCompilationUnit;

    public Gen() throws Exception {
        compiler = new CeyloncTool();
        // compiler = ToolProvider.getSystemJavaCompiler();

        diagnostics =
            new DiagnosticCollector<JavaFileObject>();
        fileManager
            = (CeyloncFileManager)compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(new File("/tmp")));

        fileManager.setLocation(StandardLocation.CLASS_PATH,
                Arrays.asList(new File("/tmp"), new File(System.getProperty("user.dir") + "/runtime")));
        Iterable<? extends JavaFileObject> compilationUnits
        = fileManager.getJavaFileObjectsFromStrings(new ArrayList<String>());

        JavaCompiler.CompilationTask aTask
        = compiler.getTask(null, fileManager,
                diagnostics,
                Arrays.asList("-g"/* , /* "-verbose", */
                        // "-source", "7", "-XDallowFunctionTypes"
                ),
                null, compilationUnits);
        setup((JavacTaskImpl)aTask);
    }

    void setup (JavacTaskImpl task) {
        this.task = task;
        setup (task.getContext());
    }

    void setup (Context context) {
        this.context = context;
        options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
        make = TreeMaker.instance(context);
        Class<?>[] interfaces = {JCTree.Factory.class};

        names = Name.Table.instance(context);
        reader = ClassReader.instance(context);
        resolve = Resolve.instance(context);
        syms = Symtab.instance(context);

        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
    }


    public Gen(Context context) {
        setup(context);
    }

   JCTree.Factory at(CeylonTree t) {
        if (t.source != null) {
            make.at(getMap().getStartPosition(t.source.line) + t.source.column);
        }
        return make;
    }

    JCTree.Factory make() {
        return make;
    }

    class Singleton<T> implements Iterable<T>{
        private T thing;
        Singleton() { }
        Singleton(T t) { thing = t; }
        List<T> asList() { return List.of(thing); }
        void append(T t) {
            if (thing != null)
                throw new RuntimeException();
            thing = t;
        }
        public Iterator<T> iterator() {
            return asList().iterator();
        }
        public T thing() { return this.thing; }
        public String toString() {
            return thing.toString();
        }
    }

    JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return make().Select(s1, names.fromString(s2));
    }

    JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(make().Ident(names.fromString(s1)), s2);
    }

    JCFieldAccess makeSelect(String s1, String s2, String... rest) {
        return makeSelect(makeSelect(s1, s2), rest);
    }

    JCFieldAccess makeSelect(JCFieldAccess s1, String[] rest) {
        JCFieldAccess acc = s1;

        for (String s : rest)
            acc = makeSelect(acc, s);

        return acc;
    }

    // Make a name from a list of strings, using only the first component.
    Name makeName(Iterable<String> components) {
        Iterator<String> iterator = components.iterator();
        String s = iterator.next();
        assert(!iterator.hasNext());
        return names.fromString(s);
    }

    String toFlatName(Iterable<String> components) {
        StringBuffer buf = new StringBuffer();
        Iterator<String> iterator;

        for (iterator = components.iterator();
            iterator.hasNext();) {
            buf.append(iterator.next());
            if (iterator.hasNext())
                buf.append('.');
        }

        return buf.toString();
    }

    public JCExpression makeIdent(Iterable<String> components) {

        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make().Ident(names.fromString(component));
            else
                type = make().Select(type, names.fromString(component));
        }

        return type;
    }

    private JCExpression makeIdent(String nameAsString) {
        return makeIdent(List.of(nameAsString));
    }

    private JCExpression makeIdent(com.sun.tools.javac.code.Type type) {
        return make.QualIdent(type.tsym);
    }

    public void run(CeylonTree.CompilationUnit t) throws IOException {

        JCCompilationUnit tree = convert(t);

        System.out.println(tree);

        try {
            Context.SourceLanguage.push(Language.CEYLON);

            Iterable<? extends TypeElement> result =
                task.enter(List.of(tree));
            /*Iterable<? extends JavaFileObject> files =*/ task.generate(result);

            System.out.println(diagnostics.getDiagnostics());

        } finally {
            Context.SourceLanguage.pop();
        }
    }

    public JCCompilationUnit convert(CeylonTree.CompilationUnit t) {
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();

        t.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.ImportDeclaration imp) {
                defs.appendList(convert(imp));
            }
            public void visit(CeylonTree.ClassDeclaration decl) {
                defs.append(convert(decl));
            }
            public void visit(CeylonTree.InterfaceDeclaration decl) {
                defs.append(convert(decl));
            }
            public void visit(CeylonTree.MethodDeclaration decl) {
                methodClass(null, decl, defs, true);
            }
        });

        // Mark overloaded top-level classes and make their names unique
        JCClassDecl rootClass = null;
        int overloadCount = 0;
        for (JCTree def : defs) {
            if (def.getKind() == Kind.CLASS) {
                JCClassDecl classDef = (JCClassDecl) def;
                if (rootClass == null) {
                    rootClass = classDef;
                }
                else if (classDef.name.equals(rootClass.name)) {
                    classDef.name = classDef.name.append(
                        names.fromString("$$overload" + new Integer(++overloadCount).toString()));
                    classDef.isOverloadedToplevelCeylonClass = true;
                }
            }
        }

        String prefix = fileManager.getSourcePath();
        JCExpression pkg = null;

        // Figure out the package name by stripping the "-src" prefix and extracting
        // the package part of the fullname.
        if (prefix != null && t.file.toString().startsWith(prefix)) {
            String fullname = t.file.toString().substring(prefix.length());
            assert fullname.endsWith(".ceylon");
            fullname = fullname.substring(0, fullname.length() - ".ceylon".length());
            fullname = fullname.replace(File.separator, ".");
            String packageName = Convert.packagePart(fullname);
            if (! packageName.equals(""))
                pkg = getPackage(packageName);
        }

        JCCompilationUnit topLev =
            at(t).TopLevel(List.<JCTree.JCAnnotation>nil(),
                    pkg, defs.toList());

        topLev.lineMap = getMap();
        topLev.sourcefile = t.file;
        topLev.isCeylonProgram = true;

        // System.out.println(topLev);
        return topLev;
    }

    private JCExpression getPackage(String fullname) {
        String shortName = Convert.shortName(fullname);
        String packagePart = Convert.packagePart(fullname);
        if (packagePart == null || packagePart.length() == 0)
            return make.Ident(names.fromString(shortName));
        else
            return make.Select(getPackage(packagePart), names.fromString(shortName));
    }

    public void methodClass(CeylonTree.ClassOrInterfaceDeclaration classDecl,
            CeylonTree.MethodDeclaration decl, final ListBuffer<JCTree> defs,
            boolean topLevel) {
        // Generate a class with the
        // name of the method and a corresponding run() method.

        final ListBuffer<JCVariableDecl> params =
            new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCStatement> annotations =
            new ListBuffer<JCStatement>();
        final Singleton<JCBlock> body =
            new Singleton<JCBlock>();
        Singleton<JCExpression> restype =
            new Singleton<JCExpression>();
        final ListBuffer<JCAnnotation> langAnnotations =
            new ListBuffer<JCAnnotation>();
        final ListBuffer<JCTypeParameter> typeParams =
            new ListBuffer<JCTypeParameter>();

        processMethodDeclaration(classDecl, decl, params, body, restype, typeParams,
                annotations, langAnnotations);

        JCMethodDecl meth = at(decl).MethodDef(make.Modifiers((topLevel ? PUBLIC|STATIC : 0)),
                names.fromString("run"),
                restype.thing(),
                processTypeConstraints(decl.typeConstraintList(), typeParams.toList()),
                params.toList(),
                List.<JCExpression>nil(), body.thing(), null);


        List<JCTree> innerDefs = List.<JCTree>of(meth);

        // FIXME: This is wrong because the annotation registration is done
        // within the scope of the class, but the annotations are lexically
        // outside it.
        if (annotations.length() > 0) {
            innerDefs = innerDefs.append(registerAnnotations(annotations.toList()));
        }

        // Try and find a class to insert this method into
        JCClassDecl classDef = null;
        for (JCTree def : defs) {
            if (def.getKind() == Kind.CLASS) {
                classDef = (JCClassDecl) def;
                break;
            }
        }

        String name;
        if (topLevel)
            name = decl.nameAsString();
        else
            name = tempName();

        // No class has been made yet so make one
        if (classDef == null) {
            classDef = at(decl).ClassDef(
                at(decl).Modifiers((topLevel ? PUBLIC : 0), List.<JCAnnotation>nil()),
                names.fromString(name),
                List.<JCTypeParameter>nil(),
                makeIdent(syms.ceylonObjectType),
                List.<JCExpression>nil(),
                List.<JCTree>nil());

            defs.append(classDef);
        }

        classDef.defs = classDef.defs.appendList(innerDefs);
    }

    // FIXME: There must be a better way to do this.
    void processMethodDeclaration(final CeylonTree.ClassOrInterfaceDeclaration classDecl,
            final CeylonTree.BaseMethodDeclaration decl,
            final ListBuffer<JCVariableDecl> params,
            final Singleton<JCBlock> block,
            final Singleton<JCExpression> restype,
            final ListBuffer<JCTypeParameter> typeParams,
            final ListBuffer<JCStatement> annotations,
            final ListBuffer<JCAnnotation> langAnnotations) {

        for (CeylonTree.FormalParameter param: decl.params) {
            params.append(convert(param));
        }

        if (decl.typeParameters() != null)
            for (CeylonTree t: decl.typeParameters()) {
                // FIXME: Nasty cast here.  Should be a visitor
                typeParams.append(convert((CeylonTree.TypeParameter)t));
            }

        if (decl.stmts != null)
            for (CeylonTree stmt: decl.stmts)
                stmt.accept(new CeylonTree.Visitor () {
                    public void visit(CeylonTree.Block b) {
                        block.thing = convert(classDecl, b);
                    }
                });

        processAnnotations(classDecl, decl.annotations, annotations, langAnnotations, decl.nameAsString());

        restype.append(convert(decl.returnType));
     }

    JCExpression convert(CeylonTree.Type type) {
        JCExpression result;

        if (type.sequenceCount > 0) {
            type.sequenceCount--;
            result = convert(type);
            result = at(type).TypeApply(makeIdent(syms.ceylonSequenceType),
                    List.<JCExpression>of(result));
        } else {
            ExpressionVisitor v =
                new ExpressionVisitor() {

                public void visit(CeylonTree.Type t) {
                    result = makeIdent(t.name().components());

                    CeylonTree.TypeArgumentList tal = t.getTypeArgumentList();
                    if (tal != null) {
                        ListBuffer<JCExpression> typeArgs =
                            new ListBuffer<JCExpression>();

                        for (CeylonTree.Type innerType: tal.types()) {
                            typeArgs.add(convert(innerType));
                        }

                        result = at(t).TypeApply(result, typeArgs.toList());
                    }
                }

                // FIXME: Add the other primitive types
                public void visit(CeylonTree.Void v) {
                    result = make.TypeIdent(VOID);
                }
            };

            type.accept(v);
            result = v.result;

            if ((type.flags & CeylonTree.OPTIONAL) != 0) {
                result = optionalType(result);
            }
            if ((type.flags & CeylonTree.MUTABLE) != 0) {
                result = mutableType(result);
            }
        }

        return result;
    }

    List<JCTree> convert (CeylonTree.ImportDeclaration imp) {
        ListBuffer<JCTree> imports = new ListBuffer<JCTree>();
        for (CeylonTree pathElement: imp.path()) {
            CeylonTree.ImportPath ip = (CeylonTree.ImportPath) pathElement;
            JCImport stmt = at(imp).Import(makeIdent(ip.pathElements), false);
            imports.append(stmt);
        }
        return imports.toList();
    }

    void processAnnotations(final CeylonTree.ClassOrInterfaceDeclaration classDecl,
            List<CeylonTree.Annotation> ceylonAnnos,
            final ListBuffer<JCStatement> annotations,
            final ListBuffer<JCAnnotation> langAnnotations,
            final String declName) {

        class V extends CeylonTree.Visitor {
            public void visit(CeylonTree.UserAnnotation userAnn) {
                annotations.append(at(userAnn).Exec(convert(userAnn, classDecl, declName)));
            }
            // FIXME: Shouldn't be here
            public void visit(CeylonTree.LanguageAnnotation langAnn) {
                langAnn.kind.accept(this);
            }
        }
        V v = new V();

        if (ceylonAnnos != null)
            for (CeylonTree.Annotation a: ceylonAnnos)
                a.accept(v);
    }

    JCBlock registerAnnotations(List<JCStatement> annos) {
        JCBlock block = make().Block(Flags.STATIC, annos);
        return block;
    }

    class ExpressionVisitor extends CeylonTree.Visitor {
        public JCExpression result;
    }

    class ListVisitor<T> extends CeylonTree.Visitor {
        public List<T> result = List.<T>nil();
    }

    JCExpression convert(final CeylonTree.UserAnnotation userAnn,
            CeylonTree.ClassOrInterfaceDeclaration classDecl, String methodName) {
       List<JCExpression> values = List.<JCExpression>nil();
        for (CeylonTree expr: userAnn.values()) {
            values = values.append(convertExpression(expr));
        }

        JCExpression classLiteral;
        if (classDecl != null) {
            classLiteral = makeSelect(classDecl.nameAsString(), "class");
        } else {
            classLiteral = makeSelect(methodName, "class");
        }

        JCExpression result = at(userAnn).Apply(null, makeSelect(userAnn.name, "run"),
                values);
        JCIdent addAnnotation = at(userAnn).Ident(names.fromString("addAnnotation"));
        List<JCExpression> args;
        if (methodName != null)
            args = List.<JCExpression>of(classLiteral, ceylonLiteral(methodName), result);
        else
            args = List.<JCExpression>of(classLiteral, result);

        result = at(userAnn).Apply(null, addAnnotation, args);

        return result;
    }

    JCExpression convert(CeylonTree.ReflectedLiteral value) {

        ListVisitor<String> v = new ListVisitor<String>() {
            public void visit(CeylonTree.Type type) {
                TypeName name = type.name();
                for (String component: name.components) {
                    result = result.append(component);
                }
            }
            public void visit(CeylonTree.MemberName name) {
                result = result.append(name.name);
            }
        };

        for (CeylonTree op: value.operands())
            op.accept(v);

        JCExpression result;

        if (Character.isUpperCase(v.result.last().charAt(0))) {
            // This looks like something of a kludge, but I think
            // it's a legitimate way to determine if this is the
            // name of a class.

            v.result = v.result.append("class");
            result = makeIdent(v.result);
       } else {
           // In the case of method literals, we're going to do this lazily.
           // To do otherwise would be very expensive

            result = at(value).Apply (null, makeSelect(makeIdent(syms.ceylonMethodType), "instance"),
                    List.<JCExpression>of(
                            make.Literal(toFlatName(v.result))));
        }

        return result;
    }

    JCExpression optionalType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonOptionalType),
                                List.<JCExpression>of(type));
    }

    JCExpression mutableType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonMutableType),
                                List.<JCExpression>of(type));
    }

    JCVariableDecl convert(CeylonTree.FormalParameter param) {
        at(param);
        Name name = names.fromString(param.name());
        JCExpression type = variableType(param.type(), param.annotations());

        if ((param.flags & CeylonTree.OPTIONAL) != 0) {
            type = optionalType(type);
        }
        if ((param.flags & CeylonTree.MUTABLE) != 0) {
            type = mutableType(type);
        }

        JCVariableDecl v = at(param).VarDef(make.Modifiers(0), name,
                type, null);

        return v;
    }
    JCExpression variableType(CeylonTree.Type t, List<Annotation> annotations) {
        return convert(t);
    }

    public JCClassDecl convert(final CeylonTree.ClassOrInterfaceDeclaration cdecl) {
        final ListBuffer<JCVariableDecl> params =
            new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCTree> defs =
            new ListBuffer<JCTree>();
        final ListBuffer<JCStatement> annotations =
            new ListBuffer<JCStatement>();
        final ListBuffer<JCAnnotation> langAnnotations =
            new ListBuffer<JCAnnotation>();
        final ListBuffer<JCStatement> stmts =
            new ListBuffer<JCStatement>();
        final ListBuffer<JCTypeParameter> typeParams =
            new ListBuffer<JCTypeParameter>();
        final ListBuffer<JCExpression> satisfies =
            new ListBuffer<JCExpression>();

        class ClassVisitor extends StatementVisitor {
            ClassVisitor(CeylonTree.ClassOrInterfaceDeclaration cdecl,
                    ListBuffer<JCStatement> stmts) {
                super(cdecl, stmts);
            }
            public void visit(CeylonTree.FormalParameter param) {
                JCVariableDecl var = at(cdecl).VarDef(make.Modifiers(0),
                        makeName(param.names), convert(param.type()), null);
                params.append(var);
            }

            public void visit(CeylonTree.Block b) {
                b.visitChildren(this);
            }

            public void visit(CeylonTree.MethodDeclaration meth) {
                defs.appendList(convert(cdecl, meth));
            }

            public void visit(CeylonTree.AbstractMethodDeclaration meth) {
                defs.appendList(convert(cdecl, meth));
            }

             public void visit(CeylonTree.LanguageAnnotation ann) {
                // Handled in processAnnotations
            }

            public void visit(CeylonTree.UserAnnotation userAnn) {
                // Handled in processAnnotations
            }

            public void visit(CeylonTree.SatisfiesList theList) {
                for (Type t: theList.types()) {
                    satisfies.append(convert(t));
                }
            }

            public void visit(CeylonTree.MemberDeclaration mem) {
                for (JCStatement def: convert(cdecl, mem)) {
                    if (def instanceof JCVariableDecl &&
                            ((JCVariableDecl) def).init != null) {
                        JCVariableDecl decl = (JCVariableDecl)def;
                        Name name = decl.name;
                        JCExpression init = decl.init;
                        decl.init = null;
                        defs.append(decl);
                        stmts.append(at(mem).Exec(at(mem).Assign(at(mem).Ident(name), init)));
                    } else {
                        defs.append(def);
                    }
                }
            }

            public void visit(final CeylonTree.ClassDeclaration cdecl) {
                defs.append(convert(cdecl));
            }

            public void visit(final CeylonTree.InterfaceDeclaration cdecl) {
                defs.append(convert(cdecl));
            }

            public void visit(CeylonTree.TypeParameter param) {
                typeParams.append(convert(param));
            }

            public void visit(CeylonTree.Type type) {
                assert type == cdecl.getSuperclass();
            }

            public void visit(CeylonTree.Superclass sc) {
                if (sc.arguments.nonEmpty()) {
                    List<JCExpression> args = List.<JCExpression>nil();

                    for (CeylonTree arg: sc.arguments)
                        args = args.append(convertArg(arg));

                    stmts.append(at(sc).Exec(at(sc).Apply(List.<JCExpression>nil(),
                                                          at(sc).Ident(names._super),
                                                          args)));
                }
            }

            public void visit(CeylonTree.TypeConstraint l) {}
        }

        cdecl.visitChildren(new ClassVisitor (cdecl, stmts));

        processAnnotations(cdecl, cdecl.annotations, annotations, langAnnotations,
                           cdecl.nameAsString());

        if (!cdecl.isInterface()) {
            JCMethodDecl meth = at(cdecl).MethodDef(make.Modifiers(convertDeclFlags(cdecl.flags)),
                    names.init,
                    at(cdecl).TypeIdent(VOID),
                    List.<JCTypeParameter>nil(),
                    params.toList(),
                    List.<JCExpression>nil(),
                    at(cdecl).Block(0, stmts.toList()), null);

            defs.append(meth);
        }

        if (annotations.length() > 0) {
            defs.append(registerAnnotations(annotations.toList()));
        }

        JCTree superclass;
        if (cdecl.getSuperclass() == null) {
            if (cdecl.isInterface())
                // The VM insists that interfaces have java.lang.Object as their superclass
                superclass = makeIdent(syms.objectType);
            else
                superclass = makeIdent(syms.ceylonObjectType);
        }
        else {
            List<String> name = cdecl.getSuperclass().name().components();
            superclass = makeIdent(name);
        }

        if ((cdecl.flags & CeylonTree.EXTENSION) != 0) {
            JCAnnotation ann =
                make.Annotation(makeIdent(syms.ceylonExtensionType),
                        List.<JCExpression>nil());
            langAnnotations.append(ann);
        }

        long mods = convertDeclFlags(cdecl.flags);
        if (cdecl.isInterface())
            mods |= INTERFACE;

         JCClassDecl classDef =
            at(cdecl).ClassDef(at(cdecl).Modifiers(mods, langAnnotations.toList()),
                    names.fromString(cdecl.nameAsString()),
                    processTypeConstraints(cdecl.typeConstraintList(), typeParams.toList()),
                    superclass,
                    satisfies.toList(),
                    defs.toList());

        return classDef;
    }

    int convertDeclFlags(int ceylonFlags) {
        int result = 0;

        /* Standard Java flags.

        public static final int PUBLIC       = 1<<0;
        public static final int PRIVATE      = 1<<1;
        public static final int PROTECTED    = 1<<2;
        public static final int STATIC       = 1<<3;
        public static final int FINAL        = 1<<4;
        public static final int SYNCHRONIZED = 1<<5;
        public static final int VOLATILE     = 1<<6;
        public static final int TRANSIENT    = 1<<7;
        public static final int NATIVE       = 1<<8;
        public static final int INTERFACE    = 1<<9;
        public static final int ABSTRACT     = 1<<10;
        public static final int STRICTFP     = 1<<11;*/

        /* Standard Ceylon flags

        public static final int PUBLIC = 1 << 0;
        public static final int DEFAULT = 1 << 1;
        public static final int PACKAGE = 1 << 2;
        public static final int ABSTRACT = 1 << 3;
        public static final int MODULE = 1 << 4;
        public static final int OPTIONAL = 1 << 5;
        public static final int MUTABLE = 1 << 6;
        public static final int EXTENSION = 1 << 7;*/

        result |= ((ceylonFlags & CeylonTree.PUBLIC) != 0) ? PUBLIC : 0;

        return result;
    }

    // Rewrite a list of Ceylon-style type constraints into Java trees.
    //    class TypeWithParameter<X, Y>()
    //    given X satisfies List
    //    given Y satisfies Comparable
    // becomes
    //    class TypeWithParameter<X extends List, Y extends Comparable> extends ceylon.Object {
    private List<JCTypeParameter> processTypeConstraints(
            List<CeylonTree.TypeConstraint> typeConstraintList, List<JCTypeParameter> typeParams) {
        if (typeConstraintList == null)
            return typeParams;

        LinkedHashMap<String, JCTypeParameter> symtab =
            new LinkedHashMap<String, JCTypeParameter>();
        for (JCTypeParameter item: typeParams) {
            symtab.put(item.getName().toString(), item);
        }

        for (final CeylonTree.TypeConstraint tc: typeConstraintList) {
            JCTypeParameter tp = symtab.get(tc.name.toString());
            if (tp == null)
                throw new RuntimeException("Class \"" + tc.name.toString() +
                        "\" in satisfies list not found");

            ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
            if (tc.satisfies != null) {
                for (CeylonTree.Type type: tc.satisfies.types())
                    bounds.add(convert(type));

                if (tp.getBounds() != null) {
                    tp.bounds = tp.getBounds().appendList(bounds.toList());
                } else {
                    JCTypeParameter newTp =
                        at(tc).TypeParameter(names.fromString(tc.name.toString()), bounds.toList());
                    symtab.put(tc.name.toString(), newTp);
                }
            }

            if (tc.abstracts != null)
                throw new RuntimeException("\"abstracts\" not supported yet");
        }

        // FIXME: This just converts a map to a List.  There ought to be a
        // better way to do it
        ListBuffer<JCTypeParameter> result = new ListBuffer<JCTypeParameter>();
        for (JCTypeParameter p: symtab.values()) {
            result.add(p);
        }
        return result.toList();
    }

    public List<JCTree> convert(CeylonTree.ClassOrInterfaceDeclaration cdecl,
            CeylonTree.BaseMethodDeclaration decl) {
        final ListBuffer<JCVariableDecl> params =
            new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCStatement> annotations =
            new ListBuffer<JCStatement>();
        final ListBuffer<JCAnnotation> langAnnotations =
            new ListBuffer<JCAnnotation>();
        final Singleton<JCBlock> body =
            new Singleton<JCBlock>();
        Singleton<JCExpression> restypebuf =
            new Singleton<JCExpression>();
        ListBuffer<JCAnnotation> jcAnnotations = new ListBuffer<JCAnnotation>();
        final ListBuffer<JCTypeParameter> typeParams =
            new ListBuffer<JCTypeParameter>();

        processMethodDeclaration(cdecl, decl, params, body, restypebuf, typeParams,
                annotations, langAnnotations);

        JCExpression restype = restypebuf.thing();

        // FIXME: Handle lots more flags here

        if ((decl.flags & CeylonTree.EXTENSION) != 0) {
            JCAnnotation ann =
                make.Annotation(makeIdent(syms.ceylonExtensionType),
                        List.<JCExpression>nil());
            jcAnnotations.append(ann);
        }

        if ((decl.flags & CeylonTree.OPTIONAL) != 0)
            restype = optionalType(restype);

        JCMethodDecl meth = at(decl).MethodDef(make.Modifiers(PUBLIC, jcAnnotations.toList()),
                names.fromString(decl.nameAsString()),
                restype,
                processTypeConstraints(decl.typeConstraintList(), typeParams.toList()),
                params.toList(),
                List.<JCExpression>nil(), body.thing(), null);;

        if (annotations.length() > 0) {
            // FIXME: Method annotations.
            JCBlock b = registerAnnotations(annotations.toList());
            return List.<JCTree>of(meth, b);
        }

      return List.<JCTree>of(meth);
    }


    public JCBlock convert(CeylonTree.ClassOrInterfaceDeclaration cdecl,
            CeylonTree.Block block) {
        return block == null ? null :
            at(block).Block(0, convertStmts(cdecl, block.getStmts()));
    }

    class StatementVisitor extends CeylonTree.Visitor {
        final ListBuffer<JCStatement> stmts;
        final CeylonTree.ClassOrInterfaceDeclaration cdecl;
        StatementVisitor(CeylonTree.ClassOrInterfaceDeclaration cdecl,
                ListBuffer<JCStatement> stmts) {
            this.stmts = stmts;
            this.cdecl = cdecl;
        }
        public ListBuffer<JCStatement> stmts() {
            return stmts;
        }
        public void visit(CeylonTree.CallExpression expr) {
            stmts.append(at(expr).Exec(convert(expr)));
        }
        public void visit(CeylonTree.ReturnStatement ret) {
            stmts.append(convert(ret));
        }
        public void visit(CeylonTree.IfStatement stat) {
            stmts.append(convert(cdecl, stat));
        }
        public void visit(CeylonTree.WhileStatement stat) {
            stmts.append(convert(cdecl, stat));
        }
        public void visit(CeylonTree.MemberDeclaration decl) {
            for (JCTree def: convert(cdecl, decl))
                 stmts.append((JCStatement)def);
        }
        public void visit(CeylonTree.Operator op) {
            stmts.append(at(op).Exec(convert(op)));
        }
        public void visit(CeylonTree.MethodDeclaration decl) {
            final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
            methodClass(cdecl, decl, defs, false);
            for (JCTree def: defs.toList()) {
                JCClassDecl innerDecl = (JCClassDecl)def;
                stmts.append(innerDecl);
                JCExpression id = make.Ident(innerDecl.name);
                stmts.append(at(decl).VarDef(make.Modifiers(FINAL),
                        names.fromString(decl.nameAsString()), id,
                        at(decl).NewClass(null, null, id,
                                List.<JCExpression>nil(), null)));
            }
        }
        public void visit(CeylonTree.PostfixExpression expr) {
            stmts.append(at(expr).Exec(convert(expr)));
        }
        public void visit(CeylonTree.PrefixExpression expr) {
            stmts.append(at(expr).Exec(convert(expr)));
        }
        public void visit(CeylonTree.ClassDeclaration decl) {
            stmts.append(convert(decl));
        }
        public void visitDefault(CeylonTree tree) {
            stmts.append(at(tree).Exec(convertExpression(tree)));
        }
    }

    List<JCStatement> convertStmts(CeylonTree.ClassOrInterfaceDeclaration cdecl,
            List<CeylonTree> stmts) {
        final ListBuffer<JCStatement> buf =
            new ListBuffer<JCStatement>();

        StatementVisitor v = new StatementVisitor (cdecl, buf);

        for (CeylonTree stmt: stmts)
            stmt.accept(v);

        return buf.toList();
    }

    JCExpression convert(PostfixExpression expr) {
        int operator = expr.operator.operatorKind;
        String methodName;
        switch (operator) {
        case CeylonParser.INCREMENT:
            methodName = "postIncrement";
            break;
        case CeylonParser.DECREMENT:
            methodName = "postDecrement";
            break;
        default:
            throw new RuntimeException(expr.toString());
        }
        return convert(expr, methodName);
    }

    JCExpression convert(PrefixExpression expr) {
        int operator = expr.operator.operatorKind;
        String methodName;
        switch (operator) {
        case CeylonParser.INCREMENT:
            methodName = "preIncrement";
            break;
        case CeylonParser.DECREMENT:
            methodName = "preDecrement";
            break;
        case CeylonParser.MINUS:
            // ????  Make a new operator with expr.operand as its operands.
            // This is rather evil.
            expr.operator.operands = List.of(expr.operand);
            return convert(expr.operator);
        default:
            throw new RuntimeException(expr.toString());
        }
        return convert(expr, methodName);
    }

    JCExpression convert(UnaryExpression expr, String methodName) {
        JCExpression operand = convertExpression(expr.operand);
        return at(expr).Apply(null, makeSelect(makeIdent(syms.ceylonMutableType), methodName),
                List.<JCExpression>of(operand));
    }


    long counter = 0;

    String tempName () {
        String result = "$ceylontmp" + counter;
        counter++;
        return result;
    }

    String tempName (String s) {
        String result = "$ceylontmp" + s + counter;
        counter++;
        return result;
    }

    JCStatement convert(CeylonTree.ClassOrInterfaceDeclaration cdecl,
            CeylonTree.IfStatement stmt) {
        JCBlock thenPart = convert(cdecl, stmt.ifTrue);
        JCBlock elsePart = convert(cdecl, stmt.ifFalse);
        return convertCondition(stmt.condition.operand, JCTree.IF, thenPart, elsePart);
    }

    JCStatement convert(CeylonTree.ClassOrInterfaceDeclaration cdecl,
            CeylonTree.WhileStatement stmt) {
        JCBlock thenPart = convert(cdecl, stmt.ifTrue);
        return convertCondition(stmt.condition.operand, JCTree.WHILELOOP, thenPart, null);
    }

    JCStatement convertCondition(CeylonTree cond, int tag,
            JCBlock thenPart, JCBlock elsePart) {

        if (cond instanceof CeylonTree.ExistsExpression) {
            CeylonTree.ExistsExpression exists =
                (CeylonTree.ExistsExpression)cond;
            CeylonTree.MemberName name = exists.name;

            // We're going to give this variable an initializer in order to be
            // able to determine its type, but the initializer will be deleted
            // in LowerCeylon.  Do not change the string "Deleted".
            Name tmp = names.fromString(tempName("DeletedExists"));
            Name tmp2 = names.fromString(name.asString());

            JCExpression type;
            if (exists.type == null) {
                type = makeIdent(syms.ceylonAnyType);
            } else {
                type = variableType(exists.type, null);
            }

            JCExpression expr;
            if (exists.expr == null) {
                expr = at(cond).Ident(tmp2);
            } else {
                expr = convertExpression(exists.expr);
            }

            expr = at(cond).Apply(null,
                    at(cond).Select(expr, names.fromString("$internalErasedExists")),
                        List.<JCExpression>nil());

            // This temp variable really should be SYNTHETIC, but then javac
            // won't let you use it...
            JCVariableDecl decl =
                at(cond).VarDef
                        (make.Modifiers(0), tmp, type,
                        exists.type == null ? expr : null);
            JCVariableDecl decl2 =
                at(cond).VarDef
                        (make.Modifiers(FINAL), tmp2, type, at(cond).Ident(tmp));
            thenPart = at(cond).Block(0, List.<JCStatement>of(decl2, thenPart));

            JCExpression assignment = at(cond).Assign(make.Ident(decl.name), expr);

            JCTree.JCBinary test = at(cond).Binary(JCTree.NE, assignment,
                    make.Literal(TypeTags.BOT, null));

            JCStatement cond1;
            switch (tag) {
            case JCTree.IF:
                cond1 = at(cond).If(test, thenPart, elsePart);
                return at(cond).Block(0, List.<JCStatement>of(decl, cond1));
            case JCTree.WHILELOOP:
                assert elsePart == null;
                cond1 = at(cond).WhileLoop(test, thenPart);
                return at(cond).Block(0, List.<JCStatement>of(decl, cond1));
            default:
                throw new RuntimeException();
            }
        } else if (cond instanceof CeylonTree.IsExpression) {
            // FIXME: This code has a lot in common with the ExistsExpression
            // above, but it has a niggling few things that are different.
            // It needs to be refactored.

            CeylonTree.IsExpression isExpr =
                (CeylonTree.IsExpression)cond;
            CeylonTree.MemberName name = isExpr.name;
            JCExpression type = variableType(isExpr.type, null);

            // We're going to give this variable an initializer in order to be
            // able to determine its type, but the initializer will be deleted
            // in LowerCeylon.  Do not change the string "Deleted".
            Name tmp = names.fromString(tempName("DeletedIs"));
            Name tmp2 = names.fromString(name.asString());

            JCExpression expr;
            if (isExpr.expr == null) {
                expr = convert(name);
            } else {
                expr = convertExpression(isExpr.expr);
            }

            // This temp variable really should be SYNTHETIC, but then javac
            // won't let you use it...
            JCVariableDecl decl =
                at(cond).VarDef
                        (make.Modifiers(0), tmp, makeIdent(syms.ceylonAnyType),
                        expr);
            JCVariableDecl decl2 =
                at(cond).VarDef
                        (make.Modifiers(FINAL), tmp2, type,
                                at(cond).TypeCast(type, at(cond).Ident(tmp)));
            thenPart = at(cond).Block(0, List.<JCStatement>of(decl2, thenPart));

            JCExpression assignment = at(cond).Assign(make.Ident(decl.name), expr);

            JCExpression test = at(cond).TypeTest(assignment, type);

            JCStatement cond1;
            switch (tag) {
            case JCTree.IF:
                cond1 = at(cond).If(test, thenPart, elsePart);
                return at(cond).Block(0, List.<JCStatement>of(decl, cond1));
            case JCTree.WHILELOOP:
                assert elsePart == null;
                cond1 = at(cond).WhileLoop(test, thenPart);
                return at(cond).Block(0, List.<JCStatement>of(decl, cond1));
            default:
                throw new RuntimeException();
            }
        } else {
            JCExpression test = convertExpression(cond);
            JCStatement result;

            switch (tag) {
            case JCTree.IF:
                result = at(cond).If(test, thenPart, elsePart);
                break;
            case JCTree.WHILELOOP:
                assert elsePart == null;
                result = at(cond).WhileLoop(test, thenPart);
                break;
            default:
                throw new RuntimeException();
            }

            return result;
        }
    }

    JCExpression convert(CeylonTree.CallExpression ce) {
        final Singleton<JCExpression> expr =
            new Singleton<JCExpression>();
        final ListBuffer<JCExpression> args =
            new ListBuffer<JCExpression>();

        for (CeylonTree arg: ce.args())
            args.append(convertArg(arg));

        ce.getMethod().accept (new CeylonTree.Visitor () {
            public void visit(CeylonTree.OperatorDot access) {
                expr.append(convert(access));
            }
            public void visit(CeylonTree.TypeName name) {
                // A constructor
                JCExpression id = makeIdent(name.components());
                expr.append(at(name).NewClass(null, null, id, args.toList(), null));
            }
            public void visit(CeylonTree.Type type) {
                // A constructor
                expr.append(at(type).NewClass(null, null, convert(type), args.toList(), null));
            }
             public void visit(CeylonTree.CallExpression chainedCall) {
                expr.append(convert(chainedCall));
            }
            public void visit(CeylonTree.MemberName access) {
                expr.append(convert(access));
            }});

        if (expr.thing() instanceof JCTree.JCNewClass)
            return expr.thing();
        else
            return at(ce).Apply(null, expr.thing(), args.toList());
    }

    JCExpression convertArg(CeylonTree arg) {
        return convertExpression(arg);
    }

    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make.Literal(s);
        return make().Apply (null, makeSelect(makeIdent(syms.ceylonStringType), "instance"),
                List.<JCExpression>of(lit));
    }

    JCExpression convert(CeylonTree.SimpleStringLiteral string) {
        at(string);
        return ceylonLiteral(string.value);
    }

    JCExpression convert(final CeylonTree.OperatorDot access)
    {
        final CeylonTree.Name memberName = access.memberName();
        final CeylonTree operand = access.operand();

        class V extends CeylonTree.Visitor {
            public JCExpression result;
            public void visit(CeylonTree.MemberName op) {
                result = makeIdent(Arrays.asList(op.name, memberName.name));
            }
            public void visit(CeylonTree.TypeName op) {
                result = makeIdent(op.components.append(memberName.name));
            }
            public void visit(CeylonTree.OperatorDot op) {
                result = at(access).Select(convert(op), names.fromString(memberName.name));
            }
            public void visit(CeylonTree.Operator op) {
                result = at(access).Select(convertExpression(op), names.fromString(memberName.name));
            }
            public void visit(CeylonTree.PrefixExpression expr) {
                // FIXME: I don't understand this
                visit(expr.operator);
            }
            public void visitDefault(CeylonTree tree) {
                result = at(access).Select(convertExpression(tree),
                        names.fromString(memberName.name));
            }
        }

        V v = new V();
        operand.accept(v);
        return v.result;
    }

    JCStatement convert(CeylonTree.ReturnStatement ret) {
        JCExpression returnExpr = convertExpression(ret.expr());
        return at(ret).Return(returnExpr);
    }

    JCIdent convert(CeylonTree.MemberName member) {
        return at(member).Ident(names.fromString(member.asString()));
    }

    List<JCStatement> convert(CeylonTree.ClassOrInterfaceDeclaration classDecl,
            CeylonTree.MemberDeclaration decl) {
        at(decl);

        JCExpression initialValue = null;
        if (decl.initialValue() != null)
            initialValue = convertExpression(decl.initialValue());

        final ListBuffer<JCAnnotation> langAnnotations =
            new ListBuffer<JCAnnotation>();
        final ListBuffer<JCStatement> annotations =
            new ListBuffer<JCStatement>();
        processAnnotations(classDecl, decl.annotations, annotations, langAnnotations, decl.nameAsString());

        JCExpression type = convert(decl.type);

        if ((decl.flags & CeylonTree.EXTENSION) != 0) {
            JCAnnotation ann =
                make.Annotation(makeIdent(syms.ceylonExtensionType),
                        List.<JCExpression>nil());
            langAnnotations.append(ann);
        }
        if ((decl.flags & CeylonTree.OPTIONAL) != 0) {
            type = optionalType(type);
        }
        JCExpression innerType = type;
        if ((decl.flags & CeylonTree.MUTABLE) != 0) {
            type = mutableType(type);
        }

        if (initialValue == null) {
            if ((decl.flags & CeylonTree.MUTABLE) == 0)
                throw new RuntimeException("Member needs a value");
            else {
                initialValue = at(decl).Apply(List.of(innerType),
                        makeSelect(makeIdent(syms.ceylonMutableType), "of"),
                        List.<JCExpression>of(at(decl).Literal(
                                TypeTags.BOT,
                                null)));
            }
        }

        int modifiers = FINAL;
        List<JCStatement> result =
            List.<JCStatement>of(at(decl).VarDef
                    (at(decl).Modifiers(modifiers, langAnnotations.toList()),
                            names.fromString(decl.nameAsString()),
                            type,
                            initialValue));

        if (annotations.length() > 0) {
            result = result.append(registerAnnotations(annotations.toList()));
        }

        return result;
    }

    JCTypeParameter convert(CeylonTree.TypeParameter param) {
        if (param.variance != null)
            throw new RuntimeException();
        TypeName name = (TypeName)param.name;
        return at(param).TypeParameter(names.fromString(name.toString()), List.<JCExpression>nil());
    }

    JCExpression convertExpression(final CeylonTree expr) {
        class V extends CeylonTree.Visitor {
            public JCExpression result;

            public void visit(This expr) {
                at(expr);
                result = makeIdent("this");
            }
            public void visit(Super expr) {
                at(expr);
                result = makeIdent("super");
            }
            public void visit(OperatorDot access) {
                result = convert(access);
            }
            public void visit(Operator op) {
                result = convert(op);
            }
            public void visit(PrefixExpression expr) {
                result = convert(expr);
            }
            public void visit(PostfixExpression expr) {
                result = convert(expr);
            }
            // NB spec 1.3.11 says "There are only two types of numeric
            // literals: literals for Naturals and literals for Floats."
            public void visit(NaturalLiteral lit) {
                JCExpression n = make.Literal(lit.value.longValue());
                result = at(expr).Apply (null, makeSelect(makeIdent(syms.ceylonNaturalType), "instance"),
                        List.of(n));
            }
            public void visit(FloatLiteral lit) {
                JCExpression n = make.Literal(lit.value);
                result = at(expr).Apply (null, makeSelect(makeIdent(syms.ceylonFloatType), "instance"),
                        List.of(n));
            }
            public void visit(CeylonTree.SimpleStringLiteral string) {
                result = convert(string);
            }
            public void visit(CeylonTree.CallExpression call) {
                result = convert(call);
            }
            public void visit(CeylonTree.ReflectedLiteral value) {
                result = convert(value);
            }
            public void visit(CeylonTree.MemberName value) {
                result = convert(value);
            }
            public void visit(CeylonTree.TypeName name) {
                result = makeIdent(name.components.append("class"));
            }
            public void visit(CeylonTree.InitializerExpression value) {
                result = convertExpression(value.value());
            }
            public void visit(CeylonTree.Null value) {
                result = at(value).TypeCast(makeIdent(syms.ceylonNothingType),
                        make.Literal(TypeTags.BOT, null));
            }
            public void visit(CeylonTree.Condition value) {
                result = convertExpression(value.operand);
            }
            public void visit(CeylonTree.SubscriptExpression expr) {
                assert expr.lowerBound != null;
                assert expr.upperBound == null;
                JCExpression lowerBound = convertExpression(expr.lowerBound);
                result = at(expr).Apply (null,
                        at(expr).Select(convertExpression(expr.operand), names.fromString("value")),
                        List.of(lowerBound));
            }
            public void visit(CeylonTree.LowerBound expr) {
                result = convertExpression(expr.initializer);
            }
            public void visit(CeylonTree.EnumList list) {
                ListBuffer<JCExpression> values = new ListBuffer<JCExpression>();
                for (CeylonTree expr: list.members) {
                    values.append(convertExpression(expr));
                }
                result = at(list).Apply(null,
                            makeSelect(makeIdent(syms.ceylonArrayListType), "of"),
                        values.toList());
            }
          }

        V v = new V();
        expr.accept(v);
        return v.result;
    }

    private static Map<Integer, String> unaryOperators;
    private static Map<Integer, String> binaryOperators;

    static {
        unaryOperators  = new HashMap<Integer, String>();
        binaryOperators = new HashMap<Integer, String>();

        // Unary operators
        unaryOperators.put(CeylonParser.MINUS,       "inverse");
        unaryOperators.put(CeylonParser.BITWISENOT,  "complement");
        unaryOperators.put(CeylonParser.RENDER,      "string");

        // Binary operators that act on types
        binaryOperators.put(CeylonParser.PLUS,       "plus");
        binaryOperators.put(CeylonParser.MINUS,      "minus");
        binaryOperators.put(CeylonParser.TIMES,      "times");
        binaryOperators.put(CeylonParser.DIVIDED,    "divided");
        binaryOperators.put(CeylonParser.POWER,      "power");
        binaryOperators.put(CeylonParser.REMAINDER,  "remainder");
        binaryOperators.put(CeylonParser.BITWISEAND, "and");
        binaryOperators.put(CeylonParser.BITWISEOR,  "or");
        binaryOperators.put(CeylonParser.BITWISEXOR, "xor");
        //binaryOperators.put(CeylonParser.EQEQ,       "operatorEqual");
        //binaryOperators.put(CeylonParser.IDENTICAL,  "operatorIdentical");
        //binaryOperators.put(CeylonParser.NOTEQ,      "operatorNotEqual");
        binaryOperators.put(CeylonParser.COMPARE,    "compare");

        // Binary operators that act on intermediary Comparison objects
        binaryOperators.put(CeylonParser.GT,         "larger");
        binaryOperators.put(CeylonParser.LT,         "smaller");
        binaryOperators.put(CeylonParser.GTEQ,       "largeAs");
        binaryOperators.put(CeylonParser.LTEQ,       "smallAs");
    }

    JCExpression convert(CeylonTree.Operator op) {
        boolean unary_operator  = false;
        boolean binary_operator = false;
        boolean lose_comparison = false;

        CeylonTree[] operands = op.toArray();

        int operator = op.operatorKind;
        switch (operator) {
        case CeylonParser.MINUS:
            if (operands.length == 1)
                unary_operator = true;
            else
                binary_operator = true;
            break;

        case CeylonParser.BITWISENOT:
        case CeylonParser.RENDER:
            unary_operator = true;
            break;

        case CeylonParser.PLUS:
        case CeylonParser.TIMES:
        case CeylonParser.POWER:
        case CeylonParser.DIVIDED:
        case CeylonParser.REMAINDER:
        case CeylonParser.BITWISEAND:
        case CeylonParser.BITWISEOR:
        case CeylonParser.BITWISEXOR:
        //case CeylonParser.EQEQ:
        //case CeylonParser.IDENTICAL:
        //case CeylonParser.NOTEQ:
        case CeylonParser.COMPARE:
            binary_operator = true;
            break;

        case CeylonParser.LT:
        case CeylonParser.GT:
        case CeylonParser.LTEQ:
        case CeylonParser.GTEQ:
            operator = CeylonParser.COMPARE;
            binary_operator = true;
            lose_comparison = true;
            break;

        case CeylonParser.COLONEQ:
        {
            JCExpression rhs = convertExpression(operands[1]);
            if (operands[0] instanceof CeylonTree.SubscriptExpression) {
                CeylonTree.SubscriptExpression expr =
                    (CeylonTree.SubscriptExpression)operands[0];
                assert expr.lowerBound != null;
                assert expr.upperBound == null;
                JCExpression lowerBound = convertExpression(expr.lowerBound);
                return at(op).Apply(null,
                        at(op).Select(convertExpression(expr.operand), names.fromString("set")),
                        List.of(lowerBound, rhs));
            } else {
                JCExpression lhs = convertExpression(operands[0]);
                return at(op).Apply(null,
                        at(op).Select(lhs, names.fromString("set")),
                        List.of(rhs));
            }
        }

        case CeylonParser.IS:
            return at(op).TypeTest(convertExpression(operands[0]), convertExpression(operands[1]));

        default:
            throw new RuntimeException(CeylonParser.tokenNames[op.operatorKind]);
        }

        assert unary_operator ^ binary_operator;

        JCExpression result = null;
        if (unary_operator) {
            assert operands.length == 1;
            if (operands[0] instanceof CeylonTree.NaturalLiteral && operator == CeylonParser.MINUS) {
                CeylonTree.NaturalLiteral lit = (CeylonTree.NaturalLiteral)operands[0];
                result = at(op).Apply(null, makeSelect(makeIdent(syms.ceylonIntegerType), "instance"),
                        List.<JCExpression>of(make.Literal(-lit.value.longValue())));

            } else {
                result = at(op).Apply(null,
                        at(op).Select(convertExpression(operands[0]),
                                names.fromString(unaryOperators.get(operator))),
                                List.<JCExpression>nil());
            }
        }
        if (binary_operator) {
            assert operands.length == 2;
            result = at(op).Apply(null,
                                  at(op).Select(convertExpression(operands[0]),
                                                names.fromString(binaryOperators.get(operator))),
                                  List.of(convertExpression(operands[1])));

            if (lose_comparison) {
                result = at(op).Apply(null,
                                      at(op).Select(result,
                                                    names.fromString(binaryOperators.get(op.operatorKind))),
                                      List.<JCExpression>nil());
            }
        }

        return result;
    }

    public void setMap(LineMap map) {
        this.map = map;
    }

    public LineMap getMap() {
        return map;
    }

}

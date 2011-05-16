package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.Flags.INTERFACE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.Flags.STATIC;
import static com.sun.tools.javac.code.TypeTags.VOID;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTool;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree;
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
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Position.LineMap;

public class Gen2 {
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

    public Gen2() throws Exception {
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


    public Gen2(Context context) {
        setup(context);
    }

   JCTree.Factory at(Node t) {
	   CommonTree antlrTreeNode = t.getAntlrTreeNode();
	   Token token = antlrTreeNode.getToken();
        if (token != null) {
            make.at(getMap().getStartPosition(token.getLine()) + token.getCharPositionInLine());
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

    public JCExpression makeIdentFromIdentifiers(Iterable<Tree.Identifier> components) {

        JCExpression type = null;
        for (Tree.Identifier component : components) {
            if (type == null)
                type = make().Ident(names.fromString(component.getText()));
            else
                type = make().Select(type, names.fromString(component.getText()));
        }

        return type;
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

    // FIXME: port handleOverloadedToplevelClasses when I figure out what it does
    
    public JCCompilationUnit convert(Tree.CompilationUnit t, JavaFileObject file) {
        System.err.println(t);
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();

        t.visitChildren(new Visitor () {
            public void visit(Tree.ImportList imp) {
                defs.appendList(convert(imp));
            }
            public void visit(Tree.ClassOrInterface decl) {
                defs.append(convert(decl));
            }
            public void visit(Tree.MethodDefinition decl) {
                methodClass(null, decl, defs, true);
            }
        });

        String[] prefixes = fileManager.getSourcePath();
        JCExpression pkg = null;

        // Figure out the package name by stripping the "-src" prefix and extracting
        // the package part of the fullname.
        for (String prefix: prefixes) {
            if (prefix != null && file.toString().startsWith(prefix)) {
                String fullname = file.toString().substring(prefix.length());
                assert fullname.endsWith(".ceylon");
                fullname = fullname.substring(0, fullname.length() - ".ceylon".length());
                fullname = fullname.replace(File.separator, ".");
                String packageName = Convert.packagePart(fullname);
                if (! packageName.equals(""))
                    pkg = getPackage(packageName);
            }
        }
        JCCompilationUnit topLev =
            at(t).TopLevel(List.<JCTree.JCAnnotation>nil(),
                    pkg, defs.toList());

        topLev.lineMap = getMap();
        topLev.sourcefile = file;
        topLev.isCeylonProgram = true;

        System.err.println(topLev);
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

    public void methodClass(Tree.ClassOrInterface classDecl,
            Tree.MethodDefinition decl, final ListBuffer<JCTree> defs,
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
                processTypeConstraints(decl.getTypeConstraintList(), typeParams.toList()),
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
            name = decl.getIdentifier().getText();
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
    void processMethodDeclaration(final Tree.ClassOrInterface classDecl,
            final Tree.MethodDefinition decl,
            final ListBuffer<JCVariableDecl> params,
            final Singleton<JCBlock> block,
            final Singleton<JCExpression> restype,
            final ListBuffer<JCTypeParameter> typeParams,
            final ListBuffer<JCStatement> annotations,
            final ListBuffer<JCAnnotation> langAnnotations) {

        for (Tree.Parameter param: decl.getParameterLists().get(0).getParameters()) {
            params.append(convert(param));
        }

        if (decl.getTypeParameterList() != null)
            for (Tree.TypeParameterDeclaration t: decl.getTypeParameterList().getTypeParameterDeclarations()) {
                typeParams.append(convert(t));
            }

        block.thing = convert(classDecl, decl.getBlock());

        processAnnotations(classDecl, decl.getAnnotationList(), annotations, langAnnotations, decl.getIdentifier().getText());

        restype.append(convert(decl.getType()));
     }

    JCExpression convert(Tree.Type type) {
        JCExpression result;

        // FIXME: handle sequences 
        ExpressionVisitor v =
        	new ExpressionVisitor() {

        	public void visit(Tree.SimpleType t) {
        		result = makeIdent(t.getIdentifier().getText());
        		
                Tree.TypeArgumentList tal = t.getTypeArgumentList();
                if (tal != null) {
                    ListBuffer<JCExpression> typeArgs =
                        new ListBuffer<JCExpression>();

                    for (Tree.Type innerType: tal.getTypes()) {
                        typeArgs.add(convert(innerType));
                    }

                    result = at(t).TypeApply(result, typeArgs.toList());
                }
        	}
        	
            // FIXME: Add the other primitive types
        	public void visit(Tree.VoidModifier t){
                result = make.TypeIdent(VOID);
        	}
        };

        type.visit(v);
        result = v.result;

        if (isOptional(type)) {
            result = optionalType(result);
        }
        /* FIXME: I'm not sure we can tell from a type if it's mutable
        if ((type.flags & CeylonTree.MUTABLE) != 0) {
            result = mutableType(result);
        }
        */

        return result;
    }

    List<JCTree> convert (Tree.ImportList importList) {
        final ListBuffer<JCTree> imports = new ListBuffer<JCTree>();
        importList.visit(new Visitor(){
        	// FIXME: handle the rest of the cases here
        	public void visit(Tree.ImportPath that) {
                JCImport stmt = at(that).Import(makeIdentFromIdentifiers(that.getIdentifiers()), false);
                imports.append(stmt);
        	}
        });
        return imports.toList();
    }

    private void log(String string) {
    	System.out.println(string);
	}

    void processAnnotations(final Tree.ClassOrInterface classDecl,
            Tree.AnnotationList ceylonAnnos,
            final ListBuffer<JCStatement> annotations,
            final ListBuffer<JCAnnotation> langAnnotations,
            final String declName) {
    	/* FIXME: this is probably just wrong
        class V extends Visitor {
            public void visit(Tree.Annotation userAnn) {
                annotations.append(at(userAnn).Exec(convert(userAnn, classDecl, declName)));
            }
        }
        V v = new V();

        if (ceylonAnnos != null)
            for (Tree.Annotation a: ceylonAnnos.getAnnotations())
                a.visit(v);
        */
    }

    JCBlock registerAnnotations(List<JCStatement> annos) {
        JCBlock block = make().Block(Flags.STATIC, annos);
        return block;
    }

	class ExpressionVisitor extends Visitor {
        public JCExpression result;
    }

    class ListVisitor<T> extends Visitor {
        public List<T> result = List.<T>nil();
    }
    
    JCExpression convert(final Tree.Annotation userAnn,
            Tree.ClassOrInterface classDecl, String methodName) {
       List<JCExpression> values = List.<JCExpression>nil();
       // FIXME: handle named arguments
        for (Tree.PositionalArgument arg: userAnn.getPositionalArgumentList().getPositionalArguments()) {
            values = values.append(convertExpression(arg.getExpression()));
        }

        JCExpression classLiteral;
        if (classDecl != null) {
            classLiteral = makeSelect(classDecl.getIdentifier().getText(), "class");
        } else {
            classLiteral = makeSelect(methodName, "class");
        }

        // FIXME: can we have something else?
        Tree.Member primary = (Tree.Member) userAnn.getPrimary();
        JCExpression result = at(userAnn).Apply(null, makeSelect(primary.getIdentifier().getText(), "run"),
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

    // FIXME: figure out what CeylonTree.ReflectedLiteral maps to
    
    JCExpression optionalType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonOptionalType),
                                List.<JCExpression>of(type));
    }

    JCExpression mutableType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonMutableType),
                                List.<JCExpression>of(type));
    }

    JCExpression iteratorType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonIteratorType),
                                List.<JCExpression>of(type));
    }

    JCVariableDecl convert(Tree.Parameter param) {
        at(param);
        Name name = names.fromString(param.getIdentifier().getText());
        JCExpression type = variableType(param.getType(), param.getAnnotationList());

        if (isOptional(param.getType())) {
            type = optionalType(type);
        }
        /* FIXME: I didn't see anywhere in the spec about mutable parameters
        if ((param.flags & CeylonTree.MUTABLE) != 0) {
            type = mutableType(type);
        }
        */

        JCVariableDecl v = at(param).VarDef(make.Modifiers(FINAL), name,
                type, null);

        return v;
    }

    JCExpression variableType(Tree.Type t, Tree.AnnotationList annotations) {
        return convert(t);
    }

    // FIXME: figure out what insertOverloadedClassConstructors does and port it
    
    public JCClassDecl convert(final Tree.ClassOrInterface cdecl) {
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
        final ListBuffer<JCStatement> initStmts =
            new ListBuffer<JCStatement>();
        final ListBuffer<JCTypeParameter> typeParams =
            new ListBuffer<JCTypeParameter>();
        final ListBuffer<JCExpression> satisfies =
            new ListBuffer<JCExpression>();

        class ClassVisitor extends StatementVisitor {
        	Tree.ExtendedType extendedType;
            ClassVisitor(Tree.ClassOrInterface cdecl,
                    ListBuffer<JCStatement> stmts) {
                super(cdecl, stmts);
            }
            public void visit(Tree.Parameter param) {
                JCVariableDecl var = at(cdecl).VarDef(make.Modifiers(0),
                        names.fromString(tempName()), convert(param.getType()), null);
                JCVariableDecl localVar = at(cdecl).VarDef(make.Modifiers(0),
                        names.fromString(param.getIdentifier().getText()), convert(param.getType()), null);
                params.append(var);
                defs.append(localVar);
                initStmts.append(at(param).
                        Exec(at(param).Assign(makeSelect("this",
                                localVar.getName().toString()),
                                at(param).Ident(var.getName()))));
            }

            public void visit(Tree.Block b) {
                b.visitChildren(this);
            }

            public void visit(Tree.MethodDefinition meth) {
                defs.appendList(convert(cdecl, meth));
            }
/* FIXME:
            public void visit(Tree.MethodDeclaration meth) {
                defs.appendList(convert(cdecl, meth));
            }
*/
             public void visit(Tree.Annotation ann) {
                // Handled in processAnnotations
            }

            public void visit(Tree.SatisfiedTypes theList) {
                for (Tree.Type t: theList.getTypes()) {
                    satisfies.append(convert(t));
                }
            }

            // FIXME: Here we've simplified CeylonTree.MemberDeclaration to Tree.AttributeDeclaration
            public void visit(Tree.AttributeDeclaration mem) {
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

            public void visit(final Tree.ClassDefinition cdecl) {
                defs.append(convert(cdecl));
            }

            public void visit(final Tree.InterfaceDefinition cdecl) {
                defs.append(convert(cdecl));
            }

            // FIXME: also support Tree.SequencedTypeParameter
            public void visit(Tree.TypeParameterDeclaration param) {
                typeParams.append(convert(param));
            }

            public void visit(Tree.ExtendedType extendedType) {
            	this.extendedType = extendedType;
                if (extendedType.getPositionalArgumentList() != null) {
                    List<JCExpression> args = List.<JCExpression>nil();

                    for (Tree.PositionalArgument arg: extendedType.getPositionalArgumentList().getPositionalArguments())
                        args = args.append(convertArg(arg));

                    stmts.append(at(extendedType).Exec(at(extendedType).Apply(List.<JCExpression>nil(),
                                                          at(extendedType).Ident(names._super),
                                                          args)));
                }
            }

            // FIXME: implement
            public void visit(Tree.TypeConstraint l) {}
        }

        ClassVisitor visitor = new ClassVisitor (cdecl, stmts);
        cdecl.visitChildren(visitor);

        processAnnotations(cdecl, cdecl.getAnnotationList(), annotations, langAnnotations,
                           cdecl.getIdentifier().getText());
        
        if (cdecl instanceof Tree.AnyClass) {
            JCMethodDecl meth = at(cdecl).MethodDef(make.Modifiers(convertDeclFlags(cdecl)),
                    names.init,
                    at(cdecl).TypeIdent(VOID),
                    List.<JCTypeParameter>nil(),
                    params.toList(),
                    List.<JCExpression>nil(),
                    at(cdecl).Block(0, initStmts.toList().appendList(stmts.toList())), null);

            defs.append(meth);

            //FIXME:
            //insertOverloadedClassConstructors(defs, (CeylonTree.ClassDeclaration) cdecl);
        }

        if (annotations.length() > 0) {
            defs.append(registerAnnotations(annotations.toList()));
        }

        JCTree superclass;
        if (cdecl instanceof Tree.AnyInterface) {
        	// The VM insists that interfaces have java.lang.Object as their superclass
        	superclass = makeIdent(syms.objectType);
        }else{
        	if(visitor.extendedType == null)
                superclass = makeIdent(syms.ceylonObjectType);
        	else {
        		// FIXME: is this typecast normal here?
        		superclass = convert((Tree.Type)visitor.extendedType.getType());
        	}
        }

        if (isExtension(cdecl)) {
            JCAnnotation ann =
                make.Annotation(makeIdent(syms.ceylonExtensionType),
                        List.<JCExpression>nil());
            langAnnotations.append(ann);
        }

        long mods = convertDeclFlags(cdecl);
        if (cdecl instanceof Tree.AnyInterface)
            mods |= INTERFACE;

         JCClassDecl classDef =
            at(cdecl).ClassDef(at(cdecl).Modifiers(mods, langAnnotations.toList()),
                    names.fromString(cdecl.getIdentifier().getText()),
                    processTypeConstraints(cdecl.getTypeConstraintList(), typeParams.toList()),
                    superclass,
                    satisfies.toList(),
                    defs.toList());

        return classDef;
    }

    int convertDeclFlags(Tree.ClassOrInterface cdecl) {
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

        result |= (isShared(cdecl)) ? PUBLIC : 0;

        return result;
    }

    // Rewrite a list of Ceylon-style type constraints into Java trees.
    //    class TypeWithParameter<X, Y>()
    //    given X satisfies List
    //    given Y satisfies Comparable
    // becomes
    //    class TypeWithParameter<X extends List, Y extends Comparable> extends ceylon.Object {
    private List<JCTypeParameter> processTypeConstraints(
            Tree.TypeConstraintList typeConstraintList, List<JCTypeParameter> typeParams) {
        if (typeConstraintList == null)
            return typeParams;

        LinkedHashMap<String, JCTypeParameter> symtab =
            new LinkedHashMap<String, JCTypeParameter>();
        for (JCTypeParameter item: typeParams) {
            symtab.put(item.getName().toString(), item);
        }

        for (final Tree.TypeConstraint tc: typeConstraintList.getTypeConstraints()) {
        	String name = tc.getIdentifier().getText();
            JCTypeParameter tp = symtab.get(name);
            if (tp == null)
                throw new RuntimeException("Class \"" + name +
                        "\" in satisfies list not found");

            ListBuffer<JCExpression> bounds = new ListBuffer<JCExpression>();
            if (tc.getSatisfiedTypes() != null) {
                for (Tree.Type type: tc.getSatisfiedTypes().getTypes())
                    bounds.add(convert(type));

                if (tp.getBounds() != null) {
                    tp.bounds = tp.getBounds().appendList(bounds.toList());
                } else {
                    JCTypeParameter newTp =
                        at(tc).TypeParameter(names.fromString(name), bounds.toList());
                    symtab.put(name, newTp);
                }
            }

            if (tc.getAbstractedType() != null)
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

	public List<JCTree> convert(Tree.ClassOrInterface cdecl,
            Tree.MethodDefinition decl) {
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

        if (isExtension(decl)) {
            JCAnnotation ann =
                make.Annotation(makeIdent(syms.ceylonExtensionType),
                        List.<JCExpression>nil());
            jcAnnotations.append(ann);
        }

        if (isOptional(decl.getType()))
            restype = optionalType(restype);

        JCMethodDecl meth = at(decl).MethodDef(make.Modifiers(PUBLIC, jcAnnotations.toList()),
                names.fromString(decl.getIdentifier().getText()),
                restype,
                processTypeConstraints(decl.getTypeConstraintList(), typeParams.toList()),
                params.toList(),
                List.<JCExpression>nil(), body.thing(), null);;

        if (annotations.length() > 0) {
        	// FIXME: Method annotations.
        	JCBlock b = registerAnnotations(annotations.toList());
        	return List.<JCTree>of(meth, b);
        }

      return List.<JCTree>of(meth);
    }


    public JCBlock convert(Tree.ClassOrInterface cdecl,
            Tree.Block block) {
        return block == null ? null :
            at(block).Block(0, convertStmts(cdecl, block.getStatements()));
    }

    class StatementVisitor extends Visitor {
        final ListBuffer<JCStatement> stmts;
        final Tree.ClassOrInterface cdecl;
        StatementVisitor(Tree.ClassOrInterface cdecl,
                ListBuffer<JCStatement> stmts) {
            this.stmts = stmts;
            this.cdecl = cdecl;
        }
        public ListBuffer<JCStatement> stmts() {
            return stmts;
        }
        public void visit(Tree.InvocationExpression expr) {
            stmts.append(at(expr).Exec(convert(expr)));
        }
        public void visit(Tree.Return ret) {
            stmts.append(convert(ret));
        }
        public void visit(Tree.IfStatement stat) {
            stmts.append(convert(cdecl, stat));
        }
        public void visit(Tree.WhileStatement stat) {
            stmts.append(convert(cdecl, stat));
        }
        public void visit(Tree.ForStatement stat) {
            stmts.append(convert(cdecl, stat));
        }
        public void visit(Tree.AttributeDeclaration decl) {
            for (JCTree def: convert(cdecl, decl))
                 stmts.append((JCStatement)def);
        }
        // FIXME: not sure why we don't have just an entry for Tree.Term here...
        public void visit(Tree.OperatorExpression op) {
            stmts.append(at(op).Exec(convertExpression(op)));
        }
        public void visit(Tree.Expression tree) {
            stmts.append(at(tree).Exec(convertExpression(tree)));
        }
        public void visit(Tree.MethodDefinition decl) {
            final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
            methodClass(cdecl, decl, defs, false);
            for (JCTree def: defs.toList()) {
                JCClassDecl innerDecl = (JCClassDecl)def;
                stmts.append(innerDecl);
                JCExpression id = make.Ident(innerDecl.name);
                stmts.append(at(decl).VarDef(make.Modifiers(FINAL),
                        names.fromString(decl.getIdentifier().getText()), id,
                        at(decl).NewClass(null, null, id,
                                List.<JCExpression>nil(), null)));
            }
        }
        public void visit(Tree.PostfixOperatorExpression expr) {
            stmts.append(at(expr).Exec(convert(expr)));
        }
        public void visit(Tree.PrefixOperatorExpression expr) {
            stmts.append(at(expr).Exec(convert(expr)));
        }
        public void visit(Tree.ExpressionStatement tree) {
            stmts.append(at(tree).Exec(convertExpression(tree.getExpression())));
        }
    }

    List<JCStatement> convertStmts(Tree.ClassOrInterface cdecl,
            java.util.List<Tree.Statement> list) {
        final ListBuffer<JCStatement> buf =
            new ListBuffer<JCStatement>();

        StatementVisitor v = new StatementVisitor (cdecl, buf);

        for (Tree.Statement stmt: list)
            stmt.visit(v);

        return buf.toList();
    }

    JCExpression convert(Tree.PostfixOperatorExpression expr) {
        String methodName;
        if(expr instanceof Tree.PostfixIncrementOp)
            methodName = "postIncrement";
        else if(expr instanceof Tree.PostfixDecrementOp)
            methodName = "postDecrement";
        else
            throw new RuntimeException("Not implemented: "+expr.getNodeType());
        return convertMutable(expr.getPrimary(), methodName);
    }

    JCExpression convert(Tree.PrefixOperatorExpression expr) {
        String methodName;
        if(expr instanceof Tree.IncrementOp)
            methodName = "preIncrement";
        else if(expr instanceof Tree.DecrementOp)
            methodName = "preDecrement";
        else
            throw new RuntimeException("Not implemented: "+expr.getNodeType());
        return convertMutable(expr.getTerm(), methodName);
    }

    JCExpression convertMutable(Tree.Term expr, String methodName) {
        JCExpression operand = convertExpression(expr);
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

    JCStatement convert(Tree.ClassOrInterface cdecl,
            Tree.IfStatement stmt) {
        JCBlock thenPart = convert(cdecl, stmt.getIfClause().getBlock());
        JCBlock elsePart = stmt.getElseClause() != null ? 
        		convert(cdecl, stmt.getElseClause().getBlock()) : null;
        return convertCondition(stmt.getIfClause().getCondition(), JCTree.IF, thenPart, elsePart);
    }

    JCStatement convert(Tree.ClassOrInterface cdecl,
            Tree.WhileStatement stmt) {
        JCBlock thenPart = convert(cdecl, stmt.getWhileClause().getBlock());
        return convertCondition(stmt.getWhileClause().getCondition(), JCTree.WHILELOOP, thenPart, null);
    }

    JCStatement convertCondition(Tree.Condition cond, int tag,
            JCBlock thenPart, JCBlock elsePart) {

        if (cond instanceof Tree.ExistsCondition) {
            Tree.ExistsCondition exists =
                (Tree.ExistsCondition)cond;
            Tree.Identifier name = exists.getVariable().getIdentifier();

            // We're going to give this variable an initializer in order to be
            // able to determine its type, but the initializer will be deleted
            // in LowerCeylon.  Do not change the string "Deleted".
            Name tmp = names.fromString(tempName("DeletedExists"));
            Name tmp2 = names.fromString(name.getText());

            JCExpression type;
            if (exists.getVariable().getType() == null) {
                type = makeIdent(syms.ceylonAnyType);
            } else {
                type = variableType(exists.getVariable().getType(), null);
            }

            JCExpression expr;
            if (exists.getExpression() == null) {
                expr = at(cond).Ident(tmp2);
            } else {
                expr = convertExpression(exists.getExpression());
            }

            expr = at(cond).Apply(null,
                    at(cond).Select(expr, names.fromString("$internalErasedExists")),
                        List.<JCExpression>nil());

            // This temp variable really should be SYNTHETIC, but then javac
            // won't let you use it...
            JCVariableDecl decl =
                at(cond).VarDef
                        (make.Modifiers(0), tmp, type,
                        exists.getVariable().getType() == null ? expr : null);
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
        } else if (cond instanceof Tree.IsCondition) {
            // FIXME: This code has a lot in common with the ExistsExpression
            // above, but it has a niggling few things that are different.
            // It needs to be refactored.

        	Tree.IsCondition isExpr =
                (Tree.IsCondition)cond;
            Tree.Identifier name = isExpr.getVariable().getIdentifier();
            JCExpression type = variableType(isExpr.getType(), null);

            // We're going to give this variable an initializer in order to be
            // able to determine its type, but the initializer will be deleted
            // in LowerCeylon.  Do not change the string "Deleted".
            Name tmp = names.fromString(tempName("DeletedIs"));
            Name tmp2 = names.fromString(name.getText());

            JCExpression expr;
            if (isExpr.getExpression() == null) {
                expr = convert(name);
            } else {
                expr = convertExpression(isExpr.getExpression());
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
        } else if (cond instanceof Tree.BooleanCondition){
        	Tree.BooleanCondition booleanCondition = (Tree.BooleanCondition) cond;
            JCExpression test = convertExpression(booleanCondition.getExpression());
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
        } else {
        	throw new RuntimeException("Not implemented: "+cond.getNodeType());
        }
    }

    JCStatement convert(Tree.ClassOrInterface cdecl, Tree.ForStatement stmt) {
        class ForVisitor extends Visitor {
            Tree.Variable variable = null;

            public void visit(Tree.ValueIterator valueIterator) {
                assert variable == null;
                variable = valueIterator.getVariable();
            }
            public void visit(Tree.KeyValueIterator keyValueIterator) {
                assert variable == null;
                // FIXME: implement this
                throw new RuntimeException("Not implemented: "+keyValueIterator.getNodeType());
            }
        };
        // FIXME: implement this
        if(stmt.getFailClause() != null)
            throw new RuntimeException("Not implemented: "+stmt.getFailClause().getNodeType());

        ForVisitor visitor = new ForVisitor();
    	stmt.getForClause().getForIterator().visit(visitor);
        JCExpression item_type = variableType(visitor.variable.getType(), null);

        // ceylon.language.Iterator<T> $ceylontmpX = ITERABLE.iterator();
        JCExpression containment = convertExpression(stmt.getForClause().getForIterator().getSpecifierExpression().getExpression());
        JCVariableDecl iter_decl = at(stmt).VarDef(
            make.Modifiers(0),
            names.fromString(tempName()),
            iteratorType(item_type),
            at(stmt).Apply(null,
                           at(stmt).Select(containment, names.fromString("iterator")),
                           List.<JCExpression>nil()));
        List<JCStatement> outer = List.<JCStatement>of(iter_decl);
        JCIdent iter = at(stmt).Ident(iter_decl.getName());

        // ceylon.language.Optional<T> $ceylontmpY = $ceylontmpX.head();
        JCVariableDecl optional_item_decl = at(stmt).VarDef(
            make.Modifiers(FINAL),
            names.fromString(tempName()),
            optionalType(item_type),
            at(stmt).Apply(null,
                           at(stmt).Select(iter, names.fromString("head")),
                           List.<JCExpression>nil()));
        List<JCStatement> while_loop = List.<JCStatement>of(optional_item_decl);
        JCIdent optional_item = at(stmt).Ident(optional_item_decl.getName());

        // T n = $ceylontmpY.t;
        JCVariableDecl item_decl = at(stmt).VarDef(
                make.Modifiers(0),
                names.fromString(visitor.variable.getIdentifier().getText()),
                item_type,
                at(stmt).Apply(null,
                               at(stmt).Select(optional_item, names.fromString("$internalErasedExists")),
                               List.<JCExpression>nil()));
        List<JCStatement> inner = List.<JCStatement>of(item_decl);

        // The user-supplied contents of the loop
        inner = inner.appendList(convertStmts(cdecl, stmt.getForClause().getBlock().getStatements()));

        // if ($ceylontmpY != null) ... else break;
        JCStatement test = at(stmt).If(
                at(stmt).Binary(JCTree.NE, optional_item, make.Literal(TypeTags.BOT, null)),
                at(stmt).Block(0, inner),
                at(stmt).Block(0, List.<JCStatement>of(at(stmt).Break(null))));
        while_loop = while_loop.append(test);

        // $ceylontmpX = $ceylontmpX.tail();
        JCExpression next = at(stmt).Assign(
            iter,
            at(stmt).Apply(null,
                at(stmt).Select(iter, names.fromString("tail")),
                List.<JCExpression>nil()));
        while_loop = while_loop.append(at(stmt).Exec(next));

        // while (True)...
        outer = outer.append(
            at(stmt).WhileLoop(
                at(stmt).Literal(TypeTags.BOOLEAN, 1),
                at(stmt).Block(0, while_loop)));

        return at(stmt).Block(0, outer);
   }

    JCExpression convert(Tree.InvocationExpression ce) {
        final Singleton<JCExpression> expr =
            new Singleton<JCExpression>();
        final ListBuffer<JCExpression> args =
            new ListBuffer<JCExpression>();

        for (Tree.PositionalArgument arg: ce.getPositionalArgumentList().getPositionalArguments())
            args.append(convertArg(arg));

        ce.getPrimary().visit (new Visitor () {
            public void visit(Tree.MemberExpression access) {
                expr.append(convert(access));
            }
            public void visit(Tree.Type type) {
                // A constructor
                expr.append(at(type).NewClass(null, null, convert(type), args.toList(), null));
            }
             public void visit(Tree.InvocationExpression chainedCall) {
                expr.append(convert(chainedCall));
            }
            public void visit(Tree.Member access) {
                expr.append(convert(access));
            }
        });

        if (expr.thing() instanceof JCTree.JCNewClass)
            return expr.thing();
        else
            return at(ce).Apply(null, expr.thing(), args.toList());
    }

    JCExpression convertArg(Tree.PositionalArgument arg) {
        return convertExpression(arg.getExpression());
    }

    JCExpression ceylonLiteral(String s) {
        JCLiteral lit = make.Literal(s);
        return make().Apply (null, makeSelect(makeIdent(syms.ceylonStringType), "instance"),
                List.<JCExpression>of(lit));
    }

    JCExpression convert(Tree.StringLiteral string) {
    	String value = string.getText().substring(1, string.getText().length() - 1); 
        at(string);
        return ceylonLiteral(value);
    }

    JCExpression convert(final Tree.MemberExpression access)
    {
        final Tree.Identifier memberName = access.getIdentifier();
        final Tree.Primary operand = access.getPrimary();

        class V extends Visitor {
            public JCExpression result;
            // FIXME: this list of cases is incomplete from Gen
            public void visit(Tree.Member op) {
                result = makeIdent(Arrays.asList(op.getIdentifier().getText(), memberName.getText()));
            }
            public void visit(Tree.MemberExpression op) {
                result = at(access).Select(convert(op), names.fromString(memberName.getText()));
            }
            public void visit(Tree.Expression tree) {
                result = at(access).Select(convertExpression(tree),
                        names.fromString(memberName.getText()));
            }
        }

        V v = new V();
        operand.visit(v);
        return v.result;
    }

    JCStatement convert(Tree.Return ret) {
        Tree.Expression expr = ret.getExpression();
        JCExpression returnExpr = expr != null ? convertExpression(expr) : null;
        return at(ret).Return(returnExpr);
    }

    JCIdent convert(Tree.Identifier identifier) {
        return at(identifier).Ident(names.fromString(identifier.getText()));
    }

    JCIdent convert(Tree.Member member) {
        return at(member).Ident(names.fromString(member.getIdentifier().getText()));
    }

    List<JCStatement> convert(Tree.ClassOrInterface classDecl,
            Tree.AttributeDeclaration decl) {
        at(decl);

        JCExpression initialValue = null;
        if (decl.getSpecifierOrInitializerExpression() != null)
            initialValue = convertExpression(decl.getSpecifierOrInitializerExpression().getExpression());

        final ListBuffer<JCAnnotation> langAnnotations =
            new ListBuffer<JCAnnotation>();
        final ListBuffer<JCStatement> annotations =
            new ListBuffer<JCStatement>();
        processAnnotations(classDecl, decl.getAnnotationList(), annotations, langAnnotations, decl.getIdentifier().getText());

        JCExpression type = convert(decl.getType());

        if (isExtension(decl)) {
            JCAnnotation ann =
                make.Annotation(makeIdent(syms.ceylonExtensionType),
                        List.<JCExpression>nil());
            langAnnotations.append(ann);
        }
        if (isOptional(decl.getType())) {
            type = optionalType(type);
        }
        JCExpression innerType = type;
        if (isMutable(decl)) {
            type = mutableType(type);
        }

        if (initialValue == null) {
            if (!isMutable(decl))
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
                            names.fromString(decl.getIdentifier().getText()),
                            type,
                            initialValue));

        if (annotations.length() > 0) {
            result = result.append(registerAnnotations(annotations.toList()));
        }

        return result;
    }
    
    
    private boolean hasCompilerAnnotation(Tree.Declaration decl, com.sun.tools.javac.code.Type annotationType) {
    	if(decl.getAnnotationList() == null)
    		return false;
    	for(Tree.Annotation a : decl.getAnnotationList().getAnnotations()){
    		if(!(a.getPrimary() instanceof Tree.Member))
    			throw new RuntimeException("Invalid annotation primary: "+a.getPrimary().getNodeType());
    		Tree.Member member = (Tree.Member) a.getPrimary();
    		if(isSameType(member.getIdentifier(), annotationType))
    			return true;
    	}
		return false;
	}
    
	// FIXME: this is ugly and probably wrong
    private boolean isSameType(Tree.Identifier ident, com.sun.tools.javac.code.Type type){
		return ident.getText().equals(type.tsym.getQualifiedName());
    }

    private boolean isExtension(Tree.Declaration decl) {
    	return hasCompilerAnnotation(decl, syms.ceylonExtensionType);
    }
    
    private boolean isShared(Tree.ClassOrInterface cdecl) {
    	return hasCompilerAnnotation(cdecl, syms.ceylonSharedType);
	}

	private boolean isMutable(Tree.AttributeDeclaration decl) {
		return decl.getSpecifierOrInitializerExpression() instanceof Tree.InitializerExpression;
	}

	private boolean isOptional(Tree.Type type) {
		// This should show in the tree as: Nothing|Type, so we just visit
		class TypeVisitor extends Visitor {
			boolean isOptional = false;
			@Override
			public void visit(Tree.SimpleType t){
				isOptional |= isSameType(t.getIdentifier(), syms.ceylonNothingType);
			}
		}
		TypeVisitor visitor = new TypeVisitor();
		type.visit(visitor);
		return visitor.isOptional;
	}

    JCTypeParameter convert(Tree.TypeParameterDeclaration param) {
    	// FIXME: implement this
        if (param.getTypeVariance() != null)
            throw new RuntimeException("Variance not implemented");
        Tree.Identifier name = param.getIdentifier();
        return at(param).TypeParameter(names.fromString(name.getText()), List.<JCExpression>nil());
    }

	JCExpression convertExpression(final Tree.Term expr) {
        class V extends Visitor {
            public JCExpression result;

            public void visit(Tree.This expr) {
                at(expr);
                result = makeIdent("this");
            }
            public void visit(Tree.Super expr) {
                at(expr);
                result = makeIdent("super");
            }
            // FIXME: port dot operator?
            public void visit(Tree.NotEqualOp op) {
                result = convert(op);
            }
            public void visit(Tree.NotOp op) {
                result = convert(op);
            }
            public void visit(Tree.AssignOp op) {
                result = convert(op);
            }
            public void visit(Tree.IsOp op) {
                result = convert(op);
            }
            public void visit(Tree.RangeOp op) {
                result = convert(op);
            }
            public void visit(Tree.UnaryOperatorExpression op) {
                result = convert(op);
            }
            public void visit(Tree.BinaryOperatorExpression op) {
                result = convert(op);
            }
            public void visit(Tree.PrefixOperatorExpression op) {
                result = convert(op);
            }
            public void visit(Tree.PostfixOperatorExpression op) {
                result = convert(op);
            }
            // NB spec 1.3.11 says "There are only two types of numeric
            // literals: literals for Naturals and literals for Floats."
            public void visit(Tree.NaturalLiteral lit) {
                JCExpression n = make.Literal(Long.parseLong(lit.getText()));
                result = at(expr).Apply (null, makeSelect(makeIdent(syms.ceylonNaturalType), "instance"),
                        List.of(n));
            }
            public void visit(Tree.FloatLiteral lit) {
                JCExpression n = make.Literal(Double.parseDouble(lit.getText()));
                result = at(expr).Apply (null, makeSelect(makeIdent(syms.ceylonFloatType), "instance"),
                        List.of(n));
            }
            public void visit(Tree.CharLiteral lit) {
                JCExpression n = make.Literal(TypeTags.CHAR, (int) lit.getText().charAt(1));
                // XXX make.Literal(lit.value) doesn't work here... something broken in javac?
                result = at(expr).Apply (null, makeSelect(makeIdent(syms.ceylonCharacterType), "instance"),
                        List.of(n));
            }

            public void visit(Tree.StringLiteral string) {
                result = convert(string);
            }
            public void visit(Tree.InvocationExpression call) {
                result = convert(call);
            }
            // FIXME: port ReflectedLiteral?
            public void visit(Tree.MemberExpression value) {
                result = convert(value);
            }
            public void visit(Tree.Member value) {
                result = convert(value);
            }
            // FIXME: port TypeName?
            public void visit(Tree.InitializerExpression value) {
                result = convertExpression(value.getExpression());
            }
            // FIXME: port Null?
            // FIXME: port Condition?
            // FIXME: port Subscript?
            // FIXME: port LowerBoud?
            // FIXME: port EnumList?
            public void visit(Tree.StringTemplate expr) {
                result = convertStringExpression(expr);
            }
          }

        V v = new V();
        expr.visit(v);
        return v.result;
    }

    JCExpression convertStringExpression(Tree.StringTemplate expr) {
        ListBuffer<JCExpression> strings = new ListBuffer<JCExpression>();
        for (Tree.Expression t: expr.getExpressions()) {
            strings.append(convertExpression(t));
        }

        return make().Apply (null, makeSelect(makeIdent(syms.ceylonStringType), "instance"),
                strings.toList());
    }

    private static Map<Class<? extends Tree.UnaryOperatorExpression>, String> unaryOperators;
    private static Map<Class<? extends Tree.BinaryOperatorExpression>, String> binaryOperators;

    static {
        unaryOperators  = new HashMap<Class<? extends Tree.UnaryOperatorExpression>, String>();
        binaryOperators = new HashMap<Class<? extends Tree.BinaryOperatorExpression>, String>();

        // Unary operators
        unaryOperators.put(Tree.NegativeOp.class,       "inverse");
        unaryOperators.put(Tree.NotOp.class,  "complement");
        unaryOperators.put(Tree.FormatOp.class,      "string");

        // Binary operators that act on types
        binaryOperators.put(Tree.SumOp.class,       "plus");
        binaryOperators.put(Tree.DifferenceOp.class,      "minus");
        binaryOperators.put(Tree.ProductOp.class,      "times");
        binaryOperators.put(Tree.QuotientOp.class,    "divided");
        binaryOperators.put(Tree.PowerOp.class,      "power");
        binaryOperators.put(Tree.RemainderOp.class,  "remainder");
        binaryOperators.put(Tree.IntersectionOp.class, "and");
        binaryOperators.put(Tree.UnionOp.class,  "or");
        binaryOperators.put(Tree.XorOp.class, "xor");
        binaryOperators.put(Tree.EqualOp.class,       "equalsXXX");
        binaryOperators.put(Tree.IdenticalOp.class,  "identical");
        binaryOperators.put(Tree.CompareOp.class,    "compare");

        // Binary operators that act on intermediary Comparison objects
        binaryOperators.put(Tree.LargerOp.class,         "larger");
        binaryOperators.put(Tree.SmallerOp.class,         "smaller");
        binaryOperators.put(Tree.LargeAsOp.class,       "largeAs");
        binaryOperators.put(Tree.SmallAsOp.class,       "smallAs");
    }

    // FIXME: I'm pretty sure sugar is not supposed to be in there
    JCExpression convert(Tree.NotEqualOp op) {
        Tree.EqualOp newOp = new Tree.EqualOp(op.getAntlrTreeNode());
        newOp.setLeftTerm(op.getLeftTerm());
        newOp.setRightTerm(op.getRightTerm());
        Tree.NotOp newNotOp = new Tree.NotOp(op.getAntlrTreeNode());
        newNotOp.setTerm(newOp);
        return convert(newNotOp);
    }
    
    // FIXME: I'm pretty sure sugar is not supposed to be in there
    JCExpression convert(Tree.NotOp op){
        return at(op).Apply(null, makeSelect(makeIdent(syms.ceylonBooleanType), "instance"),
                List.<JCExpression>of(at(op).Conditional(convertExpression(op.getTerm()),
                        make.Literal(TypeTags.BOOLEAN, 0),
                        make.Literal(TypeTags.BOOLEAN, 1))));
    }
    
    JCExpression convert(Tree.AssignOp op){
        JCExpression rhs = convertExpression(op.getRightTerm());
        JCExpression lhs = convertExpression(op.getLeftTerm());
        return at(op).Apply(null,
        		at(op).Select(lhs, names.fromString("set")),
        		List.of(rhs));
    }
    
    JCExpression convert(Tree.IsOp op){
    	// FIXME: this is only working for SimpleType
    	// FIXME: Nasty cast here.  We can't call convertExpression()operands[1])
    	// because that returns TypeName.class, not simply TypeName.
    	Tree.SimpleType name = (Tree.SimpleType)op.getRightTerm();
    	return at(op).Apply(null, makeSelect(makeIdent(syms.ceylonBooleanType), "instance"),
    			List.<JCExpression>of(at(op).TypeTest(convertExpression(op.getLeftTerm()),
    					makeIdent(name.getIdentifier().getText()))));
    }
    
    JCExpression convert(Tree.RangeOp op){
    	JCExpression lower = convertExpression(op.getLeftTerm());
    	JCExpression upper = convertExpression(op.getRightTerm());
    	return at(op).NewClass(
    			null,
    			null,
    			at(op).TypeApply(makeIdent(syms.ceylonRangeType), List.<JCExpression>of(null)),
    			List.<JCExpression>of(lower, upper),
    			null);
    }
    
    JCExpression convert(Tree.UnaryOperatorExpression op) {
    	Tree.Term term = op.getTerm();
    	if (term instanceof Tree.NaturalLiteral && op instanceof Tree.NegativeOp) {
    		Tree.NaturalLiteral lit = (Tree.NaturalLiteral)term;
    		return at(op).Apply(null, makeSelect(makeIdent(syms.ceylonIntegerType), "instance"),
    				List.<JCExpression>of(make.Literal(-Long.parseLong(lit.getText()))));
    	}
    	return at(op).Apply(null,
    			at(op).Select(convertExpression(term),
    					names.fromString(unaryOperators.get(op.getClass()))),
    					List.<JCExpression>nil());
    }
    
    JCExpression convert(Tree.BinaryOperatorExpression op) {
        JCExpression result = null;
        Class<? extends Tree.OperatorExpression> operatorClass = op.getClass();
        
        boolean loseComparison = op instanceof Tree.SmallAsOp
        || op instanceof Tree.SmallerOp
        || op instanceof Tree.LargerOp
        || op instanceof Tree.LargeAsOp;
        
        if(loseComparison)
        	operatorClass = Tree.CompareOp.class;
        
        result = at(op).Apply(null,
        		at(op).Select(convertExpression(op.getLeftTerm()),
        				names.fromString(binaryOperators.get(operatorClass))),
        				List.of(convertExpression(op.getRightTerm())));

        if (loseComparison) {
        	result = at(op).Apply(null,
        			at(op).Select(result,
        					names.fromString(binaryOperators.get(op.getClass()))),
        					List.<JCExpression>nil());
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

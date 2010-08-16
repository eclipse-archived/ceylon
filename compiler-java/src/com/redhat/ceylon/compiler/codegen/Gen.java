package com.redhat.ceylon.compiler.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.lang.model.element.TypeElement;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.tree.*;
import com.sun.source.tree.*;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.file.JavacFileManager;
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
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Options;
import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.TypeTags.*;
import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.CeylonTree.*;

public class Gen {
    Context context;
    TreeMaker make;
    Names names;
    ClassReader reader;
    Resolve resolve;
    JavaCompiler compiler;
    DiagnosticCollector<JavaFileObject> diagnostics;
    JavacFileManager fileManager;
    JavacTaskImpl task;
    Options options;
    
    JCCompilationUnit jcCompilationUnit;

    public Gen() throws Exception {
        compiler = ToolProvider.getSystemJavaCompiler();
        diagnostics
        = new DiagnosticCollector<JavaFileObject>();
        fileManager
        = (JavacFileManager)compiler.getStandardFileManager(diagnostics, null, null);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                Arrays.asList(new File("/tmp")));

        fileManager.setLocation(StandardLocation.CLASS_PATH,
                Arrays.asList(new File("/tmp"), new File(System.getProperty("user.dir") + "/runtime")));
        Iterable<? extends JavaFileObject> compilationUnits
        = fileManager.getJavaFileObjectsFromStrings(new ArrayList<String>());

        JavaCompiler.CompilationTask aTask
        = compiler.getTask(null, fileManager,
                diagnostics,
                Arrays.asList("-g", /* "-verbose", */
                        "-source", "7", "-XDallowFunctionTypes"),
                        null, compilationUnits);
        setup((JavacTaskImpl)aTask);
    }

    void setup (JavacTaskImpl task) {
        this.task = task;

        context = task.getContext();
        options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
        make = TreeMaker.instance(context);
        names = Names.instance(context);
        reader = ClassReader.instance(context);
        resolve = Resolve.instance(context);
    }

    class Accumulator<T> implements Iterable<T>{
        private List<T> things;
        Accumulator() { things = List.<T>nil(); }
        List<T> asList() { return things; }
        void append(T t) { things = things.append(t); }
        public String toString() { return asList().toString(); }
        public Iterator<T> iterator() {
            return things.iterator();
        }
    }
    
    class Singleton<T> implements Iterable<T>{
        private T thing;
        Singleton() { }
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
    }
    
    JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return make.Select(s1, names.fromString(s2));
    }

    JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(make.Ident(names.fromString(s1)), s2);
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
    
    public JCExpression makeIdent(Iterable<String> components) {

        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make.Ident(names.fromString(component));
            else
                type = make.Select(type, names.fromString(component));
        }
        
        return type;
    }
    
    public void run(CeylonTree.CompilationUnit t) throws IOException {        
        JCCompilationUnit tree = convert(t);
        tree.sourcefile = new CeylonFileObject(fileManager.getFileForInput(t.source.path));

        Iterable<? extends TypeElement> result =
            task.enter(List.of(tree));
        Iterable<? extends JavaFileObject> files = task.generate(result);

        System.out.println(diagnostics.getDiagnostics());
    }
    
    public JCCompilationUnit convert(CeylonTree.CompilationUnit t) {
        final Accumulator<JCTree> defs = new Accumulator<JCTree>();
        
        defs.append(make.Import(makeIdent(Arrays.asList("ceylon", "*")), false));
                
        t.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.ClassDeclaration decl) {
                defs.append(convert(decl));
            }
            public void visit(CeylonTree.MethodDeclaration decl) {
                // This is a top-level method.  Generate a class with the
                // name of the method and a corresponding run() method.
                
                final Accumulator<JCVariableDecl> params = 
                    new Accumulator<JCVariableDecl>();
                final Accumulator<JCAnnotation> annotations = 
                    new Accumulator<JCAnnotation>();
                final Singleton<JCBlock> body = 
                    new Singleton<JCBlock>();
                Singleton<JCExpression> restype =
                    new Singleton<JCExpression>();
                
                processMethodDeclaration(decl, params, body, restype, (Accumulator<JCTypeParameter>)null,
                        annotations);
                
                JCMethodDecl meth = make.MethodDef(make.Modifiers(PUBLIC|STATIC),
                        names.fromString("run"),
                        make.TypeIdent(VOID),
                        List.<JCTypeParameter>nil(),
                        params.asList(),
                        List.<JCExpression>nil(), body.thing(), null);
                
                JCClassDecl classDef = 
                    make.ClassDef(make.Modifiers(PUBLIC, annotations.asList()),
                            names.fromString(decl.nameAsString()),
                            List.<JCTypeParameter>nil(), null,
                            List.<JCExpression>nil(),
                            List.<JCTree>of(meth));
                
                defs.append(classDef);
            }
        });

        JCCompilationUnit topLev =
            make.TopLevel(List.<JCTree.JCAnnotation>nil(),
                    /* package id*/ null, defs.asList());

        System.out.println(topLev);
        return topLev;
    }

    // FIXME: There must be a better way to do this.
    void processMethodDeclaration(CeylonTree.MethodDeclaration decl, 
            final Accumulator<JCVariableDecl> params, 
            final Singleton<JCBlock> block,
            final Singleton<JCExpression> restype,
            final Accumulator<JCTypeParameter> typarams,
            final Accumulator<JCAnnotation> annotations) {

        System.err.println(decl);
        
        for (FormalParameter param: decl.params) {
            params.append(convert(param));
        }
        
        for (CeylonTree stmt: decl.stmts)
            stmt.accept(new CeylonTree.Visitor () {
                public void visit(CeylonTree.Block b) {
                    block.thing = convert(b);
                }
            });
        
        for (CeylonTree.Annotation a: decl.annotations) {
            a.accept(new CeylonTree.Visitor () {
                public void visit(CeylonTree.UserAnnotation userAnn) {
                    annotations.append(convertUserAnnotation(userAnn));
                }
                public void visit(CeylonTree.LanguageAnnotation langAnn) {
                    // FIXME
                }
            });
        }
    }                

    class ExpressionVisitor extends CeylonTree.Visitor {
        public JCExpression result;
    }
    
    JCAnnotation convertUserAnnotation(CeylonTree.UserAnnotation userAnn) {
        ExpressionVisitor v = new ExpressionVisitor() {
            public void visit(CeylonTree.SimpleStringLiteral value) {
                result = make.Literal(value.value);
            }          
        };
        userAnn.value().accept(v);
        JCAnnotation result = make.Annotation(make.Ident(names.fromString(userAnn.name)),
                List.<JCExpression>of(v.result));
        return result;
    }
    
    JCVariableDecl convert(CeylonTree.FormalParameter param) {
        Name name = names.fromString(param.name());
        
        JCVariableDecl v = make.VarDef(make.Modifiers(0), name,
                variableType(param.type()), null);
        
        return v;
    }

    JCExpression variableType(CeylonTree.Type t) {
        final Singleton<JCExpression>result =
            new Singleton<JCExpression>();
        t.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.Void v) {
                result.thing = make.TypeIdent(VOID);
            }
            public void visit(CeylonTree.TypeName name) {
                result.thing = makeIdent(name.components());
            }
        });
        return result.thing();
    }
    
    public JCClassDecl convert(CeylonTree.ClassDeclaration cdecl) {
        final Accumulator<JCVariableDecl> params = 
            new Accumulator<JCVariableDecl>();
        
        cdecl.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.FormalParameter param) {
                JCExpression vartype = makeIdent(param.type().name().components());
                JCVariableDecl var = make.VarDef(make.Modifiers(PUBLIC), makeName(param.names), vartype, null);
                System.out.println(var);
                params.append(var);
            }
            
            public void visit(CeylonTree.Block b) {
                
            }
        });
        
        JCClassDecl classDef = 
            make.ClassDef(make.Modifiers(PUBLIC, List.<JCTree.JCAnnotation>nil()),
                    names.fromString(cdecl.nameAsString()),
                    List.<JCTypeParameter>nil(), null,
                    List.<JCExpression>nil(),
                    List.<JCTree>nil());

        System.out.println(classDef);

        return classDef;
    }

    public JCBlock convert(CeylonTree.Block block) {
        final Accumulator<JCStatement> stmts =
            new Accumulator<JCStatement>();
        
        for (CeylonTree stmt: block.getStmts()) 
            stmt.accept(new CeylonTree.Visitor () {
                public void visit(CeylonTree.CallExpression expr) {
                    stmts.append(make.Exec(convert(expr)));
            }});
        
        return make.Block(0, stmts.asList());
    }

    JCExpression convert(CeylonTree.CallExpression ce) {
        final Singleton<JCExpression> expr =
            new Singleton<JCExpression>();
        final Accumulator<JCExpression> args =
            new Accumulator<JCExpression>();
        
        ce.getMethod().accept (new CeylonTree.Visitor () {
            public void visit(CeylonTree.OperatorDot access) {
                expr.append(convert(access));
            }});
        
        for (CeylonTree arg: ce.args())
            args.append(convertArg(arg));
          
        return make.Apply(null, expr.thing(), args.asList());
    }
    
    JCExpression convertArg(CeylonTree arg) {
        return convertExpression(arg);
    }
    
    JCExpression convert(CeylonTree.SimpleStringLiteral string) {
        String s = string.value;
        JCLiteral lit = make.Literal (s);
        return make.Apply (null, makeSelect("ceylon", "String", "instance"),
                List.<JCExpression>of(lit));
    }
    
    JCExpression convert(CeylonTree.OperatorDot access)
    {
        final CeylonTree.Name memberName = access.memberName();
        final CeylonTree operand = access.operand();
        
        class V extends CeylonTree.Visitor {
            public JCExpression result;
            public void visit(CeylonTree.MemberName op) {
                result = makeIdent(Arrays.asList(op.name, memberName.name));
            }
            public void visit(CeylonTree.OperatorDot op) {
                result = make.Select(convert(op), names.fromString(memberName.name));
            }
            public void visit(CeylonTree.Operator op) {
                result = make.Select(convertExpression(op), names.fromString(memberName.name));
            }
        }

        V v = new V();
        operand.accept(v);
        return v.result;
    }   

    JCExpression convertExpression(CeylonTree expr) {
        class V extends CeylonTree.Visitor {
            public JCExpression result;

            public void visit(OperatorDot access) {
                result = convert(access);
            }
            public void visit(Operator op) {
                result = convert(op);
            }
            public void visit(NaturalLiteral lit) {
                JCExpression n = make.Literal(lit.value.longValue());
                result = make.Apply (null, makeSelect("ceylon", "Integer", "instance"),
                        List.of(n));
            }
            public void visit(CeylonTree.SimpleStringLiteral string) {
                result = convert(string);
            }
            public void visit(CeylonTree.CallExpression call) {
                result = convert(call);
            }
        }

        V v = new V();
        expr.accept(v);
        return v.result;        
    }
    
    JCExpression convert(CeylonTree.Operator op) {
        JCExpression result;
        CeylonTree[] operands = op.toArray();

        switch (op.kind()) {
        case CeylonParser.PLUS:
            result = make.Apply (null, make.Select(convertExpression(operands[0]),
                    names.fromString("operatorPlus")),
                    List.of(convertExpression(operands[1])));

            break;
            
            default:
                throw new RuntimeException();
        }
        return result;
    }

}


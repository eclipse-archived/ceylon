package com.redhat.ceylon.compiler.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Flags;
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
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

import static com.sun.tools.javac.code.Flags.*;
import static com.sun.tools.javac.code.TypeTags.*;
import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.CeylonTree.*;

public class Gen {
    Context context;
    TreeMaker _make;
    Name.Table names;
    ClassReader reader;
    Resolve resolve;
    JavaCompiler compiler;
    DiagnosticCollector<JavaFileObject> diagnostics;
    JavacFileManager fileManager;
    JavacTaskImpl task;
    Options options;
    LineMap map;
    
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
                Arrays.asList("-g"/* , /* "-verbose", */
                        // "-source", "7", "-XDallowFunctionTypes"
                        ),
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
        _make = TreeMaker.instance(context);
        names = Name.Table.instance(context);
        reader = ClassReader.instance(context);
        resolve = Resolve.instance(context);
    }

    TreeMaker make(CeylonTree t) {
        if (t.source != null)
            return _make.at(map.getStartPosition(t.source.line) + t.source.column);
        return _make;
    }
    
    TreeMaker make() {
        return _make;
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
    
    public void run(CeylonTree.CompilationUnit t) throws IOException {

        CeylonFileObject file = new CeylonFileObject(fileManager.getFileForInput(t.source.path));
        char[] chars = file.getCharContent(true).toString().toCharArray();
        map = Position.makeLineMap(chars, chars.length, false);

        JCCompilationUnit tree = convert(t);
        tree.sourcefile = file;
        
        Iterable<? extends TypeElement> result =
            task.enter(List.of(tree));
        /*Iterable<? extends JavaFileObject> files =*/ task.generate(result);

        System.out.println(diagnostics.getDiagnostics());
    }
    
    public JCCompilationUnit convert(CeylonTree.CompilationUnit t) {
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
                
        t.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.ClassDeclaration decl) {
                defs.append(convert(decl));
            }
            public void visit(CeylonTree.MethodDeclaration decl) {
                // This is a top-level method.  Generate a class with the
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
                
                processMethodDeclaration(decl, params, body, restype, (ListBuffer<JCTypeParameter>)null,
                        annotations, langAnnotations);
                
                JCMethodDecl meth = make(decl).MethodDef(make().Modifiers(PUBLIC|STATIC),
                        names.fromString("run"),
                        make().TypeIdent(VOID),
                        List.<JCTypeParameter>nil(),
                        params.toList(),
                        List.<JCExpression>nil(), body.thing(), null);
                
                
                List<JCTree> innerDefs = List.<JCTree>of(meth);
                
                // FIXME: This is wrong because the annotation registration is done
                // within the scope of the class, but the annotations are lexically
                // outside it.
                if (annotations.length() > 0) {
                    innerDefs = innerDefs.append(registerAnnotations(annotations.toList()));
                }
                
                JCClassDecl classDef = 
                    make(decl).ClassDef(make().Modifiers(PUBLIC, List.<JCAnnotation>nil()),
                            names.fromString(decl.nameAsString()),
                            List.<JCTypeParameter>nil(), makeSelect("ceylon", "Object"),
                            List.<JCExpression>nil(),
                            innerDefs);
                
                defs.append(classDef);
            }
        });

        JCCompilationUnit topLev =
            make(t).TopLevel(List.<JCTree.JCAnnotation>nil(),
                    /* package id*/ null, defs.toList());
        
        topLev.isCeylonProgram = true;
        topLev.lineMap = map;

        System.out.println(topLev);
        return topLev;
    }

    // FIXME: There must be a better way to do this.
    void processMethodDeclaration(final CeylonTree.MethodDeclaration decl, 
            final ListBuffer<JCVariableDecl> params, 
            final Singleton<JCBlock> block,
            final Singleton<JCExpression> restype,
            final ListBuffer<JCTypeParameter> typarams,
            final ListBuffer<JCStatement> annotations,
    		final ListBuffer<JCAnnotation> langAnnotations) {

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
        
        processAnnotations(decl.annotations, annotations, langAnnotations, decl.nameAsString());
        
        CeylonTree.TypeName name = decl.returnType.name();
        if (name != null)
            restype.append(makeIdent(name.components()));
    }                

    void processAnnotations(List<CeylonTree.Annotation> ceylonAnnos,
    		final ListBuffer<JCStatement> annotations,
    		final ListBuffer<JCAnnotation> langAnnotations,
    		final String declName) {

    	class V extends CeylonTree.Visitor {
    		public void visit(CeylonTree.UserAnnotation userAnn) {
    			annotations.append(make().Exec(convert(userAnn, declName)));
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

    	/* if (! v.optional) {
    		JCAnnotation ann = make().Annotation(makeSelect("ceylon", "nonOptional"),
    				List.<JCExpression>nil());
    		langAnnotations.append(ann);
    	} */
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

    JCExpression convert(CeylonTree.UserAnnotation userAnn, String methodName) {
       List<JCExpression> values = List.<JCExpression>nil();
        for (CeylonTree expr: userAnn.values()) {
            values = values.append(convertExpression(expr));
        }
        JCExpression result = make().Apply(null, makeSelect(userAnn.name, "run"),
                values);
        JCIdent addAnnotation = make(userAnn).Ident(names.fromString("addAnnotation"));
        List<JCExpression> args =
        	methodName != null ?
        			(List.<JCExpression>of(ceylonLiteral(methodName), result)) :
        				List.<JCExpression>of(result);
        					
        result = make().Apply(null, addAnnotation, args);
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
           
            result = make(value).Apply (null, makeSelect("ceylon", "Method", "instance"),
                    List.<JCExpression>of(
                            make(value).Literal(toFlatName(v.result))));
        }

        result.setPos(Position.encodePosition(value.source.line, value.source.column));
        return result;
    }
    
    JCExpression optionalType(JCExpression type) {
    	return make().TypeApply(makeSelect("ceylon", "Optional"), List.<JCExpression>of(type));
    }
    
    JCVariableDecl convert(CeylonTree.FormalParameter param) {
        make(param);
        Name name = names.fromString(param.name());
        JCExpression type = variableType(param.type(), param.annotations());
        if ((param.flags & CeylonTree.OPTIONAL) != 0)
        	type = optionalType(type);
       
        JCVariableDecl v = make(param).VarDef(make().Modifiers(0), name,
                type, null);
        
        return v;
    }

    JCExpression variableType(CeylonTree.Type t, List<Annotation> annotations) {
        final Singleton<JCExpression>result =
            new Singleton<JCExpression>();
        t.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.Void v) {
                result.thing = make().TypeIdent(VOID);
            }
            public void visit(CeylonTree.TypeName name) {
                result.thing = makeIdent(name.components());
            }
        });
        
        return result.thing();
    }
    
    public JCClassDecl convert(final CeylonTree.ClassDeclaration cdecl) {
        final ListBuffer<JCVariableDecl> params = 
            new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCTree> defs =
            new ListBuffer<JCTree>();
        final ListBuffer<JCStatement> annotations = 
            new ListBuffer<JCStatement>();
		final ListBuffer<JCAnnotation> langAnnotations =
			new ListBuffer<JCAnnotation>();

        
        cdecl.visitChildren(new CeylonTree.Visitor () {
            public void visit(CeylonTree.FormalParameter param) {
                JCExpression vartype = makeIdent(param.type().name().components());
                JCVariableDecl var = make(cdecl).VarDef(make().Modifiers(PUBLIC), 
                		makeName(param.names), vartype, null);
                System.out.println(var);
                params.append(var);
            }
            
            public void visit(CeylonTree.Block b) {
                b.visitChildren(this);
            }
            
            public void visit(CeylonTree.MethodDeclaration meth) {
                defs.appendList(convert(meth));
            }
            
            public void visit(CeylonTree.LanguageAnnotation ann) {
                // Handled in processAnnotations
            }
            
            public void visit(CeylonTree.UserAnnotation userAnn) {
                // Handled in processAnnotations
            }
            
            public void visit(CeylonTree.MemberDeclaration mem) {
            	for (JCTree def: convert(mem))
            		defs.append(def);
            }
         });
        
        processAnnotations(cdecl.annotations, annotations, langAnnotations, 
                           cdecl.nameAsString());
        
        if (annotations.length() > 0) {
            defs.append(registerAnnotations(annotations.toList()));
        }
        
        JCClassDecl classDef = 
            make(cdecl).ClassDef(make().Modifiers(PUBLIC, langAnnotations.toList()),
                    names.fromString(cdecl.nameAsString()),
                    List.<JCTypeParameter>nil(), makeSelect("ceylon", "Object"),
                    List.<JCExpression>nil(),
                    defs.toList());

        System.out.println(classDef);

        return classDef;
    }

    public List<JCTree> convert(CeylonTree.MethodDeclaration decl) {
        final ListBuffer<JCVariableDecl> params = 
            new ListBuffer<JCVariableDecl>();
        final ListBuffer<JCStatement> annotations = 
            new ListBuffer<JCStatement>();
    	final ListBuffer<JCAnnotation> langAnnotations =
    		new ListBuffer<JCAnnotation>();
        final Singleton<JCBlock> body = 
            new Singleton<JCBlock>();
        Singleton<JCExpression> restype =
            new Singleton<JCExpression>();

        processMethodDeclaration(decl, params, body, restype, (ListBuffer<JCTypeParameter>)null,
                annotations, langAnnotations);

        JCMethodDecl meth = make(decl).MethodDef(make().Modifiers(PUBLIC),
                names.fromString(decl.nameAsString()),
                restype.thing(),
                List.<JCTypeParameter>nil(),
                params.toList(),
                List.<JCExpression>nil(), body.thing(), null);;
        
        if (annotations.length() > 0) {
            // FIXME: Method annotations.
        	JCBlock b = registerAnnotations(annotations.toList());
            return List.<JCTree>of(meth, b);
        }

      return List.<JCTree>of(meth);
    }

    
    public JCBlock convert(CeylonTree.Block block) {
        return make(block).Block(0, convertStmts(block.getStmts()));
    }
    
    List<JCStatement> convertStmts(List<CeylonTree> stmts) {
        final ListBuffer<JCStatement> buf =
            new ListBuffer<JCStatement>();
        
        CeylonTree.Visitor v = new CeylonTree.Visitor () {
            public void visit(CeylonTree.CallExpression expr) {
                buf.append(make(expr).Exec(convert(expr)));
            }
            public void visit(CeylonTree.ReturnStatement ret) {
                buf.append(convert(ret));
            }
            public void visit(CeylonTree.IfStatement stat) {
                buf.append(convert(stat));
            }
            public void visit(CeylonTree.MemberDeclaration decl) {
               	for (JCTree def: convert(decl))
                     buf.append((JCStatement)def);
            }};
            
        for (CeylonTree stmt: stmts) 
        	stmt.accept(v);
            
        return buf.toList();
    }

    JCStatement convert(CeylonTree.IfStatement stmt) {
    	JCBlock thenPart = convert(stmt.ifTrue);
    	JCBlock elsePart = convert(stmt.ifFalse);
    	
    	if (stmt.condition.operand instanceof CeylonTree.ExistsExpression) {
    		CeylonTree.ExistsExpression exists =
    			(CeylonTree.ExistsExpression)stmt.condition.operand;
    		CeylonTree.MemberName name = exists.name;
    		JCExpression type = variableType(exists.type, null);
    		JCExpression expr = convertExpression(exists.expr);
    		expr = make(stmt).Apply(null, 
    				make(stmt).Select(expr, names.fromString("$internalErasedExists")),
    					List.<JCExpression>nil());
    		
    		Name tmp = names.fromString(name.asString());
    		JCVariableDecl decl = 
            	make(stmt).VarDef
            			(make().Modifiers(0), tmp, type, expr);
    		
        	JCStatement cond = 
        		make(stmt).If(make(stmt).Binary
        				(JCTree.NE, make().Ident(tmp),
        						make().Literal(TypeTags.BOT, null)), 
        						thenPart, elsePart);

        	JCBlock result = make().Block(0, List.<JCStatement>of(decl, cond));
        	
    		return result;
    	}
    	JCExpression cond = convertExpression(stmt.condition);
    	JCStatement result = make(stmt).If(cond, thenPart, elsePart);
    	
    	return result;
    }
    
    JCExpression convert(CeylonTree.CallExpression ce) {
        final Singleton<JCExpression> expr =
            new Singleton<JCExpression>();
        final ListBuffer<JCExpression> args =
            new ListBuffer<JCExpression>();
        
        ce.getMethod().accept (new CeylonTree.Visitor () {
            public void visit(CeylonTree.OperatorDot access) {
                expr.append(convert(access));
            }
            public void visit(CeylonTree.MemberName access) {
                expr.append(convert(access));
            }});
        
        for (CeylonTree arg: ce.args())
            args.append(convertArg(arg));
          
        JCExpression call = make(ce).Apply(null, expr.thing(), args.toList());
        return call;
    }
    
    JCExpression convertArg(CeylonTree arg) {
        return convertExpression(arg);
    }
    
    JCExpression ceylonLiteral(String s) {
    	JCLiteral lit = make().Literal(s);
        return make().Apply (null, makeSelect("ceylon", "String", "instance"),
                List.<JCExpression>of(lit));
    }
    
    JCExpression convert(CeylonTree.SimpleStringLiteral string) {
    	make(string);
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
                result = make(access).Select(convert(op), names.fromString(memberName.name));
            }
            public void visit(CeylonTree.Operator op) {
                result = make(access).Select(convertExpression(op), names.fromString(memberName.name));
            }
        }

        V v = new V();
        operand.accept(v);
        return v.result;
    }   

    JCStatement convert(CeylonTree.ReturnStatement ret) {
        JCExpression returnExpr = convertExpression(ret.expr());
        return make(ret).Return(returnExpr);
    }
    
    JCIdent convert(CeylonTree.MemberName member) {
        return make(member).Ident(names.fromString(member.asString()));
    }
    
    List<JCStatement> convert(CeylonTree.MemberDeclaration decl) {
    	make(decl);
    	
    	JCExpression initialValue = null;
    	if (decl.initialValue() != null)
    		initialValue = convertExpression(decl.initialValue());
    		
    	final ListBuffer<JCAnnotation> langAnnotations =
    		new ListBuffer<JCAnnotation>();
    	final ListBuffer<JCStatement> annotations = 
            new ListBuffer<JCStatement>();
        processAnnotations(decl.annotations, annotations, langAnnotations, decl.nameAsString());
    	
        JCExpression type = makeIdent(decl.type.name().components());
        
        if ((decl.flags & CeylonTree.OPTIONAL) != 0)
        	type = optionalType(type);
        
        List<JCStatement> result = 
        	List.<JCStatement>of(make(decl).VarDef
        			(make().Modifiers(0, langAnnotations.toList()), 
        					names.fromString(decl.nameAsString()),
        					type, 
        					initialValue));

        if (annotations.length() > 0) {
        	result = result.append(registerAnnotations(annotations.toList()));
        }

        return result;
    }
    
    JCExpression convertExpression(final CeylonTree expr) {
        class V extends CeylonTree.Visitor {
            public JCExpression result;

            public void visit(OperatorDot access) {
                result = convert(access);
            }
            public void visit(Operator op) {
                result = convert(op);
            }
            public void visit(NaturalLiteral lit) {
                JCExpression n = make(expr).Literal(lit.value.longValue());
                result = make(expr).Apply (null, makeSelect("ceylon", "Integer", "instance"),
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
            public void visit(CeylonTree.InitializerExpression value) {
                result = convertExpression(value.value());
            }
            public void visit(CeylonTree.Null value) {
            	result = makeSelect("ceylon", "Nothing", "NULL");
            }
            public void visit(CeylonTree.Condition value) {
            	result = convertExpression(value.operand);
            }
          }

        V v = new V();
        expr.accept(v);
        return v.result;
    }

    private static Map<Integer, String> operatorImplementors;

    static {
        operatorImplementors = new HashMap<Integer, String>();

        // Operators that act on the types themselves
        operatorImplementors.put(CeylonParser.PLUS,       "plus");
        operatorImplementors.put(CeylonParser.MINUS,      "minus");
        operatorImplementors.put(CeylonParser.TIMES,      "times");
        operatorImplementors.put(CeylonParser.DIVIDED,    "divided");
        operatorImplementors.put(CeylonParser.POWER,      "power");
        operatorImplementors.put(CeylonParser.REMAINDER,  "remainder");
        operatorImplementors.put(CeylonParser.BITWISEAND, "and");
        operatorImplementors.put(CeylonParser.BITWISEOR,  "or");
        operatorImplementors.put(CeylonParser.BITWISEXOR, "xor");
        operatorImplementors.put(CeylonParser.EQEQ,       "operatorEqual");
        operatorImplementors.put(CeylonParser.IDENTICAL,  "operatorIdentical");
        operatorImplementors.put(CeylonParser.NOTEQ,      "operatorNotEqual");
        operatorImplementors.put(CeylonParser.COMPARE,    "compare");

        // Operators that act on Comparison objects
        operatorImplementors.put(CeylonParser.GT,         "larger");
        operatorImplementors.put(CeylonParser.LT,         "smaller");
        operatorImplementors.put(CeylonParser.GTEQ,       "largeAs");
        operatorImplementors.put(CeylonParser.LTEQ,       "smallAs");
    }

    JCExpression convert(CeylonTree.Operator op) {
        boolean binary_operator = false;
        boolean lose_comparison = false;

        int operator = op.operatorKind;
        switch (operator) {
        case CeylonParser.PLUS:
        case CeylonParser.MINUS:
        case CeylonParser.TIMES:
        case CeylonParser.POWER:
        case CeylonParser.DIVIDED:
        case CeylonParser.REMAINDER:
        case CeylonParser.BITWISEAND:
        case CeylonParser.BITWISEOR:
        case CeylonParser.BITWISEXOR:
        case CeylonParser.EQEQ:
        case CeylonParser.IDENTICAL:
        case CeylonParser.NOTEQ:
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

        default:
            throw new RuntimeException(CeylonParser.tokenNames[op.operatorKind]);
        }

        CeylonTree[] operands = op.toArray();

        JCExpression result = null;
        if (binary_operator) {
            result = make(op).Apply(null,
                                    make(op).Select(convertExpression(operands[0]),
                                                    names.fromString(operatorImplementors.get(operator))),
                                    List.of(convertExpression(operands[1])));

            if (lose_comparison) {
                result = make(op).Apply(null,
                                        make(op).Select(result,
                                                        names.fromString(operatorImplementors.get(op.operatorKind))),
                                        List.<JCExpression>nil());
            }
        }

        return result;
    }

}


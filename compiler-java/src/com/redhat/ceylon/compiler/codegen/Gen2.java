package com.redhat.ceylon.compiler.codegen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LocalModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.code.BoundKind;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.parser.Keywords;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Position.LineMap;

public class Gen2 {
    private TreeMaker make;
    Name.Table names;
    private CeyloncFileManager fileManager;
    private LineMap map;
    Symtab syms;
    Keywords keywords;
    CeylonModelLoader modelLoader;
    private Map<String, String> varNameSubst = new HashMap<String, String>();
    
    ExpressionGen expressionGen;
    StatementGen statementGen;
    ClassGen classGen;
    GlobalGen globalGen;

    private boolean disableModelAnnotations = false;
    
    public static Gen2 getInstance(Context context) throws Exception {
        Gen2 gen2 = context.get(Gen2.class);
        if (gen2 == null) {
            gen2 = new Gen2(context);
            context.put(Gen2.class, gen2);
        }
        return gen2;
    }

    public Gen2(Context context) {
        setup(context);
        expressionGen = new ExpressionGen(this);
        statementGen = new StatementGen(this);
        classGen = new ClassGen(this);
        globalGen = new GlobalGen(this);
    }

    private void setup(Context context) {
        Options options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
        make = TreeMaker.instance(context);

        names = Name.Table.instance(context);
        syms = Symtab.instance(context);
        keywords = Keywords.instance(context);
        modelLoader = CeylonModelLoader.instance(context);

        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
    }

    JCTree.Factory at(Node t) {
        CommonTree antlrTreeNode = t.getAntlrTreeNode();
        Token token = antlrTreeNode.getToken();
        if (token != null) {
            make.at(getMap().getStartPosition(token.getLine()) + token.getCharPositionInLine());
        }
        return make;
    }

    TreeMaker make() {
        return make;
    }

    public GlobalGen globalGen() {
        return globalGen;
    }

    public GlobalGen globalGenAt(Node t) {
        at(t);
        return globalGen;
    }

    static class Singleton<T> implements Iterable<T> {
        private T thing;

        Singleton() {
        }

        Singleton(T t) {
            thing = t;
        }

        List<T> asList() {
            return List.of(thing);
        }

        void append(T t) {
            if (thing != null)
                throw new RuntimeException();
            thing = t;
        }

        public Iterator<T> iterator() {
            return asList().iterator();
        }

        public T thing() {
            return this.thing;
        }

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
        assert (!iterator.hasNext());
        return names.fromString(s);
    }

    String toFlatName(Iterable<String> components) {
        StringBuffer buf = new StringBuffer();
        Iterator<String> iterator;

        for (iterator = components.iterator(); iterator.hasNext();) {
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
                type = makeSelect(type, component.getText());
        }

        return type;
    }

    public JCExpression makeIdent(Iterable<String> components) {

        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make().Ident(names.fromString(component));
            else
                type = makeSelect(type, component);
        }

        return type;
    }

    public JCExpression makeIdent(String... components) {

        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make().Ident(names.fromString(component));
            else
                type = makeSelect(type, component);
        }

        return type;
    }

    JCExpression makeIdent(String nameAsString) {
        return makeIdent(List.of(nameAsString));
    }

    JCExpression makeIdent(com.sun.tools.javac.code.Type type) {
        return make.QualIdent(type.tsym);
    }

    // FIXME: port handleOverloadedToplevelClasses when I figure out what it
    // does

    /**
     * This runs after _some_ typechecking has been done
     */
    public ListBuffer<JCTree> convertAfterTypeChecking(Tree.CompilationUnit t) {
        final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
        disableModelAnnotations = false;
        t.visitChildren(new Visitor() {
            public void visit(Tree.ImportList imp) {
                defs.appendList(convert(imp));
            }

            private void checkCompilerAnnotations(Tree.Declaration decl){
                if(hasCompilerAnnotation(decl, "nomodel"))
                    disableModelAnnotations  = true;
            }

            private void resetCompilerAnnotations(){
                disableModelAnnotations = false;
            }
            
            public void visit(Tree.ClassOrInterface decl) {
                checkCompilerAnnotations(decl);
                defs.append(classGen.convert(decl));
                resetCompilerAnnotations();
            }

            public void visit(Tree.ObjectDefinition decl) {
                checkCompilerAnnotations(decl);
                defs.append(classGen.objectClass(decl, true));
                resetCompilerAnnotations();
            }
            
            public void visit(Tree.AttributeDeclaration decl){
                checkCompilerAnnotations(decl);
                defs.append(classGen.convert(decl));
                resetCompilerAnnotations();
            }

            public void visit(Tree.MethodDefinition decl) {
                checkCompilerAnnotations(decl);
                // Generate a class with the
                // name of the method and a corresponding run() method.
                defs.append(classGen.methodClass(decl));
                resetCompilerAnnotations();
            }
        });
        return defs;
    }

    /**
     * In this pass we only make an empty placeholder which we'll fill in the
     * EnterCeylon phase later on
     */
    public JCCompilationUnit makeJCCompilationUnitPlaceholder(Tree.CompilationUnit t, JavaFileObject file, String pkgName) {
        System.err.println(t);
        JCExpression pkg = pkgName != null ? getPackage(pkgName) : null;
        at(t);
        JCCompilationUnit topLev = new CeylonCompilationUnit(List.<JCTree.JCAnnotation> nil(), pkg, List.<JCTree> nil(), null, null, null, null, t);

        topLev.lineMap = getMap();
        topLev.sourcefile = file;
        topLev.isCeylonProgram = true;

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

    private List<JCTree> convert(Tree.ImportList importList) {
        final ListBuffer<JCTree> imports = new ListBuffer<JCTree>();
        importList.visit(new Visitor() {
            // FIXME: handle the rest of the cases here
            public void visit(Tree.ImportPath that) {
                JCImport stmt = at(that).Import(makeIdentFromIdentifiers(that.getIdentifiers()), false);
                imports.append(stmt);
            }
        });
        return imports.toList();
    }

    static class ExpressionVisitor extends Visitor {
        public JCExpression result;
    }

    static class ListVisitor<T> extends Visitor {
        public List<T> result = List.<T> nil();
    }

    // FIXME: figure out what CeylonTree.ReflectedLiteral maps to

    JCExpression iteratorType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonIteratorType), List.<JCExpression> of(type));
    }

    long counter = 0;

    String tempName() {
        String result = "$ceylontmp" + counter;
        counter++;
        return result;
    }

    String tempName(String s) {
        String result = "$ceylontmp" + s + counter;
        counter++;
        return result;
    }

    String aliasName(String s) {
        String result = "$" + s + "$" + counter;
        counter++;
        return result;
    }
    
    // A type is optional when it is a union of Nothing|Type...
    boolean isOptional(ProducedType type) {
        return (type.getDeclaration() instanceof UnionType && type.getDeclaration().getCaseTypes().size() > 1 && toPType(syms.ceylonNothingType).isSubtypeOf(type));
    }

    // FIXME: this is ugly and probably wrong
    boolean isSameType(Tree.Identifier ident, com.sun.tools.javac.code.Type type) {
        return ident.getText().equals(type.tsym.getQualifiedName());
    }

    public void setMap(LineMap map) {
        this.map = map;
    }

    public LineMap getMap() {
        return map;
    }

    public String addVariableSubst(String origVarName, String substVarName) {
        return varNameSubst.put(origVarName, substVarName);
    }

    public void removeVariableSubst(String origVarName, String prevSubst) {
        if (prevSubst != null) {
            varNameSubst.put(origVarName, prevSubst);
        } else {
            varNameSubst.remove(origVarName);
        }
    }
    
    public String substitute(String varName) {
        if (varNameSubst.containsKey(varName)) {
            return varNameSubst.get(varName);            
        } else {
            return varName;
        }
    }

    private ProducedType toPType(com.sun.tools.javac.code.Type t) {
        return modelLoader.getType(t.tsym.getQualifiedName().toString(), null);
    }
    
    private boolean isUnion(ProducedType type) {
        TypeDeclaration tdecl = type.getDeclaration();
        return (tdecl instanceof UnionType && tdecl.getCaseTypes().size() > 1);
    }
    
    private boolean hasUnion(java.util.List<ProducedType> types) {
        for (ProducedType t : types) {
            if (isUnion(simplifyType(t))) {
                return true;
            }
        }
        return false;
    }
    
    // Determines if a type will be erased once converted to Java
    public boolean willErase(ProducedType type) {
        type = simplifyType(type);
        return (toPType(syms.ceylonVoidType).isExactly(type) || toPType(syms.ceylonObjectType).isExactly(type)
                || toPType(syms.ceylonNothingType).isExactly(type) || toPType(syms.ceylonEqualityType).isExactly(type)
                || toPType(syms.ceylonIdentifiableObjectType).isExactly(type)
                || type.getDeclaration() instanceof BottomType
                || isUnion(type));
    }
    
    public JCExpression makeJavaType(ProducedType type, boolean isSatisfiesOrExtends) {
        if (willErase(type)) {
            // For an erased type:
            // - Any of the Ceylon types Void, Object, Nothing, Equality,
            //   IdentifiableObject, and Bottom result in the Java type Object
            // For any other union type U|V (U nor V is Optional):
            // - The Ceylon type U|V results in the Java type Object
            return makeIdent("Object");
        }
        
        JCExpression jt;
        type = simplifyType(type);
        java.util.List<ProducedType> tal = type.getTypeArgumentList();
        if (tal != null && !tal.isEmpty()) {
            if (!hasUnion(tal)) {
                // GENERIC TYPES
                TypeDeclaration tdecl = type.getDeclaration();
                
                ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();
    
                int idx = 0;
                for (ProducedType ta : tal) {
                    JCExpression jta;
                    if (isSatisfiesOrExtends) {
                        // For an ordinary class or interface type T:
                        // - The Ceylon type Foo<T> appearing in an extends or satisfies clause
                        //   results in the Java type Foo<T>
                        jta = makeJavaType(ta, true);
                    } else {
                        // For an ordinary class or interface type T:
                        // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
                        // - Foo<T> if Foo is invariant in T,
                        // - Foo<? extends T> if Foo is covariant in T, or
                        // - Foo<? super T> if Foo is contravariant in T
                        TypeParameter tp = tdecl.getTypeParameters().get(idx);
                        if (tp.isContravariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.SUPER), makeJavaType(ta, false));
                        } else if (tp.isCovariant()) {
                            jta = make().Wildcard(make().TypeBoundKind(BoundKind.EXTENDS), makeJavaType(ta, false));
                        } else {
                            jta = makeJavaType(ta, false);
                        }
                    }
                    typeArgs.add(jta);
                    idx++;
                }
    
                jt = make().TypeApply(makeIdent(tdecl.getName()), typeArgs.toList());
            } else {
                // For any other union type U|V (U nor V is Optional):
                // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
                jt = makeIdent(type.getDeclaration().getName());
            }
        } else {
            // For an ordinary class or interface type T:
            // - The Ceylon type T results in the Java type T
            jt = makeIdent(type.getProducedTypeName());
        }
        
        return jt;
    }

    public List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Method decl, ProducedType type) {
        return makeJavaTypeAnnotations(type, decl.isShared() || decl.isToplevel());
    }

    public List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Parameter decl, ProducedType type) {
        Scope container = decl.getContainer();
        List<JCTree.JCAnnotation> ret;
        // if method, rely on method rules
        if(container instanceof Method)
            ret = makeJavaTypeAnnotations((Method) container, type);
        else
            // if not method must be a Class, so always true
            ret = makeJavaTypeAnnotations(type, true);
        
        if(!ret.isEmpty())
            ret = ret.prepend(makeAtName(decl.getName()));
        return ret;
    }

    public List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Value decl, ProducedType type) {
        return makeJavaTypeAnnotations(type, decl.isToplevel() || (decl.isClassOrInterfaceMember() && decl.isShared()));
    }

    public List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Getter decl, ProducedType type) {
        return makeJavaTypeAnnotations(type, decl.isToplevel() || (decl.isClassOrInterfaceMember() && decl.isShared()));
    }

    public List<JCTree.JCAnnotation> makeJavaTypeAnnotations(Setter decl, ProducedType type) {
        return makeJavaTypeAnnotations(type, decl.isToplevel() || (decl.isClassOrInterfaceMember() && decl.isShared()));
    }

    private List<JCTree.JCAnnotation> makeJavaTypeAnnotations(ProducedType type, boolean required) {
        if(!required || disableModelAnnotations)
            return List.nil();
        // Add the original type to the annotations
        return List.of(makeAtType(type.getProducedTypeQualifiedName()));
    }
    
    private ProducedType simplifyType(ProducedType type) {
        if (isOptional(type)) {
            // For an optional type T?:
            //  - The Ceylon type T? results in the Java type T
            // Nasty cast because we just so happen to know that nothingType is a Class
            type = type.minus((ClassOrInterface)(toPType(syms.ceylonNothingType).getDeclaration()));
        }
        
        TypeDeclaration tdecl = type.getDeclaration();
        if (tdecl instanceof UnionType && tdecl.getCaseTypes().size() == 1) {
            // Special case when the Union contains only a single CaseType
            // FIXME This is not correct! We might lose information about type arguments!
            type = tdecl.getCaseTypes().get(0);
        }
        
        return type;
    }
    
    public ProducedType actualType(TypedDeclaration decl) {
        ProducedType t = decl.getType().getTypeModel();
        if (decl.getType() instanceof LocalModifier) {
            LocalModifier m = (LocalModifier)(decl.getType());
            t = m.getTypeModel();
        }
        return t;
    }

    public JCAnnotation makeAtOverride() {
        return make().Annotation(makeIdent(syms.overrideType), List.<JCExpression> nil());
    }

    public JCAnnotation makeAtName(String name) {
        return make().Annotation(makeIdent(syms.ceylonAtNameType), List.<JCExpression> of(make().Literal(name)));
    }

    public JCAnnotation makeAtType(String name) {
        return make().Annotation(makeIdent(syms.ceylonAtTypeInfoType), List.<JCExpression> of(make().Literal(name)));
    }

    protected boolean isJavaKeyword(Name name) {
        return keywords.key(name) != com.sun.tools.javac.parser.Token.IDENTIFIER;
    }

    protected Name quoteName(String text) {
        Name name = names.fromString(text);

        if (isJavaKeyword(name)) {
            return names.fromString('$' + text);
        }

        return name;
    }
    
    protected boolean hasCompilerAnnotation(Tree.Declaration decl, String name){
        for(CompilerAnnotation annotation : decl.getCompilerAnnotations()){
            if(annotation.getIdentifier().getText().equals(name))
                return true;
        }
        return false;
    }
}

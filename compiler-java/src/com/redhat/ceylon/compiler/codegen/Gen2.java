package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.TypeTags.VOID;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LocalModifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.code.Symtab;
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
    CeylonModelLoader modelLoader;
    private Map<String, String> varNameSubst = new HashMap<String, String>();
    
    ExpressionGen expressionGen = new ExpressionGen(this);
    StatementGen statementGen = new StatementGen(this);
    ClassGen classGen = new ClassGen(this);

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
    }

    private void setup(Context context) {
        Options options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
        make = TreeMaker.instance(context);

        names = Name.Table.instance(context);
        syms = Symtab.instance(context);
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
        t.visitChildren(new Visitor() {
            public void visit(Tree.ImportList imp) {
                defs.appendList(convert(imp));
            }

            public void visit(Tree.ClassOrInterface decl) {
                defs.append(classGen.convert(decl));
            }

            public void visit(Tree.ObjectDefinition decl) {
                defs.append(classGen.objectClass(decl, true));
            }
            
            public void visit(Tree.AttributeDeclaration decl){
                defs.append(classGen.convert(decl));
            }

            public void visit(Tree.MethodDefinition decl) {
                // Generate a class with the
                // name of the method and a corresponding run() method.
                defs.append(classGen.methodClass(decl, true));
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
    
    // A type is optional when it is a union of Nothing|Type
    boolean isOptional(ProducedType type) {
        // FIXME VERY naive implementation!!
        // Should be something like type.isSubtypeOf(nothingType)
        ProducedType nothingType = modelLoader.getType("ceylon.language.Nothing", null);
        java.util.List<ProducedType> types = type.getDeclaration().getCaseTypes();
        for (ProducedType t : types) {
            if (t.isExactly(nothingType)) {
                return true;
            }
        }
        return false;
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

    public JCExpression makeJavaType(ProducedType type) {
        if (isOptional(type)) {
            ProducedType nothingType = modelLoader.getType("ceylon.language.Nothing", null);
            // Nasty cast because we just so happen to know that nothingType is a Class
            type = type.minus((ClassOrInterface)(nothingType.getDeclaration()));
        }
        
        ProducedType voidType = modelLoader.getType("ceylon.language.Void", null);
        if (voidType.isExactly(type)) {
            return make.TypeIdent(VOID);
        }
        
        JCExpression jt; 
        if (type.getDeclaration().getCaseTypes().isEmpty()) {
            java.util.List<ProducedType> tal = type.getTypeArgumentList();
            if (tal != null && !tal.isEmpty()) {
                ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();
    
                for (ProducedType innerType : tal) {
                    typeArgs.add(makeJavaType(innerType));
                }
    
                jt = make().TypeApply(makeIdent(type.getDeclaration().getName()), typeArgs.toList());
            } else {
                jt = makeIdent(type.getProducedTypeName());
            }
        } else {
            // FIXME This should return the common base class of all the union types
            // but we have to wait until this is implemented in the typechecker
            jt = makeIdent("Object");
        }
        
        return jt;
    }

    public List<JCTree.JCAnnotation> makeJavaTypeAnnotations(ProducedType type, boolean force) {
        // For shared types we keep a list of annotations to apply to the resulting Java type
        // to make reverse engineering of the final class file possible
        boolean applyAnnotations = false; // type.getDeclaration().isShared() || force;
        ListBuffer<JCTree.JCAnnotation> annotations = new ListBuffer<JCTree.JCAnnotation>();
        
        if (applyAnnotations) {
            // Add the original type to the annotations
            annotations.append(makeAtType(type.getProducedTypeQualifiedName()));
        }
        
        return annotations.toList();
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
        // FIXME Using plain "Name" is probably not correct
        return make().Annotation(makeIdent("Name"), List.<JCExpression> of(make().Literal(name)));
    }

    public JCAnnotation makeAtType(String name) {
        // FIXME Using plain "TypeInfo" is probably not correct
        return make().Annotation(makeIdent("TypeInfo"), List.<JCExpression> of(make().Literal(name)));
    }
}

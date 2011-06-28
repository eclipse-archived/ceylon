package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.TypeTags.VOID;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
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
    ExpressionGen expressionGen = new ExpressionGen(this);
    StatementGen statementGen = new StatementGen(this);
    ClassGen classGen = new ClassGen(this);
	private Map<String, String> varNameSubst = new HashMap<String, String>();

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

    public JCExpression makeIdent(String... components) {

        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = make().Ident(names.fromString(component));
            else
                type = make().Select(type, names.fromString(component));
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
                defs.append(classGen.convert(decl));
            }
            
            public void visit(Tree.AttributeDeclaration decl){
                defs.append(classGen.convert(decl));
            }

            public void visit(Tree.MethodDefinition decl) {
                classGen.methodClass(null, decl, defs, true);
            }
        });
        return defs;
    }

    /**
     * In this pass we only make an empty placeholder which we'll fill in the
     * EnterCeylon phase later on
     */
    public JCCompilationUnit makeJCCompilationUnitPlaceholder(Tree.CompilationUnit t, JavaFileObject file) {
        System.err.println(t);
        String[] prefixes = fileManager.getSourcePath();
        JCExpression pkg = null;

        // Figure out the package name by stripping the "-src" prefix and
        // extracting
        // the package part of the fullname.
        for (String prefix : prefixes) {
            if (prefix != null && file.toString().startsWith(prefix)) {
                String fullname = file.toString().substring(prefix.length());
                assert fullname.endsWith(".ceylon");
                fullname = fullname.substring(0, fullname.length() - ".ceylon".length());
                fullname = fullname.replace(File.separator, ".");
                String packageName = Convert.packagePart(fullname);
                if (!packageName.equals(""))
                    pkg = getPackage(packageName);
            }
        }
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

    JCExpression convert(Tree.Type type) {
        JCExpression result;

        // FIXME: handle sequences
        ExpressionVisitor v = new ExpressionVisitor() {

            public void visit(Tree.SimpleType t) {
                result = makeIdent(t.getIdentifier().getText());

                Tree.TypeArgumentList tal = t.getTypeArgumentList();
                if (tal != null) {
                    ListBuffer<JCExpression> typeArgs = new ListBuffer<JCExpression>();

                    for (Tree.Type innerType : tal.getTypes()) {
                        typeArgs.add(convert(innerType));
                    }

                    result = at(t).TypeApply(result, typeArgs.toList());
                }
            }

            // FIXME: Add the other primitive types
            public void visit(Tree.VoidModifier t) {
                result = make.TypeIdent(VOID);
            }
        };

        type.visit(v);
        result = v.result;

        if (isOptional(type)) {
            result = optionalType(result);
        }

        return result;
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

    JCExpression optionalType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonOptionalType), List.<JCExpression> of(type));
    }

    JCExpression iteratorType(JCExpression type) {
        return make().TypeApply(makeIdent(syms.ceylonIteratorType), List.<JCExpression> of(type));
    }

    JCExpression variableType(Tree.Type t, Tree.AnnotationList annotations) {
        return convert(t);
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

    boolean isOptional(Tree.Type type) {
        // This should show in the tree as: Nothing|Type, so we just visit
        class TypeVisitor extends Visitor {
            boolean isOptional = false;

            @Override
            public void visit(Tree.SimpleType t) {
                isOptional |= isSameType(t.getIdentifier(), syms.ceylonNothingType);
            }
        }
        TypeVisitor visitor = new TypeVisitor();
        type.visit(visitor);
        return visitor.isOptional;
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
}

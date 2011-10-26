package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Decl;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class CeylonVisitor extends Visitor implements NaturalVisitor {
    protected final CeylonTransformer gen;
    private final ListBuffer<JCTree> defs;
    
    private final ToplevelAttributesDefinitionBuilder topattrBuilder;
    private final ClassDefinitionBuilder classBuilder;
    private final List<JCExpression> typeArgs;
    private final ListBuffer<JCExpression> args;
    
    public CeylonVisitor(CeylonTransformer ceylonTransformer) {
        this.gen = ceylonTransformer;
        this.defs = new ListBuffer<JCTree>();
        this.topattrBuilder = null;
        this.classBuilder = null;
        this.typeArgs = null;
        this.args = null;
    }

    public CeylonVisitor(CeylonTransformer ceylonTransformer, ToplevelAttributesDefinitionBuilder topattrBuilder) {
        this.gen = ceylonTransformer;
        this.defs = new ListBuffer<JCTree>();
        this.topattrBuilder = topattrBuilder;
        this.classBuilder = null;
        this.typeArgs = null;
        this.args = null;
    }

    public CeylonVisitor(CeylonTransformer ceylonTransformer, ClassDefinitionBuilder classBuilder) {
        this.gen = ceylonTransformer;
        this.defs = new ListBuffer<JCTree>();
        this.topattrBuilder = null;
        this.classBuilder = classBuilder;
        this.typeArgs = null;
        this.args = null;
    }

    public CeylonVisitor(CeylonTransformer ceylonTransformer, List<JCExpression> typeArgs, ListBuffer<JCExpression> args) {
        this.gen = ceylonTransformer;
        this.defs = new ListBuffer<JCTree>();
        this.topattrBuilder = null;
        this.classBuilder = null;
        this.typeArgs = typeArgs;
        this.args = args;
    }
    
    public void handleException(Exception e, Node that) {
        that.getErrors().add(new CodeGenError(that, e.getMessage(), e)); 
    }
    
    /*
     * Compilation Unit
     */
    
    public void visit(Tree.ImportList that) {
        //append(gen.transform(that));
    }
    
    private boolean checkCompilerAnnotations(Tree.Declaration decl){
        boolean old = gen.disableModelAnnotations;
        if(Util.hasCompilerAnnotation(decl, "nomodel"))
            gen.disableModelAnnotations  = true;
        return old;
    }

    private void resetCompilerAnnotations(boolean value){
        gen.disableModelAnnotations = value;
    }

    public void visit(Tree.ClassOrInterface decl) {
        boolean annots = checkCompilerAnnotations(decl);
        if (Decl.withinClass(decl)) {
            classBuilder.defs(gen.classGen().transform(decl));
        } else {
            appendList(gen.classGen().transform(decl));
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.ObjectDefinition decl) {
        boolean annots = checkCompilerAnnotations(decl);
        if (Decl.withinClass(decl)) {
            classBuilder.defs(gen.classGen().transformObject(decl, classBuilder));
        } else {
            appendList(gen.classGen().transformObject(decl, null));
        }
        resetCompilerAnnotations(annots);
    }
    
    public void visit(Tree.AttributeDeclaration decl){
        boolean annots = checkCompilerAnnotations(decl);
        if (Decl.withinPackage(decl)) {
            // Toplevel attributes
            appendList(gen.transform(decl));
        } else if (Decl.withinClassOrInterface(decl)) {
            // Class attributes
            gen.classGen().transform(decl, classBuilder);
        } else if (Decl.withinMethod(decl) && decl.getDeclarationModel().isCaptured()) {
            // Captured local attributes get turned into an inner getter/setter class
            appendList(gen.transform(decl));
        } else {
            // All other local attributes
            append(gen.statementGen().transform(decl));
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.AttributeGetterDefinition decl){
        boolean annots = checkCompilerAnnotations(decl);
        if (Decl.withinClass(decl)) {
            classBuilder.defs(gen.classGen().transform(decl));
        } else if (Decl.withinMethod(decl)) {
            appendList(gen.transform(decl));
        } else {
            topattrBuilder.add(decl);
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(final Tree.AttributeSetterDefinition decl) {
        boolean annots = checkCompilerAnnotations(decl);
        if (Decl.withinClass(decl)) {
            classBuilder.defs(gen.classGen().transform(decl));
        } else if (Decl.withinMethod(decl)) {
            appendList(gen.transform(decl));
        } else {
            topattrBuilder.add(decl);
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.MethodDefinition decl) {
        boolean annots = checkCompilerAnnotations(decl);
        Scope container = decl.getDeclarationModel().getContainer();
        if (container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) {
            boolean isInterface = container instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
            classBuilder.defs(gen.classGen().transform(decl));
            if(isInterface && decl.getBlock() != null)
                classBuilder.concreteInterfaceMemberDefs(gen.classGen().transformConcreteInterfaceMember(decl, ((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)container).getType()));
        } else {
            appendList(gen.classGen().transformWrappedMethod(decl));
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.MethodDeclaration meth) {
        classBuilder.defs(gen.classGen().transform(meth));
    }
    
    /*
     * Class or Interface
     */
    
    // Class Initializer parameter
    public void visit(Tree.Parameter param) {
        classBuilder.parameter(param);
    }

    public void visit(Tree.Block b) {
        b.visitChildren(this);
    }

    public void visit(Tree.Annotation ann) {
        // Handled in processAnnotations
    }

    // FIXME: also support Tree.SequencedTypeParameter
    public void visit(Tree.TypeParameterDeclaration param) {
        classBuilder.typeParameter(param);
    }

    public void visit(Tree.ExtendedType extendedType) {
        classBuilder.extending(extendedType);
    }

    // FIXME: implement
    public void visit(Tree.TypeConstraint l) {
    }

    public void visit(Tree.CaseTypes t){
        // FIXME: ignore for now, probably we'll need to add an annotation for it in M2.
        // no need to warn here since the typechecker already warns for M1's unsupported status
        // we do need to avoid visiting its children since that leads to invalid code otherwise as
        // other node types are handled as if they were the body of the class
    }
    
    /*
     * Statements
     */

    public void visit(Tree.Return ret) {
        append(gen.statementGen().transform(ret));
    }

    public void visit(Tree.IfStatement stat) {
        appendList(gen.statementGen().transform(stat));
    }

    public void visit(Tree.WhileStatement stat) {
        appendList(gen.statementGen().transform(stat));
    }

//    public void visit(Tree.DoWhileStatement stat) {
//        append(gen.statementGen().transform(stat));
//    }

    public void visit(Tree.ForStatement stat) {
        appendList(gen.statementGen().transform(stat));
    }

    public void visit(Tree.Break stat) {
        appendList(gen.statementGen().transform(stat));
    }

    public void visit(Tree.SpecifierStatement op) {
        append(gen.statementGen().transform(op));
    }

    // FIXME: not sure why we don't have just an entry for Tree.Term here...
    public void visit(Tree.OperatorExpression op) {
        append(gen.at(op).Exec(gen.expressionGen().transformExpression(op)));
    }

    public void visit(Tree.Expression tree) {
        append(gen.at(tree).Exec(gen.expressionGen().transformExpression(tree)));
    }

    // FIXME: I think those should just go in transformExpression no?
    public void visit(Tree.PostfixOperatorExpression expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.PrefixOperatorExpression expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.ExpressionStatement tree) {
        append(gen.at(tree).Exec(gen.expressionGen().transformExpression(tree.getExpression(), BoxingStrategy.INDIFFERENT)));
    }
    
    /*
     * Expression - Invocations
     */
    
    public void visit(Tree.InvocationExpression expr) {
        append(gen.expressionGen().transform(expr));
    }
    
    public void visit(Tree.QualifiedMemberExpression access) {
        append(gen.expressionGen().transform(access));
    }

    public void visit(Tree.BaseTypeExpression typeExp) {
        // A constructor
        append(gen.expressionGen().transform(typeExp, args.toList()));
    }

    public void visit(Tree.BaseMemberExpression access) {
        append(gen.expressionGen().transform(access));
    }
    
    /*
     * Expression - Terms
     */
    
    public void visit(Tree.This expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.Super expr) {
        append(gen.expressionGen().transform(expr));
    }

    public void visit(Tree.Outer expr) {
        append(gen.expressionGen().transform(expr));
    }

    // FIXME: port dot operator?
    public void visit(Tree.NotEqualOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.NotOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.AssignOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.IsOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.RangeOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.EntryOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.LogicalOp op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.UnaryOperatorExpression op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.BinaryOperatorExpression op) {
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.ArithmeticAssignmentOp op){
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.BitwiseAssignmentOp op){
        append(gen.expressionGen().transform(op));
    }

    public void visit(Tree.LogicalAssignmentOp op){
        append(gen.expressionGen().transform(op));
    }

    // NB spec 1.3.11 says "There are only two types of numeric
    // literals: literals for Naturals and literals for Floats."
    public void visit(Tree.NaturalLiteral lit) {
        append(gen.expressionGen().transform(lit));
    }

    public void visit(Tree.FloatLiteral lit) {
        append(gen.expressionGen().transform(lit));
    }

    public void visit(Tree.CharLiteral lit) {
        append(gen.expressionGen().transform(lit));
    }

    public void visit(Tree.StringLiteral string) {
        append(gen.expressionGen().transform(string));
    }

    public void visit(Tree.QuotedLiteral string) {
        append(gen.expressionGen().transform(string));
    }

    // FIXME: port TypeName?
    public void visit(Tree.InitializerExpression value) {
        append(gen.expressionGen().transformExpression(value.getExpression()));
    }

    public void visit(Tree.SequenceEnumeration value) {
        append(gen.expressionGen().transform(value));
    }

    // FIXME: port Null?
    // FIXME: port Condition?
    // FIXME: port Subscript?
    // FIXME: port LowerBoud?
    // FIXME: port EnumList?
    public void visit(Tree.StringTemplate expr) {
        append(gen.expressionGen().transformStringExpression(expr));
    }
    
    public void visit(Tree.Throw throw_) {
        append(gen.statementGen().transform(throw_));
    }
    
    public void visit(Tree.TryCatchStatement t) {
        append(gen.statementGen().transform(t));
    }
    
    /**
     * Gets all the results which were appended during the visit
     * @return The results
     * 
     * @see #getSingleResult()
     */
    public ListBuffer<? extends JCTree> getResult() {
        return defs;
    }
    
    /**
     * Asserts that there's a single result, and returns it
     * @return The result
     * 
     * @see #getResult()
     */
    public <K extends JCTree> K getSingleResult() {
        if (defs.size() != 1) {
            throw new RuntimeException("Got "+defs.size()+" results instead of 1");
        }
        return (K) defs.first();
    }
    
    public boolean hasResult() {
        return (defs.size() > 0);
    }
    
    private ListBuffer<JCTree> append(JCTree x) {
        return defs.append(x);
    }

    private ListBuffer<JCTree> appendList(List<? extends JCTree> xs) {
        for (JCTree x : xs) {
            append(x);
        }
        return defs;
    }
}

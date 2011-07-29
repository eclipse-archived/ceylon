package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;

import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class CeylonVisitor extends AbstractVisitor<JCTree> {
    final ClassDefinitionBuilder classBuilder;
    final ListBuffer<JCExpression> args;
    
    public CeylonVisitor(CeylonTransformer ceylonTransformer) {
        super(ceylonTransformer);
        this.classBuilder = null;
        this.args = null;
    }

    public CeylonVisitor(CeylonTransformer ceylonTransformer, ClassDefinitionBuilder classBuilder) {
        super(ceylonTransformer);
        this.classBuilder = classBuilder;
        this.args = null;
    }

    public CeylonVisitor(CeylonTransformer ceylonTransformer, ListBuffer<JCExpression> args) {
        super(ceylonTransformer);
        this.classBuilder = null;
        this.args = args;
    }

    /*
     * Compilation Unit
     */
    
    public void visit(Tree.ImportList imp) {
        appendList(gen.transform(imp));
    }

    private boolean checkCompilerAnnotations(Tree.Declaration decl){
        boolean old = gen.disableModelAnnotations;
        if(gen.hasCompilerAnnotation(decl, "nomodel"))
            gen.disableModelAnnotations  = true;
        return old;
    }

    private void resetCompilerAnnotations(boolean value){
        gen.disableModelAnnotations = value;
    }

    public void visit(Tree.ClassOrInterface decl) {
        boolean annots = checkCompilerAnnotations(decl);
        append(gen.classGen.transform(decl));
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.ObjectDefinition decl) {
        boolean annots = checkCompilerAnnotations(decl);
        append(gen.classGen.objectClass(decl, true));
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.AttributeDeclaration decl){
        boolean annots = checkCompilerAnnotations(decl);
        Scope container = decl.getDeclarationModel().getContainer();
        if (container instanceof com.redhat.ceylon.compiler.typechecker.model.Package) {
            append(gen.transform(decl));
        } else if (container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) {
            classGen.transform(decl, classBuilder);
        } else {
            append(statementGen.transform(decl));
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(Tree.AttributeGetterDefinition decl){
        boolean annots = checkCompilerAnnotations(decl);
        Scope container = decl.getDeclarationModel().getContainer();
        if (container instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            classBuilder.defs(classGen.transform(decl));
        } else {
            append(gen.transform(decl));
        }
        resetCompilerAnnotations(annots);
    }

    public void visit(final Tree.AttributeSetterDefinition decl) {
        Scope container = decl.getDeclarationModel().getContainer();
        if (container instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            classBuilder.defs(classGen.transform(decl));
        }
    }

    public void visit(Tree.MethodDefinition decl) {
        boolean annots = checkCompilerAnnotations(decl);
        Scope container = decl.getDeclarationModel().getContainer();
        if (container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) {
            classBuilder.defs(classGen.transform(decl));
        } else {
            // Generate a wrapper class for the method
            JCTree.JCClassDecl innerDecl = classGen.methodClass(decl);
            append(innerDecl);
            if (container instanceof Method) {
                JCTree.JCIdent name = make().Ident(innerDecl.name);
                JCVariableDecl call = at(decl).VarDef(
                        make().Modifiers(FINAL),
                        names().fromString(decl.getIdentifier().getText()),
                        name,
                        at(decl).NewClass(null, null, name, List.<JCTree.JCExpression>nil(), null));
                append(call);
            }
        }
        resetCompilerAnnotations(annots);
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

    public void visit(Tree.MethodDeclaration meth) {
        classBuilder.defs(classGen.transform(meth));
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

    /*
     * Statements
     */

    public void visit(Tree.Return ret) {
        append(statementGen.transform(ret));
    }

    public void visit(Tree.IfStatement stat) {
        appendList(statementGen.transform(stat));
    }

    public void visit(Tree.WhileStatement stat) {
        appendList(statementGen.transform(stat));
    }

//    public void visit(Tree.DoWhileStatement stat) {
//        append(statementGen.transform(stat));
//    }

    public void visit(Tree.ForStatement stat) {
        appendList(statementGen.transform(stat));
    }

    public void visit(Tree.Break stat) {
        appendList(statementGen.transform(stat));
    }

    public void visit(Tree.SpecifierStatement op) {
        append(statementGen.transform(op));
    }

    // FIXME: not sure why we don't have just an entry for Tree.Term here...
    public void visit(Tree.OperatorExpression op) {
        append(at(op).Exec(expressionGen.transformExpression(op)));
    }

    public void visit(Tree.Expression tree) {
        append(at(tree).Exec(expressionGen.transformExpression(tree)));
    }

    // FIXME: I think those should just go in transformExpression no?
    public void visit(Tree.PostfixOperatorExpression expr) {
        append(expressionGen.transform(expr));
    }

    public void visit(Tree.PrefixOperatorExpression expr) {
        append(expressionGen.transform(expr));
    }

    public void visit(Tree.ExpressionStatement tree) {
        append(at(tree).Exec(expressionGen.transformExpression(tree.getExpression())));
    }
    
    /*
     * Expression - Invocations
     */
    
    public void visit(Tree.InvocationExpression expr) {
        append(expressionGen.transform(expr));
    }
    
    public void visit(Tree.QualifiedMemberExpression access) {
        append(expressionGen.transform(access));
    }

    public void visit(Tree.Type type) {
        // A constructor
        append(at(type).NewClass(null, null, gen.makeJavaType(type.getTypeModel()), args.toList(), null));
    }

    public void visit(Tree.BaseTypeExpression typeExp) {
        // A constructor
        append(at(typeExp).NewClass(null, null, makeIdent(typeExp.getIdentifier().getText()), args.toList(), null));
    }

    public void visit(Tree.BaseMemberExpression access) {
        append(expressionGen.transform(access));
    }
    
    /*
     * Expression - Terms
     */
    
    public void visit(Tree.This expr) {
        at(expr);
        append(makeIdent("this"));
    }

    public void visit(Tree.Super expr) {
        at(expr);
        append(makeIdent("super"));
    }

    // FIXME: port dot operator?
    public void visit(Tree.NotEqualOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.NotOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.AssignOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.IsOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.RangeOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.EntryOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.LogicalOp op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.UnaryOperatorExpression op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.BinaryOperatorExpression op) {
        append(expressionGen.transform(op));
    }

    public void visit(Tree.ArithmeticAssignmentOp op){
        append(expressionGen.transform(op));
    }

    public void visit(Tree.BitwiseAssignmentOp op){
        append(expressionGen.transform(op));
    }

    public void visit(Tree.LogicalAssignmentOp op){
        append(expressionGen.transform(op));
    }

    // NB spec 1.3.11 says "There are only two types of numeric
    // literals: literals for Naturals and literals for Floats."
    public void visit(Tree.NaturalLiteral lit) {
        JCExpression n = make().Literal(Long.parseLong(lit.getText()));
        append(at(lit).Apply(null, makeSelect(makeIdent(syms().ceylonNaturalType), "instance"), List.of(n)));
    }

    public void visit(Tree.FloatLiteral lit) {
        JCExpression n = make().Literal(Double.parseDouble(lit.getText()));
        append(at(lit).Apply(null, makeSelect(makeIdent(syms().ceylonFloatType), "instance"), List.of(n)));
    }

    public void visit(Tree.CharLiteral lit) {
        JCExpression n = make().Literal(TypeTags.CHAR, (int) lit.getText().charAt(1));
        // XXX make().Literal(lit.value) doesn't work here... something
        // broken in javac?
        append(at(lit).Apply(null, makeSelect(makeIdent(syms().ceylonCharacterType), "instance"), List.of(n)));
    }

    public void visit(Tree.StringLiteral string) {
        append(expressionGen.transform(string));
    }

    // FIXME: port TypeName?
    public void visit(Tree.InitializerExpression value) {
        append(expressionGen.transformExpression(value.getExpression()));
    }

    public void visit(Tree.SequenceEnumeration value) {
        append(expressionGen.transform(value));
    }

    // FIXME: port Null?
    // FIXME: port Condition?
    // FIXME: port Subscript?
    // FIXME: port LowerBoud?
    // FIXME: port EnumList?
    public void visit(Tree.StringTemplate expr) {
        append(expressionGen.transformStringExpression(expr));
    }
}

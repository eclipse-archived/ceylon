package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NegativeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositiveOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.util.Util;

public class BoxingVisitor extends Visitor {

    private AbstractTransformer transformer;
    
    public BoxingVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }

    @Override
    public void visit(BaseMemberExpression that) {
        super.visit(that);
        if(Util.isUnBoxed(that.getDeclaration()) || transformer.isCeylonBoolean(that.getTypeModel()))
            markUnboxed(that);
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        super.visit(that);
        propagateFromDeclaration(that, that.getDeclaration());
    }

    @Override
    public void visit(AnyAttribute that) {
        super.visit(that);
        propagateFromDeclaration(that, that.getDeclarationModel());
    }

    @Override
    public void visit(Expression that) {
        super.visit(that);
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(InvocationExpression that) {
        super.visit(that);
        propagateFromTerm(that, that.getPrimary());
    }

    @Override
    public void visit(NaturalLiteral that) {
        super.visit(that);
        markUnboxed(that);
    }

    @Override
    public void visit(FloatLiteral that) {
        super.visit(that);
        markUnboxed(that);
    }

    @Override
    public void visit(StringLiteral that) {
        super.visit(that);
        markUnboxed(that);
    }

    @Override
    public void visit(CharLiteral that) {
        super.visit(that);
        markUnboxed(that);
    }

    @Override
    public void visit(PositiveOp that) {
        super.visit(that);
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(NegativeOp that) {
        super.visit(that);
        propagateFromTerm(that, that.getTerm());
    }

    @Override
    public void visit(NotOp that) {
        super.visit(that);
        propagateFromTerm(that, that.getTerm());
    }

    private void propagateFromDeclaration(Node that, Declaration term) {
        if(Util.isUnBoxed(term))
            markUnboxed(that);
    }

    private void propagateFromTerm(Node that, Term term) {
        if(Util.isUnBoxed(term))
            markUnboxed(that);
    }

    private void markUnboxed(Node that) {
        that.setAttribute(BoxingDeclarationVisitor.IS_UNBOXED, true);
    }

}

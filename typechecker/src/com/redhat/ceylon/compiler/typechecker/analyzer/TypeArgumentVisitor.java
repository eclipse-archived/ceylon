package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates the position in which covariant and contravariant
 * type parameters appear in the schemas of declarations.
 * 
 * @author Gavin King
 *
 */
public class TypeArgumentVisitor extends Visitor {
    
    private boolean contravariant = false;
    private Declaration parameterizedDeclaration;
    
    private void flip() {
        contravariant = !contravariant;
    }
    
    public TypeArgumentVisitor() {}
    
    @Override public void visit(Tree.ParameterList that) {
        flip();
        super.visit(that);
        flip();
    }
    
    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        TypeParameter dec = that.getDeclarationModel();
        if (dec!=null) {
            parameterizedDeclaration = dec.getDeclaration();
            flip();
            if (that.getSatisfiedTypes()!=null) {
                for (Tree.Type type: that.getSatisfiedTypes().getTypes()) {
                    check(type, false);
                }
            }
            flip();
            parameterizedDeclaration = null;
        }
    }
    
    @Override public void visit(Tree.Parameter that) {
        boolean topLevel = parameterizedDeclaration==null;
        if (topLevel) {
            parameterizedDeclaration = that.getDeclarationModel().getDeclaration();
        }
        super.visit(that);
        check(that.getType(), false);
        if (topLevel) {
            parameterizedDeclaration = null;
        }
    }
    
    @Override public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        if (!(that instanceof Tree.Variable)) { //TODO: is this really the correct condition?!
            check(that.getType(), that.getDeclarationModel().isVariable());
        }
    }
    
    @Override public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        if (that.getSatisfiedTypes()!=null) {
            for (Tree.Type type: that.getSatisfiedTypes().getTypes()) {
                check(type, false);
            }
        }
    }
    
    @Override public void visit(Tree.AnyClass that) {
        super.visit(that);
        if (that.getExtendedType()!=null) {
            check(that.getExtendedType().getType(), false);
        }
    }

    private void check(Tree.Type that, boolean variable) {
        if (that!=null) {
            check(that.getTypeModel(), that, variable);
        }
    }
    
    private void check(ProducedType type, Node that, boolean variable) {
        if ( type!=null && !type.checkVariance(!contravariant && !variable, contravariant && !variable, parameterizedDeclaration) ) {
            that.addError("incorrect variance: " + type.getProducedTypeName());
        }
    }
    
}

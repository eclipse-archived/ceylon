package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
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
    
    @Override public void visit(Tree.Parameter that) {
        boolean topLevel = parameterizedDeclaration==null;
        if (topLevel) {
            parameterizedDeclaration = ( (Parameter) that.getDeclarationModel() ).getDeclaration();
        }
        super.visit(that);
        if (topLevel) {
            parameterizedDeclaration = null;
        }
    }
    
    @Override public void visit(Tree.Type that) {
        ProducedType pt = that.getTypeModel();
        if ( pt!=null && !pt.checkVariance(!contravariant, contravariant, parameterizedDeclaration) ) {
            that.addError("incorrect variance: " + pt.getProducedTypeName());
        }
    }
    
}

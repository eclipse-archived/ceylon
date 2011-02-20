package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * 
 * @author Gavin King
 *
 */
public class TypeArgumentVisitor extends Visitor {
    
    private boolean contravariant = false;
    private Declaration declaration;
    
    private void flip() {
        contravariant = !contravariant;
    }
    
    public TypeArgumentVisitor() {}
    
    @Override public void visit(Tree.ParameterList that) {
        flip();
        super.visit(that);
        flip();
    }
    
    @Override public void visit(Tree.Type that) {
        ProducedType pt = that.getTypeModel();
        if ( pt!=null && !pt.checkVariance(!contravariant, contravariant, declaration) ) {
            that.addError("incorrect variance: " + pt.getProducedTypeName());
        }
    }
    
    @Override public void visit(Tree.Method that) {
        Declaration d = declaration;
        declaration = that.getDeclarationModel();
        super.visit(that);
        declaration = d;
    }
        
    @Override public void visit(Tree.Attribute that) {
        Declaration d = declaration;
        declaration = that.getDeclarationModel();
        super.visit(that);
        declaration = d;
    }
        
    @Override public void visit(Tree.AttributeSetterDefinition that) {
        Declaration d = declaration;
        declaration = that.getDeclarationModel();
        super.visit(that);
        declaration = d;
    }
        
    @Override public void visit(Tree.ClassOrInterface that) {
        Declaration d = declaration;
        declaration = that.getDeclarationModel();
        super.visit(that);
        declaration = d;
    }
        
}

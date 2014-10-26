package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Detects recursive default type arguments
 * 
 * @author Gavin King
 *
 */
public class DefaultTypeArgVisitor extends Visitor {
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        TypeParameter tpd = that.getDeclarationModel();
        ProducedType dta = tpd.getDefaultTypeArgument();
        if (dta!=null && dta.containsDeclaration(tpd.getDeclaration())) {
            tpd.setDefaultTypeArgument(null);
        }
        super.visit(that);
    }

}

package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.DecidabilityException;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;

/**
 * Detects recursive default type arguments
 * 
 * @author Gavin King
 *
 */
public class DefaultTypeArgVisitor extends Visitor {
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            TypeParameter tpd = that.getDeclarationModel();
            ProducedType dta = tpd.getDefaultTypeArgument();
            if (dta!=null) {
                try {
                    if (dta.involvesDeclaration(tpd.getDeclaration())) {
                        tpd.setDefaultTypeArgument(null);
                    }
                }
                //Note: this might not be truly necessary, 
                //at least I don't seem to have any tests
                //that crash in this way!
                catch (DecidabilityException re) {
                    ts.addError("undecidable default type argument");
                    tpd.setDefaultTypeArgument(null);
                }
            }
        }
        super.visit(that);
    }

}

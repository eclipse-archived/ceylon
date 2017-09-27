package org.eclipse.ceylon.compiler.typechecker.analyzer;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.DecidabilityException;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;

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
            TypeParameter tp = that.getDeclarationModel();
            Declaration dec = tp.getDeclaration();
            Type dta = tp.getDefaultTypeArgument();
            if (dta!=null) {
                try {
                    if (dta.involvesDeclaration(dec)) {
                        tp.setDefaultTypeArgument(null);
                    }
                }
                catch (DecidabilityException re) {
                    ts.addError("undecidable default type argument");
                    tp.setDefaultTypeArgument(null);
                }
            }
        }
        super.visit(that);
    }

}

package org.eclipse.ceylon.compiler.typechecker.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.IntersectionType;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.UnionType;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 *
 * @author kulikov
 */
public class ReferenceCounter extends Visitor {
    
    private Set<Declaration> referencedDeclarations = new HashSet<Declaration>();
    
    void referenced(Declaration d) {
        referencedDeclarations.add(d);
        //TODO: check that the value is actually assigned!
        if (d instanceof Value) {
            Setter setter = ((Value) d).getSetter();
            if (setter!=null) {
                referencedDeclarations.add(setter);
            }
        }
    }
    
    boolean isReferenced(Declaration d) {
        for (Declaration rd: referencedDeclarations) {
            Scope container = rd.getContainer();
            if (container!=null &&
                    container.equals(d.getContainer()) &&
                    rd.getName().equals(d.getName())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        super.visit(that);
        Declaration d = that.getDeclaration();
        if (d!=null) referenced(d);
    }
    
    @Override
    public void visit(Tree.SimpleType that) {
        super.visit(that);
        TypeDeclaration t = that.getDeclarationModel();
        if (t!=null && 
                !(t instanceof UnionType) && 
                !(t instanceof IntersectionType)) {
            referenced(t);
        }
    }
    
    @Override
    public void visit(Tree.MemberLiteral that) {
        super.visit(that);
        Declaration d = that.getDeclaration();
        if (d!=null) {
            referenced(d);
        }
    }

    @Override
    public void visit(Tree.DocLink that) {
        super.visit(that);
        Declaration d = that.getBase();
        if (d!=null) {
            referenced(d);
        }
    }

}


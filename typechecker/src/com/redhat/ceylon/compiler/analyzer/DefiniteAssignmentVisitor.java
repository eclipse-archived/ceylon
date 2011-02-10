package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class DefiniteAssignmentVisitor extends Visitor {
    
    Typed declaration;
    boolean assigned = false;
        
    public DefiniteAssignmentVisitor(Typed declaration) {
        this.declaration = declaration;
    }
    
    public boolean isAssigned() {
        return assigned;
    }

    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.Member that) {
        if (!assigned && Util.getDeclaration(that)==declaration) {
            throw new RuntimeException("Not definitely assigned: " + 
                    that.getIdentifier().getText());
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        if (Util.getDeclaration(that.getMember())==declaration) {
            assigned=true;
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        Boolean o = assigned;
        super.visit(that);
        assigned = o;
    }
    
    @Override
    public void visit(Tree.IfStatement that) {
        boolean o = assigned;
        assigned = false;
        super.visit(that.getIfClause());
        boolean assignedByIfClause = assigned;
        assigned = false;
        if (that.getElseClause()!=null)
            super.visit(that.getElseClause());
        boolean assignedByElseClause = assigned;
        assigned = o || (assignedByIfClause && assignedByElseClause);
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        //TODO!!!
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        Boolean o = assigned;
        super.visit(that);
        assigned = o;
    }
    
    //Note: we do not need DoClause, here since it 
    //is always executed at least once!

    @Override
    public void visit(Tree.ForStatement that) {
        boolean o = assigned;
        assigned = false;
        super.visit(that.getForClause());
        boolean assignedByForClause = assigned;
        assigned = false;
        if (that.getFailClause()!=null)
            super.visit(that.getFailClause());
        boolean assignedByFailClause = assigned;
        assigned = o || (assignedByForClause && assignedByFailClause);
    }
    

    @Override
    public void visit(Tree.FailClause that) {
        Boolean o = assigned;
        super.visit(that);
        assigned = o;
    }

    @Override
    public void visit(Tree.TryClause that) {
        Boolean o = assigned;
        super.visit(that);
        assigned = o;
    }

    @Override
    public void visit(Tree.CatchClause that) {
        Boolean o = assigned;
        super.visit(that);
        assigned = o;
    }
    
    @Override
    public void visit(Tree.FinallyClause that) {
        Boolean o = assigned;
        super.visit(that);
        assigned = o;
    }

    /**
     * Suppress navigation to members
     */
    @Override
    public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);            
    }
    
}

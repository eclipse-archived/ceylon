package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class DefiniteAssignmentVisitor extends Visitor {
    
    Typed declaration;
    boolean definitelyAssigned = false;
    boolean possiblyAssigned = false;
        
    public DefiniteAssignmentVisitor(Typed declaration) {
        this.declaration = declaration;
    }
    
    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.Member that) {
        if (!definitelyAssigned && Util.getDeclaration(that)==declaration) {
            throw new RuntimeException("Not definitely assigned: " + 
                    that.getIdentifier().getText());
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        if (possiblyAssigned) {
            throw new RuntimeException("Not definitely unassigned: " + 
                    that.getMember().getIdentifier().getText());
        }
        if (Util.getDeclaration(that.getMember())==declaration) {
            definitelyAssigned=true;
            possiblyAssigned=true;
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        //TODO: I think this stuff is unnecessary
        //      what we really should be checking 
        //      is that the nested dec doesn't try
        //      to assign it at all!
        Boolean o = definitelyAssigned;
        Boolean p = possiblyAssigned;
        super.visit(that);
        definitelyAssigned = o;
        possiblyAssigned = p;
    }
    
    @Override
    public void visit(Tree.IfStatement that) {
        boolean o = definitelyAssigned;
        boolean p = possiblyAssigned;
        super.visit(that.getIfClause());
        boolean definitelyAssignedByIfClause = definitelyAssigned;
        boolean possiblyAssignedByIfClause = possiblyAssigned;
        definitelyAssigned = o;
        possiblyAssigned = p;
        if (that.getElseClause()!=null)
            super.visit(that.getElseClause());
        boolean definitelyAssignedByElseClause = definitelyAssigned;
        boolean possiblyAssignedByElseClause = possiblyAssigned;
        definitelyAssigned = o || (definitelyAssignedByIfClause && definitelyAssignedByElseClause);
        possiblyAssigned = o || possiblyAssignedByIfClause || possiblyAssignedByElseClause;
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        //TODO!!!
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        Boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }
    
    //Note: we do not need DoClause, here since it 
    //is always executed at least once!

    @Override
    public void visit(Tree.ForStatement that) {
        boolean o = definitelyAssigned;
        boolean p = possiblyAssigned;
        super.visit(that.getForClause());
        boolean definitelyAssignedByForClause = definitelyAssigned;
        boolean possiblyAssignedByForClause = possiblyAssigned;
        definitelyAssigned = o;
        possiblyAssigned = p;
        if (that.getFailClause()!=null)
            super.visit(that.getFailClause());
        boolean definitelyAssignedByFailClause = definitelyAssigned;
        boolean possiblyAssignedByFailClause = possiblyAssigned;
        definitelyAssigned = o || (definitelyAssignedByForClause && definitelyAssignedByFailClause);
        possiblyAssigned = o || possiblyAssignedByForClause || possiblyAssignedByFailClause;
    }
    

    @Override
    public void visit(Tree.FailClause that) {
        Boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }

    @Override
    public void visit(Tree.TryClause that) {
        Boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }

    @Override
    public void visit(Tree.CatchClause that) {
        Boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }
    
    @Override
    public void visit(Tree.FinallyClause that) {
        Boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }

    /**
     * Suppress navigation to members
     */
    @Override
    public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);            
    }
    
}

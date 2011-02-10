package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.This;
import com.redhat.ceylon.compiler.tree.Visitor;

public class DefiniteAssignmentVisitor extends Visitor {
    
    Typed declaration;
    
    boolean definitelyAssigned = false;
    boolean possiblyAssigned = false;
    boolean cannotAssign = true;
    boolean declared = false;
        
    public DefiniteAssignmentVisitor(Typed declaration) {
        this.declaration = declaration;
    }
    
    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.Member that) {
        if (Util.getDeclaration(that)==declaration) {
            if (!declared) {
               throw new RuntimeException("Not yet declared: " + 
                        that.getIdentifier().getText());
            }
            if (!definitelyAssigned) {
                throw new RuntimeException("Not definitely assigned: " + 
                        that.getIdentifier().getText());
            }
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        if (Util.getDeclaration(that.getMember())==declaration) {
            if (!declared) {
                throw new RuntimeException("Not yet declared: " + 
                        that.getMember().getIdentifier().getText());
            }
            if (possiblyAssigned) {
                throw new RuntimeException("Not definitely unassigned: " + 
                        that.getMember().getIdentifier().getText());
            }
            if (cannotAssign) {
                throw new RuntimeException("Cannot assign from here: " + 
                        that.getMember().getIdentifier().getText());
            }
            definitelyAssigned=true;
            possiblyAssigned=true;
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        if (that.getModelNode()==declaration) {
            super.visit(that);
            declared = true;
            cannotAssign = false;
        }
        else {
            boolean c = cannotAssign;
            cannotAssign=true;
            super.visit(that);
            cannotAssign = c;
        }
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
        boolean c = cannotAssign;
        cannotAssign=true;
        super.visit(that);
        cannotAssign = c;
    }
    
    @Override
    public void visit(Tree.DoClause that) {
        boolean c = cannotAssign;
        cannotAssign=true;
        super.visit(that);
        cannotAssign = c;
    }

    @Override
    public void visit(Tree.ForStatement that) {
        boolean o = definitelyAssigned;
        boolean c = cannotAssign;
        cannotAssign = true;
        super.visit(that.getForClause());
        cannotAssign = c;
        if (that.getFailClause()!=null)
            super.visit(that.getFailClause());
        definitelyAssigned = o;
    }
    

    @Override
    public void visit(Tree.FailClause that) {
        boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }

    @Override
    public void visit(Tree.TryClause that) {
        boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }

    @Override
    public void visit(Tree.CatchClause that) {
        boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }
    
    @Override
    public void visit(Tree.FinallyClause that) {
        boolean o = definitelyAssigned;
        super.visit(that);
        definitelyAssigned = o;
    }

    /**
     * Suppress navigation to members
     */
    @Override
    public void visit(Tree.MemberExpression that) {
        if (that.getPrimary() instanceof This) {
            super.visit(that);
        }
        else {
            that.getPrimary().visit(this);
        }
    }
    
}

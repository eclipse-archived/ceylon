package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * Validates that non-variable values are well-defined
 * within the local scope in which they occur. Checks
 * that they are not used before they are defined, that
 * they are always specified before they are used, and
 * that they are never specified twice.
 * 
 * @author Gavin King
 *
 */
public class SpecificationVisitor extends Visitor {
    
    private final Declaration declaration;
    
    private SpecificationState specified = new SpecificationState(false, false);
    private boolean cannotSpecify = true;
    private boolean declared = false;
    private Context context;

    class SpecificationState {
        boolean definitely;
        boolean possibly;
        boolean exited;
        SpecificationState(boolean definitely, boolean possibly) {
            this.definitely = definitely;
            this.possibly = possibly;
            this.exited = false;
        }
    }
    
    public SpecificationVisitor(Declaration declaration, Context context) {
        this.declaration = declaration;
        this.context = context;
    }
    
    private void declare() {
        declared = true;
    }
    
    private boolean beginDeclarationScope() {
        return declared;
    }
    
    private void endDeclarationScope(boolean d) {
        declared = d;
    }
    
    private boolean beginDisabledSpecificationScope() {
        boolean ca = cannotSpecify;
        cannotSpecify = true;
        return ca;
    }
    
    private void endDisabledSpecificationScope(boolean ca) {
        cannotSpecify = ca;
    }
    
    private void specify() {
        specified.definitely = true;
        specified.possibly = true;
    }
    
    private void exit() {
        specified.exited = true;
    }
    
    private SpecificationState beginSpecificationScope() {
        SpecificationState as = specified;
        specified = new SpecificationState(specified.definitely, specified.possibly);
        return as;
    }
    
    private void endSpecificationScope(SpecificationState as) {
        specified = as;
    }
    
    private boolean beginIndefiniteSpecificationScope() {
        return specified.definitely;
    }
    
    private void endIndefiniteSpecificationScope(boolean da) {
        specified.definitely = da;
    }
    
    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.Member that) {
        if (Util.getDeclaration(that, context)==declaration) {
            if (!declared) {
                that.addError("not yet declared: " + 
                        that.getIdentifier().getText());
            }
            else if (!specified.definitely) {
                that.addError("not definitely specified: " + 
                        that.getIdentifier().getText());
            }
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        if (Util.getDeclaration(that.getMember(), context)==declaration) {
            if (!declared) {
                that.addError("not yet declared: " + 
                        that.getMember().getIdentifier().getText());
            }
            else if (cannotSpecify) {
                that.addError("cannot specify value from here: " + 
                        that.getMember().getIdentifier().getText());
            }
            else if (specified.possibly) {
                that.addError("not definitely unspecified: " + 
                        that.getMember().getIdentifier().getText());
            }
            else {
                super.visit(that.getSpecifierExpression());
                specify();
                super.visit(that.getMember());
            }
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        if (that.getDeclarationModel()==declaration) {
            beginDisabledSpecificationScope();
            super.visit(that);
            declare();
            endDisabledSpecificationScope(false);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        //TODO: allow references to un-assigned things
        //      in interface bodies or the declaration
        //      section of class bodies.
        super.visit(that);
        if (that.getDeclarationModel()==declaration &&
                that.getSpecifierExpression()!=null) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        //TODO: allow references to un-assigned things
        //      in interface bodies or the declaration
        //      section of class bodies.
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        //TODO: allow references to un-assigned things
        //      in interface bodies or the declaration
        //      section of class bodies.
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Variable that) {
        super.visit(that);
        if (that.getDeclarationModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        super.visit(that);
        if (that.getDeclarationModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);        
        if (that.getDeclarationModel()==declaration &&
                that.getSpecifierOrInitializerExpression()!=null) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.AttributeArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.ObjectDeclaration that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.InterfaceDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        exit();
    }

    @Override
    public void visit(Tree.Throw that) {
        super.visit(that);
        exit();
    }
    
    @Override
    public void visit(Tree.Break that) {
        super.visit(that);
        exit();
    }

    @Override
    public void visit(Tree.Continue that) {
        super.visit(that);
        exit();
    }
    
    @Override
    public void visit(Tree.IfStatement that) {
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        
        visit(that.getIfClause());
        boolean definitelyAssignedByIfClause = specified.definitely || specified.exited;
        boolean possiblyAssignedByIfClause = specified.possibly;
        endDeclarationScope(d);
        endSpecificationScope(as);
        
        boolean definitelyAssignedByElseClause;
        boolean possiblyAssignedByElseClause;
        if (that.getElseClause()!=null) {
            visit(that.getElseClause());
            endDeclarationScope(d);
            definitelyAssignedByElseClause = specified.definitely || specified.exited;
            possiblyAssignedByElseClause = specified.possibly;
        }
        else {
            definitelyAssignedByElseClause = false;
            possiblyAssignedByElseClause = false;
        }
        endSpecificationScope(as);
        
        specified.definitely = specified.definitely || (definitelyAssignedByIfClause && definitelyAssignedByElseClause);
        specified.possibly = specified.possibly || possiblyAssignedByIfClause || possiblyAssignedByElseClause;
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        //TODO!!!
        //if every case and the default case definitely
        //assigns, then it is definitely assigned after
        //the switch statement
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        boolean c = beginDisabledSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endDisabledSpecificationScope(c);
        endDeclarationScope(d);
    }
    
    @Override
    public void visit(Tree.DoClause that) {
        boolean c = beginDisabledSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endDisabledSpecificationScope(c);
        endDeclarationScope(d);
    }

    @Override
    public void visit(Tree.ForClause that) {
        boolean c = beginDisabledSpecificationScope();
        boolean d = beginDeclarationScope();        
        super.visit(that);
        endDisabledSpecificationScope(c);
        endDeclarationScope(d);
    }
    

    @Override
    public void visit(Tree.FailClause that) {
        boolean o = beginIndefiniteSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endIndefiniteSpecificationScope(o);
        endDeclarationScope(d);
    }

    @Override
    public void visit(Tree.TryClause that) {
        //TODO: this isn't correct - if there are 
        //      no catch clauses, and the try clause 
        //      definitely assigns, it is definitely
        //      assigned after the try
        boolean o = beginIndefiniteSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endIndefiniteSpecificationScope(o);
        endDeclarationScope(d);
    }

    @Override
    public void visit(Tree.CatchClause that) {
        boolean o = beginIndefiniteSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endIndefiniteSpecificationScope(o);
        endDeclarationScope(d);
    }
    
    @Override
    public void visit(Tree.FinallyClause that) {
        boolean d = beginDeclarationScope();
        super.visit(that);
        endDeclarationScope(d);
    }

    /**
     * Suppress navigation to members
     */
    @Override
    public void visit(Tree.MemberExpression that) {
        if (that.getPrimary() instanceof Tree.This) {
            super.visit(that);
        }
        else {
            that.getPrimary().visit(this);
        }
    }
    
}

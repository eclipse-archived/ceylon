package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

/**
 * Validates that flow of control is well-defined.
 * Checks that control directives occur only where
 * they are allowed, and that every non-void method 
 * always ends via return or throw.
 * 
 * @author Gavin King
 *
 */
public class ControlFlowVisitor extends Visitor {
    
    boolean definitelyReturns = false;
    boolean broken = false;
    boolean canReturn = false;
    boolean canBreakOrContinue = false;
        
    @Override
    public void visit(Tree.AttributeGetter that) {
        boolean c = canReturn;
        canReturn = true;
        boolean d = definitelyReturns;
        definitelyReturns = false;
        super.visit(that);
        if (!definitelyReturns) {
            throw new RuntimeException("Does not definitely return: " + 
                    that.getIdentifier().getText());
        }
        definitelyReturns = d;
        canReturn = c;
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        if (that.getBlock()!=null) {
            boolean c = canReturn;
            canReturn = true;
            boolean d = definitelyReturns;
            definitelyReturns = false;
            super.visit(that);
            if (that.getBlock()!=null && 
                    !(that.getTypeOrSubtype() instanceof Tree.VoidModifier)) {
                if (!definitelyReturns) {
                    throw new RuntimeException("Does not definitely return: " + 
                            that.getIdentifier().getText());
                }
            }
            definitelyReturns = d;
            canReturn = c;
        }
    }
    
    @Override
    public void visit(Tree.AttributeSetter that) {
        boolean c = canReturn;
        canReturn = true;
        boolean d = definitelyReturns;
        definitelyReturns = false;
        super.visit(that);
        canReturn = c;
        definitelyReturns = d;
    }
    
    @Override
    public void visit(Tree.ClassOrInterfaceDeclaration that) {
        boolean c = canReturn;
        canReturn = false;
        boolean d = definitelyReturns;
        definitelyReturns = false;
        super.visit(that);
        canReturn = c;
        definitelyReturns = d;
    }
    
    @Override
    public void visit(Tree.Return that) {
        if (!canReturn)
            throw new RuntimeException("nothing to return from");
        super.visit(that);
        definitelyReturns = true;
    }

    @Override
    public void visit(Tree.Throw that) {
        super.visit(that);
        definitelyReturns = true;
    }
    
    @Override
    public void visit(Tree.Break that) {
        if (!canBreakOrContinue)
            throw new RuntimeException("nothing to break");
        super.visit(that);
        broken = true;
    }

    @Override
    public void visit(Tree.Continue that) {
        if (!canBreakOrContinue)
            throw new RuntimeException("nothing to continue");
        super.visit(that);
        definitelyReturns = true;
    }
    
    @Override
    public void visit(Tree.ExecutableStatement that) {
        if (definitelyReturns) {
            throw new RuntimeException("Unreachable code");
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Directive that) {
        if (definitelyReturns) {
            throw new RuntimeException("Unreachable code");
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        boolean d = definitelyReturns;
        boolean b = broken;
        boolean c = canBreakOrContinue;
        canBreakOrContinue = true;
        broken = false;
        super.visit(that);
        definitelyReturns = d;
        broken = b;
        canBreakOrContinue = c;
    }

    @Override
    public void visit(Tree.DoClause that) {
        boolean d = definitelyReturns;
        boolean b = broken;
        boolean c = canBreakOrContinue;
        canBreakOrContinue = true;
        broken = false;
        super.visit(that);
        definitelyReturns = d || (definitelyReturns && !broken);
        broken = b;
        canBreakOrContinue = c;
    }

    @Override
    public void visit(Tree.IfStatement that) {
        boolean d = definitelyReturns;
        
        visit(that.getIfClause());
        boolean definitelyReturnsFromIf = definitelyReturns;
        definitelyReturns = d;
        
        boolean definitelyReturnsFromElse = false;
        if (that.getElseClause()!=null) {
            visit(that.getElseClause());
            definitelyReturnsFromElse = definitelyReturns;
        }
        
        definitelyReturns = d || (definitelyReturnsFromIf && definitelyReturnsFromElse);
    }

    @Override
    public void visit(Tree.ForStatement that) {
        boolean d = definitelyReturns;
        
        boolean b = broken;
        boolean c = canBreakOrContinue;
        canBreakOrContinue = true;
        broken = false;
        visit(that.getForClause());
        boolean definitelyReturnsFromFor = definitelyReturns && !broken;
        broken = b;
        definitelyReturns = d;
        canBreakOrContinue = c;
        
        boolean definitelyReturnsFromFail = false;
        if (that.getFailClause()!=null) {
            visit(that.getFailClause());
            definitelyReturnsFromFail = definitelyReturns;
        }
        
        definitelyReturns = d || (definitelyReturnsFromFor && definitelyReturnsFromFail);
    }

    @Override
    public void visit(Tree.SwitchStatement that) {
        //TODO!
        //if every case and the default case definitely
        //returns, then it has definitely returned after
        //the switch statement
    }

    @Override
    public void visit(Tree.TryCatchStatement that) {
        //TODO!
        //if the try and every catch definitely returns, 
        //then it is has definitely returned after the 
        //try/catch statement
        //or if the finally definitely returns, then it 
        //has definitely returned after the try/catch 
        //statement
    }
}

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
    boolean canExecute = true;
        
    @Override
    public void visit(Tree.AttributeGetter that) {
        boolean c = canReturn;
        canReturn = true;
        boolean d = definitelyReturns;
        definitelyReturns = false;
        super.visit(that);
        if (!definitelyReturns) {
            that.getErrors().add( new AnalysisError(that, 
                    "Does not definitely return: " + 
                    that.getIdentifier().getText()) );
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
                    that.getErrors().add( new AnalysisError(that, 
                            "Does not definitely return: " + 
                            that.getIdentifier().getText()) );
                }
            }
            definitelyReturns = d;
            canReturn = c;
        }
        else if (that.getSpecifierExpression()!=null) {
            checkExecutableStatementAllowed(that);
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (that.getSpecifierOrInitializerExpression()!=null) {
            checkExecutableStatementAllowed(that);
            super.visit(that);
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
    public void visit(Tree.Body that) {
        boolean e = canExecute;
        canExecute = !(that instanceof Tree.InterfaceBody);
        super.visit(that);
        canExecute = e;
    }
    
    @Override
    public void visit(Tree.ExecutableStatement that) {
        checkExecutableStatementAllowed(that);
        super.visit(that);
    }

    private void checkExecutableStatementAllowed(Tree.Statement that) {
        if (!canExecute) {
            that.getErrors().add( new AnalysisError(that, 
            "Misplaced statement") );
        }
    }
    
    @Override
    public void visit(Tree.Return that) {
        if (!canReturn) {
            that.getErrors().add( new AnalysisError(that, 
                    "Nothing to return from") );
        }
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
        if (!canBreakOrContinue) {
            that.getErrors().add( new AnalysisError(that, 
                    "No loop to break") );
        }
        super.visit(that);
        broken = true;
    }

    @Override
    public void visit(Tree.Continue that) {
        if (!canBreakOrContinue) {
            that.getErrors().add( new AnalysisError(that, 
                    "No loop to continue") );
        }
        super.visit(that);
        definitelyReturns = true;
    }
    
    @Override
    public void visit(Tree.Statement that) {
        if (definitelyReturns) {
            that.getErrors().add( new AnalysisError(that, 
                    "Unreachable code") );
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
        checkExecutableStatementAllowed(that);
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
        checkExecutableStatementAllowed(that);
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
        checkExecutableStatementAllowed(that);
        //TODO!
        //if every case and the default case definitely
        //returns, then it has definitely returned after
        //the switch statement
        super.visit(that);
    }

    @Override
    public void visit(Tree.TryCatchStatement that) {
        checkExecutableStatementAllowed(that);
        //TODO!
        //if the try and every catch definitely returns, 
        //then it is has definitely returned after the 
        //try/catch statement
        //or if the finally definitely returns, then it 
        //has definitely returned after the try/catch 
        //statement
        super.visit(that);
    }
}

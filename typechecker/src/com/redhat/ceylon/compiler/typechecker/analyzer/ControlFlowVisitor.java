package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.name;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
    
    private boolean definitelyReturns = false;
    private boolean canReturn = false;
    private boolean canExecute = true;
    private Boolean exitedFromLoop = null;
    
    boolean beginDefiniteReturnScope() {
        boolean dr = definitelyReturns;
        definitelyReturns = false;
        return dr;
    }
        
    boolean beginIndefiniteReturnScope() {
        return definitelyReturns;
    }
    
    void endDefiniteReturnScope(boolean dr) {
        definitelyReturns = dr;
    }
    
    void exit() {
        definitelyReturns = true;
    }
    
    boolean beginReturnScope(boolean cr) {
        boolean ocr = canReturn;
        canReturn = cr;
        return ocr;
    }
    
    void endReturnScope(boolean cr) {
        canReturn = cr;
    }
    
    boolean beginStatementScope(boolean ce) {
        boolean oce = canExecute;
        canExecute = ce;
        return oce;
    }
    
    void endStatementScope(boolean ce) {
        canExecute = ce;
    }
    
    Boolean beginLoop() {
        Boolean efl = exitedFromLoop;
        exitedFromLoop = false;
        return efl;
    }
    
    void endLoop(Boolean efl) {
        exitedFromLoop = efl;
    }
    
    void exitLoop() {
        exitedFromLoop = true;
    }
    
    boolean inLoop() {
        return exitedFromLoop!=null;
    }
    
    Boolean pauseLoop() {
        Boolean efl = exitedFromLoop;
        exitedFromLoop = null;
        return efl;
    }
    
    void unpauseLoop(Boolean efl) {
        exitedFromLoop = efl;
    }
        
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        checkDefiniteReturn(that, name(that.getIdentifier()));
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }

    @Override
    public void visit(Tree.AttributeArgument that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        checkDefiniteReturn(that, name(that.getIdentifier()));
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }

    private void checkDefiniteReturn(Node that, String name) {
        if (!definitelyReturns) {
            that.addError("does not definitely return: " + 
                    name);
        }
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        if (that.getSpecifierExpression()!=null) {
            checkExecutableStatementAllowed(that.getSpecifierExpression());
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        if (!(that.getType() instanceof Tree.VoidModifier)) {
            checkDefiniteReturn(that, name(that.getIdentifier()));
        }
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        if (!(that.getType() instanceof Tree.VoidModifier)) {
            checkDefiniteReturn(that, name(that.getIdentifier()));
        }
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (that.getSpecifierOrInitializerExpression()!=null) {
            checkExecutableStatementAllowed(that.getSpecifierOrInitializerExpression());
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.AttributeSetterDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.InterfaceDefinition that) {
        boolean c = beginReturnScope(false);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.Body that) {
        boolean e = beginStatementScope(!(that instanceof Tree.InterfaceBody));
        super.visit(that);
        endStatementScope(e);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        Boolean efl = pauseLoop();
        super.visit(that);
        unpauseLoop(efl);
    }
    
    @Override
    public void visit(Tree.ExecutableStatement that) {
        checkExecutableStatementAllowed(that);
        super.visit(that);
    }

    private void checkExecutableStatementAllowed(Node that) {
        if (!canExecute) {
            that.addError("misplaced statement or initializer");
        }
    }
    
    @Override
    public void visit(Tree.Return that) {
        if (!canReturn) {
            that.addError("nothing to return from");
        }
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
        if (!inLoop()) {
            that.addError("no surrounding loop to break");
        }
        super.visit(that);
        exitLoop();
    }

    @Override
    public void visit(Tree.Continue that) {
        if (!inLoop()) {
            that.addError("no surrounding loop to continue");
        }
        super.visit(that);
        exit();
    }
    
    @Override
    public void visit(Tree.Statement that) {
        if (definitelyReturns) {
            that.addError("unreachable code");
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        boolean d = beginIndefiniteReturnScope();
        Boolean b = beginLoop();
        super.visit(that);
        endDefiniteReturnScope(d);
        endLoop(b);
    }

    @Override
    public void visit(Tree.DoClause that) {
        boolean d = beginIndefiniteReturnScope();
        Boolean b = beginLoop();
        super.visit(that);
        definitelyReturns = d || (definitelyReturns && !exitedFromLoop);
        endLoop(b);
    }

    @Override
    public void visit(Tree.IfStatement that) {
        checkExecutableStatementAllowed(that);
        boolean d = beginIndefiniteReturnScope();
        
        if (that.getIfClause()!=null) {
            that.getIfClause().visit(this);
        }
        boolean definitelyReturnsFromIf = definitelyReturns;
        endDefiniteReturnScope(d);
        
        boolean definitelyReturnsFromElse;
        if (that.getElseClause()!=null) {
            that.getElseClause().visit(this);
            definitelyReturnsFromElse = definitelyReturns;
        }
        else {
            definitelyReturnsFromElse = false;
        }
        
        definitelyReturns = d || (definitelyReturnsFromIf && definitelyReturnsFromElse);
    }

    @Override
    public void visit(Tree.ForStatement that) {
        checkExecutableStatementAllowed(that);
        boolean d = beginIndefiniteReturnScope();
        
        Boolean b = beginLoop();
        if (that.getForClause()!=null) {
            that.getForClause().visit(this);
        }
        boolean definitelyDoesNotExitFromFor = !exitedFromLoop;
        endLoop(b);
        endDefiniteReturnScope(d);
        
        boolean definitelyReturnsFromFail;
        if (that.getFailClause()!=null) {
            that.getFailClause().visit(this);
            definitelyReturnsFromFail = definitelyReturns;
        }
        else {
            definitelyReturnsFromFail = false;
        }
        
        definitelyReturns = d || (definitelyReturnsFromFail && definitelyDoesNotExitFromFor);
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
    
    @Override
    public void visit(Tree.ExpressionStatement that) {
        super.visit(that);
        Tree.Expression expr = that.getExpression();
        if (expr!=null) {
            Tree.Term t = expr.getTerm();
            if (t==null) {
                expr.addError("malformed expression statement");
            }
            else {
                if (!(t instanceof Tree.InvocationExpression
                        || t instanceof Tree.PostfixOperatorExpression
                        || t instanceof Tree.PrefixOperatorExpression
                        || t instanceof Tree.AssignmentOp)) {
                    expr.addError("not a legal statement");
                }
            }
        }
    }
}

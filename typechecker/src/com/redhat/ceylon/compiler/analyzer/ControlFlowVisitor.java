package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.tree.Node;
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
        
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        checkDefiniteReturn(that, Util.name(that));
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }

    @Override
    public void visit(Tree.AttributeArgument that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        checkDefiniteReturn(that, Util.name(that));
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
            checkExecutableStatementAllowed(that);
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        if (!(that.getTypeOrSubtype() instanceof Tree.VoidModifier)) {
            checkDefiniteReturn(that, Util.name(that));
        }
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        if (!(that.getTypeOrSubtype() instanceof Tree.VoidModifier)) {
            checkDefiniteReturn(that, Util.name(that));
        }
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (that.getSpecifierOrInitializerExpression()!=null) {
            checkExecutableStatementAllowed(that);
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
        boolean c = beginReturnScope(false);
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
    public void visit(Tree.ObjectDeclaration that) {
        boolean c = beginReturnScope(false);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        boolean c = beginReturnScope(false);
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
    public void visit(Tree.ExecutableStatement that) {
        checkExecutableStatementAllowed(that);
        super.visit(that);
    }

    private void checkExecutableStatementAllowed(Tree.Statement that) {
        if (!canExecute) {
            that.addError("misplaced statement");
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
        
        visit(that.getIfClause());
        boolean definitelyReturnsFromIf = definitelyReturns;
        endDefiniteReturnScope(d);
        
        boolean definitelyReturnsFromElse;
        if (that.getElseClause()!=null) {
            visit(that.getElseClause());
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
        visit(that.getForClause());
        boolean definitelyReturnsFromFor = definitelyReturns && !exitedFromLoop;
        endLoop(b);
        endDefiniteReturnScope(d);
        
        boolean definitelyReturnsFromFail;
        if (that.getFailClause()!=null) {
            visit(that.getFailClause());
            definitelyReturnsFromFail = definitelyReturns;
        }
        else {
            definitelyReturnsFromFail = false;
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

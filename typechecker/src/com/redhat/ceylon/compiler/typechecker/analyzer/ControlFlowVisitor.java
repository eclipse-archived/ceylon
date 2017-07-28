package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isAlwaysSatisfied;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isAtLeastOne;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isNeverSatisfied;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates that flow of control is well-defined. Checks 
 * that control directives occur only where they are 
 * allowed, and that every non-void method always ends via 
 * return or throw.
 * 
 * @author Gavin King
 *
 */
public class ControlFlowVisitor extends Visitor {
    
    private static class LoopState {
        private boolean breaks = false;
        private boolean continues = false;
        private LoopState() {}
        private LoopState(LoopState loopState) {
            breaks = loopState.breaks;
            continues = loopState.continues;
        }
    }
    
    private boolean definitelyReturns = false;
    private boolean definitelyBreaksOrContinues = false;
    private boolean canReturn = false;
    private boolean canExecute = true;
    private boolean unreachabilityReported = false;
    private LoopState loopState = null;
    
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
        unreachabilityReported = false;
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
    
    LoopState beginLoop() {
        LoopState efl = loopState;
        loopState = new LoopState();
        return efl;
    }
    
    void endLoop(LoopState efl) {
        loopState = efl;
    }
    
    void exitLoop() {
        if (loopState!=null) {
            loopState.breaks = true;
        }
    }
    
    void continueLoop() {
        if (loopState!=null) {
            loopState.continues = true;
        }
    }
    
    boolean inLoop() {
        return loopState!=null;
    }
    
    boolean beginLoopScope() {
        boolean obc = definitelyBreaksOrContinues;
        definitelyBreaksOrContinues = false;
        return obc;
    }
    
    void endLoopScope(boolean bc) {
        definitelyBreaksOrContinues = bc;
    }
    
    void exitLoopScope() {
        definitelyBreaksOrContinues = true;
    }
    
    LoopState pauseLoop() {
        LoopState efl = loopState;
        loopState = null;
        return efl;
    }
    
    void unpauseLoop(LoopState efl) {
        loopState = efl;
    }
        
    boolean pauseLoopScope() {
        Boolean bc = definitelyBreaksOrContinues;
        definitelyBreaksOrContinues = false;
        return bc;
    }
    
    void unpauseLoopScope(boolean bc) {
        definitelyBreaksOrContinues = bc;
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
        if (that.getSpecifierExpression()==null) {
            boolean c = beginReturnScope(true);
            boolean d = beginDefiniteReturnScope();
            super.visit(that);
            checkDefiniteReturn(that, name(that.getIdentifier()));
            endDefiniteReturnScope(d);
            endReturnScope(c);
        }
        else {
            super.visit(that);
        }
    }

    private void checkDefiniteReturn(Node that, String name) {
        if (!definitelyReturns) {
            if (name==null) {
                name = "anonymous function";
            }
            else {
                name = "'" + name + "'";
            }
            that.addError("does not definitely return: " + 
                    name + " has branches which do not end in a 'return' statement",
                    12100);
        }
    }

//    @Override
//    public void visit(Tree.MethodDeclaration that) {
//        if (that.getSpecifierExpression()!=null) {
//            checkExecutableStatementAllowed(that.getSpecifierExpression());
//            super.visit(that);
//        }
//    }
//    
    @Override
    public void visit(Tree.MethodDefinition that) {
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        if (!that.getDeclarationModel().isDeclaredVoid()) {
            checkDefiniteReturn(that, name(that.getIdentifier()));
        }
        endDefiniteReturnScope(d);
        endReturnScope(c);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        if (that.getSpecifierExpression()==null) {
            boolean c = beginReturnScope(true);
            boolean d = beginDefiniteReturnScope();
            super.visit(that);
            if (!that.getDeclarationModel().isDeclaredVoid()) {
                checkDefiniteReturn(that, name(that.getIdentifier()));
            }
            endDefiniteReturnScope(d);
            endReturnScope(c);
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.FunctionArgument that) {
        if (that.getExpression()==null) {
            LoopState efl = pauseLoop();
            boolean bc = pauseLoopScope();
            boolean c = beginReturnScope(true);
            boolean d = beginDefiniteReturnScope();
            super.visit(that);
            if (!(that.getDeclarationModel().isDeclaredVoid())) {
                checkDefiniteReturn(that, null);
            }
            endDefiniteReturnScope(d);
            endReturnScope(c);
            unpauseLoop(efl);
            unpauseLoopScope(bc);
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (!that.getDeclarationModel().isParameter() &&
            that.getSpecifierOrInitializerExpression()!=null &&
                !(that.getSpecifierOrInitializerExpression() instanceof Tree.LazySpecifierExpression)) {
            checkExecutableStatementAllowed(that.getSpecifierOrInitializerExpression());
            super.visit(that);
        }
        else {
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
    public void visit(Tree.Constructor that) {
        checkReachable(that);
        boolean c = beginReturnScope(true);
        boolean d = beginDefiniteReturnScope();
        super.visit(that);
        endReturnScope(c);
        endDefiniteReturnScope(d);
    }
    
    @Override
    public void visit(Tree.Enumerated that) {
        checkReachable(that);
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
    public void visit(Tree.ObjectExpression that) {
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
    public void visit(Tree.LazySpecifierExpression that) {
        boolean e = beginStatementScope(true);
        super.visit(that);
        endStatementScope(e);
    }
    
    @Override
    public void visit(Tree.Block that) {
        super.visit(that);
        that.setDefinitelyReturns(definitelyReturns);
        that.setDefinitelyBreaksOrContinues(definitelyBreaksOrContinues);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        LoopState efl = pauseLoop();
        boolean bc = pauseLoopScope();
        super.visit(that);
        unpauseLoop(efl);
        unpauseLoopScope(bc);
    }
    
    @Override
    public void visit(Tree.TypedArgument that) {
        LoopState efl = pauseLoop();
        boolean bc = pauseLoopScope();
        super.visit(that);
        unpauseLoop(efl);
        unpauseLoopScope(bc);
    }
    
    @Override
    public void visit(Tree.ExecutableStatement that) {
        boolean executable = true;
        //shortcut refinement statements with => aren't really "executable"
        if (that instanceof Tree.SpecifierStatement) {
            Tree.SpecifierStatement s = (Tree.SpecifierStatement) that;
            executable = !(s.getSpecifierExpression() instanceof Tree.LazySpecifierExpression) ||
                        !s.getRefinement();
        }
        if (executable) {
            checkExecutableStatementAllowed(that);
        }
        super.visit(that);
    }

    private void checkExecutableStatementAllowed(Node that) {
        if (!canExecute) {
            that.addError("statement or initializer may not occur directly in interface body");
        }
    }
    
    @Override
    public void visit(Tree.Return that) {
        if (!canReturn) {
            that.addError("nothing to return from");
        }
        super.visit(that);
        exit();
        exitLoopScope();
    }

    @Override
    public void visit(Tree.Throw that) {
        super.visit(that);
        exit();
        exitLoopScope();
    }
    
    @Override
    public void visit(Tree.Break that) {
        if (!inLoop()) {
            that.addError("no surrounding loop to break");
        }
        super.visit(that);
        exitLoop();
        exitLoopScope();
    }

    @Override
    public void visit(Tree.Continue that) {
        if (!inLoop()) {
            that.addError("no surrounding loop to continue");
        }
        super.visit(that);
        continueLoop();
        exitLoopScope();
    }
    
    @Override
    public void visit(Tree.Statement that) {
        if (!(that instanceof Tree.Variable)) {
            checkReachable(that);
        }
        super.visit(that);
    }

    private void checkReachable(Tree.Statement that) {
        if (definitelyReturns || definitelyBreaksOrContinues) {
            if (!unreachabilityReported) {
                that.addError("unreachable code");
                unreachabilityReported = true;
            }
        }
    }
    
    @Override
    public void visit(Tree.WhileStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        LoopState b = beginLoop();
        boolean bc = beginLoopScope();
        that.getWhileClause().visit(this);
        boolean definitelyDoesNotBreakWhile = !loopState.breaks;
        endDefiniteReturnScope(d);
        endLoop(b);
        endLoopScope(bc);
        if (isAlwaysSatisfied(that.getWhileClause().getConditionList())) {
            if (definitelyDoesNotBreakWhile) {
                definitelyReturns = true;
            }
        }
    }
    
    @Override
    public void visit(Tree.ForStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        
        LoopState b = beginLoop();
        boolean bc = beginLoopScope();
        boolean atLeastOneIteration = false;
        Tree.ForClause forClause = that.getForClause();
        if (forClause!=null) {
            forClause.visit(this);
            atLeastOneIteration = isAtLeastOne(forClause);
        }
        boolean definitelyDoesNotBreakFor = !loopState.breaks;
        boolean definitelyDoesNotContinueFor = !loopState.continues;
        boolean definitelyReturnsFromFor = definitelyReturns && 
                atLeastOneIteration 
                    && definitelyDoesNotBreakFor
                    && definitelyDoesNotContinueFor;
        that.setExits(loopState.breaks);
        endLoop(b);
        endLoopScope(bc);
        
        definitelyReturns = d || definitelyReturnsFromFor;
        
        boolean definitelyReturnsFromElse;
        Tree.ElseClause elseClause = that.getElseClause();
        if (elseClause!=null) {
            elseClause.visit(this);
            definitelyReturnsFromElse = definitelyReturns && 
                    definitelyDoesNotBreakFor;
        }
        else {
            definitelyReturnsFromElse = false;
        }
        endLoopScope(bc);
        
        definitelyReturns = d || 
                definitelyReturnsFromFor || 
                definitelyReturnsFromElse;
    }

    @Override
    public void visit(Tree.IfStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        LoopState ls = loopState;
        boolean bc = definitelyBreaksOrContinues;
        
        loopState = ls==null ? null : new LoopState(ls);
        Tree.IfClause ifClause = that.getIfClause();
        if (ifClause!=null) {
            ifClause.visit(this);
        }
        boolean definitelyReturnsFromIf = definitelyReturns;
        boolean possiblyBreaksFromIf = inLoop() && loopState.breaks;
        boolean possiblyContinuesFromIf = inLoop() && loopState.continues;
        boolean breaksOrContinuesFromIf = definitelyBreaksOrContinues;
        loopState = ls;
        endDefiniteReturnScope(d);
        endLoopScope(bc);
        
        loopState = ls==null ? null : new LoopState(ls);
        boolean definitelyReturnsFromElse;
        boolean breaksOrContinuesFromElse;
        Tree.ElseClause elseClause = that.getElseClause();
        if (elseClause!=null) {
            elseClause.visit(this);
            definitelyReturnsFromElse = definitelyReturns;
            breaksOrContinuesFromElse = definitelyBreaksOrContinues;
        }
        else {
            definitelyReturnsFromElse = false;
            breaksOrContinuesFromElse = false;
        }
        Boolean possiblyBreaksFromElse = inLoop() && loopState.breaks;
        boolean possiblyContinuesFromElse = inLoop() && loopState.continues;
        loopState = ls;
        endLoopScope(bc);
        
        Tree.ConditionList cl = ifClause==null ? null : ifClause.getConditionList();
        if (isAlwaysSatisfied(cl)) {
            definitelyReturns = d || definitelyReturnsFromIf;
            definitelyBreaksOrContinues = bc || breaksOrContinuesFromIf;
            if (inLoop()) {
                loopState.breaks = loopState.breaks || possiblyBreaksFromIf;
                loopState.continues = loopState.continues || possiblyContinuesFromIf;
            }
        } 
        else if (isNeverSatisfied(cl)) {
            definitelyReturns = d || definitelyReturnsFromElse;
            definitelyBreaksOrContinues = bc || breaksOrContinuesFromElse;
            if (inLoop()) {
                loopState.breaks = loopState.breaks || possiblyBreaksFromElse;
                loopState.continues = loopState.continues || possiblyContinuesFromElse;
            }
        }
        else {
            definitelyReturns = d || (definitelyReturnsFromIf && definitelyReturnsFromElse);
            definitelyBreaksOrContinues = bc || breaksOrContinuesFromIf && breaksOrContinuesFromElse;
            if (inLoop()) {
                loopState.breaks = loopState.breaks || possiblyBreaksFromIf || possiblyBreaksFromElse;
                loopState.continues = loopState.continues || possiblyContinuesFromIf || possiblyContinuesFromElse;
            }
        }
    }

    @Override
    public void visit(Tree.SwitchStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        boolean bc = definitelyBreaksOrContinues;
        
        that.getSwitchClause().visit(this);
        
        boolean definitelyReturnsFromEveryCase = true;
        boolean definitelyBreaksOrContinuesFromEveryCase = true;
        List<Tree.CaseClause> caseClauses = 
                that.getSwitchCaseList().getCaseClauses();
        for (Tree.CaseClause cc: caseClauses) {
            cc.visit(this);
            definitelyReturnsFromEveryCase = definitelyReturnsFromEveryCase && definitelyReturns;
            definitelyBreaksOrContinuesFromEveryCase = definitelyBreaksOrContinuesFromEveryCase 
                    && definitelyBreaksOrContinues;
            endDefiniteReturnScope(d);
            endLoopScope(bc);
        }
        
        Tree.ElseClause elseClause = 
                that.getSwitchCaseList().getElseClause();
        if (elseClause!=null) {
            elseClause.visit(this);
            definitelyReturnsFromEveryCase = definitelyReturnsFromEveryCase && definitelyReturns;
            definitelyBreaksOrContinuesFromEveryCase = definitelyBreaksOrContinuesFromEveryCase 
                    && definitelyBreaksOrContinues;
            endDefiniteReturnScope(d);
            endLoopScope(bc);
        }
        
        definitelyReturns = d || definitelyReturnsFromEveryCase;
        definitelyBreaksOrContinues = bc || definitelyBreaksOrContinuesFromEveryCase;
    }

    @Override
    public void visit(Tree.TryCatchStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        boolean bc = definitelyBreaksOrContinues;
        
        Tree.TryClause tryClause = that.getTryClause();
        if (tryClause!=null) {
            tryClause.visit(this);
        }
        boolean definitelyReturnsFromTry = definitelyReturns;
        boolean definitelyBreaksOrContinuesFromTry = definitelyBreaksOrContinues;
        endDefiniteReturnScope(d);
        endLoopScope(bc);
        
        boolean definitelyReturnsFromEveryCatch = true;
        boolean definitelyBreaksOrContinuesFromEveryCatch = true;
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            cc.visit(this);
            definitelyReturnsFromEveryCatch = definitelyReturnsFromEveryCatch && definitelyReturns;
            definitelyBreaksOrContinuesFromEveryCatch = definitelyBreaksOrContinuesFromEveryCatch 
                    && definitelyBreaksOrContinues;
            endDefiniteReturnScope(d);
            endLoopScope(bc);
        }
        
        boolean definitelyReturnsFromFinally;
        boolean definitelyBreaksOrContinuesFromFinally;
        Tree.FinallyClause finallyClause = that.getFinallyClause();
        if (finallyClause!=null) {
            finallyClause.visit(this);
            definitelyReturnsFromFinally = definitelyReturns;
            definitelyBreaksOrContinuesFromFinally = definitelyBreaksOrContinues;
        }
        else {
            definitelyReturnsFromFinally = false;
            definitelyBreaksOrContinuesFromFinally = false;
        }
        
        definitelyReturns = d || (definitelyReturnsFromTry && definitelyReturnsFromEveryCatch) 
                || definitelyReturnsFromFinally;
        definitelyBreaksOrContinues = bc || (definitelyBreaksOrContinuesFromTry && definitelyBreaksOrContinuesFromEveryCatch
                || definitelyBreaksOrContinuesFromFinally);
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
                    expr.addError("not a legal statement (not an invocation, assignment, or increment/decrement)", 
                            3000);
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.Assertion that) {
        super.visit(that);
        if (isNeverSatisfied(that.getConditionList())) {
            definitelyReturns = true;
        }
    }
    
}

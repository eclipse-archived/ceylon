package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isAlwaysSatisfied;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isNeverSatisfied;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
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
    private boolean definitelyBreaksOrContinues = false;
    private boolean canReturn = false;
    private boolean canExecute = true;
    private Boolean possiblyBreaks = null;
    
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
        Boolean efl = possiblyBreaks;
        possiblyBreaks = false;
        return efl;
    }
    
    void endLoop(Boolean efl) {
        possiblyBreaks = efl;
    }
    
    boolean beginLoopScope() {
        boolean obc = definitelyBreaksOrContinues;
        definitelyBreaksOrContinues = false;
        return obc;
    }
    
    void exitLoopScope() {
    	definitelyBreaksOrContinues = true;
    }
    
    void endLoopScope(boolean bc) {
    	definitelyBreaksOrContinues = bc;
    }
    
    void exitLoop() {
        possiblyBreaks = true;
    }
    
    boolean inLoop() {
        return possiblyBreaks!=null;
    }
    
    Boolean pauseLoop() {
        Boolean efl = possiblyBreaks;
        possiblyBreaks = null;
        return efl;
    }
    
    void unpauseLoop(Boolean efl) {
        possiblyBreaks = efl;
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
            that.addError("does not definitely return: " + 
                    name);
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
        if (!(that.getType() instanceof Tree.VoidModifier)) {
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
        	if (!(that.getType() instanceof Tree.VoidModifier)) {
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
            Boolean efl = pauseLoop();
            boolean bc = pauseLoopScope();
        	boolean c = beginReturnScope(true);
        	boolean d = beginDefiniteReturnScope();
        	super.visit(that);
        	if (!(that.getType() instanceof Tree.VoidModifier)) {
        		checkDefiniteReturn(that, "anonymous function");
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
    public void visit(Tree.Block that) {
        super.visit(that);
        that.setDefinitelyReturns(definitelyReturns);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        Boolean efl = pauseLoop();
        boolean bc = pauseLoopScope();
        super.visit(that);
        unpauseLoop(efl);
        unpauseLoopScope(bc);
    }
    
    @Override
    public void visit(Tree.TypedArgument that) {
        Boolean efl = pauseLoop();
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
            that.addError("unreachable code");
        }
    }
    
    @Override
    public void visit(Tree.WhileStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        Boolean b = beginLoop();
        boolean bc = beginLoopScope();
        that.getWhileClause().visit(this);
        boolean definitelyDoesNotBreakFromWhile = !possiblyBreaks;
        boolean definitelyReturnsFromWhile = definitelyReturns;
        endDefiniteReturnScope(d);
        endLoop(b);
        endLoopScope(bc);
        if (isAlwaysSatisfied(that.getWhileClause().getConditionList())) {
        	if (definitelyDoesNotBreakFromWhile 
        			|| definitelyReturnsFromWhile) { //superfluous?
        		definitelyReturns = true;
        	}
        }
    }
    
    @Override
    public void visit(Tree.ForStatement that) {
        checkExecutableStatementAllowed(that);
        checkReachable(that);
        boolean d = beginIndefiniteReturnScope();
        
        Boolean b = beginLoop();
        boolean bc = beginLoopScope();
        boolean atLeastOneIteration = false;
        if (that.getForClause()!=null) {
            that.getForClause().visit(this);
            Unit unit = that.getUnit();
			ProducedType neit = unit.getNonemptyIterableType(unit
            		.getAnythingDeclaration().getType());
            Tree.ForIterator fi = that.getForClause().getForIterator();
            if (fi!=null) {
            	Tree.SpecifierExpression se = fi.getSpecifierExpression();
            	if (se!=null) {
            		Tree.Expression e = se.getExpression();
            		if (e!=null) {
            			ProducedType t = e.getTypeModel();
            			atLeastOneIteration = t!=null && t.isSubtypeOf(neit);
            		}
            	}
            }
        }
        boolean definitelyDoesNotBreakFromFor = !possiblyBreaks;
        boolean definitelyReturnsFromFor = definitelyReturns && 
        		atLeastOneIteration && definitelyDoesNotBreakFromFor;
        that.setExits(possiblyBreaks);
        endLoop(b);
        endLoopScope(bc);
        
        definitelyReturns = d || definitelyReturnsFromFor;
        
        boolean definitelyReturnsFromElse;
        if (that.getElseClause()!=null) {
            that.getElseClause().visit(this);
            definitelyReturnsFromElse = definitelyReturns && 
            		definitelyDoesNotBreakFromFor;
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
        Boolean e = possiblyBreaks;
        boolean bc = definitelyBreaksOrContinues;
        
        Tree.IfClause ifClause = that.getIfClause();
        if (ifClause!=null) {
            ifClause.visit(this);
        }
        boolean definitelyReturnsFromIf = definitelyReturns;
        Boolean possiblyBreaksFromIf = possiblyBreaks;
        boolean breaksOrContinuesFromIf = definitelyBreaksOrContinues;
        possiblyBreaks = e;
        endDefiniteReturnScope(d);
        endLoopScope(bc);
        
        boolean definitelyReturnsFromElse;
        boolean breaksOrContinuesFromElse;
        if (that.getElseClause()!=null) {
            that.getElseClause().visit(this);
            definitelyReturnsFromElse = definitelyReturns;
            breaksOrContinuesFromElse = definitelyBreaksOrContinues;
        }
        else {
            definitelyReturnsFromElse = false;
            breaksOrContinuesFromElse = false;
        }
        Boolean possiblyBreaksFromElse = possiblyBreaks;
        possiblyBreaks = e;
        endLoopScope(bc);
        
        Tree.ConditionList cl = ifClause==null ? null : ifClause.getConditionList();
        if (isAlwaysSatisfied(cl)) {
            definitelyReturns = d || definitelyReturnsFromIf;
            possiblyBreaks = possiblyBreaksFromIf;
            definitelyBreaksOrContinues = bc || breaksOrContinuesFromIf;
        } 
        else if (isNeverSatisfied(cl)) {
            definitelyReturns = d || definitelyReturnsFromElse;
            possiblyBreaks = possiblyBreaksFromElse;
            definitelyBreaksOrContinues = bc || breaksOrContinuesFromElse;
        }
        else {
            definitelyReturns = d || (definitelyReturnsFromIf && definitelyReturnsFromElse);
            possiblyBreaks = e==null ? null : possiblyBreaksFromIf || possiblyBreaksFromElse;
            definitelyBreaksOrContinues = bc || breaksOrContinuesFromIf && breaksOrContinuesFromElse;

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
        for (Tree.CaseClause cc: that.getSwitchCaseList().getCaseClauses()) {
            cc.visit(this);
            definitelyReturnsFromEveryCase = definitelyReturnsFromEveryCase && definitelyReturns;
            definitelyBreaksOrContinuesFromEveryCase = definitelyBreaksOrContinuesFromEveryCase 
            		&& definitelyBreaksOrContinues;
            endDefiniteReturnScope(d);
            endLoopScope(bc);
        }
        
        if (that.getSwitchCaseList().getElseClause()!=null) {
            that.getSwitchCaseList().getElseClause().visit(this);
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
        
        if (that.getTryClause()!=null) {
            that.getTryClause().visit(this);
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
        if (that.getFinallyClause()!=null) {
            that.getFinallyClause().visit(this);
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

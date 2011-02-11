package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

public class DefiniteReturnVisitor extends Visitor {
    
    boolean definitelyReturns = false;
        
    @Override
    public void visit(Tree.AttributeGetter that) {
        boolean d = definitelyReturns;
        definitelyReturns = false;
        super.visit(that);
        if (!definitelyReturns) {
            throw new RuntimeException("Does not definitely return: " + 
                    that.getIdentifier().getText());
        }
        definitelyReturns = d;
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
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
    }
    
    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        definitelyReturns = true;
    }

    @Override
    public void visit(Tree.Throw that) {
        super.visit(that);
        definitelyReturns = true;
    }
    
    //TODO: break and contine????
    //      they have the result that a for or do
    //      that looks like it definitely returns
    //      really doesn't, since the return statement
    //      is not reached in some branches
    
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
        super.visit(that);
        definitelyReturns = d;   
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
        visit(that.getForClause());
        boolean definitelyReturnsFromFor = definitelyReturns;
        definitelyReturns = d;
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

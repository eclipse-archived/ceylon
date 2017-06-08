package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.TreeUtil;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Marks Terms as small if they're being used in an assignment to 
 * a small declaration, and all other terms in the expression are 
 * also small.
 */
public class SmallVisitor extends Visitor {

    /**
     * Things like {@code <=>} return a non-small type, but we use the rule
     * that only if the expression result is small can the computation be 
     * small. We use a fake "small" value for operators which return 
     * non-smallable types like Comparison.  
     */
    private static final Value NON_SMALLABLE = new Value();
    static {
        NON_SMALLABLE.setSmall(true);
    }
    
    /** 
     * Mark the given term as small and reset the type to with an 
     * appropriate underlying type.
     */
    private static void markSmall(Tree.Term that) {
        that.setSmall(true);
        that.setTypeModel(SmallDeclarationVisitor.smallUnderlyingType(that.getTypeModel()));
    }
    
    private Declaration assigning;
    
    /**
     * Warn is assigning a literal that is not small to a declaration that 
     * is small.
     */
    protected void checkSmallAssignment(Tree.Term term) {
        term = TreeUtil.unwrapExpressionUntilTerm(term);
        if (Decl.isSmall(assigning) && 
                term != null &&
                !term.getSmall() && 
                (term instanceof Tree.CharLiteral ||
                 term instanceof Tree.NaturalLiteral ||
                ((term instanceof Tree.NegativeOp || term instanceof Tree.PositiveOp) &&  
                    ((Tree.UnaryOperatorExpression)term).getTerm() instanceof Tree.NaturalLiteral))) {
            term.addUsageWarning(Warning.literalNotSmall, 
                    "literal value is not small but is assignable to small declaration '"+
                            assigning.getName(term.getUnit())+"'", 
                    Backend.Java);
        }
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        Declaration preva = assigning;
        assigning = that.getDeclarationModel();
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        Declaration preva = assigning;
        assigning = that.getParameterModel().getModel();
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        Declaration preva = assigning;
        assigning = that.getDeclarationModel().getRefinedDeclaration();
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        //Declaration preva = assigning;
        //assigning = declaration;
        super.visit(that);
        //assigning = preva; 
        checkSmallAssignment(that.getExpression());
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Declaration d = that.getDeclaration();
        if (d == null) {
            if (that.getBaseMemberExpression() instanceof Tree.MemberOrTypeExpression) {
                d = ((Tree.MemberOrTypeExpression)that.getBaseMemberExpression()).getDeclaration();
            }
        }
        
        
        Declaration preva = assigning;
        assigning = d;
        super.visit(that);
        assigning = preva; 
        
        /*
        Declaration d = that.getDeclaration();
        if (Decl.isSmall(d)) {
            Tree.Term term = that.getSpecifierExpression().getExpression().getTerm();
            checkSmallAssignment(d, term);
        }*/
    }
    
    @Override
    public void visit(Tree.NamedArgument that) {
        Declaration d;
        if (that.getParameter() != null) {
            d = that.getParameter().getModel();
        } else {
            d = null;
        }
        Declaration preva = assigning;
        assigning = d;
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.Dynamic that) {
        // don't do there
    }
    
    @Override
    public void visit(Tree.SequencedArgument that) {
        Declaration d;
        if (that.getParameter() != null) {
            d = that.getParameter().getModel();
        } else {
            d = null;
        }
        Declaration preva = assigning;
        assigning = d;
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.PositionalArgument that) {
        Declaration d;
        if (that.getParameter() != null) {
            d = that.getParameter().getModel();
        } else {
            d = null;
        }
        Declaration preva = assigning;
        assigning = d;
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.Return that) {
        Declaration preva = assigning;
        assigning = that.getDeclaration();
        super.visit(that);
        checkSmallAssignment(that.getExpression());
        assigning = preva; 
        
    }
    
    private boolean isAssigningSmall() {
        //if (assigning != null && 
        //        assigning instanceof TypedDeclaration &&
        //        !((TypedDeclaration)assigning).getType().isInteger() &&
        //        !((TypedDeclaration)assigning).getType().isFloat()) {
        //    return true;
        //} else {
            return Decl.isSmall(assigning);
        //}
    }
    
    @Override 
    public void visit(Tree.Expression that) {
        super.visit(that);
        if (isAssigningSmall() &&
                that.getTerm().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.MemberOrTypeExpression that) {
        if (isAssigningSmall() &&
                Decl.isSmall(that.getDeclaration())) {
            markSmall(that);
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ValueIterator that) {
        Declaration preva = assigning;
        assigning = that.getVariable().getDeclarationModel();
        Term term = TreeUtil.unwrapExpressionUntilTerm(that.getSpecifierExpression().getExpression());
        if (term instanceof Tree.SegmentOp|| term instanceof Tree.RangeOp) {
            ((FunctionOrValue)assigning).setSmall(true);
        }
        super.visit(that);
        if (term instanceof Tree.SegmentOp || term instanceof Tree.RangeOp) {
            boolean small = ((Tree.BinaryOperatorExpression)term).getLeftTerm().getSmall()
                    && ((Tree.BinaryOperatorExpression)term).getRightTerm().getSmall();
            ((FunctionOrValue)assigning).setSmall(small);
            if (small) {
                that.getVariable().getDeclarationModel().setType(SmallDeclarationVisitor.smallUnderlyingType(that.getVariable().getDeclarationModel().getType()));
            }
        }
        checkSmallAssignment(that.getSpecifierExpression().getExpression());
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.BinaryOperatorExpression that) {
        super.visit(that);
        if (isAssigningSmall() &&
                that.getLeftTerm().getSmall() && 
                that.getRightTerm().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.CompareOp that) {
        Declaration preva = assigning;
        assigning = NON_SMALLABLE;
        super.visit(that);
        assigning = preva;
    }
    
    @Override
    public void visit(Tree.WithinOp that) {
        Declaration preva = assigning;
        assigning = NON_SMALLABLE;
        super.visit(that);
        assigning = preva;
        if (that.getLowerBound().getSmall() && 
                that.getUpperBound().getSmall() &&
                that.getTerm().getSmall()) {
            markSmall(that);
        }
    }
    @Override
    public void visit(Tree.Bound that) {
        super.visit(that);
        if (that.getTerm().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.AssignmentOp that) {
        if (that.getLeftTerm() instanceof Tree.MemberOrTypeExpression) {
            Declaration d = ((Tree.MemberOrTypeExpression)that.getLeftTerm()).getDeclaration();
            Declaration preva = assigning;
            assigning = d;
            super.visit(that);
            if (isAssigningSmall()) {
                checkSmallAssignment(that.getRightTerm());
            }
            assigning = preva;
        } else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.UnaryOperatorExpression that) {
        super.visit(that);
        if (isAssigningSmall() &&
                that.getTerm().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.PostfixOperatorExpression that) {
        if (that.getTerm() instanceof Tree.MemberOrTypeExpression) {
            Declaration d = ((Tree.MemberOrTypeExpression)that.getTerm()).getDeclaration();
            Declaration preva = assigning;
            assigning = d;
            super.visit(that);
            if (isAssigningSmall()) {
                checkSmallAssignment(that.getTerm());
            }
            assigning = preva;
        } else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.PrefixOperatorExpression that) {
        if (that.getTerm() instanceof Tree.MemberOrTypeExpression) {
            Declaration d = ((Tree.MemberOrTypeExpression)that.getTerm()).getDeclaration();
            Declaration preva = assigning;
            assigning = d;
            super.visit(that);
            if (isAssigningSmall()) {
                checkSmallAssignment(that.getTerm());
            }
            assigning = preva;
        } else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.IfExpression that) {
        super.visit(that);
        if (isAssigningSmall() &&
                that.getIfClause().getExpression().getSmall() &&
                that.getElseClause().getExpression().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.SwitchExpression that) {
        super.visit(that);
        if (isAssigningSmall()) {
            boolean smallCases = true;
            for (Tree.CaseClause c : that.getSwitchCaseList().getCaseClauses()) {
                smallCases &= c.getExpression().getTerm().getSmall();
            }
            boolean smallElse = true;
            if (that.getSwitchCaseList().getElseClause() != null) { 
                smallElse = that.getSwitchCaseList().getElseClause().getExpression().getTerm().getSmall();
            }
            if (smallCases && smallElse) {
                markSmall(that);
            }
        }
    }
    
    @Override
    public void visit(Tree.LetExpression that) {
        super.visit(that);
        if (isAssigningSmall() &&
                that.getLetClause().getExpression().getTerm().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.CharLiteral that) {
        if (isAssigningSmall() &&
                Character.isBmpCodePoint(ExpressionTransformer.literalValue(that))) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.NaturalLiteral that) {
        try {
            if (isAssigningSmall() &&
                    ExpressionTransformer.literalValue(that) instanceof Integer) {
                markSmall(that);
            }
        } catch (ErroneousException e) {
            // Ignore
        }
    }
    
    @Override
    public void visit(Tree.NegativeOp that) {
        if (that.getTerm() instanceof Tree.NaturalLiteral) {
            try {
                if (isAssigningSmall() &&
                        ExpressionTransformer.literalValue(that) instanceof Integer) {
                    markSmall(that);
                }
            } catch (ErroneousException e) {
                // Ignore
            }
        } else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.PositiveOp that) {
        if (that.getTerm() instanceof Tree.NaturalLiteral) {
            try {
                if (isAssigningSmall() &&
                        ExpressionTransformer.literalValue(that) instanceof Integer) {
                    markSmall(that);
                }
            } catch (ErroneousException e) {
                // Ignore
            }
        } else {
            super.visit(that);
        }
    }
    

    /*
    @Override
    public void visit(Tree.ValueIterator that) {
        Declaration preva = assigning;
        assigning = that.getVariable().getDeclarationModel();
        that.getVariable().getDeclarationModel().setSmall(true);
        super.visit(that);
        assigning = preva;
        if (that.getSpecifierExpression().getExpression().getSmall()) {
            
        }
    }
    */
    @Override 
    public void visit(Tree.Variable that) {
        Declaration preva = assigning;
        assigning = that.getDeclarationModel();
        super.visit(that);
        assigning = preva;
        /*
        if (that.getSpecifierExpression() != null &&
                that.getSpecifierExpression().getExpression().getSmall()) {
            that.getDeclarationModel().setSmall(true);
        }
        SmallDeclarationVisitor.smallDeclaration(that.getDeclarationModel());
        */
    }
    
    /*
    @Override
    public void visit(Tree.SegmentOp that) {
        // XXX In order for our right term to be marked small we need to set assigning
        // to a Declaration that is small
        super.visit(that);
        // note: We don't really care about the left term in lhs:rhs
        // it's the length that has to be small
        if (that.getRightTerm().getSmall()) {
            markSmall(that);
        }
    }
    
    @Override
    public void visit(Tree.RangeOp that) {
        super.visit(that);
        if (that.getLeftTerm().getSmall() && 
                that.getRightTerm().getSmall()) {
            markSmall(that);
        }
    }
    */
}

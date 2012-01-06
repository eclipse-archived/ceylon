package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignmentOp;
import com.sun.tools.javac.tree.JCTree;

public class Operators {

    private enum PrimitiveType {
        BOOLEAN, CHARACTER, INTEGER, FLOAT, STRING;
    }

    private static final PrimitiveType[] IntegerFloat = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT};
    private static final PrimitiveType[] IntegerFloatString = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.STRING};
    private static final PrimitiveType[] NotString = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.BOOLEAN};

    //
    // Unary and binary operators
    
    public enum OperatorTranslation {
        
        // Unary operators
        UNARY_POSITIVE(Tree.PositiveOp.class, "positiveValue", JCTree.POS, IntegerFloat),
        UNARY_NEGATIVE(Tree.NegativeOp.class, "negativeValue", JCTree.NEG, IntegerFloat),
        
        UNARY_POSTFIX_INCREMENT(Tree.PostfixIncrementOp.class, "getSuccessor", JCTree.POSTINC, PrimitiveType.INTEGER),
        UNARY_POSTFIX_DECREMENT(Tree.PostfixDecrementOp.class, "getPredecessor", JCTree.POSTDEC, PrimitiveType.INTEGER),
        UNARY_PREFIX_INCREMENT(Tree.IncrementOp.class, "getSuccessor", JCTree.PREINC, PrimitiveType.INTEGER),
        UNARY_PREFIX_DECREMENT(Tree.DecrementOp.class, "getPredecessor", JCTree.PREDEC, PrimitiveType.INTEGER),

        // Binary operators
        BINARY_SUM(Tree.SumOp.class, "plus", JCTree.PLUS, IntegerFloatString),
        BINARY_DIFFERENCE(Tree.DifferenceOp.class, "minus", JCTree.MINUS, IntegerFloat),
        BINARY_PRODUCT(Tree.ProductOp.class, "times", JCTree.MUL, IntegerFloat),
        BINARY_QUOTIENT(Tree.QuotientOp.class, "divided", JCTree.DIV, IntegerFloat),
        BINARY_POWER(Tree.PowerOp.class, "power"),
        BINARY_REMAINDER(Tree.RemainderOp.class, "remainder", JCTree.MOD, PrimitiveType.INTEGER),

        BINARY_AND(Tree.AndOp.class, "<not-used>", JCTree.AND, PrimitiveType.BOOLEAN),
        BINARY_OR(Tree.OrOp.class, "<not-used>", JCTree.OR, PrimitiveType.BOOLEAN),

        BINARY_INTERSECTION(Tree.IntersectionOp.class, "and", JCTree.BITAND, PrimitiveType.BOOLEAN),
        BINARY_UNION(Tree.UnionOp.class, "or", JCTree.BITOR, PrimitiveType.BOOLEAN),
        BINARY_XOR(Tree.XorOp.class, "xor", JCTree.BITXOR, PrimitiveType.BOOLEAN),
        
        BINARY_EQUAL(Tree.EqualOp.class, "equals", JCTree.EQ, NotString),
        BINARY_COMPARE(Tree.CompareOp.class, "compare"),

        // Binary operators that act on intermediary Comparison objects
        BINARY_LARGER(Tree.LargerOp.class, "largerThan", JCTree.GT, IntegerFloat),
        BINARY_SMALLER(Tree.SmallerOp.class, "smallerThan", JCTree.LT, IntegerFloat),
        BINARY_LARGE_AS(Tree.LargeAsOp.class, "asLargeAs", JCTree.GE, IntegerFloat),
        BINARY_SMALL_AS(Tree.SmallAsOp.class, "asSmallAs", JCTree.LE, IntegerFloat),
        ;

        Class<? extends Tree.OperatorExpression> operatorClass;
        String ceylonMethod;
        int javacOperator;
        PrimitiveType[] optimisableTypes;
        
        OperatorTranslation(Class<? extends Tree.OperatorExpression> operatorClass, String ceylonMethod, 
                int javacOperator, PrimitiveType... optimisableTypes) {
            this.operatorClass = operatorClass;
            this.ceylonMethod = ceylonMethod;
            this.javacOperator = javacOperator;
            this.optimisableTypes = optimisableTypes;
        }
        OperatorTranslation(Class<? extends Tree.BinaryOperatorExpression> operatorClass, String ceylonMethod) {
            this(operatorClass, ceylonMethod, -1);
        }
        
        public boolean isOptimisable(Tree.UnaryOperatorExpression t, AbstractTransformer gen){
            return isTermOptimisable(t.getTerm(), gen);
        }
        public boolean isOptimisable(Tree.BinaryOperatorExpression t, AbstractTransformer gen){
            return isTermOptimisable(t.getLeftTerm(), gen)
                    && isTermOptimisable(t.getRightTerm(), gen);
        }
        
        private boolean isTermOptimisable(Tree.Term t, AbstractTransformer gen){
            if(javacOperator < 0 || !t.getUnboxed())
                return false;
            ProducedType pt = t.getTypeModel();
            if(pt == null) // typechecker error?
                return false;
            if(optimisableTypes == null)
                return false;
            // see if it's a supported type
            for(PrimitiveType type : optimisableTypes){
                switch(type){
                case BOOLEAN:
                    if(gen.isCeylonBoolean(pt))
                        return true;
                    break;
                case CHARACTER:
                    if(gen.isCeylonCharacter(pt))
                        return true;
                    break;
                case FLOAT:
                    if(gen.isCeylonFloat(pt))
                        return true;
                    break;
                case INTEGER:
                    if(gen.isCeylonInteger(pt))
                        return true;
                    break;
                case STRING:
                    if(gen.isCeylonString(pt))
                        return true;
                    break;
                }
            }
            return false;
        }
    }
    
    //
    // Assignment operators
    
    public enum AssignmentOperatorTranslation {
        // Assignment operators
        ADD(Tree.AddAssignOp.class, OperatorTranslation.BINARY_SUM, JCTree.PLUS_ASG),
        SUBSTRACT(Tree.SubtractAssignOp.class, OperatorTranslation.BINARY_DIFFERENCE, JCTree.MINUS_ASG),
        MULTIPLY(Tree.MultiplyAssignOp.class, OperatorTranslation.BINARY_PRODUCT, JCTree.MUL_ASG),
        DIVIDE(Tree.DivideAssignOp.class, OperatorTranslation.BINARY_QUOTIENT, JCTree.DIV_ASG),
        REMAINDER(Tree.RemainderAssignOp.class, OperatorTranslation.BINARY_REMAINDER, JCTree.MOD_ASG),
        AND(Tree.AndAssignOp.class, OperatorTranslation.BINARY_AND, JCTree.BITAND_ASG),
        OR(Tree.OrAssignOp.class, OperatorTranslation.BINARY_OR, JCTree.BITOR_ASG)
        ;
        
        int javacOperator;
        OperatorTranslation binaryOperator;
        Class<? extends AssignmentOp> operatorClass;

        AssignmentOperatorTranslation(Class<? extends Tree.AssignmentOp> operatorClass,
                OperatorTranslation binaryOperator,
                int javacOperator) {
            this.operatorClass = operatorClass;
            this.javacOperator = javacOperator;
            this.binaryOperator = binaryOperator;
        }
    }

    //
    // Public API
    
    private static final Map<Class<? extends Tree.OperatorExpression>, OperatorTranslation> operators;
    private static final Map<Class<? extends Tree.AssignmentOp>, AssignmentOperatorTranslation> assignmentOperators;

    static {
        operators = new HashMap<Class<? extends Tree.OperatorExpression>, OperatorTranslation>();
        assignmentOperators = new HashMap<Class<? extends Tree.AssignmentOp>, AssignmentOperatorTranslation>();
        
        for(OperatorTranslation operator : OperatorTranslation.values())
            operators.put(operator.operatorClass, operator);

        for(AssignmentOperatorTranslation operator : AssignmentOperatorTranslation.values())
            assignmentOperators.put(operator.operatorClass, operator);
    }

    public static OperatorTranslation getOperator(Class<? extends Tree.OperatorExpression> operatorClass) {
        return operators.get(operatorClass);
    }

    public static AssignmentOperatorTranslation getAssignmentOperator(Class<? extends Tree.AssignmentOp> operatorClass) {
        return assignmentOperators.get(operatorClass);
    }
}

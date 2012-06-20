/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignmentOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BinaryOperatorExpression;
import com.sun.tools.javac.tree.JCTree;

/**
 * Aggregation of mappings from ceylon operator to java operator and optimisation strategy.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Operators {

    private enum PrimitiveType {
        BOOLEAN, CHARACTER, INTEGER, FLOAT, STRING;
    }

    private static final PrimitiveType[] IntegerFloat = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT};
    private static final PrimitiveType[] IntegerCharacter = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.CHARACTER};
    private static final PrimitiveType[] IntegerFloatCharacter = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.CHARACTER};
    private static final PrimitiveType[] IntegerFloatString = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.STRING};
    private static final PrimitiveType[] All = PrimitiveType.values();

    public enum OptimisationStrategy {
        OPTIMISE(true, BoxingStrategy.UNBOXED),
        OPTIMISE_BOXING(false, BoxingStrategy.INDIFFERENT),
        NONE(false, BoxingStrategy.BOXED);
        
        private BoxingStrategy boxingStrategy;
        private boolean useJavaOperator;

        OptimisationStrategy(boolean useJavaOperator, BoxingStrategy boxingStrategy){
            this.useJavaOperator = useJavaOperator;
            this.boxingStrategy = boxingStrategy;
        }
        
        public BoxingStrategy getBoxingStrategy(){
            return boxingStrategy;
        }
        
        public boolean useJavaOperator(){
            return useJavaOperator;
        }
    }
    
    //
    // Unary and binary operators
    
    public enum OperatorTranslation {
        
        // Unary operators
        UNARY_POSITIVE(Tree.PositiveOp.class, "positiveValue", JCTree.POS, IntegerFloat),
        UNARY_NEGATIVE(Tree.NegativeOp.class, "negativeValue", JCTree.NEG, IntegerFloat),
        
        UNARY_POSTFIX_INCREMENT(Tree.PostfixIncrementOp.class, "getSuccessor", JCTree.POSTINC, IntegerCharacter),
        UNARY_POSTFIX_DECREMENT(Tree.PostfixDecrementOp.class, "getPredecessor", JCTree.POSTDEC, IntegerCharacter),
        UNARY_PREFIX_INCREMENT(Tree.IncrementOp.class, "getSuccessor", JCTree.PREINC, IntegerCharacter),
        UNARY_PREFIX_DECREMENT(Tree.DecrementOp.class, "getPredecessor", JCTree.PREDEC, IntegerCharacter),

        // Binary operators
        BINARY_SUM(Tree.SumOp.class, "plus", JCTree.PLUS, IntegerFloatString),
        BINARY_DIFFERENCE(Tree.DifferenceOp.class, "minus", JCTree.MINUS, IntegerFloat),
        BINARY_PRODUCT(Tree.ProductOp.class, "times", JCTree.MUL, IntegerFloat),
        BINARY_QUOTIENT(Tree.QuotientOp.class, "divided", JCTree.DIV, IntegerFloat),
        BINARY_POWER(Tree.PowerOp.class, "power"),
        BINARY_REMAINDER(Tree.RemainderOp.class, "remainder", JCTree.MOD, PrimitiveType.INTEGER),

        BINARY_AND(Tree.AndOp.class, "<not-used>", JCTree.AND, PrimitiveType.BOOLEAN),
        BINARY_OR(Tree.OrOp.class, "<not-used>", JCTree.OR, PrimitiveType.BOOLEAN),

        BINARY_UNION(Tree.UnionOp.class, "union"),
        BINARY_INTERSECTION(Tree.IntersectionOp.class, "intersection"),
        BINARY_XOR(Tree.XorOp.class, "exclusiveUnion"),
        BINARY_COMPLEMENT(Tree.ComplementOp.class, "complement"), 
        
        BINARY_EQUAL(Tree.EqualOp.class, "equals", JCTree.EQ, All){
            @Override
            public OptimisationStrategy getOptimisationStrategy(BinaryOperatorExpression t, AbstractTransformer gen) {
                // no optimised operator returns a boxed type 
                if(!t.getUnboxed())
                    return OptimisationStrategy.NONE;
                OptimisationStrategy left = isTermOptimisable(t.getLeftTerm(), gen);
                OptimisationStrategy right = isTermOptimisable(t.getRightTerm(), gen);
                if(left == OptimisationStrategy.OPTIMISE
                        && right == OptimisationStrategy.OPTIMISE){
                    // these two previous checks ensure that the term is unboxed and has a type model
                    ProducedType leftType = t.getLeftTerm().getTypeModel();
                    ProducedType rightType = t.getRightTerm().getTypeModel();

                    // make sure both types are the same, can't optimise otherwise
                    if(!leftType.isExactly(rightType))
                        return OptimisationStrategy.NONE;
                    
                    // special case for String where we can't use == but don't need to box
                    if(gen.isCeylonString(leftType)
                        && gen.isCeylonString(rightType)){
                        return OptimisationStrategy.OPTIMISE_BOXING;
                    }
                }
                return lessPermissive(left, right);
            }
        },
        BINARY_COMPARE(Tree.CompareOp.class, "compare"),

        // Binary operators that act on intermediary Comparison objects
        BINARY_LARGER(Tree.LargerOp.class, "largerThan", JCTree.GT, IntegerFloatCharacter),
        BINARY_SMALLER(Tree.SmallerOp.class, "smallerThan", JCTree.LT, IntegerFloatCharacter),
        BINARY_LARGE_AS(Tree.LargeAsOp.class, "asLargeAs", JCTree.GE, IntegerFloatCharacter),
        BINARY_SMALL_AS(Tree.SmallAsOp.class, "asSmallAs", JCTree.LE, IntegerFloatCharacter),
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
        
        public OptimisationStrategy getOptimisationStrategy(Tree.UnaryOperatorExpression t, AbstractTransformer gen){
            // no optimised operator returns a boxed type 
            if(!t.getUnboxed())
                return OptimisationStrategy.NONE;
            return isTermOptimisable(t.getTerm(), gen);
        }
        public OptimisationStrategy getOptimisationStrategy(Tree.BinaryOperatorExpression t, AbstractTransformer gen){
            // no optimised operator returns a boxed type 
            if(!t.getUnboxed())
                return OptimisationStrategy.NONE;
            OptimisationStrategy left = isTermOptimisable(t.getLeftTerm(), gen);
            OptimisationStrategy right = isTermOptimisable(t.getRightTerm(), gen);
            return lessPermissive(left, right);
        }
        
        protected OptimisationStrategy lessPermissive(OptimisationStrategy left, OptimisationStrategy right) {
            // it's from most permissive to less permissive, so return the one with the higher ordinal (less permissive)
            if(left.ordinal() > right.ordinal())
                return left;
            return right;
        }
        protected OptimisationStrategy isTermOptimisable(Tree.Term t, AbstractTransformer gen){
            if(javacOperator < 0 || !t.getUnboxed())
                return OptimisationStrategy.NONE;
            ProducedType pt = t.getTypeModel();
            if(pt == null) // typechecker error?
                return OptimisationStrategy.NONE;
            if(optimisableTypes == null)
                return OptimisationStrategy.NONE;
            // see if it's a supported type
            for(PrimitiveType type : optimisableTypes){
                switch(type){
                case BOOLEAN:
                    if(gen.isCeylonBoolean(pt))
                        return OptimisationStrategy.OPTIMISE;
                    break;
                case CHARACTER:
                    if(gen.isCeylonCharacter(pt))
                        return OptimisationStrategy.OPTIMISE;
                    break;
                case FLOAT:
                    if(gen.isCeylonFloat(pt))
                        return OptimisationStrategy.OPTIMISE;
                    break;
                case INTEGER:
                    if(gen.isCeylonInteger(pt))
                        return OptimisationStrategy.OPTIMISE;
                    break;
                case STRING:
                    if(gen.isCeylonString(pt))
                        return OptimisationStrategy.OPTIMISE;
                    break;
                }
            }
            return OptimisationStrategy.NONE;
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
        OR(Tree.OrAssignOp.class, OperatorTranslation.BINARY_OR, JCTree.BITOR_ASG),
        
        // Set assignment
        BINARY_UNION(Tree.UnionAssignOp.class, OperatorTranslation.BINARY_UNION),
        BINARY_INTERSECTION(Tree.IntersectAssignOp.class, OperatorTranslation.BINARY_INTERSECTION),
        BINARY_XOR(Tree.XorAssignOp.class, OperatorTranslation.BINARY_XOR),
        BINARY_COMPLEMENT(Tree.ComplementAssignOp.class, OperatorTranslation.BINARY_COMPLEMENT),
        ;
        
        int javacOperator;
        OperatorTranslation binaryOperator;
        Class<? extends AssignmentOp> operatorClass;
        
        AssignmentOperatorTranslation(Class<? extends Tree.AssignmentOp> operatorClass, OperatorTranslation binaryOperator) {
            this.operatorClass = operatorClass;
            this.binaryOperator = binaryOperator;
        }

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
    
    // only there to make sure this class is initialised before the enums defined in it, otherwise we
    // get an initialisation error
    public static void init(){}
}

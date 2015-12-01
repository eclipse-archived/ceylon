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
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignmentOp;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree;
import com.redhat.ceylon.model.typechecker.model.Type;

/**
 * Aggregation of mappings from ceylon operator to java operator and optimisation strategy.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class Operators {

    private enum PrimitiveType {
        BOOLEAN("ceylon.language.Boolean"),
        BYTE("ceylon.language.Byte"),
        CHARACTER("ceylon.language.Character"),
        INTEGER("ceylon.language.Integer"),
        FLOAT("ceylon.language.Float"),
        STRING("ceylon.language.String");
        
        public final String fqn;
        
        PrimitiveType(String fqn) {
            this.fqn = fqn;
        }
    }

    private static final PrimitiveType[] IntegerByte = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.BYTE};
    private static final PrimitiveType[] IntegerFloat = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT};
    private static final PrimitiveType[] IntegerFloatByte = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.BYTE};
    private static final PrimitiveType[] IntegerCharacterByte = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.CHARACTER, PrimitiveType.BYTE};
    private static final PrimitiveType[] IntegerFloatCharacter = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.CHARACTER};
    private static final PrimitiveType[] IntegerFloatStringByte = new PrimitiveType[]{PrimitiveType.INTEGER, PrimitiveType.FLOAT, PrimitiveType.STRING, PrimitiveType.BYTE};
    private static final PrimitiveType[] All = PrimitiveType.values();

    public enum OptimisationStrategy {
        /** Optimize using the equivalent javac operator */
        OPTIMISE(true, false, BoxingStrategy.UNBOXED),
        /** Optimize using the static method (for a value type) */
        OPTIMISE_VALUE_TYPE(false, true, BoxingStrategy.UNBOXED),
        /** A special case used for String == */
        OPTIMISE_BOXING(false, false, BoxingStrategy.INDIFFERENT),
        /** No optimization possible: Call the (virtual) method corresponding to the ceylon operator */
        NONE(false, false, BoxingStrategy.BOXED);
        
        private BoxingStrategy boxingStrategy;
        private boolean useJavaOperator;
        private boolean useValueTypeMethod;

        OptimisationStrategy(boolean useJavaOperator, boolean useValueTypeMethod, BoxingStrategy boxingStrategy){
            this.useJavaOperator = useJavaOperator;
            this.useValueTypeMethod = useValueTypeMethod;
            this.boxingStrategy = boxingStrategy;
        }
        
        public BoxingStrategy getBoxingStrategy(){
            return boxingStrategy;
        }
        
        public boolean useJavaOperator(){
            return useJavaOperator;
        }
        
        public boolean useValueTypeMethod(){
            return useValueTypeMethod;
        }
    }
    
    //
    // Unary and binary operators
    
    public enum OperatorTranslation {
        
        // Unary operators
        UNARY_POSITIVE(Tree.PositiveOp.class, 1, "<noop>", JCTree.POS, IntegerFloatByte),
        UNARY_NEGATIVE(Tree.NegativeOp.class, 1, "negated", JCTree.NEG, IntegerFloatByte),
        
        UNARY_BITWISE_NOT(1, "not", JCTree.COMPL, IntegerByte),

        UNARY_POSTFIX_INCREMENT(Tree.PostfixIncrementOp.class, 1, "getSuccessor", JCTree.POSTINC, IntegerCharacterByte),
        UNARY_POSTFIX_DECREMENT(Tree.PostfixDecrementOp.class, 1, "getPredecessor", JCTree.POSTDEC, IntegerCharacterByte),
        UNARY_PREFIX_INCREMENT(Tree.IncrementOp.class, 1, "getSuccessor", JCTree.PREINC, IntegerCharacterByte),
        UNARY_PREFIX_DECREMENT(Tree.DecrementOp.class, 1, "getPredecessor", JCTree.PREDEC, IntegerCharacterByte),

        // Binary operators
        BINARY_SUM(Tree.SumOp.class, 2, "plus", JCTree.PLUS, IntegerFloatStringByte),
        BINARY_DIFFERENCE(Tree.DifferenceOp.class, 2, "minus", JCTree.MINUS, IntegerFloatByte),
        BINARY_PRODUCT(Tree.ProductOp.class, 2, "times", JCTree.MUL, IntegerFloat),
        BINARY_QUOTIENT(Tree.QuotientOp.class, 2, "divided", JCTree.DIV, IntegerFloat),
        BINARY_POWER(Tree.PowerOp.class, 2, "power", -1),
        BINARY_REMAINDER(Tree.RemainderOp.class, 2, "remainder", JCTree.MOD, PrimitiveType.INTEGER),
        
        BINARY_SCALE(Tree.ScaleOp.class, 2, "scale"),

        BINARY_BITWISE_AND(2, "and", JCTree.BITAND, IntegerByte),
        BINARY_BITWISE_OR(2, "or", JCTree.BITOR, IntegerByte),
        BINARY_BITWISE_XOR(2, "xor", JCTree.BITXOR, IntegerByte),
        BINARY_BITWISE_LOG_LEFT_SHIFT(2, "leftLogicalShift", JCTree.SL, IntegerByte),
        BINARY_BITWISE_LOG_RIGHT_SHIFT_INT(2, "rightLogicalShift", 0, JCTree.USR, PrimitiveType.INTEGER),
        BINARY_BITWISE_LOG_RIGHT_SHIFT_BYTE(2, "rightLogicalShift", 0xff, JCTree.USR, PrimitiveType.BYTE),
        BINARY_BITWISE_ARI_RIGHT_SHIFT(2, "rightArithmeticShift", JCTree.SR, IntegerByte),

        BINARY_AND(Tree.AndOp.class, 2, "<not-used>", JCTree.AND, PrimitiveType.BOOLEAN),
        BINARY_OR(Tree.OrOp.class, 2, "<not-used>", JCTree.OR, PrimitiveType.BOOLEAN),

        BINARY_UNION(Tree.UnionOp.class, 2, "union"),
        BINARY_INTERSECTION(Tree.IntersectionOp.class, 2, "intersection"),
        BINARY_COMPLEMENT(Tree.ComplementOp.class, 2, "complement"), 
        
        BINARY_EQUAL(Tree.EqualOp.class, 2, "equals", JCTree.EQ, All){
            @Override
            public OptimisationStrategy getBinOpOptimisationStrategy(Tree.Term t, 
                    Tree.Term leftTerm, Type leftType, 
                    Tree.Term rightTerm, Type rightType, AbstractTransformer gen) {
                // no optimised operator returns a boxed type 
                if(!t.getUnboxed())
                    return OptimisationStrategy.NONE;
                OptimisationStrategy left = isTermOptimisable(leftTerm, gen);
                OptimisationStrategy right = isTermOptimisable(rightTerm, gen);
                if(left == OptimisationStrategy.OPTIMISE
                        && right == OptimisationStrategy.OPTIMISE){
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
        BINARY_COMPARE(Tree.CompareOp.class, 2, "compare"),

        // Binary operators that act on intermediary Comparison objects
        BINARY_LARGER(Tree.LargerOp.class, 2, JCTree.EQ, "larger", JCTree.GT, IntegerFloatCharacter),
        BINARY_SMALLER(Tree.SmallerOp.class, 2, JCTree.EQ, "smaller", JCTree.LT, IntegerFloatCharacter),
        BINARY_LARGE_AS(Tree.LargeAsOp.class, 2, JCTree.NE, "smaller",  JCTree.GE, IntegerFloatCharacter),
        BINARY_SMALL_AS(Tree.SmallAsOp.class, 2, JCTree.NE, "larger", JCTree.LE, IntegerFloatCharacter);

        // we can have either a mapping from Tree operator class
        Class<? extends Tree.OperatorExpression> operatorClass;
        
        int arity;
        /** The name of the method which the boxed transformation invokes */
        String ceylonMethod;
        int javacOperator;
        PrimitiveType[] optimisableTypes;
        /** The name of a top level value from the language module */
        String ceylonValue;
        /** The operator with which to compare against {@link #ceylonValue} */
        int javacValueOperator;
        /** A mask to apply to the LHS before applying the operator */
        int valueMask;
        
        OperatorTranslation(int arity, String ceylonMethod, 
                int javacOperator, PrimitiveType... optimisableTypes) {
            this.ceylonMethod = ceylonMethod;
            this.javacOperator = javacOperator;
            this.optimisableTypes = optimisableTypes;
            this.arity = arity;
        }
        OperatorTranslation(int arity, String ceylonMethod, int valueMask,
                int javacOperator, PrimitiveType... optimisableTypes) {
            this(arity, ceylonMethod, javacOperator, optimisableTypes);
            this.valueMask = valueMask;
        }
        OperatorTranslation(Class<? extends Tree.OperatorExpression> operatorClass, 
                int arity, String ceylonMethod, 
                int javacOperator, PrimitiveType... optimisableTypes) {
            this(arity, ceylonMethod, javacOperator, optimisableTypes);
            this.operatorClass = operatorClass;
        }
        OperatorTranslation(Class<? extends Tree.OperatorExpression> operatorClass, 
                int arity, int javacOperator1, String ceylonValue, 
                int javacOperator, PrimitiveType... optimisableTypes) {
            this.ceylonValue = ceylonValue;
            this.javacValueOperator = javacOperator1;
            this.javacOperator = javacOperator;
            this.optimisableTypes = optimisableTypes;
            this.arity = arity;
            this.operatorClass = operatorClass;
        }
        OperatorTranslation(Class<? extends Tree.BinaryOperatorExpression> operatorClass, 
                int arity, String ceylonMethod) {
            this(operatorClass, arity, ceylonMethod, -1);
        }
        
        public final OptimisationStrategy getUnOpOptimisationStrategy(Tree.Term expression, Tree.Term term, AbstractTransformer gen){
            // no optimised operator returns a boxed type 
            if(!expression.getUnboxed())
                return OptimisationStrategy.NONE;
            return isTermOptimisable(term, gen);
        }
        
        public OptimisationStrategy getBinOpOptimisationStrategy(Tree.Term expression, 
                Tree.Term leftTerm, 
                Tree.Term rightTerm, AbstractTransformer gen){
            return getBinOpOptimisationStrategy(expression, leftTerm, leftTerm.getTypeModel(),
                    rightTerm, rightTerm.getTypeModel(), gen);
        }
        
        public OptimisationStrategy getBinOpOptimisationStrategy(Tree.Term expression, 
                Tree.Term leftTerm, Type leftType, 
                Tree.Term rightTerm, Type rightType, AbstractTransformer gen){
            // no optimised operator returns a boxed type 
            if(!expression.getUnboxed())
                return OptimisationStrategy.NONE;
            // Can we do an operator optimization?
            OptimisationStrategy optimisationStrategy;
            OptimisationStrategy left = isTermOptimisable(leftTerm, leftType, gen);
            OptimisationStrategy right = isTermOptimisable(rightTerm, rightType, gen);
            optimisationStrategy = mostAggressive(left, right);
            if (optimisationStrategy != OptimisationStrategy.OPTIMISE) {
                // we can't use operator optimization, but maybe was can
                // use static value type method to avoid boxing
                if (Decl.isValueTypeDecl(leftType)) {
                    optimisationStrategy = OptimisationStrategy.OPTIMISE_VALUE_TYPE;
                } else if (leftType.getDeclaration().getSelfType() != null
                        && Decl.isValueTypeDecl(leftType.getTypeArguments().get(leftType.getDeclaration().getSelfType().getDeclaration()))) {
                    // a self type of a value type (e.g. Summable<Integer>)
                    optimisationStrategy = OptimisationStrategy.OPTIMISE_VALUE_TYPE;
                }
            }
            return optimisationStrategy;
        }
        
        /** Returns the least aggressive optimization of the two given optimizations */
        protected static OptimisationStrategy lessPermissive(OptimisationStrategy left, OptimisationStrategy right) {
            // it's from most permissive to less permissive, so return the one with the higher ordinal (less permissive)
            if(left.ordinal() > right.ordinal())
                return left;
            return right;
        }
        /** Returns the more aggressive optimization of the two given optimizations */
        protected static OptimisationStrategy mostAggressive(OptimisationStrategy left, OptimisationStrategy right) {
            // it's from most permissive to less permissive, so return the one with the higher ordinal (less permissive)
            if(left.ordinal() < right.ordinal())
                return left;
            return right;
        }
        final OptimisationStrategy isTermOptimisable(Tree.Term t, AbstractTransformer gen){
            return isTermOptimisable(t, t.getTypeModel(), gen);
        }
        final OptimisationStrategy isTermOptimisable(Tree.Term t, Type pt, AbstractTransformer gen){
            if(javacOperator < 0 || !t.getUnboxed())
                return OptimisationStrategy.NONE;
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
                case BYTE:
                    if(gen.isCeylonByte(pt))
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
        
        public int getArity(){
            return arity;
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
    private static final Map<String, OperatorTranslation> methodsAsOperators;
    private static final Map<Class<? extends Tree.AssignmentOp>, AssignmentOperatorTranslation> assignmentOperators;

    static {
        operators = new HashMap<Class<? extends Tree.OperatorExpression>, OperatorTranslation>();
        methodsAsOperators = new HashMap<String, OperatorTranslation>();
        assignmentOperators = new HashMap<Class<? extends Tree.AssignmentOp>, AssignmentOperatorTranslation>();
        
        for(OperatorTranslation operator : OperatorTranslation.values()){
            // some operators are virtual translations from method calls to java operators and so don't have
            // a Tree class
            if(operator.operatorClass != null) {
                operators.put(operator.operatorClass, operator);
            } else {
                for (PrimitiveType t : operator.optimisableTypes) {
                    String optimisedMethod = t.fqn + "." + operator.ceylonMethod;
                    methodsAsOperators.put(optimisedMethod , operator);
                }
            }
        }

        for(AssignmentOperatorTranslation operator : AssignmentOperatorTranslation.values())
            assignmentOperators.put(operator.operatorClass, operator);
    }

    public static OperatorTranslation getOperator(Class<? extends Tree.OperatorExpression> operatorClass) {
        return operators.get(operatorClass);
    }

    public static OperatorTranslation getOperator(String signature) {
        return methodsAsOperators.get(signature);
    }

    public static AssignmentOperatorTranslation getAssignmentOperator(Class<? extends Tree.AssignmentOp> operatorClass) {
        return assignmentOperators.get(operatorClass);
    }
    
    // only there to make sure this class is initialised before the enums defined in it, otherwise we
    // get an initialisation error
    public static void init(){}
}

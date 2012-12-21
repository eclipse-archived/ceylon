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
package com.redhat.ceylon.compiler.java.test.expression;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ExpressionTest extends CompilerTest {
	
	// Attributes
	
    @Test
    public void testAtrAttributeAccess(){
        compareWithJavaSource("attribute/AttributeAccess");
    }
    @Test
    public void testAtrAttributeAssign(){
        compareWithJavaSource("attribute/AttributeAssign");
    }
    @Test
    public void testAtrAttributeHidingMethodParam(){
        compareWithJavaSource("attribute/AttributeHidingMethodParam");
    }
    @Test
    public void testAtrIndirectQualifiedAttributeAccess(){
        compareWithJavaSource("attribute/IndirectQualifiedAttributeAccess");
    }
    @Test
    public void testAtrInitializerParamAccess(){
        compareWithJavaSource("attribute/InitializerParamAccess");
    }
    @Test
    public void testAtrInitializerParamAccessInInitializer(){
        compareWithJavaSource("attribute/InitializerParamAccessInInitializer");
    }
    @Test
    public void testAtrInitializerParamAccessWithExtends(){
        compareWithJavaSource("attribute/InitializerParamAccessWithExtends");
    }
    @Test
    public void testAtrInitializerParamHidingAttribute(){
        compareWithJavaSource("attribute/InitializerParamHidingAttribute");
    }
    @Test
    public void testAtrMethodLocalAccess(){
        compareWithJavaSource("attribute/MethodLocalAccess");
    }
    @Test
    public void testAtrMethodLocalAssign(){
        compareWithJavaSource("attribute/MethodLocalAssign");
    }
    @Test
    public void testAtrMethodLocalHidingAttribute(){
        compareWithJavaSource("attribute/MethodLocalHidingAttribute");
    }
    @Test
    public void testAtrBoxedLocalVariable(){
        compareWithJavaSource("attribute/BoxedLocalVariable");
    }
    @Test
    public void testAtrMethodParamAccess(){
        compareWithJavaSource("attribute/MethodParamAccess");
    }
    @Test
    public void testAtrMethodParamHidingAttribute(){
        compareWithJavaSource("attribute/MethodParamHidingAttribute");
    }
    @Test
    public void testAtrQualifiedAttributeAccess(){
        compareWithJavaSource("attribute/QualifiedAttributeAccess");
    }
    @Test
    public void testAtrQualifiedAttributeAssign(){
        compareWithJavaSource("attribute/QualifiedAttributeAssign");
    }
    @Test
    public void testAtrToplevelAccess(){
        compareWithJavaSource("attribute/TopLevelAccess");
    }
    @Test
    public void testAtrToplevelAssign(){
        compareWithJavaSource("attribute/TopLevelAssign");
    }

    // Boxing and unboxing
    
    @Test
    public void testBoxBooleanBoxing(){
        compareWithJavaSource("boxing/BooleanBoxing");
    }
    @Test
    public void testBoxCharacterBoxing(){
        compareWithJavaSource("boxing/CharacterBoxing");
    }
    @Test
    public void testBoxIntegerBoxing(){
        compareWithJavaSource("boxing/IntegerBoxing");
    }
    @Test
    public void testBoxFloatBoxing(){
        compareWithJavaSource("boxing/FloatBoxing");
    }
    @Test
    public void testBoxStringBoxing(){
        compareWithJavaSource("boxing/StringBoxing");
    }
    @Test
    public void testBoxVoidBoxing(){
        compareWithJavaSource("boxing/VoidBoxing");
    }

    // Erasure
    
    @Test
    public void testErsErasure(){
        compareWithJavaSource("erasure/Erasure");
    }
    @Test
    public void testErsTypeParameterErasure(){
        compareWithJavaSource("erasure/TypeParameterErasure");
    }


    // Literals
    
    @Test
    public void testLitSequenceLiteral(){
        compareWithJavaSource("literal/SequenceLiteral");
    }
    @Test
    public void testLitStringLiteral(){
        compareWithJavaSource("literal/StringLiteral");
    }
    @Test
    public void testLitCharacterLiteral(){
        compareWithJavaSource("literal/CharacterLiteral");
    }
    @Test
    public void testLitQuotedLiteral(){
        compareWithJavaSource("literal/QuotedLiteral");
        assertErrors("literal/QuotedLiteralErrors",
                new CompilerError(24, "Invalid hexadecimal literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(25, "Invalid hexadecimal literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(26, "Invalid hexadecimal literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(27, "Invalid hexadecimal literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(28, "Invalid hexadecimal literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(30, "Invalid binary literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(31, "Invalid binary literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(32, "Invalid binary literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(33, "Invalid binary literal (must be unsigned and fit in 64 bits)"),
                new CompilerError(34, "Invalid binary literal (must be unsigned and fit in 64 bits)")
        );
    }
    @Test
    public void testLitNumericLiteral(){
        compareWithJavaSource("literal/NumericLiteral");
        assertErrors("literal/NumericLiteralErrors",
                new CompilerError(24, "Literal outside representable range"),
                new CompilerError(25, "Literal outside representable range"),
                new CompilerError(27, "Literal so large it is indistinguishable from infinity"),
                new CompilerError(28, "Literal so large it is indistinguishable from infinity"),
                new CompilerError(29, "Literal so small it is indistinguishable from zero"),
                new CompilerError(30, "Literal so small it is indistinguishable from zero")
        );
    }
    
    // Self references
    
    @Test
    public void testSlfThisReference(){
        compareWithJavaSource("selfref/ThisReference");
    }
    @Test
    public void testSlfSuperReference(){
        compareWithJavaSource("selfref/SuperReference");
    }
    @Test
    public void testSlfOuterReference(){
        compareWithJavaSource("selfref/OuterReference");
    }
    @Test
    public void testSlfOuterInterface(){
        compareWithJavaSource("selfref/OuterInterface");
    }
    @Test
    public void testSlfOuterInterfaceFormal(){
        compareWithJavaSource("selfref/OuterInterfaceFormal");
    }
    @Test
    public void testSlfBaseOuter_fail() {
        compareWithJavaSource("selfref/BaseOuter");
    }
    @Test
    public void testSlfAssignOuter_fail() {
        compareWithJavaSource("selfref/AssignOuter");
    }
    @Test
    public void testSlfSuperInterface(){
        compareWithJavaSource("selfref/SuperInterface");
    }
    @Test
    public void testSlfSuperInterfaceTypeMember(){
        compareWithJavaSource("selfref/SuperInterfaceTypeMember");
    }
    @Test
    public void testSlfSuperClass(){
        compareWithJavaSource("selfref/SuperClass");
    }
    
    // Instantiation
    
    @Test
    public void testInsClassInstantiation(){
        compareWithJavaSource("instantiation/ClassInstantiation");
    }
    @Test
    public void testInsSubclassInstantiationVarargs(){
        compareWithJavaSource("instantiation/SubclassInstantiationVarargs");
    }
    @Test
    public void testInsSubclassInstantiationDefaultParam(){
        compareWithJavaSource("instantiation/SubclassInstantiationDefaultParam");
    }
    @Test
    public void testInsDefaultedParameterClassInstantiation(){
        compareWithJavaSource("instantiation/DefaultedParameterClassInstantiation");
    }
    @Test
    public void testInsDefaultedVariableAttribute(){
        compareWithJavaSource("instantiation/DefaultedVariableAttribute");
    }
    @Test
    public void testInsSequencedParameterClassInstantiation(){
        compareWithJavaSource("instantiation/SequencedParameterClassInstantiation");
    }
    @Test
    public void testInsEntryInstantiation(){
        compareWithJavaSource("instantiation/EntryInstantiation");
    }
    @Test
    public void testInsNestedGenericEntryInstantiation(){
        compareWithJavaSource("instantiation/NestedGenericEntryInstantiation");
    }
    @Test
    public void testInsGenericClassInstantiation(){
        compareWithJavaSource("instantiation/GenericClassInstantiation");
    }
    @Test
    public void testInsRangeInstantiation(){
        compareWithJavaSource("instantiation/RangeInstantiation");
    }
    @Test
    public void testInsSequenceInstantiation(){
        compareWithJavaSource("instantiation/SequenceInstantiation");
    }
    @Test
    public void testInsMethodLocalInstantiation(){
        compareWithJavaSource("instantiation/MethodLocalInstantiation");
    }
    @Test
    public void testInsGetterLocalInstantiation(){
        compareWithJavaSource("instantiation/GetterLocalInstantiation");
    }
    @Test
    public void testInsSetterLocalInstantiation(){
        compareWithJavaSource("instantiation/SetterLocalInstantiation");
    }
    @Test
    public void testQualifiedInstantiation(){
        compareWithJavaSource("instantiation/QualifiedInstantiation");
    }
    @Test
    public void testQualifiedParameterisedInstantiation(){
        compareWithJavaSource("instantiation/QualifiedParameterisedInstantiation");
    }
    @Test
    public void testQualifiedInstantiationInInterface(){
        compareWithJavaSource("instantiation/QualifiedInstantiationInInterface");
    }
    // Operators
    
    @Test
    public void testOprEqualityAndComparisonOperators(){
        compareWithJavaSource("operator/EqualityAndComparisonOperators");
    }
    @Test
    public void testOprLogicalOperators(){
        compareWithJavaSource("operator/LogicalOperators");
    }
    @Test
    public void testOprNullHandlingOperators(){
        compareWithJavaSource("operator/NullHandlingOperators");
    }
    @Test
    public void testOprSequenceOperators(){
        compareWithJavaSource("operator/SequenceOperators");
    }
    @Test
    public void testOprIndexingTuples(){
        compareWithJavaSource("operator/IndexingTuples");
    }
    @Test
    public void testOprCreatorOperators(){
        compareWithJavaSource("operator/CreatorOperators");
    }
    @Test
    public void testOprArithmeticOperators(){
        compareWithJavaSource("operator/ArithmeticOperators");
    }
    @Test
    public void testOprBitwiseOperators(){
        compareWithJavaSource("operator/BitwiseOperators");
    }
    @Test
    public void testOprOptionalTypeParamAssign(){
        compareWithJavaSource("operator/OptionalTypeParamAssign");
    }
    @Test
    public void testOprOptim(){
        compareWithJavaSource("operator/Optim");
    }
    @Test
    public void testOprSetOperators(){
    	compareWithJavaSource("operator/SetOperators");
    }
    @Test
    public void testOprParenthesized(){
        compareWithJavaSource("operator/Parenthesized");
    }
    @Test
    public void testOprSpread(){
        compareWithJavaSource("operator/Spread");
    }
    @Test
    public void testOprSpreadMethodReference(){
        compareWithJavaSource("operator/SpreadMethodReference");
    }
    @Test
    public void testOprNullSafeMethodReference(){
        compareWithJavaSource("operator/NullSafeMethodReference");
    }
    
    @Test
    public void testOprNullSafeVoidMethodReference(){
        compareWithJavaSource("operator/NullSafeVoidMethodReference");
    }
    
    @Test
    public void testStpStringTemplate(){
        compareWithJavaSource("stringtemp/StringTemplate");
    }
    
    @Test
    public void testLmdAnonFunctionNullary(){
        compareWithJavaSource("lambda/AnonFunctionNullary");
    }
    
    @Test
    public void testLmdToplevelMethodSpecifyingLambda(){
        compareWithJavaSource("lambda/ToplevelMethodSpecifyingLambda");
    }
    
    @Test
    public void testLmdMethodSpecifyingLambda(){
        compareWithJavaSource("lambda/MethodSpecifyingLambda");
    }
    
    @Test
    public void testLmdDefaultedParameter(){
        compareWithJavaSource("lambda/DefaultedParameter");
    }
    
    @Test
    public void testLmdConstrainedTypeParam(){
        compareWithJavaSource("lambda/ConstrainedTypeParam");
    }
    
    //
    // Optimisations

    @Test
    public void testOptmCharacterAsInteger(){
        compareWithJavaSource("optimisations/CharacterAsInteger");
    }
}

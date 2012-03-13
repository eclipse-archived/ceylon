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
    public void testAtrInitializerParamHidingGetter(){
        compareWithJavaSource("attribute/InitializerParamHidingGetter");
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
    public void testLitStringLiteral(){
        compareWithJavaSource("literal/StringLiteral");
    }
    @Test
    public void testLitCharacterLiteral(){
        compareWithJavaSource("literal/CharacterLiteral");
    }
    @Ignore("M2")
    @Test
    public void testLitQuotedLiteral(){
        compareWithJavaSource("literal/QuotedLiteral");
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
    
    // Method invocation

    @Test
    public void testInvChainedInvocations(){
        compareWithJavaSource("invoke/ChainedInvocations");
    }

    @Test
    public void testInvGenericMethodInvocation(){
        compareWithJavaSource("invoke/GenericMethodInvocation");
    }
    
    @Test
    public void testInvGenericMethodInvocationMixed(){
        compareWithJavaSource("invoke/GenericMethodInvocationMixed");
    }

    @Test
    public void testInvInnerMethodInvocation(){
        compareWithJavaSource("invoke/InnerMethodInvocation");
    }

    @Test
    public void testInvInvocationErasure(){
        compareWithJavaSource("invoke/InvocationErasure");
    }
    
    @Test
    public void testInvMethodInvocation(){
        compareWithJavaSource("invoke/MethodInvocation");
    }

    @Test
    public void testInvMethodInvocationWithDefaultedParameters(){
        compareWithJavaSource("invoke/MethodInvocationWithDefaultedParameters");
    }

    @Test
    @Ignore("Not for needed for M1")
    public void testInvNamedArgumentGetterInvocation(){
        compareWithJavaSource("invoke/NamedArgumentGetterInvocation");
    }

    @Test
    public void testInvNamedArgumentInvocation(){
        compareWithJavaSource("invoke/NamedArgumentInvocation");
    }
    
    @Test
    public void testInvNamedArgumentNoArgs(){
        compareWithJavaSource("invoke/NamedArgumentNoArgs");
    }
    
    @Test
    public void testInvNamedArgumentInvocationInit(){
        compareWithJavaSource("invoke/NamedArgumentInvocationInit");
    }
    
    @Test
    public void testInvNamedArgumentInvocationTopLevel(){
        compareWithJavaSource("invoke/NamedArgumentInvocationTopLevel");
    }
    
    @Test
    public void testInvNamedArgumentInvocationLocal(){
        compareWithJavaSource("invoke/NamedArgumentInvocationLocal");
    }

    @Test
    public void testInvNamedArgumentWithSequence(){
        compareWithJavaSource("invoke/NamedArgumentWithSequence");
    }
    
    @Test
    public void testInvNamedArgumentWithEmptySequence(){
        compareWithJavaSource("invoke/NamedArgumentWithEmptySequence");
    }
    
    @Test
    public void testInvNamedArgumentInvocationInitWithSequence(){
        compareWithJavaSource("invoke/NamedArgumentInvocationInitWithSequence");
    }
    
    @Test
    public void testInvNamedArgumentInvocationInitWithEmptySequence(){
        compareWithJavaSource("invoke/NamedArgumentInvocationInitWithEmptySequence");
    }
    
    @Test
    public void testInvNamedArgumentInvocationOnPrimitive(){
        compareWithJavaSource("invoke/NamedArgumentInvocationOnPrimitive");
    }
    
    @Test
    public void testInvNamedArgumentSequencedTypeParamInvocation(){
        compareWithJavaSource("invoke/NamedArgumentSequencedTypeParamInvocation");
    }
    
    @Test
    public void testInvSequencedParameterInvocation(){
        compareWithJavaSource("invoke/SequencedParameterInvocation");
    }
    
    @Test
    public void testInvSequencedTypeParamInvocation(){
        compareWithJavaSource("invoke/SequencedTypeParamInvocation");
    }

    @Test
    public void testInvToplevelMethodInvocation(){
        compareWithJavaSource("invoke/ToplevelMethodInvocation");
    }

    @Test
    public void testInvToplevelMethodWithDefaultedParams(){
        compareWithJavaSource("invoke/ToplevelMethodWithDefaultedParams");
    }
    
    @Test
    public void testInvOptionalTypeParamArgument(){
        compareWithJavaSource("invoke/OptionalTypeParamArgument");
    }
    
    @Test
    public void testCallableArgumentNullary(){
        compareWithJavaSource("invoke/CallableArgumentNullary");
    }
    
    @Test
    public void testCallableArgumentUnary(){
        compareWithJavaSource("invoke/CallableArgumentUnary");
    }
    
    @Test
    public void testCallableArgumentBinary(){
        compareWithJavaSource("invoke/CallableArgumentBinary");
    }
    
    @Test
    public void testCallableArgumentTernary(){
        compareWithJavaSource("invoke/CallableArgumentTernary");
    }
    
    @Test
    public void testCallableArgumentNary(){
        compareWithJavaSource("invoke/CallableArgumentNary");
    }
    
    @Test
    public void testCallableArgumentSequenced(){
        compareWithJavaSource("invoke/CallableArgumentSequenced");
    }
    @Test
    public void testCallableArgumentVarargs(){
        compareWithJavaSource("invoke/CallableArgumentVarargs");
    }
    @Test
    public void testCallableArgumentVarargs2(){
        compareWithJavaSource("invoke/CallableArgumentVarargs2");
    }
    
    @Test
    public void testCallableArgumentParameterClass(){
        compareWithJavaSource("invoke/CallableArgumentParameterClass");
    }
    
    @Test
    public void testCallableArgumentParameterQualified(){
        compareWithJavaSource("invoke/CallableArgumentParameterQualified");
    }
    
    @Test
    public void testCallableArgumentParameterTypeParam(){
        compareWithJavaSource("invoke/CallableArgumentParameterTypeParam");
    }
    
    @Test
    public void testCallableArgumentParameterTypeParamMixed(){
        compareWithJavaSource("invoke/CallableArgumentParameterTypeParamMixed");
    }
    
    @Test
    @Ignore("Awaiting support for parameter bounds")
    public void testCallableArgumentParameterCtor(){
        compareWithJavaSource("invoke/CallableArgumentParameterCtor");
    }
    
    @Test
    public void testCallableArgumentPassed(){
        compareWithJavaSource("invoke/CallableArgumentPassed");
    }
    
    @Test
    public void testCallableArgumentReturningInteger(){
        compareWithJavaSource("invoke/CallableArgumentReturningInteger");
    }
    
    @Test
    public void testCallableArgumentReturningClass(){
        compareWithJavaSource("invoke/CallableArgumentReturningClass");
    }
    
    @Test
    public void testCallableArgumentReturningQualifiedClass(){
        compareWithJavaSource("invoke/CallableArgumentReturningQualifiedClass");
    }
    
    @Test
    public void testCallableArgumentReturningTypeParam(){
        compareWithJavaSource("invoke/CallableArgumentReturningTypeParam");
    }
    
    @Test
    public void testCallableReturnNullary(){
        compareWithJavaSource("invoke/CallableReturnNullary");
    }
    
    @Test
    public void testCallableReturnUnary(){
        compareWithJavaSource("invoke/CallableReturnUnary");
    }
    
    @Test
    public void testCallableReturnBinary(){
        compareWithJavaSource("invoke/CallableReturnBinary");
    }
    
    @Test
    public void testCallableReturnTernary(){
        compareWithJavaSource("invoke/CallableReturnTernary");
    }
    
    @Test
    public void testCallableReturnNary(){
        compareWithJavaSource("invoke/CallableReturnNary");
    }
    
    @Test
    public void testCallableReturnCallable(){
        compareWithJavaSource("invoke/CallableReturnCallable");
    }
    
    @Test
    public void testCallableReturnMethod(){
        compareWithJavaSource("invoke/CallableReturnMethod");
    }
    
    @Test
    public void testCallableReturnReturningInteger(){
        compareWithJavaSource("invoke/CallableReturnReturningInteger");
    }
    
    @Test
    public void testCallableReturnReturningClass(){
        compareWithJavaSource("invoke/CallableReturnReturningClass");
    }
    
    @Test
    public void testCallablePositionalInvocationNullary(){
        compareWithJavaSource("invoke/CallablePositionalInvocationNullary");
    }
    
    @Test
    public void testCallablePositionalInvocationUnary(){
        compareWithJavaSource("invoke/CallablePositionalInvocationUnary");
    }
    
    @Test
    public void testCallablePositionalInvocationSequenced(){
        compareWithJavaSource("invoke/CallablePositionalInvocationSequenced");
    }
    
    @Test
    @Ignore("Awaiting fix for ceylon-spec#218")
    public void testCallablePositionalInvocationQualified(){
        compareWithJavaSource("invoke/CallablePositionalInvocationQualified");
    }
    
    @Test
    public void testCallableNamedInvocationNullary(){
        compareWithJavaSource("invoke/CallableNamedInvocationNullary");
    }
    
    @Test
    public void testCallableNamedInvocationUnary(){
        compareWithJavaSource("invoke/CallableNamedInvocationUnary");
    }
    
    @Test
    public void testCallableNamedInvocationBinary(){
        compareWithJavaSource("invoke/CallableNamedInvocationBinary");
    }
    
    @Test
    public void testCallableNamedInvocationNary(){
        compareWithJavaSource("invoke/CallableNamedInvocationNary");
    }
    
    @Test
    public void testCallableNamedInvocationSequenced(){
        compareWithJavaSource("invoke/CallableNamedInvocationSequenced");
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
    
    // Instantiation
    
    @Test
    public void testInsClassInstantiation(){
        compareWithJavaSource("instantiation/ClassInstantiation");
    }
    @Test
    public void testInsDefaultedParameterClassInstantiation(){
        compareWithJavaSource("instantiation/DefaultedParameterClassInstantiation");
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
    public void testInsNestedGenericClassInstantiation(){
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
    public void testOprCreatorOperators(){
        compareWithJavaSource("operator/CreatorOperators");
    }
    @Test
    public void testOprArithmeticOperators(){
        compareWithJavaSource("operator/ArithmeticOperators");
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
    public void testStpStringTemplate(){
        compareWithJavaSource("stringtemp/StringTemplate");
    }
}

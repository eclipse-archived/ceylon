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
package com.redhat.ceylon.compiler.test.expression;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

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
    @Ignore("M2")
    @Test
    public void testLitQuotedLiteral(){
        compareWithJavaSource("literal/QuotedLiteral");
    }
    @Test
    public void testLitNumericLiteral(){
        compareWithJavaSource("literal/NumericLiteral");
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
    @Ignore("M2")
    public void testOprSlotwiseOperators(){
        compareWithJavaSource("operator/SlotwiseOperators");
    }
    
    @Test
    public void testStpStringTemplate(){
        compareWithJavaSource("stringtemp/StringTemplate");
    }
}

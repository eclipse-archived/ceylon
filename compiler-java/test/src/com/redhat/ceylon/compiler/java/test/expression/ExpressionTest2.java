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

public class ExpressionTest2 extends CompilerTest {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-2";
    }

    // Method invocation

    @Test
    public void testInvAnonymousFunctionPositionalInvocation(){
        compareWithJavaSource("invoke/AnonymousFunctionPositionalInvocation");
    }
    @Test
    public void testInvAnonymousFunctionPositionalInvocation2(){
        compareWithJavaSource("invoke/AnonymousFunctionPositionalInvocation2");
    }
    @Test
    public void testInvAnonymousStatementFunction(){
        compareWithJavaSource("invoke/AnonymousStatementFunction");
    }
    @Test
    public void testInvMethodArgumentNamedInvocation(){
        compareWithJavaSource("invoke/MethodArgumentNamedInvocation");
    }
    @Test
    public void testInvMethodArgumentNamedInvocationVoid(){
        compareWithJavaSource("invoke/MethodArgumentNamedInvocationVoid");
    }
    @Test
    public void testInvMethodArgumentNamedInvocation2(){
        compareWithJavaSource("invoke/MethodArgumentNamedInvocation2");
    }
    @Test
    public void testInvMethodArgumentNamedInvocationMPL(){
        compareWithJavaSource("invoke/MethodArgumentNamedInvocationMPL");
    }
    
    @Test
    public void testInvMethodArgumentWithVariableParameterNamedInvocation(){
        compareWithJavaSource("invoke/MethodArgumentWithVariableParameterNamedInvocation");
    }
    
    
    @Test
    public void testInvObjectArgumentNamedInvocation(){
        compareWithJavaSource("invoke/ObjectArgumentNamedInvocation");
    }
    
    @Test
    public void testInvObjectArgumentNamedInvocationChained(){
        compareWithJavaSource("invoke/ObjectArgumentNamedInvocationChained");
        compileAndRun("com.redhat.ceylon.compiler.java.test.expression.invoke.objectArgumentNamedInvocationChained", "invoke/ObjectArgumentNamedInvocationChained.ceylon");
    }
    
    @Test
    public void testInvGetterArgumentNamedInvocation(){
        compareWithJavaSource("invoke/GetterArgumentNamedInvocation");
    }
    
    @Test
    public void testInvGetterArgumentNamedInvocationGeneric(){
        compareWithJavaSource("invoke/GetterArgumentNamedInvocationGeneric");
    }
    
    @Test
    public void testInvGetterArgumentNamedInvocationBoxing(){
        compareWithJavaSource("invoke/GetterArgumentNamedInvocationBoxing");
    }
    
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
    public void testInvNamedArgumentWithIterable(){
        compareWithJavaSource("invoke/NamedArgumentWithIterable");
    }
    
    @Test
    public void testInvInvocationWithVarargsAndComprehensions(){
        compareWithJavaSource("invoke/InvocationWithVarargsAndComprehensions");
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
    public void testInvNamedArgumentInvocationWithMethodReference(){
        compareWithJavaSource("invoke/NamedArgumentInvocationWithMethodReference");
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
    public void testInvSequencedTypeParamInvocation2(){
        compareWithJavaSource("invoke/SequencedTypeParamInvocation2");
    }
    
    @Test
    public void testInvZeroSequencedArgs(){
        compareWithJavaSource("invoke/ZeroSequencedArgs");
    }
    
    @Test
    public void testInvDefaultedAndSequenced(){
        compareWithJavaSource("invoke/DefaultedAndSequencedParams");
    }
    @Test
    public void testInvDefaultedAndTypeParams(){
        compareWithJavaSource("invoke/DefaultedAndTypeParams");
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
    public void testCallableAndDefaultedArguments(){
        compile("invoke/CallableAndDefaultedArguments_foo.ceylon");
        compareWithJavaSource("invoke/CallableAndDefaultedArguments_bar");
    }
    
    @Test
    @Ignore("M5??: #512: Not supported at the moment")
    public void testCallableArgumentWithDefaultedArguments(){
        compareWithJavaSource("invoke/CallableArgumentWithDefaulted");
        // Note we want to run it as well, because one of the problems 
        // is not found at compile time (#512)
        compileAndRun("com.redhat.ceylon.compiler.java.test.expression.invoke.callableArgumentWithDefaulted_main", 
                "invoke/CallableArgumentWithDefaulted");
    }

    @Test
    public void testCallableWithDefaultedArguments(){
        compareWithJavaSource("invoke/CallableWithDefaulted");
    }

    @Test
    public void testCallableArgumentVoid(){
        compareWithJavaSource("invoke/CallableArgumentVoid");
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
    @Ignore("M5: Awaiting support for parameter bounds")
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
    public void testCallableCapture(){
        compareWithJavaSource("invoke/CallableCapture");
    }
    
    @Test
    public void testCallablePositionalInvocationAndReturn(){
        compareWithJavaSource("invoke/CallablePositionalInvocationAndReturn");
    }
    
    @Test
    public void testCallablePositionalInvocationSequenced(){
        compareWithJavaSource("invoke/CallablePositionalInvocationSequenced");
    }
    
    @Test
    public void testCallablePositionalInvocationSequencedComprehension(){
        compareWithJavaSource("invoke/CallablePositionalInvocationSequencedComprehension");
    }
    
    @Test
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
    
    @Test
    public void testIndirectInvoke(){
        compareWithJavaSource("invoke/IndirectInvoke");
    }
    
    @Test
    public void testIndirectTypeParam(){
        compareWithJavaSource("invoke/IndirectTypeParam");
    }
    
    @Test
    public void testDefaultFunctionReference(){
        compareWithJavaSource("invoke/DefaultFunctionReference");
    }
    
    @Test
    public void testFunctionalParameterMpl(){
        compile("invoke/FunctionalParameterMpl.ceylon");
        compile("invoke/FunctionalParameterMpl2.ceylon");
    }
    
    @Test
    public void testInvSelfType(){
        compareWithJavaSource("invoke/SelfType");
    }
    
    @Test @Ignore("Functionality not available anymore, keeping it for possible future language enhancement")
    public void testInvSelfTypeGeneric(){
        compareWithJavaSource("invoke/SelfTypeGeneric");
    }
    
    @Test
    public void testInvTypeFamily(){
        compareWithJavaSource("invoke/TypeFamily");
    }
    
    @Test @Ignore("Functionality not available anymore, keeping it for possible future language enhancement")
    public void testInvTypeFamilyGeneric(){
        compareWithJavaSource("invoke/TypeFamilyGeneric");
    }
    
    @Test
    public void testInvSelfTypeInstantiation(){
        compareWithJavaSource("invoke/SelfTypeInstantiation");
    }
    
    @Test
    public void testInvOptionalCallable(){
        compareWithJavaSource("invoke/OptionalCallable");
    }

    @Test
    public void testInvMultipleParameterLists(){
        compareWithJavaSource("invoke/MultipleParameterLists");
        compareWithJavaSource("invoke/MultipleParameterLists_call");
    }
    @Test
    public void testInvMultipleParameterListsWithVariableParameters(){
        compareWithJavaSource("invoke/MultipleParameterListsWithVariableParameters");
    }
    
    @Test
    public void testInvMultipleParameterListsMemberQual(){
        compareWithJavaSource("invoke/MultipleParameterListsMemberQual");
    }
    
    @Test
    public void testInvMultipleParameterListsFatArrow(){
        compareWithJavaSource("invoke/MultipleParameterListsFatArrow");
    }
    
    @Test
    public void testAvoidBackwardBranchWithVarargs(){
        compileAndRun(
                "com.redhat.ceylon.compiler.java.test.expression.invoke.avoidBackwardBranchWithVarargs_run", 
                "invoke/AvoidBackwardBranchWithVarargs.ceylon");
    }
    
    @Test
    public void testInvSpreadArguments(){
        compareWithJavaSource("invoke/SpreadArguments");
    }
    
    @Test
    public void testInvSpreadArgumentsNoOpt(){
        compareWithJavaSourceNoOpt("invoke/SpreadArguments");
    }
}

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
package com.redhat.ceylon.compiler.java.test.statement;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class StatementTest extends CompilerTest {
    
    //
    // Method attributes and variables
    
    @Test
    public void testAtrMethodAttribute(){
        compareWithJavaSource("attribute/MethodAttribute");
    }
    
    @Test
    public void testAtrMethodAttributeWithInitializer(){
        compareWithJavaSource("attribute/MethodAttributeWithInitializer");
    }

    @Test
    public void testAtrMethodAttributeWithLateInitializer(){
        compareWithJavaSource("attribute/MethodAttributeWithLateInitializer");
    }

    @Test
    public void testAtrMethodVariable(){
        compareWithJavaSource("attribute/MethodVariable");
    }

    @Test
    public void testAtrMethodVariableWithInitializer(){
        compareWithJavaSource("attribute/MethodVariableWithInitializer");
    }

    @Test
    public void testAtrMethodVariableWithLateInitializer(){
        compareWithJavaSource("attribute/MethodVariableWithLateInitializer");
    }

    //
    // if/else

    @Test
    public void testConInitializerIf(){
        compareWithJavaSource("conditional/InitializerIf");
    }

    @Test
    public void testConInitializerIfElse(){
        compareWithJavaSource("conditional/InitializerIfElse");
    }

    @Test
    public void testConInitializerIfElseIf(){
        compareWithJavaSource("conditional/InitializerIfElseIf");
    }

    @Test
    public void testConMethodIf(){
        compareWithJavaSource("conditional/MethodIf");
    }

    @Test
    public void testConMethodIfElse(){
        compareWithJavaSource("conditional/MethodIfElse");
    }

    @Test
    public void testConMethodIfElseIf(){
        compareWithJavaSource("conditional/MethodIfElseIf");
    }
    
    @Test
    public void testConMethodIfTrue(){
        compareWithJavaSource("conditional/MethodIfTrue");
    }
    
    @Test
    public void testConMethodIfTrueElse(){
        compareWithJavaSource("conditional/MethodIfTrueElse");
    }
    
    @Test
    public void testConMethodIfFalse(){
        compareWithJavaSource("conditional/MethodIfFalse");
    }
    
    @Test
    public void testConMethodIfFalseElse(){
        compareWithJavaSource("conditional/MethodIfFalseElse");
    }

    @Test
    public void testConMethodIfExists(){
        compareWithJavaSource("conditional/MethodIfExists");
    }
    
    @Test
    public void testConMethodIfExistsSequence(){
        compareWithJavaSource("conditional/MethodIfExistsSequence");
    }
    
    @Test
    public void testConMethodIfExistsWithMethod(){
        compareWithJavaSource("conditional/MethodIfExistsWithMethod");
    }
    
    @Test
    public void testConMethodIfExists2dArray(){
        compareWithJavaSource("conditional/MethodIfExists2dArray");
    }

    @Test
    public void testConMethodIfIsFoo(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/MethodIfIsFoo");
    }

    @Test
    public void testConMethodIfIsVoidSeq(){
        compareWithJavaSource("conditional/MethodIfIsVoidSeq");
    }
    
    @Test
    public void testConMethodIfIsNotNull(){
        compareWithJavaSource("conditional/MethodIfIsNotNull");
    }
    
    @Test
    public void testConMethodIfIsNotObject(){
        compareWithJavaSource("conditional/MethodIfIsNotObject");
    }
    
    @Test
    public void testConMethodIfIsNullUnion(){
        compareWithJavaSource("conditional/MethodIfIsNullUnion");
    }
    
    @Test
    public void testConMethodIfIsWithIntersection(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/MethodIfIsWithIntersection");
    }
    
    @Test
    public void testConMethodIfIsWithMethod(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/MethodIfIsWithMethod");
    }
    

    @Test
    public void testConMethodIfIsWithUnion(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/MethodIfIsWithUnion");
    }
    
    @Test
    public void testConMethodIfIsNull2(){
        compareWithJavaSource("conditional/MethodIfIsNull2");
    }
    
    @Test
    public void testConMethodIfIsNull(){
        compareWithJavaSource("conditional/MethodIfIsNull");
    }

    @Test
    @Ignore("M5: requires reified generics")
    public void testConMethodIfIsGeneric(){
        compareWithJavaSource("conditional/MethodIfIsGeneric");
    }

    @Test
    @Ignore("M5: requires support from spec for satisfies conditions (reified generics)")
    public void testConMethodIfSatisfies(){
        compareWithJavaSource("conditional/MethodIfSatisfies");
    }

    @Test
    @Ignore("M5: requires support from spec for satisfies conditions (reified generics)")
    public void testConMethodIfSatisfiesMultiple(){
        compareWithJavaSource("conditional/MethodIfSatisfiesMultiple");
    }

    @Test
    public void testConMethodIfNonEmptySequence(){
        compareWithJavaSource("conditional/MethodIfNonEmptySequence");
    }
    
    @Test
    public void testConMethodIfConditionListBoolBool(){
        compareWithJavaSource("conditional/MethodIfConditionListBoolBool");
    }
    @Test
    public void testConMethodIfConditionListIsIs(){
        compareWithJavaSource("conditional/MethodIfConditionListIsIs");
    }
    @Test
    public void testConMethodIfConditionListBoolBoolIs(){
        compareWithJavaSource("conditional/MethodIfConditionListBoolBoolIs");
    }
    @Test
    public void testConMethodIfConditionListBoolIsBool(){
        compareWithJavaSource("conditional/MethodIfConditionListBoolIsBool");
    }
    @Test
    public void testConMethodIfConditionListExistsIsBool(){
        compareWithJavaSource("conditional/MethodIfConditionListExistsIsBool");
    }
    @Test
    public void testConMethodIfConditionListIsBool(){
        compareWithJavaSource("conditional/MethodIfConditionListIsBool");
    }
    @Test
    public void testConMethodIfConditionListIsBoolBool(){
        compareWithJavaSource("conditional/MethodIfConditionListIsBoolBool");
    }
    @Test
    public void testConMethodIfConditionListNonemptyIsBool(){
        compareWithJavaSource("conditional/MethodIfConditionListNonemptyIsBool");
    }

    //
    // for

    @Test
    public void testLopMethodForRange(){
        compareWithJavaSource("loop/MethodForRange");
    }
    
    @Test
    public void testLopMethodForIterator(){
        compareWithJavaSource("loop/MethodForIterator");
    }
    
    @Test
    public void testLopMethodForDoubleIterator(){
        compareWithJavaSource("loop/MethodForDoubleIterator");
    }
    
    @Test
    public void testLopMethodForElse(){
        compareWithJavaSource("loop/MethodForElse");
    }
    
    @Test
    public void testLopMethodForBreakElse(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.methodForBreakElse", 
                "loop/MethodForBreakElse.ceylon");
    }
    
    @Test
    public void testLopRangeOpIterationOptimization(){
        compareWithJavaSource("loop/RangeOpIterationOptimization");
    }
    
    @Test
    public void testLopRangeOpIterationOptimizationCorrect(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.statement.loop.rangeOpIterationOptimizationCorrect", 
                "loop/RangeOpIterationOptimizationCorrect.ceylon");
    }
    
    //
    // [do] while
    
    @Test
    public void testLopMethodWhile(){
        compareWithJavaSource("loop/MethodWhile");
    }
    
    @Test
    public void testLopMethodWhileTrue(){
        compareWithJavaSource("loop/MethodWhileTrue");
    }
    
    @Test
    public void testLopMethodWhileConditionList(){
        compareWithJavaSource("loop/MethodWhileConditionList");
    }
    
    @Test
    public void testConMethodWhileExists(){
        compareWithJavaSource("loop/MethodWhileExists");
    }
    
    @Test
    public void testConMethodWhileExistsSequence(){
        compareWithJavaSource("loop/MethodWhileExistsSequence");
    }
    
    @Test
    public void testConMethodWhileExistsWithMethod(){
        compareWithJavaSource("loop/MethodWhileExistsWithMethod");
    }
    
    @Test
    public void testConMethodWhileExists2dArray(){
        compareWithJavaSource("loop/MethodWhileExists2dArray");
    }

    @Test
    public void testConMethodWhileIsFoo(){
        compile("loop/FooBar.ceylon");
        compareWithJavaSource("loop/MethodWhileIsFoo");
    }
    
    @Test
    public void testConMethodWhileIsNotNull(){
        compareWithJavaSource("loop/MethodWhileIsNotNull");
    }
    
    @Test
    public void testConMethodWhileIsNotObject(){
        compareWithJavaSource("loop/MethodWhileIsNotObject");
    }
    
    @Test
    public void testConMethodWhileIsNullUnion(){
        compareWithJavaSource("loop/MethodWhileIsNullUnion");
    }
    
    @Test
    public void testConMethodWhileIsWithIntersection(){
        compile("loop/FooBar.ceylon");
        compareWithJavaSource("loop/MethodWhileIsWithIntersection");
    }
    
    @Test
    public void testConMethodWhileIsWithMethod(){
        compile("loop/FooBar.ceylon");
        compareWithJavaSource("loop/MethodWhileIsWithMethod");
    }
    

    @Test
    public void testConMethodWhileIsWithUnion(){
        compile("loop/FooBar.ceylon");
        compareWithJavaSource("loop/MethodWhileIsWithUnion");
    }
    
    @Test
    public void testConMethodWhileIsNull2(){
        compareWithJavaSource("loop/MethodWhileIsNull2");
    }
    
    @Test
    public void testConMethodWhileIsNull(){
        compareWithJavaSource("loop/MethodWhileIsNull");
    }

    @Test
    @Ignore("M5: requires reified generics")
    public void testConMethodWhileIsGeneric(){
        compareWithJavaSource("loop/MethodWhileIsGeneric");
    }

    @Test
    @Ignore("M5: requires support from spec for satisfies conditions (reified generics)")
    public void testConMethodWhileSatisfies(){
        compareWithJavaSource("loop/MethodWhileSatisfies");
    }

    @Test
    @Ignore("M5: requires support from spec for satisfies conditions (reified generics)")
    public void testConMethodWhileSatisfiesMultiple(){
        compareWithJavaSource("loop/MethodWhileSatisfiesMultiple");
    }

    @Test
    public void testConMethodWhileNonEmptySequence(){
        compareWithJavaSource("loop/MethodWhileNonEmptySequence");
    }
    
    @Test
    public void testConMethodWhileConditionListBoolBool(){
        compareWithJavaSource("loop/MethodWhileConditionListBoolBool");
    }
    @Test
    public void testConMethodWhileConditionListIsIs(){
        compareWithJavaSource("loop/MethodWhileConditionListIsIs");
    }
    @Test
    public void testConMethodWhileConditionListBoolBoolIs(){
        compareWithJavaSource("loop/MethodWhileConditionListBoolBoolIs");
    }
    @Test
    public void testConMethodWhileConditionListBoolIsBool(){
        compareWithJavaSource("loop/MethodWhileConditionListBoolIsBool");
    }
    @Test
    public void testConMethodWhileConditionListExistsIsBool(){
        compareWithJavaSource("loop/MethodWhileConditionListExistsIsBool");
    }
    @Test
    public void testConMethodWhileConditionListIsBool(){
        compareWithJavaSource("loop/MethodWhileConditionListIsBool");
    }
    @Test
    public void testConMethodWhileConditionListIsBoolBool(){
        compareWithJavaSource("loop/MethodWhileConditionListIsBoolBool");
    }
    @Test
    public void testConMethodWhileConditionListNonemptyIsBool(){
        compareWithJavaSource("loop/MethodWhileConditionListNonemptyIsBool");
    }

    
    //
    // Locals (value / function)
    
    @Test
    public void testLocValueKeyword(){
        compareWithJavaSource("local/ValueKeyword");
    }
    
    @Test
    public void testLocFunctionKeyword(){
        compareWithJavaSource("local/FunctionKeyword");
    }
    
    @Test
    public void testLocFunctionAndValueKeyword(){
        compareWithJavaSource("local/FunctionAndValueKeyword");
    }
    
    @Test
    public void testTryExceptionAttr(){
        compareWithJavaSource("trycatch/ExceptionAttr");
    }
    
    @Test
    public void testTryExceptionAttributes(){
        compareWithJavaSource("trycatch/ExceptionAttributes");
    }
    
    @Test
    public void testTryBareThrow(){
        compareWithJavaSource("trycatch/Throw");
    }
    
    @Test
    public void testTryThrowException(){
        compareWithJavaSource("trycatch/ThrowException");
    }
    
    @Test
    public void testTryThrowExceptionNamedArgs(){
        compareWithJavaSource("trycatch/ThrowExceptionNamedArgs");
    }
    
    @Test
    public void testTryThrowExceptionSubclass(){
        compareWithJavaSource("trycatch/ThrowExceptionSubclass");
    }
    
    @Test
    public void testTryThrowMethodResult(){
        compareWithJavaSource("trycatch/ThrowMethodResult");
    }
    
    @Test
    public void testTryThrowThrowable(){
        compareWithJavaSource("trycatch/ThrowThrowable");
    }
    
    @Test
    public void testTryThrowNpe(){
        compareWithJavaSource("trycatch/ThrowNpe");
    }
    
    @Test
    public void testTryTryFinally(){
        compareWithJavaSource("trycatch/TryFinally");
    }
    
    @Test
    public void testTryTryCatch(){
        compareWithJavaSource("trycatch/TryCatch");
    }
    
    @Test
    public void testTryTryCatchFinally(){
        compareWithJavaSource("trycatch/TryCatchFinally");
    }
    
    @Test
    public void testTryTryCatchSubclass(){
        compareWithJavaSource("trycatch/TryCatchSubclass");
    }
    
    @Test
    public void testTryTryCatchUnion(){
        compareWithJavaSource("trycatch/TryCatchUnion");
    }
    
    @Test
    public void testTryTryWithResource(){
        compareWithJavaSource("trycatch/TryWithResource");
    }
    
    @Test
    public void testTryReplaceExceptionAtJavaCallSite(){
        compareWithJavaSource("trycatch/WrapExceptionAtJavaCallSite");
    }
    
    @Test
    public void testSwitchIsExhaustive(){
        compareWithJavaSource("swtch/SwitchIsExhaustive");
    }

    @Test
    public void testSwitchIsEnumeratedPrimitives(){
        compareWithJavaSource("swtch/SwitchIsEnumeratedPrimitives");
    }
    
    @Test
    public void testSwitchIsNonExhaustive(){
        compareWithJavaSource("swtch/SwitchIsNonExhaustive");
    }
    
    @Test
    public void testSwitchIsVarSubst(){
        compareWithJavaSource("swtch/SwitchIsVarSubst");
    }
    
    @Test
    public void testSwitchMatch(){
        compareWithJavaSource("swtch/SwitchMatch");
    }
    
    @Test
    public void testSwitchValues(){
        compareWithJavaSource("swtch/SwitchValues");
    }
    
    
    @Test
    public void testReturnAnonFunction(){
        compareWithJavaSource("retrn/ReturnAnonFunction");
    }

    @Test
    public void testAssertSimple() {
        compareWithJavaSource("conditional/AssertionSimple");
    }

    @Test
    public void testAssertSpecial() {
        compareWithJavaSource("conditional/AssertionSpecial");
    }
    
    @Test
    public void testAssertConditionList() {
        compareWithJavaSource("conditional/AssertionConditionList");
    }
    
    @Test
    public void testConAssertExists(){
        compareWithJavaSource("conditional/AssertExists");
    }
    
    @Test
    public void testConAssertExistsSequence(){
        compareWithJavaSource("conditional/AssertExistsSequence");
    }
    
    @Test
    public void testConAssertExistsWithMethod(){
        compareWithJavaSource("conditional/AssertExistsWithMethod");
    }
    
    @Test
    public void testConAssertExists2dArray(){
        compareWithJavaSource("conditional/AssertExists2dArray");
    }

    @Test
    public void testConAssertIsFoo(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/AssertIsFoo");
    }
    
    @Test
    public void testConAssertIsNotNull(){
        compareWithJavaSource("conditional/AssertIsNotNull");
    }
    
    @Test
    public void testConAssertIsNotObject(){
        compareWithJavaSource("conditional/AssertIsNotObject");
    }
    
    @Test
    public void testConAssertIsNullUnion(){
        compareWithJavaSource("conditional/AssertIsNullUnion");
    }
    
    @Test
    public void testConAssertIsWithIntersection(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/AssertIsWithIntersection");
    }
    
    @Test
    public void testConAssertIsWithMethod(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/AssertIsWithMethod");
    }
    

    @Test
    public void testConAssertIsWithUnion(){
        compile("conditional/FooBar.ceylon");
        compareWithJavaSource("conditional/AssertIsWithUnion");
    }
    
    @Test
    public void testConAssertIsNull2(){
        compareWithJavaSource("conditional/AssertIsNull2");
    }
    
    @Test
    public void testConAssertIsNull(){
        compareWithJavaSource("conditional/AssertIsNull");
    }

    @Test
    @Ignore("M5: requires reified generics")
    public void testConAssertIsGeneric(){
        compareWithJavaSource("conditional/AssertIsGeneric");
    }

    @Test
    @Ignore("M5: requires support from spec for satisfies conditions (reified generics)")
    public void testConAssertSatisfies(){
        compareWithJavaSource("conditional/AssertSatisfies");
    }

    @Test
    @Ignore("M5: requires support from spec for satisfies conditions (reified generics)")
    public void testConAssertSatisfiesMultiple(){
        compareWithJavaSource("conditional/AssertSatisfiesMultiple");
    }

    @Test
    public void testConAssertNonEmptySequence(){
        compareWithJavaSource("conditional/AssertNonEmptySequence");
    }
    
    @Test
    public void testConAssertConditionListBoolBool(){
        compareWithJavaSource("conditional/AssertConditionListBoolBool");
    }
    @Test
    public void testConAssertConditionListIsIs(){
        compareWithJavaSource("conditional/AssertConditionListIsIs");
    }
    @Test
    public void testConAssertConditionListBoolBoolIs(){
        compareWithJavaSource("conditional/AssertConditionListBoolBoolIs");
    }
    @Test
    public void testConAssertConditionListBoolIsBool(){
        compareWithJavaSource("conditional/AssertConditionListBoolIsBool");
    }
    @Test
    public void testConAssertConditionListExistsIsBool(){
        compareWithJavaSource("conditional/AssertConditionListExistsIsBool");
    }
    @Test
    public void testConAssertConditionListIsBool(){
        compareWithJavaSource("conditional/AssertConditionListIsBool");
    }
    @Test
    public void testConAssertConditionListIsBoolBool(){
        compareWithJavaSource("conditional/AssertConditionListIsBoolBool");
    }
    @Test
    public void testConAssertConditionListNonemptyIsBool(){
        compareWithJavaSource("conditional/AssertConditionListNonemptyIsBool");
    }
    @Test
    public void testConAssertVariableScopes(){
        compareWithJavaSource("conditional/AssertVariableScopes");
    }
    @Test
    public void testConAssertFalse(){
        compareWithJavaSource("conditional/AssertFalse");
    }

    //
    // Dynamic blocks

    @Test
    public void testDynBlock(){
        assertErrors("dynamic/Dynamic",
                new CompilerError(22, "Dynamic not yet supported on the JVM"),
                new CompilerError(26, "value type could not be inferred"));
    }
}

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
    public void testConMethodIfExists(){
        compareWithJavaSource("conditional/MethodIfExists");
    }

    @Test
    public void testConMethodIfIs(){
        compareWithJavaSource("conditional/MethodIfIs");
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
    public void testConMethodIfNonEmpty(){
        compareWithJavaSource("conditional/MethodIfNonEmpty");
    }
    
    @Test
    public void testConMethodIfConditionList(){
        compareWithJavaSource("conditional/MethodIfConditionList");
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
    
    //
    // [do] while
    
    @Test
    public void testLopMethodWhile(){
        compareWithJavaSource("loop/MethodWhile");
    }
    
    @Test
    public void testLopMethodWhileConditionList(){
        compareWithJavaSource("loop/MethodWhileConditionList");
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
    public void testTryReplaceExceptionAtJavaCallSite(){
        compareWithJavaSource("trycatch/WrapExceptionAtJavaCallSite");
    }
    
    @Test
    public void testSwitchIsExhaustive(){
        compareWithJavaSource("swtch/SwitchIsExhaustive");
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

}

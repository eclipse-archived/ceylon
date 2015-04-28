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
package com.redhat.ceylon.compiler.java.test.nativecode;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class NativeTests extends CompilerTests {
	
    private void testNative(String test) {
        try {
            compileAndRun("com.redhat.ceylon.compiler.java.test.nativecode.test" + test, test + ".ceylon");
        } catch (RuntimeException ex) {
            assert((test + "-JVM").equals(ex.getMessage()));
        }
    }
    
    private void testNativeErrors(String test, CompilerError... expectedErrors) {
        assertErrors(test, expectedErrors);
    }
    
    // Methods
    
    @Test
    public void testNativeMethodPrivate() {
        testNative("NativeMethodPrivate");
    }
    
    @Test
    public void testNativeMethodPrivateAbstraction() {
        testNative("NativeMethodPrivateAbstraction");
    }
    
    @Test
    public void testNativeMethodShared() {
        testNative("NativeMethodShared");
    }
    
    @Test
    public void testNativeMethodSharedInvalid() {
        testNativeErrors("NativeMethodSharedInvalid",
                new CompilerError(20, "native implementation should have an abstraction or not be shared"),
                new CompilerError(23, "native implementation should have an abstraction or not be shared"));
    }
    
    @Test
    public void testNativeMethodMismatch() {
        testNativeErrors("NativeMethodMismatch",
                new CompilerError(25, "native declarations have different annotations: 'nativeMethodMismatch1'"),
                new CompilerError(30, "native methods do not have the same return type: 'nativeMethodMismatch2'"),
                new CompilerError(34, "native methods do not have the same return type: 'nativeMethodMismatch2'"),
                new CompilerError(40, "native declarations do not have the same number of parameters nativeMethodMismatch3' declared by 'com.redhat.ceylon.compiler.java.test.nativecode'"),
                new CompilerError(44, "type of parameter 's' of 'nativeMethodMismatch3' declared by 'com.redhat.ceylon.compiler.java.test.nativecode' is different to type of corresponding parameter 'i' of native abstraction 'nativeMethodMismatch3' of 'com.redhat.ceylon.compiler.java.test.nativecode': 'String' is not exactly 'Integer'"));
    }
    
    // Attributes
    
    @Test
    public void testNativeAttributePrivate() {
        testNative("NativeAttributePrivate");
    }
    
    @Test
    public void testNativeAttributePrivateAbstraction() {
        testNative("NativeAttributePrivateAbstraction");
    }
    
    @Test
    public void testNativeAttributeShared() {
        testNative("NativeAttributeShared");
    }
    
    @Test
    public void testNativeAttributeSharedInvalid() {
        testNativeErrors("NativeAttributeSharedInvalid",
                new CompilerError(20, "native implementation should have an abstraction or not be shared"),
                new CompilerError(22, "native implementation should have an abstraction or not be shared"));
    }
    
    @Test
    public void testNativeAttributeMismatch() {
        testNativeErrors("NativeAttributeMismatch",
                new CompilerError(24, "native declarations have different annotations: 'nativeAttributeMismatch1'"),
                new CompilerError(28, "native attributes do not have the same type: 'nativeAttributeMismatch2'"));
    }
    
    // Classes
    
    @Test
    public void testNativeClassPrivate() {
        testNative("NativeClassPrivate");
    }
    
    @Test
    public void testNativeClassPrivateAbstraction() {
        testNative("NativeClassPrivateAbstraction");
    }
    
    @Test
    public void testNativeClassShared() {
        testNative("NativeClassShared");
    }
    
    @Test
    public void testNativeClassExtends() {
        testNative("NativeClassExtends");
    }
    
    @Test
    public void testNativeClassSatisfies() {
        testNative("NativeClassSatisfies");
    }
    
    @Test
    public void testNativeClassSharedInvalid() {
        testNativeErrors("NativeClassSharedInvalid",
                new CompilerError(20, "native implementation should have an abstraction or not be shared"),
                new CompilerError(22, "native implementation should have an abstraction or not be shared"));
    }
    
    @Test
    public void testNativeClassMismatch() {
        testNativeErrors("NativeClassMismatch",
                new CompilerError(35, "native declarations have different annotations: 'NativeClassMismatch1'"),
                new CompilerError(40, "native declarations do not have the same number of parameters NativeClassMismatch2' declared by 'com.redhat.ceylon.compiler.java.test.nativecode'"),
                new CompilerError(42, "type of parameter 's' of 'NativeClassMismatch2' declared by 'com.redhat.ceylon.compiler.java.test.nativecode' is different to type of corresponding parameter 'i' of native abstraction 'NativeClassMismatch2' of 'com.redhat.ceylon.compiler.java.test.nativecode': 'String' is not exactly 'Integer'"),
                new CompilerError(57, "native classes do not satisfy the same interfaces: 'NativeClassMismatch4'"),
                new CompilerError(75, "formal member 'test2' of 'NativeClassMismatchSuper2' not implemented in class hierarchy"),
                new CompilerError(75, "native classes do not satisfy the same interfaces: 'NativeClassMismatch5'")
        );
    }
    
    @Test
    public void testNativeInvalidTypes() {
        testNativeErrors("NativeInvalidTypes",
                new CompilerError(22, "native declarations not of same type: 'nativeInvalidTypes'"),
                new CompilerError(24, "native declarations not of same type: 'nativeInvalidTypes'"));
    }
}

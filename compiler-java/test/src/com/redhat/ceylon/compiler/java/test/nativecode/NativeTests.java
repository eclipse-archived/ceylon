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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class NativeTests extends CompilerTests {
	
    private void testNative(String test) {
        testNative("simple", test);
    }
    
    private void testNative(String dir, String test) {
        boolean ok = false;
        try {
            compileAndRun("com.redhat.ceylon.compiler.java.test.nativecode." + dir + ".test" + test, dir + "/" + test + ".ceylon");
        } catch (RuntimeException ex) {
            assert(("ceylon.language.Exception \"" + test + "-JVM\"").equals(ex.getMessage()));
            ok = true;
        }
        if (!ok) {
            Assert.fail("Test terminated incorrectly");
        }
    }
    
    private void testNativeErrors(String test, CompilerError... expectedErrors) {
        testNativeErrors("simple", test, expectedErrors);        
    }
    
    private void testNativeErrors(String dir, String test, CompilerError... expectedErrors) {
        assertErrors(dir + "/" + test, expectedErrors);
    }
    
    private void testNativeModule(String dir) {
        testNativeModule(dir, "test.ceylon", "package.ceylon", "module.ceylon");
    }
    
    private void testNativeModule(String dir, String... files) {
        boolean ok = false;
        try {
            String[] paths = new String[files.length];
            for (int i=0; i < files.length; i++) {
                paths[i] = dir + "/" + files[i];
            }
            compile(paths);
            run("com.redhat.ceylon.compiler.java.test.nativecode." + dir + ".test", new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.nativecode." + dir, "1.0"));
        } catch (RuntimeException ex) {
            assert(("ceylon.language.Exception \"" + dir + "-JVM\"").equals(ex.getMessage()));
            ok = true;
        }
        if (!ok) {
            Assert.fail("Test terminated incorrectly");
        }
    }
    
    private void testNativeModuleErrors(String dir, CompilerError... expectedErrors) {
        assertErrors(new String[] {dir + "/test.ceylon", dir + "/module.ceylon"}, defaultOptions, null, expectedErrors);
    }
    
    // Methods
    
    @Test @Ignore("While we can't have header-less natives")
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
    public void testNativeMethodHeaderImpl() {
        testNative("NativeMethodHeaderImpl");
        testNative("NativeMethodHeaderImpl");
    }
    
    @Test
    public void testNativeMethodSharedInvalid() {
        testNativeErrors("NativeMethodSharedInvalid",
                new CompilerError(20, "native implementation must have a header: 'nativeMethodSharedInvalid'"),
                new CompilerError(23, "native implementation must have a header: 'nativeMethodSharedInvalid'"));
    }
    
    @Test
    public void testNativeMethodMismatch() {
        testNativeErrors("NativeMethodMismatch",
                new CompilerError(25, "native header is not shared: 'nativeMethodMismatch1'"),
                new CompilerError(30, "native implementation must have the same return type as native header: 'nativeMethodMismatch2' must have the type 'Anything'"),
                new CompilerError(34, "native implementation must have the same return type as native header: 'nativeMethodMismatch2' must have the type 'Anything'"),
                new CompilerError(40, "member does not have the same number of parameters as native header: 'nativeMethodMismatch3'"),
                new CompilerError(44, "type of parameter 's' of 'nativeMethodMismatch3' is different to type of corresponding parameter 'i' of native header 'nativeMethodMismatch3': 'String' is not exactly 'Integer'"),
                new CompilerError(51, "no native implementation for backend: native 'nativeMethodMismatch4js' is not implemented for one or more backends"),
                new CompilerError(54, "no native implementation for backend: native 'nativeMethodMismatch4js' is not implemented for one or more backends"),
                new CompilerError(57, "no native implementation for backend: native 'nativeMethodMismatch4jvm' is not implemented for one or more backends"),
                new CompilerError(62, "no native implementation for backend: native 'nativeMethodMismatch4js' is not implemented for one or more backends")
        );
    }
    
    // Attributes
    
    @Test @Ignore("While we can't have header-less natives")
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
    public void testNativeSetterShared() {
        testNative("NativeSetterShared");
    }
    
    @Test
    public void testNativeVariableSetter() {
        testNative("NativeVariableSetter");
    }
    
    @Test
    public void testNativeSetterVariable() {
        testNative("NativeSetterVariable");
    }
    
    @Test
    public void testNativeAttributeHeaderImpl() {
        testNative("NativeAttributeHeaderImpl");
        testNative("NativeAttributeHeaderImpl");
    }
    
    @Test
    public void testNativeSetterInvalid() {
        testNativeErrors("NativeSetterInvalid",
                new CompilerError(21, "setter must be marked native: 'nativeSetterInvalid'"),
                new CompilerError(24, "setter must be marked native: 'nativeSetterInvalid'"),
                new CompilerError(27, "setter must be marked native: 'nativeSetterInvalid'")
        );
    }
    
    @Test
    public void testNativeAttributeSharedInvalid() {
        testNativeErrors("NativeAttributeSharedInvalid",
                new CompilerError(20, "native implementation must have a header: 'nativeAttributeSharedInvalid'"),
                new CompilerError(22, "native implementation must have a header: 'nativeAttributeSharedInvalid'"));
    }
    
    @Test
    public void testNativeAttributeMismatch() {
        testNativeErrors("NativeAttributeMismatch",
                new CompilerError(24, "native header is not shared: 'nativeAttributeMismatch1'"),
                new CompilerError(28, "native implementation must have the same type as native header: 'nativeAttributeMismatch2' must have the type 'Integer'"));
    }
    
    // Classes
    
    @Test @Ignore("While we can't have header-less natives")
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
    public void testNativeClassWithImpl() {
        testNative("NativeClassWithImpl");
    }
    
    @Test
    public void testNativeClassSharedInvalid() {
        testNativeErrors("NativeClassSharedInvalid",
                new CompilerError(20, "native implementation must have a header: 'NativeClassSharedInvalid'"),
                new CompilerError(22, "native implementation must have a header: 'NativeClassSharedInvalid'"));
    }
    
    @Test
    public void testNativeClassMismatch() {
        testNativeErrors("NativeClassMismatch",
                new CompilerError(35, "native header is not shared: 'NativeClassMismatch1'"),
                new CompilerError(40, "member does not have the same number of parameters as native header: 'NativeClassMismatch2'"),
                new CompilerError(42, "type of parameter 's' of 'NativeClassMismatch2' is different to type of corresponding parameter 'i' of native header 'NativeClassMismatch2': 'String' is not exactly 'Integer'"),
                new CompilerError(57, "native classes do not satisfy the same interfaces: 'NativeClassMismatch4'"),
                new CompilerError(75, "native classes do not satisfy the same interfaces: 'NativeClassMismatch5'"),
                new CompilerError(82, "native header for non-native declaration: 'NativeClassMismatch6'"),
                new CompilerError(82, "no native implementation for backend: native 'NativeClassMismatch6' is not implemented for one or more backends"),
                new CompilerError(84, "native implementation for non-native header: 'NativeClassMismatch6'"),
                new CompilerError(91, "formal member 'test1' of 'NativeClassMismatchSuper1' not implemented in class hierarchy"),
                new CompilerError(92, "native backend name on member conflicts with its container: 'test1' of 'NativeClassMismatch7'"),
                new CompilerError(102, "no native implementation for backend: native 'NativeClassMismatch8js' is not implemented for one or more backends"),
                new CompilerError(102, "no native implementation for backend: native 'test2' is not implemented for one or more backends"),
                new CompilerError(106, "no native implementation for backend: native 'NativeClassMismatch8js' is not implemented for one or more backends"),
                new CompilerError(112, "no native implementation for backend: native 'NativeClassMismatch8jvm' is not implemented for one or more backends"),
                new CompilerError(112, "no native implementation for backend: native 'test1' is not implemented for one or more backends"),
                new CompilerError(124, "native header 'test5' of 'NativeClassMismatch9' has no native implementation"),
                new CompilerError(126, "type of parameter 's' of 'test2' is different to type of corresponding parameter 'i' of native header 'test2': 'String' is not exactly 'Integer'"),
                new CompilerError(127, "native implementation must have the same return type as native header: 'test3' in 'NativeClassMismatch9' must have the type 'Anything'"),
                new CompilerError(128, "member does not have the same number of parameters as native header: 'test4'"),
                new CompilerError(129, "native member does not implement any header member: 'testX' in 'NativeClassMismatch9'"),
                new CompilerError(130, "non-native shared members not allowed in native implementations: 'testY' in 'NativeClassMismatch9'"),
                new CompilerError(133, "native header 'test5' of 'NativeClassMismatch9' has no native implementation"),
                new CompilerError(135, "type of parameter 's' of 'test2' is different to type of corresponding parameter 'i' of native header 'test2': 'String' is not exactly 'Integer'"),
                new CompilerError(136, "native implementation must have the same return type as native header: 'test3' in 'NativeClassMismatch9' must have the type 'Anything'"),
                new CompilerError(137, "member does not have the same number of parameters as native header: 'test4'"),
                new CompilerError(138, "native member does not implement any header member: 'testX' in 'NativeClassMismatch9'"),
                new CompilerError(139, "non-native shared members not allowed in native implementations: 'testY' in 'NativeClassMismatch9'"),
                new CompilerError(144, "no native implementation for backend: native 'NativeClassMismatch8js' is not implemented for one or more backends"),
                new CompilerError(144, "no native implementation for backend: native 'test2' is not implemented for one or more backends")
        );
    }
    
    // Modules
    
    @Test
    public void testNativeModule() {
        testNativeModule("modok");
    }
    
    @Test
    public void testNativeModuleImport() {
        testNativeModule("modsample");
        testNativeModule("modimport");
    }
    
    @Test
    public void testNativeModuleMissing() {
        testNativeModuleErrors("modmissing",
                new CompilerError(21, "missing backend argument for native annotation on module: com.redhat.ceylon.compiler.java.test.nativecode.modmissing")
        );
    }
    
    @Test
    public void testNativeModuleWrong() {
        testNativeModuleErrors("modwrong",
                new CompilerError(21, "module not meant for this backend: com.redhat.ceylon.compiler.java.test.nativecode.modwrong")
        );
    }
    
    @Test
    public void testNativeOtherRef() {
        testNativeModule("otherref");
    }
    
    @Test @Ignore("see https://github.com/ceylon/ceylon-compiler/issues/2196")
    public void testNativeWithJava() {
        testNativeModule("withjava", "NativeClass.java", "test.ceylon", "module.ceylon");
    }
    
    @Test
    public void testNativeConflict() {
        testNativeModuleErrors("modconflict",
                new CompilerError(20, "no native implementation for backend: native 'conflicting' is not implemented for one or more backends"),
                new CompilerError(22, "native backend name on declaration conflicts with module descriptor: '\"js\"' is not '\"jvm\"' for 'conflicting'")
        );
    }
    
    @Test @Ignore("We do away with incremental compilation for now")
    public void testNativeIncremental() {
        compile("modincremental/test.ceylon", "modincremental/testheader.ceylon", "modincremental/module.ceylon");
        testNativeModule("modincremental");
    }
    
    // Misc
    
    @Test
    public void testNativeInvalidTypes() {
        testNativeErrors("NativeInvalidTypes",
                new CompilerError(22, "native declarations not of same type: 'nativeInvalidTypes'"),
                new CompilerError(24, "native declarations not of same type: 'nativeInvalidTypes'"),
                new CompilerError(27, "illegal native backend name: '\"foo\"' (must be either '\"jvm\"' or '\"js\"')"),
                new CompilerError(29, "native declaration is not a class, method or attribute: 'NativeInvalidInterface1'"),
                new CompilerError(31, "native declaration is not a class, method or attribute: 'NativeInvalidInterface2'"),
                new CompilerError(33, "native declaration is not a class, method or attribute: 'nativeInvalidObject'")
        );
    }
    
    @Test
    public void testNativeNonNativeMixed() {
        testNativeErrors("NativeNonNativeMixed",
                new CompilerError(21, "native implementation for non-native header: 'NativeNonNativeMixed1'"),
                new CompilerError(22, "native implementation for non-native header: 'NativeNonNativeMixed1'"),
                new CompilerError(25, "native header for non-native declaration: 'nativeNonNativeMixed2'"),
                new CompilerError(25, "no native implementation for backend: native 'nativeNonNativeMixed2' is not implemented for one or more backends"),
                new CompilerError(26, "native implementation for non-native header: 'nativeNonNativeMixed2'"),
                new CompilerError(27, "native implementation for non-native header: 'nativeNonNativeMixed2'"),
                new CompilerError(30, "duplicate declaration name: 'nativeNonNativeMixed3'")
        );
    }
    
    @Test
    public void testNativeDuplicates() {
        testNativeErrors("NativeDuplicates",
                new CompilerError(20, "no native implementation for backend: native 'nativeDuplicates1' is not implemented for one or more backends"),
                new CompilerError(22, "duplicate native header: 'nativeDuplicates1'"),
                new CompilerError(22, "no native implementation for backend: native 'nativeDuplicates1' is not implemented for one or more backends"),
                new CompilerError(28, "duplicate native implementation: 'nativeDuplicates2'"),
                new CompilerError(30, "no native implementation for backend: native 'nativeDuplicates3' is not implemented for one or more backends"),
                new CompilerError(34, "duplicate native implementation: 'nativeDuplicates3'"),
                new CompilerError(40, "duplicate native implementation: 'nativeDuplicates4'"),
                new CompilerError(42, "no native implementation for backend: native 'nativeDuplicates5' is not implemented for one or more backends"),
                new CompilerError(46, "duplicate native implementation: 'nativeDuplicates5'")
        );
    }
    
    @Test
    public void testNativeMissing() {
        testNativeErrors("NativeMissing",
                new CompilerError(20, "no native implementation for backend: native 'nativeMissingMethod' is not implemented for one or more backends"),
                new CompilerError(21, "no native implementation for backend: native 'NativeMissingClass' is not implemented for one or more backends"),
                new CompilerError(27, "no native implementation for backend: native 'nativeMissingMethod' is not implemented for one or more backends"),
                new CompilerError(28, "no native implementation for backend: native 'nativeMissingMethod2' is not implemented for one or more backends"),
                new CompilerError(29, "no native implementation for backend: native 'NativeMissingClass' is not implemented for one or more backends"),
                new CompilerError(32, "no native implementation for backend: native 'NativeMissingClass' is not implemented for one or more backends")
        );
    }
}

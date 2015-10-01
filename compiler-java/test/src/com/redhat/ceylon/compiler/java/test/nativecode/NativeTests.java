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
    
    private ModuleWithArtifact testNativeModule(String dir) {
        return testNativeModule(dir, null, null, "test.ceylon", "package.ceylon", "module.ceylon");
    }
    
    private ModuleWithArtifact testNativeModule(String dir, ModuleWithArtifact extraModule) {
        return testNativeModule(dir, extraModule, null, "test.ceylon", "package.ceylon", "module.ceylon");
    }
    
    private ModuleWithArtifact testNativeModule(String dir, ModuleWithArtifact extraModule1, ModuleWithArtifact extraModule2) {
        return testNativeModule(dir, extraModule1, extraModule2, "test.ceylon", "package.ceylon", "module.ceylon");
    }
    
    private ModuleWithArtifact testNativeModule(String dir, ModuleWithArtifact extraModule1, ModuleWithArtifact extraModule2, String... files) {
        ModuleWithArtifact mainModule = null;
        boolean ok = false;
        try {
            String[] paths = new String[files.length];
            for (int i=0; i < files.length; i++) {
                paths[i] = dir + "/" + files[i];
            }
            compile(paths);
            String main = "com.redhat.ceylon.compiler.java.test.nativecode." + dir + ".test";
            mainModule = new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.nativecode." + dir, "1.0");
            if (extraModule1 != null) {
                if (extraModule2 != null) {
                    run(main, mainModule, extraModule1, extraModule2);
                } else {
                    run(main, mainModule, extraModule1);
                }
            } else {
                run(main, mainModule);
            }
        } catch (RuntimeException ex) {
            assert(("ceylon.language.Exception \"" + dir + "-JVM\"").equals(ex.getMessage()));
            ok = true;
        }
        if (!ok) {
            Assert.fail("Test terminated incorrectly");
        }
        return mainModule;
    }
    
    private void testNativeModuleErrors(String dir, CompilerError... expectedErrors) {
        assertErrors(new String[] {dir + "/test.ceylon", dir + "/module.ceylon"}, defaultOptions, null, expectedErrors);
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
    public void testNativeMethodHeaderImpl() {
        testNative("NativeMethodHeaderImpl");
        testNative("NativeMethodHeaderImpl");
    }
    
    @Test
    public void testNativeMethodLocal() {
        testNative("NativeMethodLocal");
    }
    
    @Test
    public void testNativeMethodSharedInvalid() {
        testNativeErrors("NativeMethodSharedInvalid",
                new CompilerError(20, "shared native implementation must have a header: 'nativeMethodSharedInvalid'"),
                new CompilerError(23, "shared native implementation must have a header: 'nativeMethodSharedInvalid'"));
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
                new CompilerError(72, "native header does not have the same number of type parameters as native implementation: 'nativeMethodMismatch5'"),
                new CompilerError(75, "native header does not have the same number of type parameters as native implementation: 'nativeMethodMismatch5'"),
                new CompilerError(80, "type parameter does not have the same bounds as its header: 'T' for 'nativeMethodMismatch6'"),
                new CompilerError(83, "type parameter does not have the same bounds as its header: 'T' for 'nativeMethodMismatch6'")
        );
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
    public void testNativeAttributeLocal() {
        testNative("NativeAttributeLocal");
    }
    
    @Test
    public void testNativeSetterInvalid() {
        testNativeErrors("NativeSetterInvalid",
                new CompilerError(21, "setter must be marked native: 'nativeSetterInvalid'"),
                new CompilerError(24, "setter must be marked native: 'nativeSetterInvalid'"),
                new CompilerError(27, "setter must be marked native: 'nativeSetterInvalid'"),
                new CompilerError(30, "native setter for non-native getter: 'nativeSetterInvalid2'")
        );
    }
    
    @Test
    public void testNativeAttributeSharedInvalid() {
        testNativeErrors("NativeAttributeSharedInvalid",
                new CompilerError(20, "shared native implementation must have a header: 'nativeAttributeSharedInvalid'"),
                new CompilerError(22, "shared native implementation must have a header: 'nativeAttributeSharedInvalid'"));
    }
    
    @Test
    public void testNativeAttributeMismatch() {
        testNativeErrors("NativeAttributeMismatch",
                new CompilerError(24, "native header is not shared: 'nativeAttributeMismatch1'"),
                new CompilerError(28, "native implementation must have the same type as native header: 'nativeAttributeMismatch2' must have the type 'Integer'"));
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
    public void testNativeClassWithImpl() {
        testNative("NativeClassWithImpl");
    }
    
    @Test
    public void testNativeClassSharedMembers() {
        testNative("NativeClassSharedMembers");
    }
    
    @Test
    public void testNativeClassMembersWithImpl() {
        testNative("ClassNativeMembersWithImpl");
    }
    
    @Test
    public void testClassNativeMembers() {
        testNative("ClassNativeMembers");
    }
    
    @Test
    public void testNativeClassLocal() {
        testNative("NativeClassLocal");
    }
    
    @Test
    public void testNativeClassWithConstructors() {
        testNative("NativeClassWithConstructors");
    }
    
    @Test
    public void testNativeClassSharedInvalid() {
        testNativeErrors("NativeClassSharedInvalid",
                new CompilerError(20, "shared native implementation must have a header: 'NativeClassSharedInvalid'"),
                new CompilerError(22, "shared native implementation must have a header: 'NativeClassSharedInvalid'"));
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
                new CompilerError(92, "native backend name on declaration conflicts with its scope: 'test1'"),
                new CompilerError(102, "no native implementation for backend: native 'NativeClassMismatch8js' is not implemented for one or more backends"),
                new CompilerError(102, "no native implementation for backend: native 'test2' is not implemented for one or more backends"),
                new CompilerError(106, "no native implementation for backend: native 'NativeClassMismatch8js' is not implemented for one or more backends"),
                new CompilerError(112, "no native implementation for backend: native 'NativeClassMismatch8jvm' is not implemented for one or more backends"),
                new CompilerError(112, "no native implementation for backend: native 'test1' is not implemented for one or more backends"),
                new CompilerError(134, "native header 'test5' of 'NativeClassMismatch9' has no native implementation"),
                new CompilerError(136, "type of parameter 's' of 'test2' is different to type of corresponding parameter 'i' of native header 'test2': 'String' is not exactly 'Integer'"),
                new CompilerError(137, "native implementation must have the same return type as native header: 'test3' in 'NativeClassMismatch9' must have the type 'Anything'"),
                new CompilerError(138, "member does not have the same number of parameters as native header: 'test4'"),
                new CompilerError(139, "native member does not implement any header member: 'testX' in 'NativeClassMismatch9'"),
                new CompilerError(140, "non-native shared members not allowed in native implementations: 'testY' in 'NativeClassMismatch9'"),
                new CompilerError(143, "native header 'test5' of 'NativeClassMismatch9' has no native implementation"),
                new CompilerError(145, "type of parameter 's' of 'test2' is different to type of corresponding parameter 'i' of native header 'test2': 'String' is not exactly 'Integer'"),
                new CompilerError(146, "native implementation must have the same return type as native header: 'test3' in 'NativeClassMismatch9' must have the type 'Anything'"),
                new CompilerError(147, "member does not have the same number of parameters as native header: 'test4'"),
                new CompilerError(148, "native member does not implement any header member: 'testX' in 'NativeClassMismatch9'"),
                new CompilerError(149, "non-native shared members not allowed in native implementations: 'testY' in 'NativeClassMismatch9'"),
                new CompilerError(160, "native header 'privattr' of 'NativeClassMismatch10' has no native implementation"),
                new CompilerError(160, "native header 'privmeth' of 'NativeClassMismatch10' has no native implementation"),
                new CompilerError(167, "native header 'privattr' of 'NativeClassMismatch10' has no native implementation"),
                new CompilerError(167, "native header 'privmeth' of 'NativeClassMismatch10' has no native implementation"),
                new CompilerError(177, "native header does not have the same number of type parameters as native implementation: 'NativeClassMismatch11'"),
                new CompilerError(179, "native header does not have the same number of type parameters as native implementation: 'NativeClassMismatch11'"),
                new CompilerError(184, "type parameter does not have the same bounds as its header: 'B' for 'NativeClassMismatch12'"),
                new CompilerError(184, "type parameter does not have the same name as its header: 'B' for 'NativeClassMismatch12'"),
                new CompilerError(186, "type parameter does not have the same bounds as its header: 'A' for 'NativeClassMismatch12'"),
                new CompilerError(195, "native header 'test3' of 'NativeClassMismatch13' has no native implementation"),
                new CompilerError(196, "member implementing a native header member must be marked native: 'test1' in 'NativeClassMismatch13'"),
                new CompilerError(197, "member implementing a native header member must be marked native: 'test2' in 'NativeClassMismatch13'"),
                new CompilerError(198, "member implementing a native header member must be marked native: 'test3' in 'NativeClassMismatch13'"),
                new CompilerError(201, "native header 'test3' of 'NativeClassMismatch13' has no native implementation"),
                new CompilerError(202, "member implementing a native header member must be marked native: 'test1' in 'NativeClassMismatch13'"),
                new CompilerError(203, "member implementing a native header member must be marked native: 'test2' in 'NativeClassMismatch13'"),
                new CompilerError(204, "member implementing a native header member must be marked native: 'test3' in 'NativeClassMismatch13'")
        );
    }
    
    // Objects
    
    @Test
    public void testNativeObjectPrivate() {
        testNative("NativeObjectPrivate");
    }
    
    @Test
    public void testNativeObjectShared() {
        testNative("NativeObjectShared");
    }
    
    @Test
    public void testNativeObjectWithImpl() {
        testNative("NativeObjectWithImpl");
    }
    
    @Test
    public void testObjectNativeMembers() {
        testNative("ObjectNativeMembers");
    }
    
    @Test
    public void testObjectNativeMembersWithImpl() {
        testNative("ObjectNativeMembersWithImpl");
    }
    
    @Test
    public void testNativeObjectLocal() {
        testNative("NativeObjectLocal");
    }
    
    // Modules
    
    @Test
    public void testNativeModule() {
        testNativeModule("modok");
    }
    
    @Test
    public void testNativeModuleImport() {
        ModuleWithArtifact sampleMod = testNativeModule("modsample");
        testNativeModule("modimport", sampleMod);
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
        testNativeModule("withjava", null, null, "NativeClass.java", "test.ceylon", "module.ceylon");
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
                new CompilerError(29, "native declaration is not a class, constructor, method, attribute or object: 'NativeInvalidInterface1'"),
                new CompilerError(31, "native declaration is not a class, constructor, method, attribute or object: 'NativeInvalidInterface2'")
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
    
    @Test
    public void testNativeDelegate() {
        testNative("NativeDelegate");
    }
    
    @Test
    public void testBugSpec1372() {
        testNative("BugSpec1372");
    }
    
    @Test
    public void testNativeRefOk() {
        ModuleWithArtifact sampleMod = testNativeModule("modsample");
        ModuleWithArtifact okMod = testNativeModule("modok");
        testNativeModule("nativerefok", sampleMod, okMod);
    }
    
    @Test
    public void testNativeRefWrong() {
        assertErrors(new String[] {"nativerefwrong/test.ceylon", "nativerefwrong/module.ceylon", "modsample/test.ceylon", "modsample/package.ceylon", "modsample/module.ceylon", "modok/test.ceylon", "modok/package.ceylon", "modok/module.ceylon"}, defaultOptions, null,
                new CompilerError(30, "non-native declaration: 'x' accesses native code: 'nativeMethodJvm', mark it or the module native"),
                new CompilerError(32, "non-native declaration: 'x' accesses native code: 'nativeMethodJvm', mark it or the module native"),
                new CompilerError(37, "non-native declaration: 'x' accesses native code: 'nativeMethodJs', mark it or the module native"),
                new CompilerError(39, "non-native declaration: 'x' accesses native code: 'nativeMethodJs', mark it or the module native"),
                new CompilerError(53, "non-native declaration: 'x' accesses native code: 'foo', mark it or the module native"),
                new CompilerError(54, "non-native declaration: 'y' accesses native code: 'Bar', mark it or the module native"),
                new CompilerError(56, "non-native declaration: 'x' accesses native code: 'foo', mark it or the module native"),
                new CompilerError(57, "non-native declaration: 'y' accesses native code: 'Bar', mark it or the module native"),
                new CompilerError(63, "native declaration: 'x' accesses native code for different backend: 'nativeMethodJvm'"),
                new CompilerError(65, "native declaration: 'x' accesses native code for different backend: 'nativeMethodJvm'"),
                new CompilerError(71, "native declaration: 'x' accesses native code for different backend: 'nativeMethodJs'"),
                new CompilerError(73, "native declaration: 'x' accesses native code for different backend: 'nativeMethodJs'")
        );
    }
    
    @Test
    public void testNativeScopes() {
        testNative("NativeScopes");
    }
    
    @Test
    public void testNativeScopesWrong() {
        testNativeErrors("NativeScopesWrong",
                new CompilerError(27, "native backend name on declaration conflicts with its scope: 'test3'"),
                new CompilerError(32, "native backend name on declaration conflicts with its scope: 'test'"),
                new CompilerError(42, "native backend name on declaration conflicts with its scope: 'test3'"),
                new CompilerError(47, "native backend name on declaration conflicts with its scope: 'test'")
        );
    }
}

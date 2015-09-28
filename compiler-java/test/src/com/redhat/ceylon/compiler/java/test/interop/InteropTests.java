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
package com.redhat.ceylon.compiler.java.test.interop;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class InteropTests extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main) {
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.interop", "1");
    }
    
    @Test
    public void testIopArrays(){
        compile("TypesJava.java");
        compareWithJavaSource("Arrays");
    }
    
    @Test
    public void testIopArraysNoOpt(){
        compile("TypesJava.java");
        compareWithJavaSourceNoOpt("Arrays");
    }

    @Test
    public void testIopConstructors(){
        compareWithJavaSource("Constructors");
    }

	@Test
	public void testIopImport(){
		compareWithJavaSource("Import");
	}

    @Test
    public void testIopMethods(){
        compile("TypesJava.java", "JavaWithOverloadedMembers.java", "JavaWithOverloadedMembersSubClass.java");
        compareWithJavaSource("Methods");
    }

    @Test
    public void testIopAmbiguousOverloading(){
        compile("TypesJava.java", "JavaWithOverloadedMembers.java");
        assertErrors("AmbiguousOverloading",
                new CompilerError(25, "ambiguous invocation of overloaded method or class: there must be exactly one overloaded declaration of 'ambiguousOverload' that accepts the given argument types 'String, String'"),
                new CompilerError(26, "ambiguous invocation of overloaded method or class: there must be exactly one overloaded declaration of 'ambiguousOverload2' that accepts the given argument types 'Integer, Integer'")
                );
    }

    @Test
    public void testIopVariadicOverloadedMethods(){
        compile("JavaWithOverloadedMembers.java");
        compareWithJavaSource("VariadicOverloadedMethods");
    }

    @Test
    public void testIopVariadicArrays(){
        compile("TypesJava.java");
        compareWithJavaSource("VariadicArraysMethods");
    }

    @Test
    public void testIopImplementOverloadedConstructors(){
        compile("JavaWithOverloadedMembers.java");
        compareWithJavaSource("ImplementOverloadedConstructors");
    }

    @Ignore("M5")
    @Test
    public void testIopImplementOverloadedMethods(){
        compile("JavaWithOverloadedMembers.java", "JavaWithOverloadedMembersSubClass.java");
        compareWithJavaSource("ImplementOverloadedMethods");
    }

    @Test
    public void testIopImplementSingleOverloadedMethods(){
        compile("JavaWithOverloadedMembers.java");
        compareWithJavaSource("ImplementSingleOverloadedMethods");
    }

    @Test
    public void testIopFields(){
        compile("JavaFields.java");
        compareWithJavaSource("Fields");
    }

    @Test
    public void testIopSpecialFields(){
        compile("JavaFields.java");
        compareWithJavaSource("SpecialFields");
    }
    
    @Test
    public void testIopGetString(){
        compile("JavaGetString.java");
        compareWithJavaSource("GetString");
    }

    @Test
    public void testIopOverloadedSpecialFields(){
        compile("JavaOverloadedSpecialFields.java");
        compareWithJavaSource("OverloadedSpecialFields");
    }

    @Test
    public void testIopAttributes(){
        compile("JavaBean.java");
        compareWithJavaSource("Attributes");
    }

    @Test
    public void testIopSatisfies(){
        compile("JavaInterface.java");
        compareWithJavaSource("Satisfies");
    }

    @Test
    public void testIopOptionality(){
        compile("JavaOptionalInterface.java");
        compareWithJavaSource("Optionality");
    }

    @Test
    public void testIopStaticMembers(){
        compile("JavaWithStaticMembers.java", "JavaWithStaticMembersSubClass.java");
        compareWithJavaSource("StaticMembers");
    }
    
    @Test
    public void testIopStaticRefs(){
        compile("JavaWithStaticMembers.java", "JavaWithStaticMembersSubClass.java");
        compareWithJavaSource("StaticRefs");
    }

    @Test
    public void testIopTypes(){
        compile("TypesJava.java");
        compareWithJavaSource("Types");
    }

    @Test
    public void testIopEnums(){
        compile("JavaEnum.java");
        compareWithJavaSource("Enums");
    }
    
    @Test
    public void testIopEnumSwitch(){
        compile("JavaEnum.java");
        compareWithJavaSource("EnumSwitch");
    }

    @Test
    public void testIopNesting(){
        compile("JavaNesting.java");
        compareWithJavaSource("Nesting");
    }

    @Test
    public void testIopVariance(){
        compile("JavaVariance.java");
        compareWithJavaSource("Variance");
    }

    // depends on #612
    @Ignore("M6: depends on https://github.com/ceylon/ceylon-compiler/issues/612")
    @Test
    public void testIopVariance2_fail(){
        compile("JavaVariance.java");
        compareWithJavaSource("Variance2");
    }

    @Test
    public void testIopCaseMismatch(){
        compile("javaCaseMismatch.java");
        compareWithJavaSource("CaseMismatch");
    }

    @Test
    public void testIopCaseMismatch2(){
        compile("javaCaseMismatch.java");
        compareWithJavaSource("CaseMismatch2");
    }

    @Test
    public void testIopCeylonKeywords(){
        compile("satisfies.java");
        compareWithJavaSource("CeylonKeywords");
    }

    @Test
    public void testIopCheckedExceptions(){
        compile("JavaCheckedExceptions.java");
        compareWithJavaSource("CheckedExceptions");
    }

    @Test
    public void testIopExceptionsAndThrowable(){
        compile("JavaExceptionsAndThrowable.java");
        compareWithJavaSource("ExceptionsAndThrowable");
    }
    
    @Test
    public void testIopJavaExceptionMessage(){
        compile("JavaExceptionMessage.java");
        compareWithJavaSource("JavaExceptionMessage");
        
    }
    
    @Test
    public void testIopJavaExceptionMessage2(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.interop.javaExceptionMessage2",
                "JavaExceptionsAndThrowable.java", "JavaExceptionMessage2.ceylon");
    }
    
    @Test
    public void testIopJavaExceptionArrays(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.interop.javaExceptionArrays", "JavaExceptionArrays.ceylon", "JavaExceptionsAndThrowable.java");
    }

    @Test
    public void testIopRefinesProtectedAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        compareWithJavaSource("access/RefinesProtectedAccessMethod");
    }

    @Test
    public void testMixedCompilationIndependent(){
        compile("mixed/independent/Java.java", "mixed/independent/Ceylon.ceylon");
    }

    @Test
    public void testMixedCompilationCeylonNeedsJava(){
        compile("mixed/ceylon_needs_java/Java.java", "mixed/ceylon_needs_java/Ceylon.ceylon");
    }

    @Ignore("M5: #470")
    @Test
    public void testMixedCompilationJavaNeedsCeylon(){
        compile("mixed/java_needs_ceylon/Java.java", "mixed/java_needs_ceylon/Ceylon.ceylon");
    }

    @Ignore("M5: #470")
    @Test
    public void testMixedCompilationInterdependent(){
        compile("mixed/interdependent/Java.java", "mixed/interdependent/Ceylon.ceylon");
    }

    @Test
    public void testUsesJDKTypes(){
        compile("JDKTypes.ceylon");
    }

    @Test
    public void testIopCallsProtectedAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        compareWithJavaSource("access/CallsProtectedAccessMethod");
    }
    
    @Test
    public void testIopRefinesAndCallsProtectedAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        compareWithJavaSource("access/RefinesAndCallsProtectedAccessMethod");
    }
    
    @Test
    public void testIopRefinesDefaultAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        compile("access/RefinesDefaultAccessMethod.ceylon");
    }
    
    @Test
    public void testIopRefinesDefaultAccessMethodWithShared(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesDefaultAccessMethodWithShared",
                new CompilerError(22, "non-actual member refines an inherited member: 'defaultAccessMethod' in 'RefinesDefaultAccessMethodWithShared' refines 'defaultAccessMethod' in 'JavaAccessModifiers'"));
    }

    @Test
    public void testIopRefinesDefaultAccessMethodWithActual(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesDefaultAccessMethodWithActual",
                new CompilerError(22, "actual declaration must be shared: 'defaultAccessMethod'"));
    }
    
    @Test
    public void testIopRefinesDefaultAccessMethodWithSharedActual(){
        compile("access/JavaAccessModifiers.java");
        compareWithJavaSource("access/RefinesDefaultAccessMethodWithSharedActual");
    }
    
    @Test
    public void testIopCallsDefaultAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        compareWithJavaSource("access/CallsDefaultAccessMethod");
    }
    
    @Test
    public void testIopExtendsDefaultAccessClass(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        compareWithJavaSource("access/ExtendsDefaultAccessClass");
    }

    @Test
    public void testIopExtendsDefaultAccessClassWithOverloading(){
        compile("access/JavaDefaultAccessClass4.java");
        assertErrors("access/ExtendsDefaultAccessClassWithOverloading",
                new CompilerError(21, "ambiguous invocation of overloaded method or class: there must be exactly one overloaded declaration of 'JavaDefaultAccessClass4' that accepts the given argument types ''")
        );
    }

    @Test
    public void testIopExtendsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        assertErrors("ExtendsDefaultAccessClassInAnotherPkg",
                new CompilerError(21, "imported declaration is not shared: 'JavaDefaultAccessClass'"),
                new CompilerError(22, "imported declaration is not shared: 'JavaDefaultAccessClass2'"),
                new CompilerError(27, "supertype is not visible everywhere type 'ExtendsDefaultAccessClassInAnotherPkg' is visible: 'JavaDefaultAccessClass' involves an unshared type declaration"),
                new CompilerError(27, "type is not visible: 'JavaDefaultAccessClass'"),
                new CompilerError(29, "supertype is not visible everywhere type 'ExtendsDefaultAccessClassInAnotherPkg2' is visible: 'JavaDefaultAccessClass2' involves an unshared type declaration"),
                new CompilerError(29, "type is not visible: 'JavaDefaultAccessClass2'"),
                new CompilerError(31, "package private constructor is not visible: 'JavaDefaultAccessClass3'")
        );
    }

    @Test
    public void testIopCallsDefaultAccessClass(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        compareWithJavaSource("access/CallsDefaultAccessClass");
    }

    @Test
    public void testIopCallsDefaultAccessClassWithOverloading(){
        compile("access/JavaDefaultAccessClass4.java");
        assertErrors("access/CallsDefaultAccessClassWithOverloading",
                new CompilerError(22, "class cannot be instantiated: 'JavaDefaultAccessClass4' does not have a default constructor"),
                new CompilerError(22, "ambiguous invocation of overloaded method or class: there must be exactly one overloaded declaration of 'JavaDefaultAccessClass4' that accepts the given argument types ''")
        );
    }

    @Test
    public void testIopCallsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        assertErrors("CallsDefaultAccessClassInAnotherPkg",
                new CompilerError(21, "imported declaration is not shared: 'JavaDefaultAccessClass'"),
                new CompilerError(22, "imported declaration is not shared: 'JavaDefaultAccessClass2'"),
                new CompilerError(28, "type is not visible: 'JavaDefaultAccessClass'"),
                new CompilerError(29, "type is not visible: 'JavaDefaultAccessClass2'"),
                new CompilerError(30, "type constructor is not visible: 'JavaDefaultAccessClass3'")
        );
    }

    @Test
    public void testIopCallsDefaultAccessClassInAnotherPkgWithOverloading(){
        compile("access/JavaDefaultAccessClass4.java");
        assertErrors("CallsDefaultAccessClassInAnotherPkgWithOverloading",
                new CompilerError(26, "ambiguous invocation of overloaded method or class: there must be exactly one overloaded declaration of 'JavaDefaultAccessClass4' that accepts the given argument types ''"),
                new CompilerError(26, "class cannot be instantiated: 'JavaDefaultAccessClass4' does not have a default constructor"),
                new CompilerError(27, "type constructor is not visible: 'JavaDefaultAccessClass4'"),
                new CompilerError(28, "protected constructor is not visible: 'JavaDefaultAccessClass4'")
        );
    }

    @Test
    public void testIopCallsDefaultAccessMethodInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("CallsDefaultAccessMethodInAnotherPkg",
                new CompilerError(25, "protected method or attribute is not visible: 'protectedAccessMethod' of type 'JavaAccessModifiers'"),
                new CompilerError(27, "package private method or attribute is not visible: 'defaultAccessMethod' of type 'JavaAccessModifiers'"),
                new CompilerError(36, "package private function or value is not visible: 'defaultAccessMethod'"));
    }

    @Test
    public void testIopRefinesDefaultAccessMethodInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("RefinesDefaultAccessMethodInAnotherPkg",
                new CompilerError(27, "refined declaration is not visible: 'defaultAccessMethod' in 'RefinesDefaultAccessMethodInAnotherPkg' refines 'defaultAccessMethod' in 'JavaAccessModifiers'"));
    }

    @Test
    public void testIopNamedInvocations(){
        assertErrors("NamedInvocations",
                new CompilerError(30, "overloaded declarations may not be called using named arguments: 'createTempFile'"),
                new CompilerError(30, "ambiguous callable reference to overloaded method or class: 'createTempFile' is overloaded"),
                new CompilerError(30, "named invocations of Java methods not supported"),
                new CompilerError(32, "named invocations of Java methods not supported"),
                new CompilerError(35, "named invocations of Java methods not supported"),
                new CompilerError(35, "overloaded declarations may not be called using named arguments: 'createTempFile'"),
                new CompilerError(35, "ambiguous callable reference to overloaded method or class: 'createTempFile' is overloaded"),
                new CompilerError(37, "named invocations of Java methods not supported")
        );
    }

    @Test
    public void testIopOverrideStaticMethods(){
        compile("JavaWithStaticMembers.java");
        assertErrors("OverrideStaticMethods",
                new CompilerError(26, "member refines a non-default, non-formal member: 'topMethod' in 'StaticMethodsOverriding' refines 'topMethod' in 'JavaWithStaticMembers'"),
                new CompilerError(28, "member refines a non-default, non-formal member: 'topField' in 'StaticMethodsOverriding' refines 'topField' in 'JavaWithStaticMembers'")
        );
    }
    

    @Test
    public void testAnnotationInterop(){
        compile("JavaAnnotation.java");
        compareWithJavaSource("AnnotationInterop");
        assertErrors("AnnotationInteropErrors",
                new CompilerError(2, "function or value does not exist: 'javaAnnotationNoTarget__TYPE'"),
                new CompilerError(3, "function or value does not exist: 'javaAnnotationNoTarget__CONSTRUCTOR'"),
                new CompilerError(6, "function or value does not exist: 'javaAnnotationNoTarget__FIELD'"),
                new CompilerError(7, "function or value does not exist: 'javaAnnotationNoTarget__GETTER'"),
                new CompilerError(8, "function or value does not exist: 'javaAnnotationNoTarget__SETTER'"),
                new CompilerError(11, "annotated program element does not satisfy annotation constraint: 'FunctionDeclaration' is not assignable to 'Nothing'"),
                new CompilerError(11, "no target for javaAnnotationNoTarget annotation: @Target of @interface JavaAnnotationNoTarget lists [] but annotated element tranforms to [METHOD]"),
                new CompilerError(12, "function or value does not exist: 'javaAnnotationNoTarget__PARAMETER'"),
                new CompilerError(14, "function or value does not exist: 'javaAnnotationNoTarget__LOCAL_VARIABLE'"),
                new CompilerError(19, "function or value does not exist: 'javaAnnotationNoTarget__ANNOTATION_TYPE'"),
                new CompilerError(21, "illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference")
                );
    }
    
    @Test
    public void testBannedAnnotation(){
      assertErrors("BannedAnnotation",
              new CompilerError(13, "inappropiate java annotation: interoperation with @Target is not supported"),
              new CompilerError(14, "inappropiate java annotation: interoperation with @Retention is not supported"),
              new CompilerError(16, "inappropiate java annotation: interoperation with @Deprecated is not supported: use deprecated"),
              new CompilerError(17, "inappropiate java annotation: interoperation with @Override is not supported: use actual"));
    }
    
    @Test
    public void testAnnotationsConstrainedClassCtor() {
        compile("JavaAnnotation.java");
        compareWithJavaSource("AnnotationsConstrainedClassCtor");
    }
    
    @Test
    public void testAnnotationInteropQualifiedEnum(){
        compareWithJavaSource("AnnotationInteropQualifiedEnum");
    }
    
    @Test
    public void testSealedInterop(){
        compile("access/JavaSealed.java");
        assertErrors("Sealed",
                new CompilerError(27, "package private constructor is not visible: 'JavaSealed'"),
                new CompilerError(29, "instantiates or references a sealed class in a different module: 'Runtime' in 'java.base'"),
                new CompilerError(30, "type constructor is not visible: 'JavaSealed'"));
    }
    
    @Test
    public void testIopBug1736(){
        compile("JavaWithStaticMembers.java");
        compareWithJavaSource("Bug1736");
    }
    
    @Test
    public void testIopBug1806(){
        compareWithJavaSource("Bug1806");
    }
    
    @Test
    public void testIopBug1977(){
        compareWithJavaSource("Bug1977");
    }
    
    @Test
    public void testIopWidening(){
        compile("Widening.ceylon");
        run("com.redhat.ceylon.compiler.java.test.interop.run");
    }

    @Test
    public void testIopBug2019(){
        compile("access/JavaBug2019.java");
        compile("Bug2019.ceylon");
    }
    
    @Test
    public void testIopBug2027(){
        compile("Bug2027.ceylon");
    }
    
    @Test
    public void testIopBug2042(){
        compareWithJavaSource("Bug2042");
    }
    
    @Test
    public void testIopBug2053(){
        compile("Bug2053Varargs.java");
        compareWithJavaSource("Bug2053");
    }
    
    @Test
    public void testIopBug2054(){
        compile("Bug2054Java.java");
        compileAndRun("com.redhat.ceylon.compiler.java.test.interop.bug2054", "Bug2054.ceylon");
    }
    
    @Test
    public void testIopBug2199(){
        compareWithJavaSource("Bug2199");
    }
    
    @Test
    public void testIopBug2271(){
        compareWithJavaSource("Bug2271");
    }

    @Test
    public void testIopBug2310(){
        compile("Bug2310Java.java");
        compile("Bug2310.ceylon");
    }

    @Test
    public void testIopBug2318(){
        compile("Bug2318Java.java");
        compile("Bug2318.ceylon");
    }
}

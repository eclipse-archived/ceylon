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
package org.eclipse.ceylon.compiler.java.test.interop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.OSUtil;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.config.Repositories;
import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.test.ErrorCollector;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.eclipse.ceylon.model.cmr.JDKUtils.JDK;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

public class InteropTests extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main) {
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.interop", "1");
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
                new CompilerError(25, "illegal argument types in invocation of overloaded method or class: there must be exactly one overloaded declaration of 'ambiguousOverload' which accepts the given argument types 'String, String'"),
                new CompilerError(26, "illegal argument types in invocation of overloaded method or class: there must be exactly one overloaded declaration of 'ambiguousOverload2' which accepts the given argument types 'Integer, Integer'")
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
    public void testIopVariadicImplementations(){
        compile("TypesJava.java");
        compareWithJavaSource("VariadicImplementations");
    }
    
    @Test
    public void testIopImplementOverloadedConstructors(){
        compile("JavaWithOverloadedMembers.java");
        compareWithJavaSource("ImplementOverloadedConstructors");
    }

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
    public void testIopDeclareOverloadedMethods(){
        assertErrors("DeclareOverloadedMethodsErrors", 
                new CompilerError(22, "duplicate declaration: the name 'method' is not unique in this scope (overloaded function must be declared with the 'overloaded' annotation in 'java.lang')"),
                new CompilerError(23, "duplicate declaration: the name 'method' is not unique in this scope (overloaded function must be declared with the 'overloaded' annotation in 'java.lang')"));
        compareWithJavaSource("DeclareOverloadedMethods");
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
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testIopJavaExceptionMessage(){
        compile("JavaExceptionMessage.java");
        compareWithJavaSource("JavaExceptionMessage");
        
    }
    
    @Test
    public void testIopJavaExceptionMessage2(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.interop.javaExceptionMessage2",
                "JavaExceptionsAndThrowable.java", "JavaExceptionMessage2.ceylon");
    }
    
    @Test
    public void testIopJavaExceptionArrays(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.interop.javaExceptionArrays", "JavaExceptionArrays.ceylon", "JavaExceptionsAndThrowable.java");
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
                new CompilerError(22, "non-actual member collides with an inherited member: 'defaultAccessMethod' in 'RefinesDefaultAccessMethodWithShared' refines 'defaultAccessMethod' in 'JavaAccessModifiers' but is not annotated 'actual'"));
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
                new CompilerError(21, "illegal argument types in invocation of overloaded method or class: there must be exactly one overloaded declaration of 'JavaDefaultAccessClass4' which accepts the given argument types ''")
        );
    }

    @Test
    public void testIopExtendsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        assertErrors("ExtendsDefaultAccessClassInAnotherPkg",
                new CompilerError(21, "imported declaration is not visible: 'JavaDefaultAccessClass' is not shared"),
                new CompilerError(22, "imported declaration is not visible: 'JavaDefaultAccessClass2' is not shared"),
                new CompilerError(27, "type is not visible: 'JavaDefaultAccessClass'"),
                new CompilerError(29, "type is not visible: 'JavaDefaultAccessClass2'"),
                new CompilerError(31, "constructor is not visible: 'JavaDefaultAccessClass3' is package private")
        );
    }

    @Test
    public void testIopCallsDefaultAccessClass(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        compareWithJavaSource("access/CallsDefaultAccessClass");
    }

    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testIopCallsDefaultAccessClassWithOverloading(){
        compile("access/JavaDefaultAccessClass4.java");
        assertErrors("access/CallsDefaultAccessClassWithOverloading",
                new CompilerError(22, "illegal argument types in invocation of overloaded method or class: there must be exactly one overloaded declaration of 'JavaDefaultAccessClass4' which accepts the given argument types ''")
        );
    }

    @Test
    public void testIopCallsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        assertErrors("CallsDefaultAccessClassInAnotherPkg",
                new CompilerError(21, "imported declaration is not visible: 'JavaDefaultAccessClass' is not shared"),
                new CompilerError(22, "imported declaration is not visible: 'JavaDefaultAccessClass2' is not shared"),
                new CompilerError(28, "type is not visible: 'JavaDefaultAccessClass'"),
                new CompilerError(29, "type is not visible: 'JavaDefaultAccessClass2'"),
                new CompilerError(30, "type constructor is not visible: 'JavaDefaultAccessClass3'")
        );
    }

    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testIopCallsDefaultAccessClassInAnotherPkgWithOverloading(){
        compile("access/JavaDefaultAccessClass4.java");
        assertErrors("CallsDefaultAccessClassInAnotherPkgWithOverloading",
                new CompilerError(26, "illegal argument types in invocation of overloaded method or class: there must be exactly one overloaded declaration of 'JavaDefaultAccessClass4' which accepts the given argument types ''"),
                new CompilerError(27, "type constructor is not visible: 'JavaDefaultAccessClass4'"),
                new CompilerError(28, "constructor is not visible: 'JavaDefaultAccessClass4' is protected")
        );
    }

    @Test
    public void testIopCallsDefaultAccessMethodInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("CallsDefaultAccessMethodInAnotherPkg",
                new CompilerError(25, "method or attribute is not visible: 'protectedAccessMethod' of type 'JavaAccessModifiers' is protected"),
                new CompilerError(27, "method or attribute is not visible: 'defaultAccessMethod' of type 'JavaAccessModifiers' is package private"),
                new CompilerError(36, "function or value is not visible: 'defaultAccessMethod' is package private"));
    }

    @Test
    public void testIopRefinesDefaultAccessMethodInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("RefinesDefaultAccessMethodInAnotherPkg",
                new CompilerError(27, "refined declaration is not visible: 'defaultAccessMethod' in 'RefinesDefaultAccessMethodInAnotherPkg' refines 'defaultAccessMethod' in 'JavaAccessModifiers' which is package private"));
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
                new CompilerError(26, "member refines a non-default, non-formal member: 'topMethod' in 'StaticMethodsOverriding' refines 'topMethod' in 'JavaWithStaticMembers' which is not annotated 'formal' or 'default'"),
                new CompilerError(28, "member refines a non-default, non-formal member: 'topField' in 'StaticMethodsOverriding' refines 'topField' in 'JavaWithStaticMembers' which is not annotated 'formal' or 'default'")
        );
    }
    
    @Test
    public void testJavaxInject(){
        compareWithJavaSource("JavaxInject");
    }
    
    @Test
    public void testJavaxValidation(){
        compareWithJavaSource("JavaxValidation");
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testAnnotationInterop(){
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/JavaAnnotation.java");
        compareWithJavaSource("sdk/AnnotationInterop");
        assertErrors("sdk/AnnotationInteropErrors",
                new CompilerError(2, "function or value is not defined: 'javaAnnotationNoTarget__TYPE' might be misspelled or is not imported (did you mean 'javaAnnotationTypeTarget__TYPE'?)"),
                new CompilerError(3, "function or value is not defined: 'javaAnnotationNoTarget__CONSTRUCTOR' might be misspelled or is not imported (did you mean 'javaAnnotationCtorTarget__CONSTRUCTOR'?)"),
                new CompilerError(6, "function or value is not defined: 'javaAnnotationNoTarget__FIELD' might be misspelled or is not imported (did you mean 'javaAnnotationFieldTarget__FIELD'?)"),
                new CompilerError(7, "function or value is not defined: 'javaAnnotationNoTarget__GETTER' might be misspelled or is not imported (did you mean 'javaAnnotationMethodTarget__GETTER'?)"),
                new CompilerError(8, "function or value is not defined: 'javaAnnotationNoTarget__SETTER' might be misspelled or is not imported (did you mean 'javaAnnotationMethodTarget__SETTER'?)"),
                new CompilerError(12, "function or value is not defined: 'javaAnnotationNoTarget__PARAMETER' might be misspelled or is not imported (did you mean 'javaAnnotationDefaultTarget__PARAMETER'?)"),
                new CompilerError(14, "function or value is not defined: 'javaAnnotationNoTarget__LOCAL_VARIABLE' might be misspelled or is not imported (did you mean 'javaAnnotationDefaultTarget__LOCAL_VARIABLE'?)"),
                new CompilerError(19, "function or value is not defined: 'javaAnnotationNoTarget__ANNOTATION_TYPE' might be misspelled or is not imported (did you mean 'javaAnnotationDefaultTarget__ANNOTATION_TYPE'?)"),
                new CompilerError(21, "illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference"),
                new CompilerError(21, "named argument must be assignable to parameter 'clas' of 'javaAnnotationClass2': 'Class<String>' is not assignable to 'ClassOrInterfaceDeclaration'")
                );
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testAnnotationSequencedArgs(){
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/JavaAnnotation.java");
        compareWithJavaSource("sdk/AnnotationSequencedArgs");
    }
    
    @Test
    public void testBannedAnnotation(){
      assertErrors("BannedAnnotation",
              new CompilerError(13, "illegal Java annotation"),
              new CompilerError(14, "illegal Java annotation"),
              new CompilerError(16, "illegal Java annotation (use 'deprecated' in 'ceylon.language')"),
              new CompilerError(17, "illegal Java annotation (use 'actual' in 'ceylon.language')"));
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testAnnotationsConstrainedClassCtor() {
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/JavaAnnotation.java");
        compareWithJavaSource("sdk/AnnotationsConstrainedClassCtor");
    }
    
    @Test
    public void testAnnotationInteropQualifiedEnum(){
        compareWithJavaSource("AnnotationInteropQualifiedEnum");
    }
    
    @Test
    public void testAnnotationBug6145() {
        compareWithJavaSource("AnnotationBug6145");
    }
    
    @Test
    public void testRepeatableAnnotation() {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compile("JavaRepeatable.java");
        compareWithJavaSource("RepeatableAnnotation");
    }
    
    @Test
    public void testSealedInterop(){
        compile("access/JavaSealed.java");
        assertErrors("Sealed",
                new CompilerError(27, "constructor is not visible: 'JavaSealed' is package private"),
                new CompilerError(29, "class cannot be instantiated: 'Runtime' does not have a default constructor"),
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
        run("org.eclipse.ceylon.compiler.java.test.interop.run");
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
    
    @Test
    public void testIopBug2327(){
        compile("Bug2327Java.java");
        compile("Bug2327.ceylon");
    }
    
    @Test
    public void testIopJpaCtor(){
        compile("JpaCtorWithoutNullary.java");
        compile("JpaCtorWithNullary.java");
        compile("JpaCtor.ceylon");
    }
    
    @Test
    public void testIopBug2331(){
        compile("Bug2331Java.java");
        compile("Bug2331.ceylon");
    }

    @Test
    public void testIopJavaSerialization() throws Throwable{
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/JavaSerialization.ceylon", "sdk/javaSerializationRoundTrip_.java");
        runInJBossModules("run",
                "org.eclipse.ceylon.compiler.java.test.interop.sdk",
                Arrays.asList(//"org.eclipse.ceylon.compiler.java.test.interop",
                        "--run", "org.eclipse.ceylon.compiler.java.test.interop.sdk::javaSerializationRoundTrip"));
    }
    
    @Test
    public void testIopJavaSerializationEeMode() throws Throwable{
        // Same as above with with --ee
        Assume.assumeTrue(allowSdkTests());
        List<String> opts = new ArrayList<>(defaultOptions);
        opts.add("-ee");
        compile(opts,
                "sdk/JavaSerialization.ceylon", "sdk/javaSerializationRoundTrip_.java");
        runInJBossModules("run",
                "org.eclipse.ceylon.compiler.java.test.interop.sdk",
                Arrays.asList(//"org.eclipse.ceylon.compiler.java.test.interop",
                        "--run", "org.eclipse.ceylon.compiler.java.test.interop.sdk::javaSerializationRoundTrip"));
    }
    
    @Test
    public void testIopSerializableAssignable() throws Throwable{
        compile("SerializableAssignable.ceylon");
    }
    
    @Test
    public void testIopBug2397() throws Throwable{
        compareWithJavaSource("Bug2397");
    }
    
    /**
     * Warning: this test requires a build with "ant -Djigsaw=true clean dist" and to run on Java 9+Jigsaw
     */
    @Test
    public void testJava9Module() throws Throwable{
        Assume.assumeTrue("Runs on JDK9", JDKUtils.jdk == JDKUtils.JDK.JDK9);
        assertErrors(new String[]{"java9/user/runError.ceylon", "java9/user/package.ceylon"}, 
        		Arrays.asList("-out", destDir, "-rep", "test/java9/modules"), null, 
        		new CompilerError(1, "imported package is not shared: 'com.ceylon.java9.internal'"));

        ErrorCollector c = new ErrorCollector();
        assertCompilesOk(c, getCompilerTask(Arrays.asList("-out", destDir, "-rep", "test/java9/modules"), c,
        		"java9/user/run.ceylon", "java9/user/package.ceylon").call2());
        // check that we do not have a module-info.class
        File archive = getModuleArchive("org.eclipse.ceylon.compiler.java.test.interop.java9.user", "1");
        try(ZipFile zf = new ZipFile(archive)){
        	ZipEntry entry = zf.getEntry("module-info.class");
        	assertNull(entry);
        }	

        c = new ErrorCollector();
        assertCompilesOk(c, getCompilerTask(Arrays.asList("-out", destDir, "-rep", "test/java9/modules", "-module-info"), c,
        		"java9/user/run.ceylon", "java9/user/package.ceylon").call2());
        // check that we do have a module-info.class
        try(ZipFile zf = new ZipFile(archive)){
        	ZipEntry entry = zf.getEntry("module-info.class");
        	assertNotNull(entry);
        }	

        run("org.eclipse.ceylon.compiler.java.test.interop.java9.user.run", 
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.interop.java9.user", "1"),
                new ModuleWithArtifact("com.ceylon.java9", "123", "test/java9/modules", "jar"));

        assertEquals(0, runInJava9(new String[]{destDir, "test/java9/modules", "../dist/dist/repo"}, 
        		new ModuleSpec(null, "org.eclipse.ceylon.compiler.java.test.interop.java9.user", "1"), 
        		Collections.<String>emptyList()));
    }
    
    @Test
    public void testIopJavaIterableInFor(){
        compareWithJavaSource("JavaIterableInFor");
    }
    
    @Test
    public void testIopJavaArrayInFor(){
        compareWithJavaSource("JavaArrayInFor");
    }
    
    @Test
    public void testIopJavaIterableInForComprehension(){
        compareWithJavaSource("JavaIterableInForComprehension");
        run("org.eclipse.ceylon.compiler.java.test.interop.javaIterableInForComprehension");
    }
    
    @Test
    public void testIopJavaArrayInForComprehension(){
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/JavaArrayInForComprehension_util.ceylon");
        compareWithJavaSource("sdk/JavaArrayInForComprehension");
        run("org.eclipse.ceylon.compiler.java.test.interop.sdk.javaArrayInForComprehension", 
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.interop.sdk", "1"),
                new ModuleWithArtifact("ceylon.interop.java", Versions.CEYLON_VERSION_NUMBER,
                        Repositories.get().getUserRepoDir().getAbsolutePath(),
                        "car"));
    }
    
    @Test
    public void testIopJavaIterableInSpreadArgument(){
        //compareWithJavaSource("JavaIterableInSpreadArgument");
        compile("JavaIterableInSpreadArgument.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.javaIterableInSpreadArgument");
    }
    
    @Test
    public void testIopJavaArrayInSpreadArgument(){
        //compareWithJavaSource("JavaArrayInSpreadArgument");
        compile("JavaArrayInSpreadArgument.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.javaArrayInSpreadArgument");
    }
    
    @Test
    public void testIopJavaIterableWithSpreadOperator(){
        //compareWithJavaSource("JavaIterableWithSpreadOperator");
        compile("JavaIterableWithSpreadOperator.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.javaIterableWithSpreadOperator");
    }
    
    @Test
    public void testIopJavaArrayWithSpreadOperator(){
        //compareWithJavaSource("JavaArrayWithSpreadOperator");
        compile("JavaArrayWithSpreadOperator.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.javaArrayWithSpreadOperator");
    }
    
    @Test
    public void testIopJavaAutoCloseableInTry(){
        compareWithJavaSource("JavaAutoCloseableInTry");
    }
    
    @Test
    public void testIopJavaIterableWithoutJavaBase() throws IOException {
        // compile our java class
        File classesOutputFolder = new File(destDir+"-jar-classes");
        cleanCars(classesOutputFolder.getPath());
        classesOutputFolder.mkdirs();

        File jarOutputFolder = new File(destDir+"-jar");
        cleanCars(jarOutputFolder.getPath());
        jarOutputFolder.mkdirs();

        compileJavaModule(jarOutputFolder, classesOutputFolder, "org.eclipse.ceylon.compiler.java.test.interop.bug4389.a", "1",
                moduleName.replace('.', '/')+"/bug4389/a/A.java");

        ArrayList<String> opts = new ArrayList<String>(defaultOptions);
        opts.add("-rep");
        opts.add(jarOutputFolder.getPath());
        compareWithJavaSource(opts, "bug4389/b/b.src", "bug4389/b/b.ceylon");
        //
    }
    
    @Test
    public void testIopJavaListIndex(){
        compareWithJavaSource("JavaListIndex");
    }
    
    @Test
    public void testIopJavaMapIndex(){
        compareWithJavaSource("JavaMapIndex");
    }
    
    @Test
    public void testIopJavaArrayIndex(){
        compareWithJavaSource("JavaArrayIndex");
    }
    
    @Test
    public void testIopJavaCollectionWithInOp(){
        compareWithJavaSource("JavaCollectionWithInOp");
    }
    
    @Test
    public void testIopJavaArrayTypeConstraint(){
        compile("JavaArrayTypeConstraint.ceylon");
    }
    
    @Test
    public void testIopBug6143(){
        compareWithJavaSource("Bug6143");
    }
    
    @Test
    public void testIopBug6156(){
        compile("Bug6156Document.java");
        compile("Bug6156.ceylon");
        compile("Bug6156b.ceylon");
    }
    
    @Test
    public void testIopBug6160(){
        compile("Bug6160.ceylon");
    }
    
    @Test
    public void testIopCallDefaultInterfaceMethod(){
        Assume.assumeTrue("Runs on JDK >= 8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compareWithJavaSource("CallDefaultInterfaceMethod");
    }
    
    @Test
    public void testIopSatisfyInterfaceWithDefaultMethod(){
        Assume.assumeTrue("Runs on JDK >= 8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compareWithJavaSource("SatisfyInterfaceWithDefaultMethod");
    }
    
    @Test
    public void testIopRefineDefaultInterfaceMethod(){
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compareWithJavaSource("RefineDefaultInterfaceMethod");
    }
    
    @Test
    public void testBug6244(){
        compile("Bug6244Java.java");
        compile("Bug6244.ceylon");
    }
    
    @Test
    public void testSdkBug571() throws Throwable{
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/SdkBug571.ceylon");
        runInJBossModules("run", 
                "org.eclipse.ceylon.compiler.java.test.interop.sdk",
                Arrays.asList("--run=org.eclipse.ceylon.compiler.java.test.interop.sdk::sdkBug571_run"));
    }

    @Test
    public void testIopBug6099(){
        compile("Bug6099Java.java");
        compile("Bug6099.ceylon");
    }
    
    @Test
    public void testIopBug6123(){
        compile("Bug6123Java.java");
        compile("Bug6123.ceylon");
    }
    
    @Test
    public void testIopBug6289(){
        compareWithJavaSource("Bug6289");
    }

    @Test
    public void testIopInterdep(){
        compile("InterdepJava.java", "Interdep.ceylon");
    }
    
    @Test
    public void testIopNullAnnotations(){
        compile("nullable/NullAnnotationsJava.java", "nullable/NullAnnotations.ceylon");
    }

    @Test
    public void testIopRawImplementations(){
        compile("RawJava.java", "Raw.ceylon");
    }

    @Test
    public void testIopLambdas(){
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        List<String> options = Arrays.asList("-source", "8", "-target", "8");
        compile(options, "LambdasJava.java");
        compareWithJavaSource(options, "Lambdas.src", "Lambdas.ceylon");
        compileAndRun("org.eclipse.ceylon.compiler.java.test.interop.classModelCoercionTest", "LambdasRuntime.ceylon");
        assertErrors("LambdasErrors", new CompilerError(12, "refined declaration is not a real method: 'm' in 'Sub3' refines 'm' in 'InterfaceWithCoercedMembers'"));
    }
    
    @Test
    @Ignore("Missing 1.3.2")
    public void testSerializableLambdas() {
        compile("SerializableLambdas.java", "SerializableLambdas.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.serializableLambdas");
    }

    @Test
    public void testIopStaticEnumSet(){
        Assume.assumeTrue(allowSdkTests());
        compile("sdk/StaticEnumSet.ceylon");
    }
    

    @Test
    public void testIopBug6574() {
        compareWithJavaSource("Bug6574");
        run("org.eclipse.ceylon.compiler.java.test.interop.bug6574");
    }
    
    @Test
    public void testIopBug6587() {
        compareWithJavaSource("Bug6587");
    }
    
    @Test
    public void testIopBug6588() {
        compareWithJavaSource("Bug6588");
    }
    
    @Test
    public void testIopBug6589() {
        compareWithJavaSource("Bug6589");
    }
    
    @Test
    public void testIopAnnotateJavaPackage() throws Exception {
        compile("packageannotations/AnnotateJavaPackage.ceylon", "packageannotations/package.ceylon");
        synchronized(RUN_LOCK){
            String main = "org.eclipse.ceylon.compiler.java.test.interop.packageannotations.AnnotateJavaPackage";
            try (URLClassLoader classLoader = getClassLoader(main, new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.interop.packageannotations", "1"))) {
                java.lang.Class<?> c = java.lang.Class.forName("org.eclipse.ceylon.compiler.java.test.interop.packageannotations.IntegerAdaptor", false, classLoader);
                java.lang.Package p = c.getPackage();
                java.lang.Class ac = java.lang.Class.forName("javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters", false, classLoader);
                assertNotNull(p.getAnnotation(ac));
            }
        }
    }
    
    @Test
    public void testIopAnnotatedJavaPackageRecompile() throws Exception {
        ArrayList<String> a = new ArrayList<String>();
        a.add(script());
        a.add("compile");
        a.add("--verbose=code");
        a.add("--rep");
        a.add(getOutPath());
        a.add("--src");
        a.add("test/src");
        a.add("org.eclipse.ceylon.compiler.java.test.interop.packageannotations");
        System.err.println(a);
        ProcessBuilder pb = new ProcessBuilder(a);
        pb.redirectError(Redirect.INHERIT);
        pb.redirectOutput(Redirect.INHERIT);
        // use the same JVM as the current one
        pb.environment().put("JAVA_HOME", System.getProperty("java.home"));
        if(JDKUtils.jdk.providesVersion(JDK.JDK9.version)){
            pb.environment().put("JAVA_OPTS", "-XaddExports:java.xml/com.sun.org.apache.xerces.internal.jaxp=ALL-UNNAMED"
                    +",java.xml/com.sun.org.apache.xalan.internal.xsltc.trax=ALL-UNNAMED"
                    +",java.xml/com.sun.xml.internal.stream=ALL-UNNAMED");
        }
        Process p = pb.start();
        assertEquals(0, p.waitFor());
        // Now run it again
        p = pb.start();
        assertEquals(0, p.waitFor());
    }
    
    @Test
    public void testBug6632() {
        compareWithJavaSource("Bug6632");
    }
    
    @Test
    public void testBug6635() {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compileAndRun("org.eclipse.ceylon.compiler.java.test.interop.run6635", 
                "Bug6635.ceylon");
    }
    
    @Test
    public void testIopTransient() {
        compareWithJavaSource("Transient");
    }
    
    @Test
    public void testIopVolatile() {
        compareWithJavaSource("Volatile");
    }
    
    @Test
    public void testIopSynchronized() {
        compareWithJavaSource("Synchronized");
    }
    
    @Test
    public void testIopNative() {
        compareWithJavaSource("Native");
    }
    
    @Test
    public void testIopStrictfp() {
        compareWithJavaSource("Strictfp");
    }
    
    @Test
    public void testIopArrayWith() {
        compareWithJavaSource("ArrayWith");
        run("org.eclipse.ceylon.compiler.java.test.interop.arrayWith");
    }
    
    @Test
    public void testIopArrayFrom() {
        //compareWithJavaSource("ArrayFrom");
        compile("ArrayFrom.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.arrayFrom");
    }
    
    @Test
    public void testIopAssertionMessageDetail() {
        compile("IopAssertionMessageDetail.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.assertionMessageDetail");
    }
    
    @Test
    public void testIopBug6854() {
        compile("Bug6854.java");
        compareWithJavaSource("Bug6854");
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testClassLiteral() {
        compile("ClassLiteral.java");
        compareWithJavaSource("ClassLiteral");
        //compile("ClassLiteral.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.classLiteral_run");
    }
    
    @Test
    public void testIopBug6899() {
        compile("Bug6899.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.interop.bug6899");
    }
}

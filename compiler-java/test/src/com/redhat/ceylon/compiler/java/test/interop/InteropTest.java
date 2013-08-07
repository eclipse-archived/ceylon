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
import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class InteropTest extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact() {
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.interop", "1");
    }
    
    @Test
    public void testIopArrays(){
        compareWithJavaSource("Arrays");
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
    public void testIopVariadicOverloadedMethods(){
        compile("JavaWithOverloadedMembers.java");
        compareWithJavaSource("VariadicOverloadedMethods");
    }

    @Test
    @Ignore("M5")
    // depends on https://github.com/ceylon/ceylon-spec/issues/420
    public void testIopVariadicArrays_fail(){
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
    public void testIopFields(){
        compile("JavaFields.java");
        compareWithJavaSource("Fields");
    }

    @Ignore("M5: #432")
    @Test
    public void testIopSpecialFields(){
        compile("JavaFields.java");
        compareWithJavaSource("SpecialFields");
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
    public void testIopTypes(){
        compareWithJavaSource("Types");
    }

    @Test
    public void testIopEnums(){
        compile("JavaEnum.java");
        compareWithJavaSource("Enums");
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
                new CompilerError(22, "non-actual member refines an inherited member: defaultAccessMethod in JavaAccessModifiers"));
    }

    @Test
    public void testIopRefinesDefaultAccessMethodWithActual(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesDefaultAccessMethodWithActual",
                new CompilerError(22, "actual declaration must be shared: defaultAccessMethod"));
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
                new CompilerError(21, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of JavaDefaultAccessClass4 that accepts the given argument types)")
        );
    }

    @Ignore("https://github.com/ceylon/ceylon-compiler/issues/773")
    @Test
    public void testIopExtendsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        assertErrors("ExtendsDefaultAccessClassInAnotherPkg",
                new CompilerError(21, "imported declaration is not visible: JavaDefaultAccessClass"),
                new CompilerError(22, "imported declaration is not visible: JavaDefaultAccessClass2"),
                new CompilerError(27, "constructor is not visible something"),
                new CompilerError(29, "constructor is not visible something")
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
                new CompilerError(22, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of JavaDefaultAccessClass4 that accepts the given argument types)")
        );
    }

    @Ignore("https://github.com/ceylon/ceylon-compiler/issues/773")
    @Test
    public void testIopCallsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        compile("access/JavaDefaultAccessClass3.java");
        assertErrors("CallsDefaultAccessClassInAnotherPkg",
                new CompilerError(21, "imported declaration is not visible: JavaDefaultAccessClass"),
                new CompilerError(22, "imported declaration is not visible: JavaDefaultAccessClass2"),
                new CompilerError(28, "package private type is not visible: JavaDefaultAccessClass"),
                new CompilerError(29, "package private constructor is not visible: JavaDefaultAccessClass2"),
                new CompilerError(30, "package private constructor is not visible: JavaDefaultAccessClass3")
        );
    }

    @Test
    public void testIopCallsDefaultAccessClassInAnotherPkgWithOverloading(){
        compile("access/JavaDefaultAccessClass4.java");
        assertErrors("CallsDefaultAccessClassInAnotherPkgWithOverloading",
                new CompilerError(26, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of JavaDefaultAccessClass4 that accepts the given argument types)"),
                new CompilerError(27, "package private constructor is not visible: JavaDefaultAccessClass4"),
                new CompilerError(28, "protected constructor is not visible: JavaDefaultAccessClass4")
        );
    }

    @Test
    public void testIopCallsDefaultAccessMethodInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("CallsDefaultAccessMethodInAnotherPkg",
                new CompilerError(25, "protected method or attribute is not visible: protectedAccessMethod of type JavaAccessModifiers"),
                new CompilerError(27, "package private method or attribute is not visible: defaultAccessMethod of type JavaAccessModifiers"),
                new CompilerError(36, "package private function or value is not visible: defaultAccessMethod"));
    }

    @Test
    public void testIopRefinesDefaultAccessMethodInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("RefinesDefaultAccessMethodInAnotherPkg",
                new CompilerError(27, "refined declaration is not visible: defaultAccessMethod in JavaAccessModifiers"));
    }

    @Test
    public void testIopNamedInvocations(){
        assertErrors("NamedInvocations",
                new CompilerError(30, "could not determine type of function or value reference: createTempFile"),
                new CompilerError(30, "overloaded declarations may not be called using named arguments: createTempFile"),
                new CompilerError(30, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of createTempFile that accepts the given argument types"),
                new CompilerError(30, "named invocations of Java methods not supported"),
                new CompilerError(32, "named invocations of Java methods not supported"),
                new CompilerError(35, "could not determine type of method or attribute reference: createTempFile of File"),
                new CompilerError(35, "named invocations of Java methods not supported"),
                new CompilerError(35, "could not determine type of method or attribute reference: createTempFile of File"),
                new CompilerError(35, "overloaded declarations may not be called using named arguments: createTempFile"),
                new CompilerError(35, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of createTempFile that accepts the given argument types"),
                new CompilerError(37, "named invocations of Java methods not supported")
        );
    }

    @Test
    public void testIopOverrideStaticMethods(){
        compile("JavaWithStaticMembers.java");
        assertErrors("OverrideStaticMethods",
                new CompilerError(26, "member refines a non-default, non-formal member: topMethod in JavaWithStaticMembers"),
                new CompilerError(28, "member refines a non-default, non-formal member: topField in JavaWithStaticMembers")
        );
    }
    

    @Test
    public void testAnnotationInterop(){
        compile("JavaAnnotation.java");
        compareWithJavaSource("AnnotationInterop");
    }
}

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
        assertErrors("access/CallsProtectedAccessMethod",
                new CompilerError(23, "member method or attribute is not visible: protectedAccessMethod of type JavaAccessModifiers"));
    }
    
    @Test
    @Ignore("M5: depends on spec #226")
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
    public void testIopRefinesDefaultAccessMethodWithShared_fail(){
        compile("access/JavaAccessModifiers.java");
        // XXX This error comes from javac rather than the ceylon typechecker
        assertErrors("access/RefinesDefaultAccessMethodWithShared",
                new CompilerError(22, "I don't know what the error message is yet, but I expect there to be one."));
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
        assertErrors("access/RefinesDefaultAccessMethodWithSharedActual",
                new CompilerError(22, "actual member does not refine any inherited member"));
    }
    
    @Test
    public void testIopCallsDefaultAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/CallsDefaultAccessMethod",
                new CompilerError(23, "member method or attribute does not exist: packageAccessMethod in type JavaAccessModifiers"));
    }
    
    @Test
    public void testIopExtendsDefaultAccessClass(){
        compile("access/JavaAccessModifiers.java");
        compareWithJavaSource("access/ExtendsDefaultAccessClass");
    }
    
    @Test
    public void testIopExtendsDefaultAccessClassInAnotherPkg(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("ExtendsDefaultAccessClassInAnotherPkg",
                new CompilerError(20, "imported declaration is not shared: JavaDefaultAccessClass"),
                new CompilerError(23, "com.redhat.ceylon.compiler.java.test.interop.access.JavaDefaultAccessClass is not public in com.redhat.ceylon.compiler.java.test.interop.access; cannot be accessed from outside package"));
    }

    @Test
    public void testIopNamedInvocations(){
        assertErrors("NamedInvocations",
                new CompilerError(30, "could not determine type of function or value reference: createTempFile"),
                new CompilerError(30, "overloaded declarations may not be called using named arguments: createTempFile"),
                new CompilerError(30, "named invocations of Java methods not supported"),
                new CompilerError(32, "named invocations of Java methods not supported"),
                new CompilerError(35, "could not determine type of method or attribute reference: createTempFile of File"),
                new CompilerError(35, "named invocations of Java methods not supported"),
                new CompilerError(35, "could not determine type of method or attribute reference: createTempFile of File"),
                new CompilerError(35, "overloaded declarations may not be called using named arguments: createTempFile"),
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
}

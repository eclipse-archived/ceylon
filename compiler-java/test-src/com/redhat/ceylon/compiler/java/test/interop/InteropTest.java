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

import org.junit.Test;

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
        compareWithJavaSource("Methods");
    }

    @Test
    public void testIopFields(){
        compile("JavaFields.java");
        compareWithJavaSource("Fields");
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
    public void testIopStaticMethods(){
        compareWithJavaSource("StaticMethods");
    }

    @Test
    public void testIopTypes(){
        compareWithJavaSource("Types");
    }
	
    @Test
    public void testIopCheckedExceptions(){
        compile("JavaCheckedExceptions.java");
        compareWithJavaSource("CheckedExceptions");
    }
    
    @Test
    public void testIopRefinesProtectedAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesProtectedAccessMethod", 
                new CompilerError(23, "non-actual member refines an inherited member"));
    }
    
    @Test
    public void testIopRefinesProtectedAccessMethodWithActual(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesProtectedAccessMethodWithActual",
                new CompilerError(23, "actual member is not shared"));
    }
    
    @Test
    public void testIopCallsProtectedAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/CallsProtectedAccessMethod",
                new CompilerError(23, "member method or attribute is not visible: protectedAccessMethod of type JavaAccessModifiers"));
    }
    
    @Test
    public void testIopRefinesDefaultAccessMethod(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesDefaultAccessMethod",
                new CompilerError(22, "non-actual member refines an inherited member"));
    }
    
    @Test
    public void testIopRefinesDefaultAccessMethodWithActual(){
        compile("access/JavaAccessModifiers.java");
        assertErrors("access/RefinesDefaultAccessMethodWithActual",
                new CompilerError(22, "actual member is not shared"));
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

}

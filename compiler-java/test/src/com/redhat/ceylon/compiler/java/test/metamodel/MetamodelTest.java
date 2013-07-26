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
package com.redhat.ceylon.compiler.java.test.metamodel;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class MetamodelTest extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.metamodel", "123");
    }

    @Test
    public void testRuntime() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.runtime", "runtime.ceylon", "types.ceylon", "visitor.ceylon");
    }

    @Test
    public void testInteropRuntime() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.interopRuntime", "interopRuntime.ceylon", "JavaType.java");
    }

    @Test
    public void testAnnotations() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.annotationTests", "annotationTypes.ceylon", "annotationTests.ceylon");
    }

    @Test
    public void testTypeLiterals() {
        compareWithJavaSource("Literals");
    }

    @Test
    public void testTypeLiteralRuntime() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.literalsRuntime", "Literals.ceylon", "literalsRuntime.ceylon");
    }

    @Test
    public void testBugCL238() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug238", "bug238.ceylon");
    }

    @Test
    public void testBugCL245() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug245", "bug245.ceylon");
    }

    @Test
    public void testBugCL257() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug257", "bug257.ceylon");
    }

    @Test
    public void testBugCL258() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug258", "bug258.ceylon");
    }

    @Test
    public void testBug1196() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug1196test", "bug1196.ceylon");
    }

    @Test
    public void testBug1197() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug1197", "bug1197.ceylon");
    }

    @Test
    public void testBug1198() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug1198", "bug1198.ceylon");
    }

    @Test
    public void testBug1199() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug1199", "bug1199.ceylon");
    }

    @Test
    public void testBug1201() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug1201", "bug1201.ceylon");
    }

    @Test
    public void testBug1209() {
        assertErrors("bug1209",
                new CompilerError(3, "metamodel references to local values not supported")
        );
    }

    @Test
    public void testBug1205() {
        compareWithJavaSource("bug1205");
    }

    @Test
    public void testBug1210() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.metamodel.bug1210", "bug1210.ceylon");
    }
}


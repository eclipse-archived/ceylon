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
package org.eclipse.ceylon.compiler.java.test.metamodel;

import java.io.File;
import java.util.Arrays;

import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.test.CompilerTests.ModuleWithArtifact;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.junit.Test;

public class MetamodelTests extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.metamodel", "123");
    }

    @Test
    public void testInteropRuntime() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.metamodel.interopRuntime", "interopRuntime.ceylon", "JavaType.java");
    }

    @Test
    public void testTypeLiterals() {
        compareWithJavaSource("Literals");
    }

    // FIXME: I guess this one should also move to the language module
    @Test
    public void testTypeLiteralRuntime() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.metamodel.literalsRuntime", "Literals.ceylon", "literalsRuntime.ceylon");
    }

    @Test
    public void testBug1793() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.metamodel.bug1793", "bug1793.ceylon", "JavaType.java");
    }

    @Test
    public void testBug1209() {
        assertErrors("bug1209",
                new CompilerError(3, "metamodel reference to local declaration")
        );
    }

    @Test
    public void testBug1205() {
        compareWithJavaSource("bug1205");
    }

    @Test
    public void testBugCL426() {
        // this sucks, but is the only way to remove these from the Eclipse classpath that the 
        // run() ClassLoader delegates to. If we don't they will be loaded from a different classloader
        // than the Ceylon files and since C1 is package-private we get a runtime access exception.
        // If we make the run() ClassLoader have no parent ClassLoader and load everything itself
        // then we can't initialise the Metamodel. This is quicker.
        new File("build/classes/org/eclipse/ceylon/compiler/java/test/metamodel/C0.class").delete();
        new File("build/classes/org/eclipse/ceylon/compiler/java/test/metamodel/C1.class").delete();
        compile("JavaType.java");
        compileAndRun("org.eclipse.ceylon.compiler.java.test.metamodel.bugCL426", "bugCL426.ceylon");
    }

    @Test
    public void testBug1926() {
        compareWithJavaSource("bug1926");
    }
    
    @Test
    public void testBugCL629() {
        compareWithJavaSource("BugCL629");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.run629");
    }
    
    @Test
    public void testBugCL634() {
        compareWithJavaSource("bugCL634");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bugCL634");
    }
    
    @Test
    public void testBugCL566() {
        compareWithJavaSource("BugCL566");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bugCl566");
    }

    @Test
    public void testBugCL645() throws Throwable {
        compile("bugCL645.ceylon");
        runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.metamodel/123", 
                Arrays.asList("--run", "org.eclipse.ceylon.compiler.java.test.metamodel::bugCL645"));
    }
    
    @Test
    public void testConstructorLiterals() {
        //compareWithJavaSource("ConstructorLiterals");
        compile("ConstructorLiterals.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.constructorLiterals");
    }
    
    @Test
    public void testInteropAnnotations() throws Throwable {
        compile("annotations/Type.java", 
                "annotations/Constructor.java",
                "annotations/TypeConstructor.java",
                "interopAnnotations.ceylon");
        runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.metamodel/123", 
                Arrays.asList("--run", "org.eclipse.ceylon.compiler.java.test.metamodel::interopAnnotations"));
        
    }
    
    @Test
    public void testBug6255() {
        compile("bug6255/Bug6255.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6255.bug6255", 
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.metamodel.bug6255", "123"));
    }
    
    @Test
    public void testBug6871() {
        compile("Bug6871.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6871");
    }
    
    @Test
    public void testBug6895() {
        compile("Bug6895.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6895");
    }
    
    @Test
    public void testBug6897() {
        compile("Bug6897.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6897");
    }
    
    @Test
    public void testBug6898() {
        compile("Bug6898.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6898");
    }
    
    @Test
    public void testBug6902() {
        compile("Bug6902.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6902");
    }
    
    @Test
    public void testStaticMetamodel() {
        compile("StaticMetamodel.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.staticMetamodel");
    }
    
    @Test
    public void testBug6882() {
        compile("Bug6882.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.metamodel.bug6882");
    }
}


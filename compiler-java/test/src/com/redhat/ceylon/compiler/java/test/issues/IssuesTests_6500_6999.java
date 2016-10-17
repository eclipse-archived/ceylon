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
package com.redhat.ceylon.compiler.java.test.issues;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Assume;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.test.RunSingleThreaded;
import com.redhat.ceylon.model.cmr.JDKUtils;

@RunSingleThreaded
public class IssuesTests_6500_6999 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-6500-6999";
    }

    @Test
    public void testBug6523() {
        compareWithJavaSource("bug65xx/Bug6523");
    }

    @Test
    public void testBug6532() {
        assertErrors("bug65xx/Bug6532",
                new CompilerError(25, "missing named argument to required parameter 'Anything(Integer, String) service' of 'Endpoint'"),
                new CompilerError(27, "parameterized expression not the target of a specification statement: parameter list is not followed by '=>' specifier (add '=>' specifier, or explicitly specify 'function' keyword or return type)")
                );
    }

    @Test
    public void testBug6533() {
        compile("bug65xx/Bug6533A.ceylon", "bug65xx/Bug6533Annotation.ceylon");
        compile("bug65xx/Bug6533B.ceylon", "bug65xx/Bug6533Annotation.ceylon");
    }

    @Test
    public void testBug6543() {
        compile("bug65xx/Bug6543.ceylon");
    }

    @Test
    public void testBug6546() {
        compile("bug65xx/Bug6546.ceylon");
    }

    @Test
    public void testBug6547() {
        compile("bug65xx/Bug6547.ceylon", "bug65xx/ANRequest.java");
    }

    @Test
    public void testBug6548() {
        compile("bug65xx/Bug6548.ceylon", "bug65xx/Bug6548.java");
    }

    @Test
    public void testBug6566() throws IOException {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compile(Arrays.asList("-apt", "maven:org.netbeans.api:org-netbeans-modules-editor-mimelookup/RELEASE82",
                "-target", "8", "-source", "8",
                "-rep", "aether:"+getPackagePath()+"bug65xx/bug6566/settings.xml"), 
                "bug65xx/bug6566/Bug6566.ceylon");
        File archive = getModuleArchive("com.redhat.ceylon.compiler.java.test.issues.bug65xx.bug6566", "1");
        assertTrue(archive.exists());

        try(JarFile car = new JarFile(archive)){
            ZipEntry generatedFile = car.getEntry("META-INF/generated-layer.xml");
            assertNotNull(generatedFile);
        }
    }
    
    @Test
    public void testBug6592() {
        PrintStream err = System.err;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setErr(new PrintStream(out));
            CompilationAssertion expectSysError = new CompilationAssertion() {
                public void onSys(ErrorCollector collector, ExitState exitState) {
                    
                }
            };
            assertCompiles(Arrays.asList("-overrides", "doesNotExist.xml"), 
                    new String[]{"bug65xx/bug6592/bug6592.ceylon"}, 
                    expectSysError);
            assertEquals("ERROR:No such overrides file: /home/tom/ceylon/ceylon/compiler-java/doesNotExist.xml", out.toString().trim());
            
            
            out = new ByteArrayOutputStream();
            System.setErr(new PrintStream(out));
            assertCompiles(Arrays.asList("-overrides", "test/src/com/redhat/ceylon/compiler/java/test/issues/bug65xx/bug6592/overrides.xml"),
                    new String[]{"bug65xx/bug6592/bug6592.ceylon"}, 
                    expectSysError);
            assertEquals("ERROR:/home/tom/ceylon/ceylon/compiler-java/test/src/com/redhat/ceylon/compiler/java/test/issues/bug65xx/bug6592/overrides.xml:2:12: Missing 'module' attribute in element [module: null].", out.toString().trim());
        } finally {
            System.setErr(err);
        }
    }
}

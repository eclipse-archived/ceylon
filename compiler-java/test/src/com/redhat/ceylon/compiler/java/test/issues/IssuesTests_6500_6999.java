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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Assume;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.javax.tools.Diagnostic.Kind;
import com.redhat.ceylon.model.cmr.JDKUtils;

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
        compile(Arrays.asList(
                "-apt", "maven:org.netbeans.api:org-netbeans-modules-editor-mimelookup/RELEASE81",
                "-apt", "maven:org.netbeans.api:org-netbeans-modules-csl-api/RELEASE81",
                // this one required due to https://netbeans.org/bugzilla/show_bug.cgi?id=255174
                "-apt", "maven:org.netbeans.modules:org-netbeans-modules-editor-errorstripe/RELEASE81",
                "-target", "8", "-source", "8",
                "-rep", "aether:"+getPackagePath()+"bug65xx/bug6566/settings.xml"), 
                "bug65xx/bug6566/Bug6566.ceylon",
                "bug65xx/bug6566/Bug6566Java.java");
        File archive = getModuleArchive("com.redhat.ceylon.compiler.java.test.issues.bug65xx.bug6566", "1");
        assertTrue(archive.exists());

        try(JarFile car = new JarFile(archive)){
            ZipEntry generatedFile = car.getEntry("META-INF/generated-layer.xml");
            assertNotNull(generatedFile);
        }
    }
    
    @Test
    public void testBug6592() {
        CompilationAssertion expectSysError = new CompilationAssertion() {
            public void onBug(ErrorCollector collector, ExitState exitState) {
                TreeSet<CompilerError> treeSet = collector.get(Kind.ERROR);
                assertEquals(1, treeSet.size());
                CompilerError e = treeSet.iterator().next();
                assertTrue(e.message.contains("No such overrides file: "));
                assertTrue(e.message.contains("doesNotExist.xml"));
            }
        };
        assertCompiles(Arrays.asList("-overrides", "doesNotExist.xml"), 
                new String[]{"bug65xx/bug6592/bug6592.ceylon"}, 
                expectSysError);
        expectSysError = new CompilationAssertion() {
            public void onBug(ErrorCollector collector, ExitState exitState) {
                TreeSet<CompilerError> treeSet = collector.get(Kind.ERROR);
                assertEquals(1, treeSet.size());
                CompilerError e = treeSet.iterator().next();
                assertTrue(e.message.contains("overrides.xml:2:12: Missing 'module' attribute in element [module: null]."));
            }
        };
        assertCompiles(Arrays.asList("-overrides", "test/src/com/redhat/ceylon/compiler/java/test/issues/bug65xx/bug6592/overrides.xml"),
                new String[]{"bug65xx/bug6592/bug6592.ceylon"}, 
                expectSysError);
    }

    @Test
    public void testBug6597() {
        compile(Arrays.asList("-fully-export-maven-dependencies"), 
                "bug65xx/bug6597/foo.ceylon");
    }

    @Test
    public void testBug6606() {
        compareWithJavaSource("bug66xx/Bug6606");
    }

    @Test
    public void testBug6624() {
        compareWithJavaSource("bug66xx/bug6624/Bug6624");
    }
    
    @Test
    public void testBug6660() {
        compareWithJavaSource("bug66xx/Bug6660");
    }

    @Test
    public void testBug6704() {
        compareWithJavaSource(defaultOptions, 1, "bug67xx/Bug6704B.src", "bug67xx/Bug6704.ceylon", "bug67xx/Bug6704B.ceylon");
        compareWithJavaSource(defaultOptions, 0, "bug67xx/Bug6704B.src", "bug67xx/Bug6704B.ceylon", "bug67xx/Bug6704.ceylon");
    }
}

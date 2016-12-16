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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Assume;
import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
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

    @Test
    public void testBug6706() {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compareWithJavaSource(Arrays.asList("-target", "8", "-source", "8"), "bug67xx/Bug6706.src", "bug67xx/Bug6706.ceylon");
    }
    
    @Test
    public void testBug6741() {
        compareWithJavaSource("bug67xx/Bug6741");
        run("com.redhat.ceylon.compiler.java.test.issues.bug67xx.bug6741");
    }
    
    @Test
    public void testBug6746() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug67xx.bug6746", "bug67xx/Bug6746.ceylon");
    }
    
    @Test
    public void testBug6769() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug67xx.bug6769", "bug67xx/Bug6769.ceylon");
    }

    @Test
    public void testBug6774() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug67xx.bug6774", "bug67xx/Bug6774.ceylon");
    }

    @Test
    public void testDynamicMetamodel() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        File classesOutputFolder = new File(destDir+"-jar-classes");
        cleanCars(classesOutputFolder.getPath());
        classesOutputFolder.mkdirs();

        File jarOutputFolder = new File(destDir+"-jar");
        cleanCars(jarOutputFolder.getPath());
        jarOutputFolder.mkdirs();

        compileJavaModule(jarOutputFolder, classesOutputFolder, 
                moduleName+".bug67xx.dyn.a", "1", 
                new File(dir), new File[0], 
                moduleName.replace('.', '/')+"/bug67xx/dyn/a/A.java");

        File mavenRepoFolder = new File(destDir+"-maven-repo");
        cleanCars(mavenRepoFolder.getPath());
        mavenRepoFolder.mkdirs();
        File mavenRepoTarget = new File(mavenRepoFolder, moduleName.replace('.', '/')+"/bug67xx/dyn/a/1");
        mavenRepoTarget.mkdirs();
        
        File jarSrc = new File(jarOutputFolder, moduleName.replace('.', '/')+"/bug67xx/dyn/a/1/"+moduleName+".bug67xx.dyn.a-1.jar");
        File jarDst = new File(mavenRepoTarget, "a-1.jar");
        Files.copy(jarSrc.toPath(), jarDst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        File pomSrc = new File(getPackagePath()+"bug67xx/dyn/a/pom.xml");
        File pomDst = new File(mavenRepoTarget, "a-1.pom");
        Files.copy(pomSrc.toPath(), pomDst.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        compile(Arrays.asList("-rep", "aether:"+getPackagePath()+"/bug67xx/dyn/b/settings.xml"), 
                "bug67xx/dyn/b/B.ceylon");
        
        File carFile = new File(destDir+"/"+moduleName.replace('.', '/')+"/bug67xx/dyn/b/1/"+moduleName+".bug67xx.dyn.b-1.car");

        cleanCars(classesOutputFolder.getPath());
        classesOutputFolder.mkdirs();

        compileJavaModule(jarOutputFolder, classesOutputFolder, 
                moduleName+".bug67xx.dyn.c", "1", 
                new File(dir), new File[]{
                        carFile, 
                        jarDst,
                        new File(LANGUAGE_MODULE_CAR),
                }, 
                moduleName.replace('.', '/')+"/bug67xx/dyn/c/C.java");

        File jarCSrc = new File(jarOutputFolder, moduleName.replace('.', '/')+"/bug67xx/dyn/c/1/"+moduleName+".bug67xx.dyn.c-1.jar");

        URLClassLoader cl = new URLClassLoader(new URL[]{
                carFile.toURL(),
                jarDst.toURL(),
                jarCSrc.toURL(),
        }, getClass().getClassLoader());
        Class<?> klass = Class.forName(moduleName+".bug67xx.dyn.c.C", true, cl);
        Method method = klass.getMethod("main", String[].class);
        Metamodel.getModuleManager().getModelLoader().setDefaultClassLoader(cl);
        method.invoke(null, (Object)new String[]{});
    }
}

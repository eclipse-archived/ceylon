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
package org.eclipse.ceylon.compiler.java.test.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.ceylon.compiler.java.launcher.Main.ExitState;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.test.ErrorCollector;
import org.eclipse.ceylon.javax.tools.Diagnostic.Kind;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.junit.Assume;
import org.junit.Test;

public class IssuesTests_6500_6999 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues", "1");
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
        File archive = getModuleArchive("org.eclipse.ceylon.compiler.java.test.issues.bug65xx.bug6566", "1");
        assertTrue(archive.exists());

        try(JarFile car = new JarFile(archive)){
            ZipEntry generatedFile = car.getEntry("META-INF/generated-layer.xml");
            assertNotNull(generatedFile);
        }
    }
    
    @Test
    public void testBug6592() {
        CompilationAssertion expectSysError = new CompilationAssertion() {
            public void onError(ErrorCollector collector, ExitState exitState) {
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
            public void onError(ErrorCollector collector, ExitState exitState) {
                TreeSet<CompilerError> treeSet = collector.get(Kind.ERROR);
                assertEquals(1, treeSet.size());
                CompilerError e = treeSet.iterator().next();
                assertTrue(e.message.contains("overrides.xml:2:12: Missing 'module' attribute in element [module: null]."));
            }
        };
        assertCompiles(Arrays.asList("-overrides", "test/src/org/eclipse/ceylon/compiler/java/test/issues/bug65xx/bug6592/overrides.xml"),
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
    public void testBug6682() {
        compile("bug66xx/Bug6682Java.java"); 
        compile("bug66xx/Bug6682.ceylon");
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
    public void testBug6719() {
        compareWithJavaSource("bug67xx/Bug6719");
        run("org.eclipse.ceylon.compiler.java.test.issues.bug67xx.bug6719");
    }
    
    @Test
    public void testBug6725() {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compareWithJavaSource("bug67xx/Bug6725");
    }
    
    @Test
    public void testBug6741() {
        compareWithJavaSource("bug67xx/Bug6741");
        run("org.eclipse.ceylon.compiler.java.test.issues.bug67xx.bug6741");
    }
    
    @Test
    public void testBug6746() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug67xx.bug6746", "bug67xx/Bug6746.ceylon");
    }

    @Test
    public void testBug6782() {
        compareWithJavaSource("bug67xx/Bug6782");
    }

    @Test
    public void testBug6747() {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compile("bug67xx/Bug6747Java.java");
        compareWithJavaSource("bug67xx/Bug6747");
    }

    @Test
    public void testBug6769() {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug67xx.bug6769", "bug67xx/Bug6769.ceylon");
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
        
        File settingsFile = File.createTempFile("settings", ".xml");
        Files.write(settingsFile.toPath(), ("<settings xmlns=\"http://maven.apache.org/SETTINGS/1.0.0\"\n"+
                "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"+
                "  xsi:schemaLocation=\"http://maven.apache.org/SETTINGS/1.0.0\n"+
                "                      http://maven.apache.org/xsd/settings-1.0.0.xsd\">\n"+
                " <profiles>\n"+
                "  <profile>\n"+
                "   <id>myprofile</id>\n"+
                "   <repositories>\n"+
                "    <repository>\n"+
                "     <id>nb-maven-repo</id>\n"+
                "     <name>Maven Repository for NetBeans Modules</name>\n"+
                "     <url>file://"+mavenRepoFolder.getAbsolutePath()+"</url>\n"+
                "    </repository>\n"+
                "   </repositories>\n"+
                "  </profile>\n"+
                " </profiles>\n"+
                " <activeProfiles>\n"+
                "  <activeProfile>myprofile</activeProfile>\n"+
                " </activeProfiles>\n"+
                "</settings>\n").getBytes("UTF-8"));
        try{
            compile(Arrays.asList("-rep", "aether:"+settingsFile.getAbsolutePath()), 
                    "bug67xx/dyn/b/B.ceylon");
        }finally{
            settingsFile.delete();
        }
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
        synchronized(RUN_LOCK){
            Metamodel.resetModuleManager();
            Class<?> klass = Class.forName(moduleName+".bug67xx.dyn.c.C", true, cl);
            Method method = klass.getMethod("main", String[].class);
            Metamodel.getModuleManager().getModelLoader().setDefaultClassLoader(cl);
            method.invoke(null, (Object)new String[]{});
            Metamodel.getModuleManager().getModelLoader().setDefaultClassLoader(null);
        }
    }

    @Test
    public void testBug6846() throws Throwable {
        compile("bug68xx/bug6846/Bug6846.ceylon");
        runInJBossModulesSameVM("run", "org.eclipse.ceylon.compiler.java.test.issues.bug68xx.bug6846/1", 
                Arrays.asList("--auto-export-maven-dependencies"));
    }

    @Test
    public void testBug6840() throws Throwable {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug68xx.Bug6840",
                "bug68xx/Bug6840.ceylon");
    }

    @Test
    public void testBug6855() throws Throwable {
        compile("bug68xx/Bug6855Java.java");
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug68xx.bug6855",
                "bug68xx/Bug6855.ceylon");
    }

    @Test
    public void testBug6884() throws Throwable {
        compile(Arrays.asList("-res", "test/src/org/eclipse/ceylon/compiler/java/test/issues/bug68xx/bug6884resources"), 
                "bug68xx/bug6884/test.ceylon",
                "bug68xx/bug6884resources/org/eclipse/ceylon/compiler/java/test/issues/bug68xx/bug6884/test.txt");
        run("org.eclipse.ceylon.compiler.java.test.issues.bug68xx.bug6884.run",
                true,
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues.bug68xx.bug6884", "1"));
    }

    @Test
    public void testBug6909() throws Throwable {
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug69xx.bug6909",
                "bug69xx/Bug6909.ceylon");
    }
    
    @Test
    public void testBug6910() throws Throwable {
        compareWithJavaSource("bug69xx/Bug6910");
    }
    
    @Test
    public void testBug6916() throws Throwable {
        assertErrors("bug69xx/bug6916/Bug6916", 
                Arrays.asList("-flat-classpath"),
                null,
                new CompilerError(5, "Failed to determine if getConverter is overriding a super method: class file for org.apache.wicket.util.convert.IConverter not found"),
                new CompilerError(5, "formal member 'canCallListenerInterfaceAfterExpiry' of 'IRequestableComponent' not implemented for concrete class 'HelloWorld': 'HelloWorld' neither directly implements nor inherits a concrete implementation of 'canCallListenerInterfaceAfterExpiry'"),
                new CompilerError(5, "formal member 'equals' of 'Object' not implemented for concrete class 'HelloWorld': 'HelloWorld' neither directly implements nor inherits a concrete implementation of 'equals'"),
                new CompilerError(5, "formal member 'getBehaviorById' of 'IRequestableComponent' not implemented for concrete class 'HelloWorld': 'HelloWorld' neither directly implements nor inherits a concrete implementation of 'getBehaviorById'"),
                new CompilerError(5, "formal member 'getBehaviorId' of 'IRequestableComponent' not implemented for concrete class 'HelloWorld': 'HelloWorld' neither directly implements nor inherits a concrete implementation of 'getBehaviorId'"),
                new CompilerError(5, "formal member 'hash' of 'Object' not implemented for concrete class 'HelloWorld': 'HelloWorld' neither directly implements nor inherits a concrete implementation of 'hash'")
                );
        compile(Arrays.asList("-fully-export-maven-dependencies"), "bug69xx/bug6916/Bug6916.ceylon");
    }

    @Test
    public void testBug6930() throws Throwable {
        compile("bug69xx/Bug6930Java.java");
        compareWithJavaSource("bug69xx/Bug6930");
    }

    @Test
    public void testBug6947() throws Throwable {
        compareWithJavaSource("bug69xx/Bug6947");
    }

    @Test
    public void testBug6949() throws Throwable {
        compareWithJavaSource("bug69xx/Bug6949");
    }

    @Test
    public void testBug6957() {
        compareWithJavaSource("bug69xx/Bug6957");
    }

    @Test
    public void testBug6959() throws Throwable {
        compileAndRun(Arrays.asList("-res", "test/src/org/eclipse/ceylon/compiler/java/test/issues/bug69xx/bug6959resources"), 
                "org.eclipse.ceylon.compiler.java.test.issues.bug69xx.bug6959", 
                "bug69xx/Bug6959.ceylon",
                "bug69xx/bug6959resources/org/eclipse/ceylon/compiler/java/test/issues/bug69xx/bug6959.properties");
    }

    @Test
    public void testBug6963() throws Throwable {
        compile("bug69xx/Bug6963Java.java");
        compareWithJavaSource("bug69xx/Bug6963");
    }

    @Test
    public void testBug6969() throws Throwable {
        compile("bug69xx/Bug6969.ceylon", "bug69xx/Bug6969Java.java");
    }

    @Test
    public void testBug6970() throws Throwable {
        compile("bug69xx/Bug6970.ceylon", "bug69xx/Bug6970Java.java");
    }

    @Test
    public void testBug6971() throws Throwable {
        compile("bug69xx/Bug6971Java.java");
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug69xx.bug6971", 
                "bug69xx/Bug6971.ceylon");
    }

    @Test
    public void testBug6974() throws Throwable {
        compile("bug69xx/Bug6974.ceylon");
    }

    @Test
    public void testBug6982() throws Throwable {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compile("bug69xx/Bug6982Java.java");
        compileAndRun("org.eclipse.ceylon.compiler.java.test.issues.bug69xx.bug6982", 
                "bug69xx/Bug6982.ceylon");
    }

    @Test
    public void testBug6997() throws Throwable {
        compareWithJavaSource("bug69xx/Bug6997");
    }
}

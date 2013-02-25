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
package com.redhat.ceylon.compiler.java.test.cmr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.util.Util;

public class CMRTest extends CompilerTest {
    
    //
    // Modules
    
    @Test
    public void testMdlByName() throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath()+"/modules/byName");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("default", "mod"));
        Boolean ret = task.call();
        assertTrue(ret);

        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("def/Foo.class");
        assertNotNull(moduleClass);
        car.close();

        carFile = getModuleArchive("mod", "1");
        assertTrue(carFile.exists());

        car = new JarFile(carFile);

        moduleClass = car.getEntry("mod/module_.class");
        assertNotNull(moduleClass);
        car.close();
    }

    @Test
    public void testMdlEndsWithJava() throws IOException{
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(dir);
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("com.redhat.ceylon.compiler.java.test.cmr.modules.java"));
        Boolean ret = task.call();
        assertTrue(ret);
    }

    @Test
    public void testMdlModuleDefault() throws IOException{
        compile("modules/def/CeylonClass.ceylon");
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/def/CeylonClass.class");
        assertNotNull(moduleClass);
        car.close();
    }

    @Test
    public void testMdlModuleDefaultJavaFile() throws IOException{
        compile("modules/def/JavaClass.java");
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/def/JavaClass.class");
        assertNotNull(moduleClass);
        car.close();
    }

    @Test
    public void testMdlModuleDefaultIncremental() throws IOException{
        compile("modules/def/A.ceylon");
        compile("modules/def/RequiresA.ceylon");
    }

    @Test
    public void testMdlModuleOnlyInOutputRepo() throws IOException {
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        assertFalse(carFile.exists());

        String cacheDir = Repositories.get().getCacheRepository().getUrl();
        File carFileInCache = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", cacheDir);
        if(carFileInCache.exists())
            carFileInCache.delete();
        
        compile("modules/single/module.ceylon");

        // make sure it was created in the output repo
        assertTrue(carFile.exists());
        // make sure it wasn't created in the cache repo
        assertFalse(carFileInCache.exists());
    }

    @Test
    public void testMdlModuleNotLoadedFromHomeRepo() throws IOException {
        File carFile = getModuleArchive("a", "1.0");
        assertFalse(carFile.exists());

        // clean up the home repo if required
        String homeRepo = FileUtil.getUserDir().getCanonicalPath();
        File carFileInHomeRepo = getModuleArchive("a", "1.0", homeRepo);
        if(carFileInHomeRepo.exists())
            carFileInHomeRepo.delete();

        // put a broken one in the home repo
        compileModuleFromSourceFolder("a", "home_repo/a_broken", homeRepo);
        assertTrue(carFileInHomeRepo.exists());

        // the good one in the default output repo
        compileModuleFromSourceFolder("a", "home_repo/a_working", null);
        assertTrue(carFile.exists());

        // now compile the dependent module
        compileModuleFromSourceFolder("b", "home_repo/b", null);

        // make sure it was created in the output repo
        assertTrue(carFile.exists());
    }

    @Test
    public void testMdlModuleNotLoadedFromCache() throws IOException {
        File carFile = getModuleArchive("b", "1.0");
        assertFalse(carFile.exists());

        // clean up the cache repo if required
        String cacheRepo = Repositories.get().getCacheRepository().getUrl();
        File carFileInHomeRepo = getModuleArchive("a", "1.0", cacheRepo);
        if(carFileInHomeRepo.exists())
            carFileInHomeRepo.delete();

        // clean up the working repo if required
        String workingRepo = destDir + "-working";
        File carFileInWorkingRepo = getModuleArchive("a", "1.0", workingRepo);
        if(carFileInWorkingRepo.exists())
            carFileInWorkingRepo.delete();
        assertFalse(carFileInWorkingRepo.exists());
        
        // put a broken one in the cache repo
        compileModuleFromSourceFolder("a", "home_repo/a_broken", cacheRepo);
        assertTrue(carFileInHomeRepo.exists());

        // the good one in a local repo
        compileModuleFromSourceFolder("a", "home_repo/a_working", workingRepo);
        assertTrue(carFileInWorkingRepo.exists());

        // now compile the dependent module by using that repo
        compileModuleFromSourceFolder("b", "home_repo/b", null, workingRepo);

        // make sure it was created in the output repo
        assertTrue(carFile.exists());
    }
    
    private void compileModuleFromSourceFolder(String module, String srcFolder, String outFolder, String... repos) {
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath()+"/modules/"+srcFolder);
        if(outFolder != null){
            options.add("-out");
            options.add(outFolder);
        }else{
            options.addAll(defaultOptions);
        }
        for(String repo : repos){
            options.add("-rep");
            options.add(repo);
        }
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList(module));
        Boolean ret = task.call();
        assertTrue(ret);
    }

    @Test
    public void testMdlWithCeylonImport() throws IOException{
        compile("modules/ceylon_import/module.ceylon", "modules/ceylon_import/ImportCeylonLanguage.ceylon");
    }
    
    @Test
    public void testMdlWithCommonPrefix() throws IOException{
        compile("modules/depend/prefix/module.ceylon");
        // This is heisenbug https://github.com/ceylon/ceylon-compiler/issues/460 and for some
        // reason it only happens _sometimes_, hence the repeats
        compile("modules/depend/prefix_suffix/module.ceylon");
        compile("modules/depend/prefix_suffix/module.ceylon");
        compile("modules/depend/prefix_suffix/module.ceylon");
        compile("modules/depend/prefix_suffix/module.ceylon");
        compile("modules/depend/prefix_suffix/module.ceylon");
    }
    
    @Test
    public void testMdlModuleFromCompiledModule() throws IOException{
        compile("modules/single/module.ceylon");
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);
        // just to be sure
        ZipEntry bogusEntry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/BOGUS");
        assertNull(bogusEntry);

        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/module_.class");
        assertNotNull(moduleClass);
        car.close();

        compile("modules/single/subpackage/Subpackage.ceylon");

        // MUST reopen it
        car = new JarFile(carFile);

        ZipEntry subpackageClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/subpackage/Subpackage.class");
        assertNotNull(subpackageClass);

        car.close();
    }

    @Test
    public void testMdlCarWithInvalidSHA1() throws IOException{
        compile("modules/single/module.ceylon");
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);
        // just to be sure
        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/module_.class");
        assertNotNull(moduleClass);
        car.close();

        // now let's break the SHA1
        File shaFile = getArchiveName("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", destDir, "car.sha1");
        Writer w = new FileWriter(shaFile);
        w.write("fubar");
        w.flush();
        w.close();
        
        // now try to compile the subpackage with a broken SHA1
        assertErrors("modules/single/subpackage/Subpackage", 
                new CompilerError(-1, "Module car /com/redhat/ceylon/compiler/java/test/cmr/modules/single/6.6.6/com.redhat.ceylon.compiler.java.test.cmr.modules.single-6.6.6.car"
                        + " obtained from repository " + destDir 
                        + " has an invalid SHA1 signature: you need to remove it and rebuild the archive, since it may be corrupted."));
    }

    @Test
    public void testMdlCompilerGeneratesModuleForValidUnits() throws IOException{
        CeyloncTaskImpl compilerTask = getCompilerTask("modules/single/module.ceylon", "modules/single/Correct.ceylon", "modules/single/Invalid.ceylon");
        Boolean success = compilerTask.call();
        assertFalse(success);
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/module_.class");
        assertNotNull(moduleClass);

        ZipEntry correctClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/Correct.class");
        assertNotNull(correctClass);

        ZipEntry invalidClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/Invalid.class");
        assertNull(invalidClass);
        
        car.close();
    }

    @Test
    public void testMdlInterdepModule(){
        // first compile it all from source
        compile("modules/interdep/a/module.ceylon", "modules/interdep/a/package.ceylon", "modules/interdep/a/b.ceylon", "modules/interdep/a/A.ceylon",
                "modules/interdep/b/module.ceylon", "modules/interdep/b/package.ceylon", "modules/interdep/b/a.ceylon", "modules/interdep/b/B.ceylon");
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.interdep.a", "6.6.6");
        assertTrue(carFile.exists());

        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.interdep.b", "6.6.6");
        assertTrue(carFile.exists());
        
        // then try to compile only one module (the other being loaded from its car) 
        compile("modules/interdep/a/module.ceylon", "modules/interdep/a/b.ceylon", "modules/interdep/a/A.ceylon");
    }

    @Test
    public void testMdlDependentModule(){
        // Compile only the first module 
        compile("modules/depend/a/module.ceylon", "modules/depend/a/package.ceylon", "modules/depend/a/A.ceylon");
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a", "6.6.6");
        assertTrue(carFile.exists());

        // then try to compile only one module (the other being loaded from its car) 
        compile("modules/depend/b/module.ceylon", "modules/depend/b/a.ceylon", "modules/depend/b/aWildcard.ceylon");

        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.depend.b", "6.6.6");
        assertTrue(carFile.exists());
    }

    @Test
    public void testMdlImplicitDependentModule(){
        // Compile only the first module 
        compile("modules/implicit/a/module.ceylon", "modules/implicit/a/package.ceylon", "modules/implicit/a/A.ceylon",
                "modules/implicit/b/module.ceylon", "modules/implicit/b/package.ceylon", "modules/implicit/b/B.ceylon", "modules/implicit/b/B2.ceylon",
                "modules/implicit/c/module.ceylon", "modules/implicit/c/package.ceylon", "modules/implicit/c/c.ceylon");
        
        // Dependencies:
        //
        // c.ceylon--> B2.ceylon
        //         |
        //         '-> B.ceylon  --> A.ceylon

        // Successfull tests :
        
        compile("modules/implicit/c/c.ceylon");
        compile("modules/implicit/b/B.ceylon", "modules/implicit/c/c.ceylon");
        compile("modules/implicit/b/B2.ceylon", "modules/implicit/c/c.ceylon");
        
        // Failing tests :
        
        Boolean success1 = getCompilerTask("modules/implicit/c/c.ceylon", "modules/implicit/b/B.ceylon").call();
        // => B.ceylon : package not found in dependent modules: com.redhat.ceylon.compiler.java.test.cmr.module.implicit.a
        Boolean success2 = getCompilerTask("modules/implicit/c/c.ceylon", "modules/implicit/b/B2.ceylon").call();
        // => c.ceylon : TypeVisitor caused an exception visiting Import node: com.sun.tools.javac.code.Symbol$CompletionFailure: class file for com.redhat.ceylon.compiler.test.cmr.module.implicit.a.A not found at unknown

        Assert.assertTrue(success1 && success2);
    }

    private void copy(File source, File dest) throws IOException {
        InputStream inputStream = new FileInputStream(source);
        OutputStream outputStream = new FileOutputStream(dest); 
        byte[] buffer = new byte[4096];
        int read;
        while((read = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, read);
        }
        inputStream.close();
        outputStream.close();
    }
    
    @Test
    public void testMdlSuppressObsoleteClasses() throws IOException{
        File sourceFile = new File(getPackagePath(), "modules/single/SuppressClass.ceylon");

        copy(new File(getPackagePath(), "modules/single/SuppressClass_1.ceylon"), sourceFile);
        CeyloncTaskImpl compilerTask = getCompilerTask("modules/single/module.ceylon", "modules/single/SuppressClass.ceylon");
        Boolean success = compilerTask.call();
        assertTrue(success);

        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        assertTrue(carFile.exists());
        ZipFile car = new ZipFile(carFile);
        ZipEntry oneClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/One.class");
        assertNotNull(oneClass);
        ZipEntry twoClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/Two.class");
        assertNotNull(twoClass);
        car.close();

        copy(new File(getPackagePath(), "modules/single/SuppressClass_2.ceylon"), sourceFile);
        compilerTask = getCompilerTask("modules/single/module.ceylon", "modules/single/SuppressClass.ceylon");
        success = compilerTask.call();
        assertTrue(success);
        
        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        assertTrue(carFile.exists());
        car = new ZipFile(carFile);
        oneClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/One.class");
        assertNotNull(oneClass);
        twoClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/Two.class");
        assertNull(twoClass);
        car.close();
        
        sourceFile.delete();
    }

    
    @Test
    public void testMdlMultipleRepos(){
        cleanCars("build/ceylon-cars-a");
        cleanCars("build/ceylon-cars-b");
        cleanCars("build/ceylon-cars-c");
        
        // Compile the first module in its own repo 
        File repoA = new File("build/ceylon-cars-a");
        repoA.mkdirs();
        Boolean result = getCompilerTask(Arrays.asList("-out", repoA.getPath()),
                "modules/depend/a/module.ceylon", "modules/depend/a/package.ceylon", "modules/depend/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a", "6.6.6", repoA.getPath());
        assertTrue(carFile.exists());

        // make another repo for the second module
        File repoB = new File("build/ceylon-cars-b");
        repoB.mkdirs();

        // then try to compile only one module (the other being loaded from its car) 
        result = getCompilerTask(Arrays.asList("-out", repoB.getPath(), "-rep", repoA.getPath()),
                "modules/depend/b/module.ceylon", "modules/depend/b/package.ceylon", "modules/depend/b/a.ceylon", "modules/depend/b/B.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.depend.b", "6.6.6", repoB.getPath());
        assertTrue(carFile.exists());

        // make another repo for the third module
        File repoC = new File("build/ceylon-cars-c");
        repoC.mkdirs();

        // then try to compile only one module (the others being loaded from their car) 
        result = getCompilerTask(Arrays.asList("-out", repoC.getPath(), 
                "-rep", repoA.getPath(), "-rep", repoB.getPath()),
                "modules/depend/c/module.ceylon", "modules/depend/c/a.ceylon", "modules/depend/c/b.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.depend.c", "6.6.6", repoC.getPath());
        assertTrue(carFile.exists());
    }

    @Test
    public void testMdlJarDependency() throws IOException{
        // compile our java class
        File outputFolder = new File("build/java-jar");
        cleanCars(outputFolder.getPath());
        outputFolder.mkdirs();
        
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null);
        File javaFile = new File(getPackagePath()+"/modules/jarDependency/java/JavaDependency.java");
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(javaFile);
        CompilationTask task = javaCompiler.getTask(null, null, null, Arrays.asList("-d", "build/java-jar", "-sourcepath", "test-src"), null, compilationUnits);
        assertEquals(Boolean.TRUE, task.call());
        
        File jarFolder = new File(outputFolder, "com/redhat/ceylon/compiler/java/test/cmr/modules/jarDependency/java/1.0/");
        jarFolder.mkdirs();
        File jarFile = new File(jarFolder, "com.redhat.ceylon.compiler.java.test.cmr.modules.jarDependency.java-1.0.jar");
        // now jar it up
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        ZipEntry entry = new ZipEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/jarDependency/java/JavaDependency.class");
        outputStream.putNextEntry(entry);
        
        FileInputStream inputStream = new FileInputStream(javaFile);
        Util.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

        // Try to compile the ceylon module
        CeyloncTaskImpl ceylonTask = getCompilerTask(Arrays.asList("-out", destDir, "-rep", outputFolder.getPath()), 
                (DiagnosticListener<? super FileObject>)null, 
                "modules/jarDependency/ceylon/module.ceylon", "modules/jarDependency/ceylon/Foo.ceylon");
        assertEquals(Boolean.TRUE, ceylonTask.call());
    }

    // fails due to https://github.com/ceylon/ceylon-spec/issues/369
    @Test
    public void testMdlMavenDependency() throws IOException{
        // Try to compile the ceylon module
        CeyloncTaskImpl ceylonTask = getCompilerTask(Arrays.asList("-out", destDir, "-rep", "mvn:http://repo1.maven.org/maven2", "-verbose:cmr"), 
                (DiagnosticListener<? super FileObject>)null, 
                "modules/maven/module.ceylon", "modules/maven/foo.ceylon");
        assertEquals(Boolean.TRUE, ceylonTask.call());
    }

    @Test
    public void testMdlAetherDependencyDefault() throws IOException{
        // Try to compile the ceylon module
        CeyloncTaskImpl ceylonTask = getCompilerTask(Arrays.asList("-out", destDir, "-rep", "aether", "-verbose:cmr"), 
                (DiagnosticListener<? super FileObject>)null, 
                "modules/aetherdefault/module.ceylon", "modules/aetherdefault/foo.ceylon");
        assertEquals(Boolean.TRUE, ceylonTask.call());
        // We're assuming a standard Maven configuration here!
        File camelJar = new File(System.getProperty("user.home"), ".m2/repository/org/apache/camel/camel-core/2.9.2/camel-core-2.9.2.jar");
        assertTrue(camelJar.exists());
        File slf4jJar = new File(System.getProperty("user.home"), ".m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar");
        assertTrue(slf4jJar.exists());
    }

    @Test
    public void testMdlAetherIgnoreRecursiveDependencies() throws IOException{
        // Try to compile the ceylon module
        CeyloncTaskImpl ceylonTask = getCompilerTask(Arrays.asList("-out", destDir, "-rep", "aether", "-verbose:cmr"), 
                (DiagnosticListener<? super FileObject>)null, 
                "modules/aetherIgnoreDependencies/module.ceylon", "modules/aetherIgnoreDependencies/foo.ceylon");
        assertEquals(Boolean.TRUE, ceylonTask.call());
        // We're assuming a standard Maven configuration here!
        File camelJar = new File(System.getProperty("user.home"), ".m2/repository/org/apache/camel/camel-core/2.9.4/camel-core-2.9.4.jar");
        assertTrue(camelJar.exists());
        File camelJettyJar = new File(System.getProperty("user.home"), ".m2/repository/org/apache/camel/camel-jetty/2.9.4/camel-jetty-2.9.4.jar");
        assertTrue(camelJettyJar.exists());
    }

    @Test
    public void testMdlAetherDependencyCustom() throws IOException{
        // Try to compile the ceylon module
        File settingsFile = new File(getPackagePath(), "modules/aethercustom/settings.xml");
        CeyloncTaskImpl ceylonTask = getCompilerTask(Arrays.asList("-out", destDir, "-rep", "aether:" + settingsFile.getAbsolutePath(), "-verbose:cmr"), 
                (DiagnosticListener<? super FileObject>)null, 
                "modules/aethercustom/module.ceylon", "modules/aethercustom/foo.ceylon");
        assertEquals(Boolean.TRUE, ceylonTask.call());
        File restletJar = new File("build/test-cars/cmr-repository", "org/restlet/org.restlet/1.1.10/org.restlet-1.1.10.jar");
        assertTrue(restletJar.exists());
    }

    @Test
    public void testMdlSourceArchive() throws IOException{
        File sourceArchiveFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        sourceArchiveFile.delete();
        assertFalse(sourceArchiveFile.exists());

        // compile one file
        compile("modules/single/module.ceylon");

        // make sure it was created
        assertTrue(sourceArchiveFile.exists());

        JarFile sourceArchive = new JarFile(sourceArchiveFile);
        assertEquals(1, countEntries(sourceArchive));

        ZipEntry moduleClass = sourceArchive.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/module.ceylon");
        assertNotNull(moduleClass);
        sourceArchive.close();

        // now compile another file
        compile("modules/single/subpackage/Subpackage.ceylon");

        // MUST reopen it
        sourceArchive = new JarFile(sourceArchiveFile);
        assertEquals(2, countEntries(sourceArchive));

        ZipEntry subpackageClass = sourceArchive.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/subpackage/Subpackage.ceylon");
        assertNotNull(subpackageClass);
        sourceArchive.close();
    }

    @Test
    public void testMdlMultipleVersionsOnSameCompilation(){
        // Compile module A/1
        Boolean result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a1"),
                "modules/multiversion/a1/a/module.ceylon", "modules/multiversion/a1/a/package.ceylon", "modules/multiversion/a1/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        ErrorCollector collector = new ErrorCollector();
        // Compile module A/2 with B importing A/1
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a2:"+getPackagePath()+"/modules/multiversion/b"),
                collector,
                "modules/multiversion/a2/a/module.ceylon", "modules/multiversion/a2/a/package.ceylon", "modules/multiversion/a2/a/A.ceylon",
                "modules/multiversion/b/b/module.ceylon", "modules/multiversion/b/b/B.ceylon").call();
        Assert.assertEquals(Boolean.FALSE, result);
        
        compareErrors(collector.get(Diagnostic.Kind.ERROR), 
                new CompilerError(20, "Trying to import or compile two different versions of the same module: a (1 and 2)"));
    }

    @Test
    public void testMdlMultipleVersionsDuringImport(){
        // Compile module A/1
        Boolean result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a1"),
                "modules/multiversion/a1/a/module.ceylon", "modules/multiversion/a1/a/package.ceylon", "modules/multiversion/a1/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module A/2
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a2"),
                "modules/multiversion/a2/a/module.ceylon", "modules/multiversion/a2/a/package.ceylon", "modules/multiversion/a2/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        ErrorCollector collector = new ErrorCollector();
        // Compile module cImportsATwice which imports both A/1 and A/2
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/c"),
                collector,
                "modules/multiversion/c/cImportsATwice/module.ceylon", "modules/multiversion/c/cImportsATwice/C.ceylon").call();
        Assert.assertEquals(Boolean.FALSE, result);
        
        compareErrors(collector.get(Diagnostic.Kind.ERROR), 
                new CompilerError(20, "Module (transitively) imports conflicting versions of a. Version 2 and version 1 found and visible at the same time."),
                new CompilerError(20, "Trying to import or compile two different versions of the same module: a (1 and 2)"),
                new CompilerError(20, "Trying to import or compile two different versions of the same module: a (2 and 1)")
        );
    }

    @Test
    public void testMdlMultipleVersionsDuringDependencyImport(){
        // Compile module A/1
        Boolean result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a1"),
                "modules/multiversion/a1/a/module.ceylon", "modules/multiversion/a1/a/package.ceylon", "modules/multiversion/a1/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module A/2
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a2"),
                "modules/multiversion/a2/a/module.ceylon", "modules/multiversion/a2/a/package.ceylon", "modules/multiversion/a2/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module B/1
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/b"),
                "modules/multiversion/b/b/module.ceylon", "modules/multiversion/b/b/package.ceylon", "modules/multiversion/b/b/B.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module cImportsABIndirectlyOK which imports both A/1 and A/2
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/c"),
                "modules/multiversion/c/cImportsABIndirectlyOK/module.ceylon", "modules/multiversion/c/cImportsABIndirectlyOK/C.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testMdlMultipleVersionsDuringImplicitImport(){
        // Compile module A/1
        Boolean result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a1"),
                "modules/multiversion/a1/a/module.ceylon", "modules/multiversion/a1/a/package.ceylon", "modules/multiversion/a1/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module A/2
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/a2"),
                "modules/multiversion/a2/a/module.ceylon", "modules/multiversion/a2/a/package.ceylon", "modules/multiversion/a2/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module bExportsA1/1
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/b"),
                "modules/multiversion/b/bExportsA1/module.ceylon", "modules/multiversion/b/bExportsA1/package.ceylon", "modules/multiversion/b/bExportsA1/B.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        // Compile module cImportsABIndirectlyFail which imports both A/1 and A/2
        ErrorCollector collector = new ErrorCollector();
        result = getCompilerTask(Arrays.asList("-src", getPackagePath()+"/modules/multiversion/c"),
                collector,
                "modules/multiversion/c/cImportsABIndirectlyFail/module.ceylon", "modules/multiversion/c/cImportsABIndirectlyFail/C.ceylon").call();
        Assert.assertEquals(Boolean.FALSE, result);
        
        compareErrors(collector.get(Diagnostic.Kind.ERROR),
                new CompilerError(20, "Module (transitively) imports conflicting versions of a. Version 1 and version 2 found and visible at the same time."),
                new CompilerError(20, "Trying to import or compile two different versions of the same module: a (1 and 2)")
        );
    }

    
    private int countEntries(JarFile jar) {
        int count = 0;
        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            count++;
            entries.nextElement();
        }
        return count;
    }

    @Test
    public void testMdlSha1Signatures() throws IOException{
        File sourceArchiveFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        File sourceArchiveSignatureFile = new File(sourceArchiveFile.getPath()+".sha1");
        File moduleArchiveFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6");
        File moduleArchiveSignatureFile = new File(moduleArchiveFile.getPath()+".sha1");
        // cleanup
        sourceArchiveFile.delete();
        sourceArchiveSignatureFile.delete();
        moduleArchiveFile.delete();
        moduleArchiveSignatureFile.delete();
        // safety check
        assertFalse(sourceArchiveFile.exists());
        assertFalse(sourceArchiveSignatureFile.exists());
        assertFalse(moduleArchiveFile.exists());
        assertFalse(moduleArchiveSignatureFile.exists());

        // compile one file
        compile("modules/single/module.ceylon");

        // make sure everything was created
        assertTrue(sourceArchiveFile.exists());
        assertTrue(sourceArchiveSignatureFile.exists());
        assertTrue(moduleArchiveFile.exists());
        assertTrue(moduleArchiveSignatureFile.exists());

        // check the signatures vaguely
        checkSha1(sourceArchiveSignatureFile);
        checkSha1(moduleArchiveSignatureFile);
    }

    private void checkSha1(File signatureFile) throws IOException {
        Assert.assertEquals(40, signatureFile.length());
        FileInputStream reader = new FileInputStream(signatureFile);
        byte[] bytes = new byte[40];
        Assert.assertEquals(40, reader.read(bytes));
        reader.close();
        char[] sha1 = new String(bytes, "ASCII").toCharArray();
        for (int i = 0; i < sha1.length; i++) {
            char c = sha1[i];
            Assert.assertTrue((c >= '0' && c <= '9')
                    || (c >= 'a' && c <= 'f')
                    || (c >= 'A' && c <= 'F'));
        }
    }

    @Test
    public void testMdlJdkBaseModule() throws IOException{
        compile("modules/jdk/appletBroken/Foo.ceylon");
    }
}

package com.redhat.ceylon.compiler.java.runtime;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Ignore;
import org.junit.Test;
//
//import com.redhat.ceylon.cmr.impl.IOUtils;
//import com.redhat.ceylon.common.FileUtil;
//import com.redhat.ceylon.compiler.java.runtime.Main.ClassPath;
//import com.redhat.ceylon.compiler.java.runtime.Main.ClassPath.Module;
//import com.redhat.ceylon.compiler.java.runtime.Main.ClassPath.ModuleNotFoundException;

public class MainTest {
//
//    @Test
//    public void testCeylonModule() throws IOException, ModuleNotFoundException{
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
//        File moduleFile = new File("test-jvm/foo/foo", "module_.java");
//        Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(moduleFile);
//        File destDir = new File("build/mainTest");
//        FileUtil.delete(destDir);
//        destDir.mkdirs();
//        CompilationTask task = compiler.getTask(null, null, null, Arrays.asList("-d", destDir.getPath()), null, units);
//        Boolean result = task.call();
//        assertTrue(result != null && result.booleanValue());
//
//        File compiledModuleFile = new File(destDir, "foo/foo/module_.class");
//        assertTrue(compiledModuleFile.isFile());
//        
//        File jar = jar(compiledModuleFile, "foo/foo");
//        checkJarDependencies(jar);
//    }
//
//    @Test
//    public void testOsgiModule() throws IOException, ModuleNotFoundException{
//        File jar = jar("MANIFEST.MF", "META-INF");
//        checkJarDependencies(jar);
//    }
//
//    @Test(expected = ModuleNotFoundException.class)
//    public void testWrongOsgiModule() throws IOException, ModuleNotFoundException {
//        File jar = jar("MANIFEST.MF", "META-INF");
//        try{
//            ArrayList<File> jars = new ArrayList<File>(1);
//            jars.add(jar);
//            ClassPath classPath = new Main.ClassPath(jars);
//            classPath.loadModule("bar", "1");
//            fail();
//        }finally{
//            //        System.err.println("Jar is at "+jar.getAbsolutePath());
//            jar.delete();
//        }
//    }
//
//    @Test
//    public void testJBossModuleXml() throws IOException, ModuleNotFoundException{
//        File jar = jar("module.xml", "META-INF/jbossmodules/foo/foo/1");
//        checkJarDependencies(jar);
//    }
//
//    @Test
//    public void testJBossModuleProperties() throws IOException, ModuleNotFoundException{
//        File jar = jar("module.properties", "META-INF/jbossmodules/foo/foo/1");
//        checkJarDependencies(jar);
//    }
//
//    @Ignore("Requires a fix to disable resolution since modules come from the classpath")
//    @Test
//    public void testMavenModule() throws IOException, ModuleNotFoundException{
//        File jar = jar("pom.xml", "META-INF/maven/foo/foo");
//        checkJarDependencies(jar);
//    }
//
//    private void checkJarDependencies(File jar) throws ModuleNotFoundException {
//        try{
//            ArrayList<File> jars = new ArrayList<File>(1);
//            jars.add(jar);
//            ClassPath classPath = new Main.ClassPath(jars);
//            Module module = classPath.loadModule("foo.foo", "1");
//            assertNotNull(module);
//            assertEquals("foo.foo", module.name());
//            assertEquals("1", module.version());
//            assertEquals(3, module.dependencies.size());
//            assertTrue(module.dependencies.contains(new ClassPath.Dependency("a.a", "1", false, false)));
//            assertTrue(module.dependencies.contains(new ClassPath.Dependency("b.b", "2", false, true)));
//            assertTrue(module.dependencies.contains(new ClassPath.Dependency("c.c", "3", true, false)));
//        }finally{
////            System.err.println("Jar is at "+jar.getAbsolutePath());
//            jar.delete();
//        }
//        
//    }
//
//    private File jar(String file, String destDir) throws IOException {
//        // relative to this file
//        File sourceFile = new File(getCurrentPackagePath(), file);
//        return jar(sourceFile, destDir);
//    }
//
//    private File jar(File sourceFile, String destDir) throws IOException {
//        File jarFile = File.createTempFile("testmain", ".jar");
//        JarOutputStream jar = new JarOutputStream(new FileOutputStream(jarFile));
//        String dirName = destDir.isEmpty() ? sourceFile.getName() : destDir+"/"+sourceFile.getName();
//        ZipEntry entry = new ZipEntry(dirName);
//        jar.putNextEntry(entry);
//        // relative to this file
//        IOUtils.copyStream(new FileInputStream(sourceFile), jar, true, true);
//        return jarFile;
//    }
//
//    private String getCurrentPackagePath() {
//        return "test-jvm/"+MainTest.class.getPackage().getName().replace('.', '/');
//    }
}

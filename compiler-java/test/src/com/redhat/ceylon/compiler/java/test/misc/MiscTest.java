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
package com.redhat.ceylon.compiler.java.test.misc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;

public class MiscTest extends CompilerTest {

    @Test
    public void testDefaultedModel() throws Exception{
        compile("defaultedmodel/DefineDefaulted.ceylon");
        compile("defaultedmodel/UseDefaulted.ceylon");
    }
    
	@Test
	public void testHelloWorld(){
		compareWithJavaSource("helloworld/helloworld");
	}
	
	@Test
	public void runHelloWorld() throws Exception{
		compileAndRun("com.redhat.ceylon.compiler.java.test.misc.helloworld.helloworld", "helloworld/helloworld.ceylon");
	}

    @Test
    public void testCompileTwoDepdendantClasses() throws Exception{
        compile("twoclasses/Two.ceylon");
        compile("twoclasses/One.ceylon");
    }

    @Test
    public void testCompileTwoClasses() throws Exception{
        compileAndRun("com.redhat.ceylon.compiler.java.test.misc.twoclasses.main", "twoclasses/One.ceylon", "twoclasses/Two.ceylon", "twoclasses/main.ceylon");
    }

    @Test
    public void testEqualsHashOverriding(){
        compareWithJavaSource("equalshashoverriding/EqualsHashOverriding");
    }

    @Test
    public void compileRuntime(){
        cleanCars("build/classes-runtime");
        
        java.util.List<File> sourceFiles = new ArrayList<File>();
        
        String ceylonSourcePath = "../ceylon.language/src";
        String javaSourcePath = "../ceylon.language/runtime";
        
        String[] ceylonPackages = {"ceylon.language", "ceylon.language.meta", "ceylon.language.impl", "ceylon.language.meta.declaration", "ceylon.language.meta.model"};
        // Native files
        FileFilter exceptions = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String filename = pathname.getName();
                filename = filename.substring(0,  filename.lastIndexOf('.'));
                for (String s : new String[]{"Array", "ArraySequence", "Boolean", "Callable", "Character", "className",
                        "Exception", "flatten", "Float", "identityHash", "Integer", "internalSort", 
                        "language", "metamodel", "modules", "operatingSystem", "process", "integerRangeByIterable",
                        "runtime", "SequenceBuilder", "SequenceAppender", "String", "StringBuilder", "system", "Tuple"}) {
                    if (s.equals(filename)) {
                        return true;
                    }
                } 
                if (filename.equals("annotations")
                        && pathname.getParentFile().getName().equals("meta")) {
                    return true;
                }
                return false;
            }   
        };
        String[] extras = new String[]{
                "arrayOfSize", "false", "infinity",
                "parseFloat", "true", "integerRangeByIterable", "unflatten"
        };
        String[] modelExtras = new String[]{
                "annotations", "modules", "type", "typeLiteral"
        };
        
        for(String pkg : ceylonPackages){
            File pkgDir = new File(ceylonSourcePath, pkg.replaceAll("\\.", "/"));
            File javaPkgDir = new File(javaSourcePath, pkg.replaceAll("\\.", "/"));
            File[] files = pkgDir.listFiles();
            if (files != null) {
                for(File src : files) {
                    if(src.isFile() && src.getName().toLowerCase().endsWith(".ceylon")) {
                        String baseName = src.getName().substring(0, src.getName().length() - 7);
                        if (!exceptions.accept(src)) {
                            sourceFiles.add(src);
                        } else {
                            addJavaSourceFile(baseName, sourceFiles, javaPkgDir, false);
                        }
                    }
                }
            }
        }
        // add extra files that are in Java
        File javaPkgDir = new File(javaSourcePath, "ceylon/language");
        for(String extra : extras)
            addJavaSourceFile(extra, sourceFiles, javaPkgDir, true);
        File javaModelPkgDir = new File(javaSourcePath, "ceylon/language/meta");
        for(String extra : modelExtras)
            addJavaSourceFile(extra, sourceFiles, javaModelPkgDir, true);
        
        String[] javaPackages = {
                "com/redhat/ceylon/compiler/java", 
                "com/redhat/ceylon/compiler/java/language", 
                "com/redhat/ceylon/compiler/java/metadata",
                "com/redhat/ceylon/compiler/java/runtime/ide",
                "com/redhat/ceylon/compiler/java/runtime/metamodel",
                "com/redhat/ceylon/compiler/java/runtime/model",
                };
        for(String pkg : javaPackages){
            File pkgDir = new File(javaSourcePath, pkg.replaceAll("\\.", "/"));
            File[] files = pkgDir.listFiles();
            if (files != null) {
                for(File src : files) {
                    if(src.isFile() && src.getName().toLowerCase().endsWith(".java")) {
                        sourceFiles.add(src);
                    }
                }
            }
        }
        
        CeyloncTool compiler;
        try {
            compiler = new CeyloncTool();
        } catch (VerifyError e) {
            System.err.println("ERROR: Cannot run tests! Did you maybe forget to configure the -Xbootclasspath/p: parameter?");
            throw e;
        }
        CeyloncFileManager fileManager = (CeyloncFileManager)compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        String compilerSourcePath = ceylonSourcePath + File.pathSeparator + javaSourcePath;
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, null, 
                Arrays.asList("-sourcepath", compilerSourcePath, "-d", "build/classes-runtime", "-Xbootstrapceylon"/*, "-verbose"*/), 
                null, compilationUnits1);
        Boolean result = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, result);
    }

    private void addJavaSourceFile(String baseName, List<File> sourceFiles, File javaPkgDir, boolean required) {
        if (Character.isLowerCase(baseName.charAt(0))) {
            File file = new File(javaPkgDir, baseName + "_.java");
            if(file.exists())
                sourceFiles.add(file);
            else if(required)
                Assert.fail("Required file not found: "+file);
        } else {
            File file = new File(javaPkgDir, baseName + "_.java");
            if(file.exists())
                sourceFiles.add(file);
            else if(required)
                Assert.fail("Required file not found: "+file);
            File impl = new File(javaPkgDir, baseName + "$impl.java");
            if (impl.exists()) {
                sourceFiles.add(impl);
            }
        }
    }

    /**
     * This test is for when we need to debug an incremental compilation error from the IDE, to make
     * sure it is indeed a bug and find which bug it is, do not enable it by default, it's only there
     * for debugging purpose. Once the bug is found, add it where it belongs (not here).
     */
    @Ignore
    @Test
    public void debugIncrementalCompilationBug(){
        java.util.List<File> sourceFiles = new ArrayList<File>();
        
        String sdkSourcePath = "../ceylon-sdk/source";
        String testSourcePath = "../ceylon-sdk/test-source";
        
        for(String s : new String[]{
                "../ceylon-sdk/source/ceylon/json/StringPrinter.ceylon",
                "../ceylon-sdk/test-source/test/ceylon/json/print.ceylon",
                "../ceylon-sdk/source/ceylon/json/Array.ceylon",
                "../ceylon-sdk/test-source/test/ceylon/json/use.ceylon",
                "../ceylon-sdk/source/ceylon/net/uri/Path.ceylon",
                "../ceylon-sdk/test-source/test/ceylon/net/run.ceylon",
                "../ceylon-sdk/source/ceylon/json/Printer.ceylon",
                "../ceylon-sdk/source/ceylon/json/Object.ceylon",
                "../ceylon-sdk/source/ceylon/net/uri/Query.ceylon",
                "../ceylon-sdk/test-source/test/ceylon/json/run.ceylon",
                "../ceylon-sdk/test-source/test/ceylon/net/connection.ceylon",
                "../ceylon-sdk/source/ceylon/json/parse.ceylon",
                "../ceylon-sdk/test-source/test/ceylon/json/parse.ceylon",
                "../ceylon-sdk/source/ceylon/net/uri/PathSegment.ceylon",
        }){
            sourceFiles.add(new File(s));
        }
        
        CeyloncTool compiler;
        try {
            compiler = new CeyloncTool();
        } catch (VerifyError e) {
            System.err.println("ERROR: Cannot run tests! Did you maybe forget to configure the -Xbootclasspath/p: parameter?");
            throw e;
        }
        CeyloncFileManager fileManager = (CeyloncFileManager)compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        String compilerSourcePath = sdkSourcePath + File.pathSeparator + testSourcePath;
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, null, 
                Arrays.asList("-sourcepath", compilerSourcePath, "-d", "../ceylon-sdk/modules"/*, "-verbose"*/), 
                null, compilationUnits1);
        Boolean result = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, result);
    }

    @Test
    public void compileSDK(){
        String[] modules = {
                "collection", 
                "dbc",
                "file",
                "interop.java",
                "io",
                "json",
                "math",
                "net",
                "process",
                "test",
                "time"
        };
        compileSDKOnly(modules);
        compileSDKTests(modules);
    }

    private void compileSDKOnly(String[] modules){
        String sourceDir = "../ceylon-sdk/source";
        // don't run this if the SDK is not checked out
        File sdkFile = new File(sourceDir);
        if(!sdkFile.exists())
            return;
        
        java.util.List<String> moduleNames = new ArrayList<String>(modules.length);
        for(String module : modules){
            moduleNames.add("ceylon." + module);
        }
        
        CeyloncTool compiler;
        try {
            compiler = new CeyloncTool();
        } catch (VerifyError e) {
            System.err.println("ERROR: Cannot run tests! Did you maybe forget to configure the -Xbootclasspath/p: parameter?");
            throw e;
        }
        ErrorCollector errorCollector = new ErrorCollector();
        CeyloncFileManager fileManager = (CeyloncFileManager)compiler.getStandardFileManager(null, null, null);
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, errorCollector, 
                Arrays.asList("-sourcepath", sourceDir, "-d", "build/classes-sdk"), 
                moduleNames, null);
        Boolean result = task.call();
        Assert.assertEquals("Compilation of SDK itself failed: " + errorCollector.getAssertionFailureMessage(), Boolean.TRUE, result);
    }

    private void compileSDKTests(String[] modules){
        String sourceDir = "../ceylon-sdk/test-source";
        String depsDir = "../ceylon-sdk/test-deps";
        // don't run this if the SDK is not checked out
        File sdkFile = new File(sourceDir);
        if(!sdkFile.exists())
            return;
        
        java.util.List<String> moduleNames = new ArrayList<String>(modules.length);
        for(String module : modules){
            moduleNames.add("test.ceylon." + module);
        }
        
        CeyloncTool compiler;
        try {
            compiler = new CeyloncTool();
        } catch (VerifyError e) {
            System.err.println("ERROR: Cannot run tests! Did you maybe forget to configure the -Xbootclasspath/p: parameter?");
            throw e;
        }
        ErrorCollector errorCollector = new ErrorCollector();
        CeyloncFileManager fileManager = (CeyloncFileManager)compiler.getStandardFileManager(null, null, null);
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, errorCollector, 
                Arrays.asList("-sourcepath", sourceDir, "-rep", depsDir, "-d", "build/classes-sdk"), 
                moduleNames, null);
        
        Boolean result = task.call();
        Assert.assertEquals("Compilation of SDK tests failed:" + errorCollector.getAssertionFailureMessage(), Boolean.TRUE, result);
    }

    //
    // Java keyword avoidance
    // Note class names and generic type arguments are not a problem because
    // in Ceylon they must begin with an upper case latter, but the Java
    // keywords are all lowercase 
    
    @Test
    public void testKeywordVariable(){
        compareWithJavaSource("keyword/Variable");
    }
    
    @Test
    public void testKeywordAttribute(){
        compareWithJavaSource("keyword/Attribute");
    }
    
    @Test
    public void testKeywordMethod(){
        compareWithJavaSource("keyword/Method");
    }
    
    @Test
    public void testKeywordParameter(){
        compareWithJavaSource("keyword/Parameter");
    }

    @Test
    public void testJDKModules(){
        Assert.assertTrue(JDKUtils.isJDKModule("java.base"));
        Assert.assertTrue(JDKUtils.isJDKModule("java.desktop"));
        Assert.assertTrue(JDKUtils.isJDKModule("java.compiler")); // last one
        Assert.assertFalse(JDKUtils.isJDKModule("java.stef"));
    }

    @Test
    public void testJDKPackages(){
        Assert.assertTrue(JDKUtils.isJDKAnyPackage("java.awt"));
        Assert.assertTrue(JDKUtils.isJDKAnyPackage("java.lang"));
        Assert.assertTrue(JDKUtils.isJDKAnyPackage("java.util"));
        Assert.assertTrue(JDKUtils.isJDKAnyPackage("javax.swing"));
        Assert.assertTrue(JDKUtils.isJDKAnyPackage("org.w3c.dom"));
        Assert.assertTrue(JDKUtils.isJDKAnyPackage("org.xml.sax.helpers"));// last one
        Assert.assertFalse(JDKUtils.isJDKAnyPackage("fr.epardaud"));
    }

    @Test
    public void testOracleJDKModules(){
        Assert.assertTrue(JDKUtils.isOracleJDKModule("oracle.jdk.base"));
        Assert.assertTrue(JDKUtils.isOracleJDKModule("oracle.jdk.desktop"));
        Assert.assertTrue(JDKUtils.isOracleJDKModule("oracle.jdk.httpserver"));
        Assert.assertTrue(JDKUtils.isOracleJDKModule("oracle.jdk.tools.base")); // last one
        Assert.assertFalse(JDKUtils.isOracleJDKModule("oracle.jdk.stef"));
        Assert.assertFalse(JDKUtils.isOracleJDKModule("jdk.base"));
    }

    @Test
    public void testOracleJDKPackages(){
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("com.oracle.net"));
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("com.sun.awt"));
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("com.sun.imageio.plugins.bmp"));
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("com.sun.java.swing.plaf.gtk"));
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("com.sun.nio.sctp"));
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("sun.nio"));
        Assert.assertTrue(JDKUtils.isOracleJDKAnyPackage("sunw.util"));// last one
        Assert.assertFalse(JDKUtils.isOracleJDKAnyPackage("fr.epardaud"));
    }
    
    @Test
    public void testLaunchDistCeylon() throws IOException, InterruptedException {
        String[] args1 = {
                "../ceylon-dist/dist/bin/ceylon",
                "compile",
                "--src",
                "../ceylon-dist/dist/samples/helloworld/source",
                "--out",
                "build/test-cars",
                "com.example.helloworld"
        };
        launchCeylon(args1);
        String[] args2 = {
                "../ceylon-dist/dist/bin/ceylon",
                "doc",
                "--src",
                "../ceylon-dist/dist/samples/helloworld/source",
                "--out",
                "build/test-cars",
                "com.example.helloworld"
        };
        launchCeylon(args2);
        String[] args3 = {
                "../ceylon-dist/dist/bin/ceylon",
                "run",
                "--rep",
                "build/test-cars",
                "com.example.helloworld/1.0.0"
        };
        launchCeylon(args3);
    }
    
    public void launchCeylon(String[] args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectInput(Redirect.INHERIT);
        pb.redirectOutput(Redirect.INHERIT);
        pb.redirectError(Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
        if (p.exitValue() > 0) {
            Assert.fail("Ceylon script execution failed");
        }
    }
}

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
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
        
        String[] ceylonPackages = {"ceylon.language", "ceylon.language.descriptor"};
        HashSet exceptions = new HashSet();
        for (String ex : new String[] {
                // Native files
                "Array", "Boolean", "Callable", "Character", "className",
                "Exception", "flatten", "Float", "identityHash", "Integer", "internalFirst", "internalSort", 
                "Keys", "language", "process", "integerRangeByIterable",
                "SequenceBuilder", "SequenceAppender", "String", "StringBuilder", "unflatten",
                // Problem files
                "LazySet"
                }) {
            exceptions.add(ex);
        }
        String[] extras = new String[]{
                "array", "arrayOfSize", "copyArray", "false", "infinity",
                "parseFloat", "parseInteger", "string", "true", "integerRangeByIterable"
        };
        
        for(String pkg : ceylonPackages){
            File pkgDir = new File(ceylonSourcePath, pkg.replaceAll("\\.", "/"));
            File javaPkgDir = new File(javaSourcePath, pkg.replaceAll("\\.", "/"));
            File[] files = pkgDir.listFiles();
            if (files != null) {
                for(File src : files) {
                    if(src.isFile() && src.getName().toLowerCase().endsWith(".ceylon")) {
                        String baseName = src.getName().substring(0, src.getName().length() - 7);
                        if (!exceptions.contains(baseName)) {
                            sourceFiles.add(src);
                        } else {
                            addJavaSourceFile(baseName, sourceFiles, javaPkgDir);
                        }
                    }
                }
            }
        }
        // add extra files that are in Java
        File javaPkgDir = new File(javaSourcePath, "ceylon/language");
        for(String extra : extras)
            addJavaSourceFile(extra, sourceFiles, javaPkgDir);
        
        String[] javaPackages = {"ceylon.language.descriptor", "com/redhat/ceylon/compiler/java", "com/redhat/ceylon/compiler/java/language", "com/redhat/ceylon/compiler/java/metadata"};
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

    private void addJavaSourceFile(String baseName, List<File> sourceFiles, File javaPkgDir) {
        if (Character.isLowerCase(baseName.charAt(0))) {
            sourceFiles.add(new File(javaPkgDir, baseName + "_.java"));
        } else {
            sourceFiles.add(new File(javaPkgDir, baseName + ".java"));
            File impl = new File(javaPkgDir, baseName + "$impl.java");
            if (impl.exists()) {
                sourceFiles.add(impl);
            }
        }
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
        };
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
        CeyloncFileManager fileManager = (CeyloncFileManager)compiler.getStandardFileManager(null, null, null);
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, null, 
                Arrays.asList("-sourcepath", sourceDir, "-d", "build/classes-sdk"), 
                moduleNames, null);
        Boolean result = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, result);
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
}

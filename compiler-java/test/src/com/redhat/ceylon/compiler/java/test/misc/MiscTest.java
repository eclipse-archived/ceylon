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

import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.loader.JDKPackageList;

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

    @Ignore("M5: requires more bootstrap work, current support dates from summer 2011")
    @Test
    public void compileRuntime(){
        String sourcePath = "../ceylon.language/src";
        String[] packages = {"ceylon.language", "ceylon.language.descriptor"};
        java.util.List<File> sourceFiles = new ArrayList<File>();
        for(String pkg : packages){
            File pkgDir = new File(sourcePath, pkg.replaceAll("\\.", "/"));
            for(File src : pkgDir.listFiles()){
                if(src.isFile() && src.getName().toLowerCase().endsWith(".ceylon"))
                    sourceFiles.add(src);
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
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, null, 
                Arrays.asList("-sourcepath", sourcePath, "-d", "build/classes-runtime", "-Xbootstrapceylon", "-verbose"), 
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
        };
        String sourcePrefix = "../ceylon-sdk/";
        // don't run this if the SDK is not checked out
        File sdkFile = new File(sourcePrefix);
        if(!sdkFile.exists())
            return;
        StringBuilder sourcePath = new StringBuilder();
        java.util.List<String> moduleNames = new ArrayList<String>(modules.length);
        for(String module : modules){
            moduleNames.add("ceylon." + module);
            if(sourcePath.length() > 0)
                sourcePath.append(File.pathSeparator);
            sourcePath.append(sourcePrefix).append(module).append(File.separator).append("source");
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
                Arrays.asList("-sourcepath", sourcePath.toString(), "-d", "build/classes-sdk"), 
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
    public void testJDKPackages(){
        Assert.assertTrue(JDKPackageList.isJDKPackage("java.awt"));
        Assert.assertTrue(JDKPackageList.isJDKPackage("java.lang"));
        Assert.assertTrue(JDKPackageList.isJDKPackage("java.util"));
        Assert.assertTrue(JDKPackageList.isJDKPackage("javax.swing"));
        Assert.assertTrue(JDKPackageList.isJDKPackage("org.w3c.dom"));
        Assert.assertTrue(JDKPackageList.isJDKPackage("org.xml.sax.helpers"));// last one
        Assert.assertFalse(JDKPackageList.isJDKPackage("fr.epardaud"));
    }

    @Test
    public void testOracleJDKPackages(){
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("com.oracle.net"));
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("com.sun.awt"));
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("com.sun.imageio.plugins.bmp"));
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("com.sun.java.swing.plaf.gtk"));
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("com.sun.nio.sctp"));
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("sun.nio"));
        Assert.assertTrue(JDKPackageList.isOracleJDKPackage("sunw.util"));// last one
        Assert.assertFalse(JDKPackageList.isOracleJDKPackage("fr.epardaud"));
    }
}

package com.redhat.ceylon.compiler.test.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.tools.CeyloncTool;

public class MiscTest extends CompilerTest {
	@Test
	public void testHelloWorld(){
		compareWithJavaSource("helloworld/helloworld");
	}
	
	@Test
	public void runHelloWorld() throws Exception{
		compileAndRun("com.redhat.ceylon.compiler.test.misc.helloworld.helloworld", "helloworld/helloworld.ceylon");
	}

    @Test
    public void testCompileTwoClasses() throws Exception{
        compileAndRun("com.redhat.ceylon.compiler.test.misc.twoclasses.main", "twoclasses/One.ceylon", "twoclasses/Two.ceylon", "twoclasses/main.ceylon");
    }

    @Test
    public void testEqualsHashOverriding(){
        compareWithJavaSource("equalshashoverriding/EqualsHashOverriding");
    }

    @Test
    public void compileRuntime(){
        String sourcePath = "../ceylon-spec/languagesrc/current";
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
        fileManager.setSourcePath(sourcePath);
        Iterable<? extends JavaFileObject> compilationUnits1 =
            fileManager.getJavaFileObjectsFromFiles(sourceFiles);
        CeyloncTaskImpl task = (CeyloncTaskImpl) compiler.getTask(null, fileManager, null, Arrays.asList("-d", "build/classes-runtime", "-Xbootstrapceylon", "-verbose"), null, compilationUnits1);
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
}

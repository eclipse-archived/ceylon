package com.redhat.ceylon.compiler.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Before;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.tools.CeyloncTool;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskEvent.Kind;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

public abstract class CompilerTest {

	private final static String dir = "test-src";

	protected String path;

	// for running
	protected JavaCompiler runCompiler;
	protected CeyloncFileManager runFileManager;

	private String pkg;

	@Before
	public void setup(){
		// for comparing with java source
	    Package pakage = getClass().getPackage();
		pkg = pakage == null ? "" : pakage.getName().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
		path = dir + File.separator + pkg + File.separator;
		// for running
		try {
			runCompiler = new CeyloncTool();
		} catch (VerifyError e) {
			System.err.println("ERROR: Cannot run tests! Did you maybe forget to configure the -Xbootclasspath/p: parameter?");
			throw e;
		}
		runFileManager = (CeyloncFileManager)runCompiler.getStandardFileManager(null, null, null);
		runFileManager.setSourcePath(dir);
	}

	protected void compareWithJavaSource(String name) {
	    compareWithJavaSource(name, dir);
	}

	protected void compareWithJavaSource(String name, String dir) {
		compareWithJavaSource(name+".ceylon", name+".src", dir);
	}

	protected void compareWithJavaSource(String ceylon, String java, String dir) {

	    // make a compiler task
        runFileManager.setSourcePath(dir);
	    CeyloncTaskImpl task = getCompilerTask(ceylon);
	    
	    // grab the CU after we've completed it
	    class Listener implements TaskListener{
            JCCompilationUnit compilationUnit;
            private String compilerSrc;
            @Override
            public void started(TaskEvent e) {
            }

            @Override
            public void finished(TaskEvent e) {
                if(e.getKind() == Kind.ENTER){
                    if(compilationUnit != null)
                        throw new RuntimeException("Compilation unit already grabbed, are we compiling more than one file?");
                    compilationUnit = (JCCompilationUnit) e.getCompilationUnit();
                    // for some reason compilationUnit is full here in the listener, but empty as soon
                    // as the compile task is done. probably to clean up for the gc?
                    compilerSrc = normalizeLineEndings(compilationUnit.toString());
                }
            }
        }
	    Listener listener = new Listener();
	    task.setTaskListener(listener);

	    // now compile it all the way
	    Boolean success = task.call();
	    
	    Assert.assertTrue("Compilation failed", success);

		// now look at what we expected
		String expectedSrc = normalizeLineEndings(readFile(new File(path+java))).trim();
        String compiledSrc = listener.compilerSrc.trim();
		Assert.assertEquals("Source code differs", expectedSrc, compiledSrc);
	}

	private String readFile(File file) {
		try{
			Reader reader = new FileReader(file);
			StringBuilder strbuf = new StringBuilder();
			try{
				char[] buf = new char[1024];
				int read;
				while((read = reader.read(buf)) > -1)
					strbuf.append(buf, 0, read);
			}finally{
				reader.close();
			}
			return strbuf.toString();
		}catch(IOException x){
			throw new RuntimeException(x);
		}
	}

	private String normalizeLineEndings(String txt) {
		String result = txt.replaceAll("\r\n", "\n"); // Windows
		result = result.replaceAll("\r", "\n"); // Mac (OS<=9)
		return result;
	}
	
	protected void compileAndRun(String ceylon, String main) {
		Boolean success = getCompilerTask(ceylon).call();
		Assert.assertTrue(success);
		try{
			java.lang.Class<?> klass = java.lang.Class.forName(main);
			Method m = klass.getMethod(klass.getSimpleName(), ceylon.language.Process.class);
			m.invoke(null, new ceylon.language.Process());
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	protected CeyloncTaskImpl getCompilerTask(String... sourcePaths){
	    java.util.List<File> sourceFiles = new ArrayList<File>(sourcePaths.length);
	    for(String file : sourcePaths){
	        sourceFiles.add(new File(path+file));
	    }
        Iterable<? extends JavaFileObject> compilationUnits1 =
            runFileManager.getJavaFileObjectsFromFiles(sourceFiles);
        return (CeyloncTaskImpl) runCompiler.getTask(null, runFileManager, null, Arrays.asList("-d", "build/classes"), null, compilationUnits1);
	}
}

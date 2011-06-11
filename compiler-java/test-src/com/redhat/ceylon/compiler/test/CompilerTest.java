package com.redhat.ceylon.compiler.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Before;

import com.redhat.ceylon.compiler.codegen.CeylonEnter;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTool;
import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public abstract class CompilerTest {

	private final static String dir = "test-src";

	private String path;

	// for running
	private JavaCompiler runCompiler;
	private CeyloncFileManager runFileManager;

	@Before
	public void setup(){
		// for comparing with java source
		String pkg = getClass().getPackage().getName().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
		path = dir + File.separator + pkg + File.separator;
		// for running
		runCompiler = new CeyloncTool();
		runFileManager = (CeyloncFileManager)runCompiler.getStandardFileManager(null, null, null);
		runFileManager.setSourcePath(dir);
	}

	protected void compareWithJavaSource(String name) {
		compareWithJavaSource(name+".ceylon", name+".src");
	}

	protected void compareWithJavaSource(String ceylon, String java) {
		// Make a new compiler each time
		Context context = new Context();
		CeyloncFileManager.preRegister(context);
		LanguageCompiler compareCompiler = (LanguageCompiler) LanguageCompiler.instance(context);
		CeyloncFileManager compareFileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
		compareFileManager.setSourcePath(dir);
		// add the files to compile
		List<JavaFileObject> files = List.nil();
		File file = new File(path+ceylon);
		for (JavaFileObject fo : compareFileManager.getJavaFileObjectsFromFiles(Collections.singletonList(file)))
			files = files.prepend(fo);
		Assert.assertEquals(1, files.size());
		// we need to parse first
		JCCompilationUnit compilationUnit = compareCompiler.parse(files.get(0));
		// then we complete it
		CeylonEnter enter = (CeylonEnter) CeylonEnter.instance(context);
		enter.completeCeylonTrees(List.of(compilationUnit));
		// now look at what we expected
		String src = readFile(new File(path+java));
		String src2 = normalizeLineEndings(compilationUnit.toString());
		Assert.assertEquals(src.trim(), src2.trim());
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
		Iterable<? extends JavaFileObject> compilationUnits1 =
			runFileManager.getJavaFileObjectsFromFiles(Arrays.asList(new File(path+ceylon)));
		Boolean success = runCompiler.getTask(null, runFileManager, null, Arrays.asList("-d", "build/classes"), null, compilationUnits1).call();
		Assert.assertTrue(success);
		try{
			Class<?> klass = Class.forName(main);
			Method m = klass.getMethod("run", ceylon.language.Process.class);
			m.invoke(null, new ceylon.language.Process());
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
}

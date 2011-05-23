package com.redhat.ceylon.compiler.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.CeyloncTool;
import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.List;

public class CompilerTest {
	
	private final static String dir = "test-src";
	
	private LanguageCompiler comp;
	private JavacFileManager fileManager;
	private String path;

	@Before
	public void setup(){
        Context context = new Context();
        CeyloncFileManager.preRegister(context);
        comp = (LanguageCompiler) LanguageCompiler.instance(context);
        fileManager = (JavacFileManager) context.get(JavaFileManager.class);
        String pkg = getClass().getPackage().getName().replaceAll("\\.", File.separator);
        path = dir + File.separator + pkg + File.separator;
	}
	
	private void compareWithJavaSource(String ceylon, String java) throws IOException{
		List<JavaFileObject> files = List.nil();
        File file = new File(path+ceylon);
        for (JavaFileObject fo : fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(file)))
            files = files.prepend(fo);
        Assert.assertEquals(1, files.size());
        JCCompilationUnit compilationUnit = comp.parse(files.get(0));
        String src = readFile(new File(path+java));
        Assert.assertEquals(src.trim(), compilationUnit.toString().trim());
	}

	private String readFile(File file) throws IOException {
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
	}

	@Test
	public void test() throws IOException{
        compareWithJavaSource("helloworld/helloworld.ceylon", "helloworld/helloworld.src");
	}

	@Test
	public void test2() throws Exception{
		compileAndRun("helloworld/helloworld.ceylon", "helloworld");
	}
	
	public void compileAndRun(String ceylon, String main) throws Exception{
		JavaCompiler compiler = new CeyloncTool();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		Iterable<? extends JavaFileObject> compilationUnits1 =
			fileManager.getJavaFileObjectsFromFiles(Arrays.asList(new File(path+ceylon)));
		compiler.getTask(null, fileManager, null, Arrays.asList("-d", "build/classes"), null, compilationUnits1).call();
		Class<?> klass = Class.forName(main);
		Method m = klass.getMethod("run", ceylon.language.Process.class);
		m.invoke(null, new ceylon.language.Process());
	}
}

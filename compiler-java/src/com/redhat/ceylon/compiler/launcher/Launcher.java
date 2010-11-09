package com.redhat.ceylon.compiler.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import com.redhat.ceylon.compiler.tools.CeyloncTool;
import com.sun.source.tree.LineMap;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.comp.Resolve;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;

public class Launcher {

	public Launcher() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	Context context;
	TreeMaker make;
	Name.Table names;
	ClassReader reader;
	Resolve resolve;
	JavaCompiler compiler;
	DiagnosticCollector<JavaFileObject> diagnostics;
	JavacFileManager fileManager;
	JavacTaskImpl task;
	Options options;
	LineMap map;
	Symtab syms;
    
    JCCompilationUnit jcCompilationUnit;

    public void startup() throws Exception {
    	compiler = new CeyloncTool();
    	// compiler = ToolProvider.getSystemJavaCompiler();

    	diagnostics = 
    		new DiagnosticCollector<JavaFileObject>();
    	fileManager
    		= (JavacFileManager)compiler.getStandardFileManager(diagnostics, null, null);
    	fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
    			Arrays.asList(new File("/tmp")));

    	fileManager.setLocation(StandardLocation.CLASS_PATH,
    			Arrays.asList(new File("/tmp"), new File(System.getProperty("user.dir") + "/runtime")));
    	Iterable<? extends JavaFileObject> compilationUnits
    	= fileManager.getJavaFileObjectsFromStrings(new ArrayList<String>());

    	JavaCompiler.CompilationTask aTask =
    		compiler.getTask(null, fileManager,
    				diagnostics,
    				Arrays.asList("-g"/* , /* "-verbose", */
    						// "-source", "7", "-XDallowFunctionTypes"
    				),
    				null, compilationUnits);
    	setup((JavacTaskImpl)aTask);
    }

    void setup (JavacTaskImpl task) {
        this.task = task;

        context = task.getContext();
        options = Options.instance(context);
        // It's a bit weird to see "invokedynamic" set here,
        // but it has to be done before Resolve.instance().
        options.put("invokedynamic", "invokedynamic");
        make = TreeMaker.instance(context);
        Class<?>[] interfaces = {JCTree.Factory.class};

        names = Name.Table.instance(context);
        reader = ClassReader.instance(context);
        resolve = Resolve.instance(context);
        syms = Symtab.instance(context);
    }


}

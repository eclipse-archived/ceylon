package com.redhat.ceylon.compiler.tools;

import java.io.IOException;

import javax.tools.JavaFileObject;

import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.main.Main;
import com.sun.tools.javac.util.Context;


public class CeyloncTaskImpl extends JavacTaskImpl {

	CeyloncTaskImpl(JavacTool tool, Main compilerMain, Iterable<String> flags,
			Context context, Iterable<String> classes,
			Iterable<? extends JavaFileObject> fileObjects) {
		super(tool, compilerMain, flags, context, classes, fileObjects);
	}

	protected void prepareCompiler() throws IOException {
		// Register the CeylonCompiler with the context
		CeylonCompiler.instance(context);
        super.prepareCompiler();
   }
}

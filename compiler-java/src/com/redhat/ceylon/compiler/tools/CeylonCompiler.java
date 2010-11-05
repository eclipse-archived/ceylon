package com.redhat.ceylon.compiler.tools;

import java.io.PrintWriter;
import java.io.Writer;

import javax.lang.model.SourceVersion;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.main.Main;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

public class CeylonCompiler extends JavaCompiler 
	implements ClassReader.SourceCompleter
{

	public CeylonCompiler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/** Get the CeylonCompiler instance for this context. */
    public static JavaCompiler instance(Context context) {
        JavaCompiler instance = context.get(compilerKey);
        if (instance == null)
            instance = new CeylonCompiler(context);
        return instance;
    }
}

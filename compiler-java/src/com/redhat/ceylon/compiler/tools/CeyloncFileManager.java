package com.redhat.ceylon.compiler.tools;

import java.nio.charset.Charset;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;

public class CeyloncFileManager extends JavacFileManager 
	implements StandardJavaFileManager
{

	public CeyloncFileManager(Context context, boolean register, Charset charset) {
		super(context, register, charset);
	}

	protected JavaFileObject.Kind getKind(String extension) {
        if (extension.equals(JavaFileObject.Kind.CLASS.extension))
            return JavaFileObject.Kind.CLASS;
        else if (extension.equals(JavaFileObject.Kind.SOURCE.extension) 
        		|| extension.equals(".ceylon")
        		)
            return JavaFileObject.Kind.SOURCE;
        else if (extension.equals(JavaFileObject.Kind.HTML.extension))
            return JavaFileObject.Kind.HTML;
        else
            return JavaFileObject.Kind.OTHER;
    }
	
}

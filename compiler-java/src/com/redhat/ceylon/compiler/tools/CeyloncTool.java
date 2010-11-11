/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.redhat.ceylon.compiler.tools;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.lang.model.SourceVersion;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.sun.source.util.JavacTask;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.main.Main;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.Log;

public class CeyloncTool extends JavacTool 
	implements JavaCompiler
{
	public JavacFileManager getStandardFileManager(
	        DiagnosticListener<? super JavaFileObject> diagnosticListener,
	        Locale locale,
	        Charset charset) {
	        Context context = new Context();
	        if (diagnosticListener != null)
	            context.put(DiagnosticListener.class, diagnosticListener);
	        context.put(Log.outKey, new PrintWriter(System.err, true)); // FIXME
	        return new CeyloncFileManager(context, true, charset);
	    }

	public JavacTask getTask(Writer out,
            JavaFileManager fileManager,
            DiagnosticListener<? super JavaFileObject> diagnosticListener,
            Iterable<String> options,
            Iterable<String> classes,
            Iterable<? extends JavaFileObject> compilationUnits)
    {
    	final String kindMsg = "All compilation units must be of SOURCE kind";
    	if (options != null)
    		for (String option : options)
    			option.getClass(); // null check
    	if (classes != null) {
    		for (String cls : classes)
    			if (!SourceVersion.isName(cls)) // implicit null check
    				throw new IllegalArgumentException("Not a valid class name: " + cls);
    	}
    	if (compilationUnits != null) {
    		for (JavaFileObject cu : compilationUnits) {
    			if (cu.getKind() != JavaFileObject.Kind.SOURCE) // implicit null check
    				throw new IllegalArgumentException(kindMsg);
    		}
    	}

    	Context context = new Context();

    	if (diagnosticListener != null)
    		context.put(DiagnosticListener.class, diagnosticListener);

    	if (out == null)
    		context.put(Log.outKey, new PrintWriter(System.err, true));
    	else
    		context.put(Log.outKey, new PrintWriter(out, true));

    	if (fileManager == null)
    		fileManager = getStandardFileManager(diagnosticListener, null, null);
    	context.put(JavaFileManager.class, fileManager);
    	processOptions(context, fileManager, options);
    	Main compiler = new Main("javacTask", context.get(Log.outKey));
    	return new CeyloncTaskImpl(this, compiler, options, context, classes, compilationUnits);
    }

}

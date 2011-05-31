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

import java.io.File;
import java.io.IOException;
import java.util.Queue;

import javax.tools.JavaFileObject;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.codegen.CeylonEnter;
import com.redhat.ceylon.compiler.codegen.CeylonFileObject;
import com.redhat.ceylon.compiler.codegen.Gen2;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.Builder;
import com.redhat.ceylon.compiler.typechecker.tree.CustomBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

public class LanguageCompiler extends JavaCompiler {

    /** The context key for the phasedUnits. */
    protected static final Context.Key<PhasedUnits> phasedUnitsKey =
        new Context.Key<PhasedUnits>();

    /** The context key for the ceylon context. */
    protected static final Context.Key<com.redhat.ceylon.compiler.typechecker.context.Context> ceylonContextKey =
        new Context.Key<com.redhat.ceylon.compiler.typechecker.context.Context>();
	
    private final Gen2 gen;
	private final PhasedUnits phasedUnits;
	private final com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
	private final VFS vfs;

    /** Get the PhasedUnits instance for this context. */
    public static PhasedUnits getPhasedUnitsInstance(Context context) {
    	PhasedUnits phasedUnits = context.get(phasedUnitsKey);
    	if(phasedUnits == null){
    		phasedUnits = new PhasedUnits(getCeylonContextInstance(context));
    		context.put(phasedUnitsKey, phasedUnits);
    	}
    	return phasedUnits;
    }

    /** Get the Ceylon context instance for this context. */
    public static com.redhat.ceylon.compiler.typechecker.context.Context getCeylonContextInstance(Context context) {
    	com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = context.get(ceylonContextKey);
    	if(ceylonContext == null){
    		ceylonContext = new com.redhat.ceylon.compiler.typechecker.context.Context(new VFS());
    		context.put(ceylonContextKey, ceylonContext);
    	}
    	return ceylonContext;
    }

    /** Get the JavaCompiler instance for this context. */
    public static JavaCompiler instance(Context context) {
        Options options = Options.instance(context);
        options.put("-Xprefer", "source");
        // make sure it's registered
        CeylonEnter.instance(context);
        JavaCompiler instance = context.get(compilerKey);
        if (instance == null)
            instance = new LanguageCompiler(context);
        return instance;
    }

    public LanguageCompiler(Context context) {
        super(context);
        ceylonContext = getCeylonContextInstance(context);
        vfs = ceylonContext.getVfs();
        phasedUnits = getPhasedUnitsInstance(context);
        try {
            gen = Gen2.getInstance(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Parse contents of file.
     *  @param filename     The name of the file to be parsed.
     */
    public JCTree.JCCompilationUnit parse(JavaFileObject filename) {
        JavaFileObject prev = log.useSource(filename);
        try {
            JCTree.JCCompilationUnit t;
            if (filename.getName().endsWith(".java")) {

                t = parse(filename, readSource(filename));
            } else {
                t = ceylonParse(filename, readSource(filename));
            }
            if (t.endPositions != null)
                log.setEndPosTable(filename, t.endPositions);
            return t;
        } finally {
            log.useSource(prev);
        }
    }

    protected JCCompilationUnit parse(JavaFileObject filename,
            CharSequence readSource) {
        // FIXME
        if (filename instanceof CeylonFileObject)
            return ceylonParse(filename, readSource);
        else
            return super.parse(filename, readSource);
    }

     private JCCompilationUnit ceylonParse(JavaFileObject filename,
            CharSequence readSource) {
        try {
        	String source = readSource.toString();
            ANTLRStringStream input = new ANTLRStringStream(source);
            CeylonLexer lexer = new CeylonLexer(input);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            CeylonParser parser = new CeylonParser(tokens);
            CeylonParser.compilationUnit_return r = parser.compilationUnit();

            char[] chars = source.toCharArray();
            LineMap map = Position.makeLineMap(chars, chars.length, false);

        	java.util.List<LexError> lexerErrors = lexer.getErrors();
        	for (LexError le: lexerErrors) {
        		printError(le,  le.getMessage(lexer), chars, map);
        	}

        	java.util.List<ParseError> parserErrors = parser.getErrors();
        	for (ParseError pe: parserErrors) {
        		printError(pe,  pe.getMessage(parser), chars, map);
        	}

            CommonTree t = (CommonTree)r.getTree();

            if (lexer.getNumberOfSyntaxErrors() != 0) {
                log.error("ceylon.lexer.failed");
            }
            else if (parser.getNumberOfSyntaxErrors() != 0) {
                log.error("ceylon.parser.failed");
            }
            else {
        		Builder builder = new CustomBuilder();
        		CompilationUnit cu = builder.buildCompilationUnit(t);

        		ModuleBuilder moduleBuilder = phasedUnits.getModuleBuilder();
            	com.redhat.ceylon.compiler.typechecker.model.Package p = moduleBuilder.getCurrentPackage();
            	File sourceFile = new File(filename.toUri().getPath());
				// FIXME: temporary solution
            	VirtualFile file = vfs.getFromFile(sourceFile );
            	VirtualFile srcDir = vfs.getFromFile(getSrcDir(sourceFile));
                PhasedUnit phasedUnit = new PhasedUnit(file, srcDir, cu, p, moduleBuilder, ceylonContext);
                phasedUnits.addPhasedUnit(file, phasedUnit);
                gen.setMap(map);

                return gen.makeJCCompilationUnitPlaceholder(cu, filename);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JCCompilationUnit result =
            make.TopLevel(List.<JCAnnotation>nil(), null, List.<JCTree>of(make.Erroneous()));
        result.sourcefile = filename;
        return result;
    }

     // FIXME: this function is terrible 
    private File getSrcDir(File sourceFile) {
    	String name = sourceFile.getAbsolutePath();
    	String[] prefixes = ((CeyloncFileManager)fileManager).getSourcePath();
    	System.err.println("Prefixes: "+prefixes.length+" name: "+name);
        for (String prefix: prefixes) {
            if (prefix != null){
            	File prefixFile = new File(prefix);
            	String path;
				try {
					path = prefixFile.getCanonicalPath();
				} catch (IOException e) {
					// FIXME
					throw new RuntimeException(e);
				}
            	System.err.println("Prefix: "+path);
            	if(name.startsWith(path)) {
            		return prefixFile;
            	}
            }
        }
        throw new RuntimeException("Failed to find source prefix for "+name);
	}

	private void printError(RecognitionError le, String message, char[] chars, LineMap map) {
    	int lineStart = map.getStartPosition(le.getLine());
    	int lineEnd = lineStart;
    	// find the end of the line
    	for(;lineEnd<chars.length
    	    && chars[lineEnd] != '\n' 
    		&& chars[lineEnd] != '\r'
    			;lineEnd++);
    	String line = new String(chars, lineStart, lineEnd - lineStart);
    	System.out.println(message);
    	System.out.println("Near:");
    	System.out.println(line);
    	for(int i=0;i<le.getCharacterInLine();i++)
    		System.out.print('-');
		System.out.println('^');
	}

	public Env<AttrContext> attribute(Env<AttrContext> env) {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                Context.SourceLanguage.push(Language.CEYLON);
                return super.attribute(env);
            } finally {
                Context.SourceLanguage.pop();
            }
        }
        return super.attribute(env);
    }

    protected JavaFileObject genCode(Env<AttrContext> env, JCClassDecl cdef) throws IOException {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                Context.SourceLanguage.push(Language.CEYLON);
                return super.genCode(env, cdef);
            } finally {
                Context.SourceLanguage.pop();
            }
        }
        return super.genCode(env, cdef);
    }

    protected void desugar(final Env<AttrContext> env, Queue<Pair<Env<AttrContext>, JCClassDecl>> results) {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                Context.SourceLanguage.push(Language.CEYLON);
                super.desugar(env, results);
                return;
            } finally {
                Context.SourceLanguage.pop();
            }
        }
        super.desugar(env, results);
    }

}

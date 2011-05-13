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

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import javax.tools.JavaFileObject;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.codegen.CeylonFileObject;
import com.redhat.ceylon.compiler.codegen.Gen;
import com.redhat.ceylon.compiler.codegen.Gen2;
import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.Grok;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.Builder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
import com.sun.tools.javac.util.Position.LineMap;

public class LanguageCompiler extends JavaCompiler {

    private Gen2 gen;

    /** Get the JavaCompiler instance for this context. */
    public static JavaCompiler instance(Context context) {
        Options options = Options.instance(context);
        options.put("-Xprefer", "source");
        JavaCompiler instance = context.get(compilerKey);
        if (instance == null)
            instance = new LanguageCompiler(context);
        return instance;
    }

    public LanguageCompiler(Context context) {
        super(context);

        try {
            gen = new Gen2(context);
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
            InputStream is = filename.openInputStream();
            ANTLRInputStream input = new ANTLRInputStream(is);
            CeylonLexer lexer = new CeylonLexer(input);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            CeylonParser parser = new CeylonParser(tokens);
            CeylonParser.compilationUnit_return r = parser.compilationUnit();

            char[] chars = readSource.toString().toCharArray();
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
            	/*
                CeylonTree.CompilationUnit cu = CeylonTree.build(t, filename.getName());
                cu.file = filename;
                cu.accept(new Grok(log));

                char[] chars = readSource.toString().toCharArray();
                LineMap map = Position.makeLineMap(chars, chars.length, false);
                gen.setMap(map);
            	return gen.convert(cu);

                */
        		Builder builder = new Builder();
        		CompilationUnit cu = builder.buildCompilationUnit(t);

                gen.setMap(map);

                return gen.convert(cu, filename);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JCCompilationUnit result =
            make.TopLevel(List.<JCAnnotation>nil(), null, List.<JCTree>of(make.Erroneous()));
        result.sourcefile = filename;
        return result;
    }

    private void printError(RecognitionError le, String message, char[] chars, LineMap map) {
    	int lineStart = map.getStartPosition(le.getLine());
    	int lineEnd = lineStart;
    	// find the end of the line
    	for(;chars[lineEnd] != '\n' 
    		&& chars[lineEnd] != '\r'
    			&& lineEnd<chars.length;lineEnd++);
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

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
import javax.tools.StandardLocation;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.codegen.CeylonClassWriter;
import com.redhat.ceylon.compiler.codegen.CeylonFileObject;
import com.redhat.ceylon.compiler.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.loader.CeylonEnter;
import com.redhat.ceylon.compiler.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.loader.CompilerModuleBuilder;
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
import com.redhat.ceylon.compiler.typechecker.tree.CustomTree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
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
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.JavacFileManager;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

public class LanguageCompiler extends JavaCompiler {

    /** The context key for the phasedUnits. */
    protected static final Context.Key<PhasedUnits> phasedUnitsKey = new Context.Key<PhasedUnits>();

    /** The context key for the ceylon context. */
    protected static final Context.Key<com.redhat.ceylon.compiler.typechecker.context.Context> ceylonContextKey = new Context.Key<com.redhat.ceylon.compiler.typechecker.context.Context>();

    private final CeylonTransformer gen;
    private final PhasedUnits phasedUnits;
    private final com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private final VFS vfs;

	private CeylonModelLoader modelLoader;

    private CeylonEnter ceylonEnter;

    /** Get the PhasedUnits instance for this context. */
    public static PhasedUnits getPhasedUnitsInstance(Context context) {
        PhasedUnits phasedUnits = context.get(phasedUnitsKey);
        if (phasedUnits == null) {
            com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = getCeylonContextInstance(context);
            CompilerModuleBuilder moduleBuilder = new CompilerModuleBuilder(ceylonContext, context);
            phasedUnits = new PhasedUnits(ceylonContext, moduleBuilder);
            context.put(phasedUnitsKey, phasedUnits);
            // we must call it here because we use the PhasedUnits constructor that doesn't call it
            moduleBuilder.initCoreModules();
        }
        return phasedUnits;
    }

    /** Get the Ceylon context instance for this context. */
    public static com.redhat.ceylon.compiler.typechecker.context.Context getCeylonContextInstance(Context context) {
        com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = context.get(ceylonContextKey);
        if (ceylonContext == null) {
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
        CeylonClassWriter.instance(context);
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
            gen = CeylonTransformer.getInstance(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        modelLoader = CeylonModelLoader.instance(context);
        ceylonEnter = CeylonEnter.instance(context);
    }

    /**
     * Parse contents of file.
     * @param filename The name of the file to be parsed.
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

    protected JCCompilationUnit parse(JavaFileObject filename, CharSequence readSource) {
        // FIXME
        if (filename instanceof CeylonFileObject)
            return ceylonParse(filename, readSource);
        else
            return super.parse(filename, readSource);
    }

    private JCCompilationUnit ceylonParse(JavaFileObject filename, CharSequence readSource) {
        if(ceylonEnter.hasRun())
            throw new RuntimeException("Trying to load new source file after CeylonEnter has been called: "+filename);
        try {
            String source = readSource.toString();
            ANTLRStringStream input = new ANTLRStringStream(source);
            CeylonLexer lexer = new CeylonLexer(input);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            CeylonParser parser = new CeylonParser(tokens);
            CompilationUnit cu = parser.compilationUnit();

            char[] chars = source.toCharArray();
            LineMap map = Position.makeLineMap(chars, chars.length, false);

            java.util.List<LexError> lexerErrors = lexer.getErrors();
            for (LexError le : lexerErrors) {
                printError(le, le.getMessage(), "ceylon.lexer", map);
            }

            java.util.List<ParseError> parserErrors = parser.getErrors();
            for (ParseError pe : parserErrors) {
                printError(pe, pe.getMessage(), "ceylon.parser", map);
            }

            if (lexer.getNumberOfSyntaxErrors() != 0) {
                log.error("ceylon.lexer.failed");
            } else if (parser.getNumberOfSyntaxErrors() != 0) {
                log.error("ceylon.parser.failed");
            } else {
                ModuleBuilder moduleBuilder = phasedUnits.getModuleBuilder();
                File sourceFile = new File(filename.toString());
                // FIXME: temporary solution
                VirtualFile file = vfs.getFromFile(sourceFile);
                VirtualFile srcDir = vfs.getFromFile(getSrcDir(sourceFile));
                // FIXME: this is bad in many ways
                String pkgName = getPackage(filename);
                // make a Package with no module yet, we will resolve them later
                com.redhat.ceylon.compiler.typechecker.model.Package p = modelLoader.findOrCreatePackage(null, pkgName == null ? "" : pkgName);
                PhasedUnit phasedUnit = new CeylonPhasedUnit(file, srcDir, cu, p, moduleBuilder, ceylonContext, filename, map);
                phasedUnits.addPhasedUnit(file, phasedUnit);
                gen.setMap(map);

                return gen.makeJCCompilationUnitPlaceholder(cu, filename, pkgName, phasedUnit);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JCCompilationUnit result = make.TopLevel(List.<JCAnnotation> nil(), null, List.<JCTree> of(make.Erroneous()));
        result.sourcefile = filename;
        return result;
    }

    // FIXME: this function is terrible, possibly refactor it with getPackage?
    private File getSrcDir(File sourceFile) {
        String name;
        try {
            name = sourceFile.getCanonicalPath();
        } catch (IOException e) {
            // FIXME
            throw new RuntimeException(e);
        }
        Iterable<? extends File> prefixes = ((JavacFileManager)fileManager).getLocation(StandardLocation.SOURCE_PATH);
        for (File prefixFile : prefixes) {
            String path;
            try {
                path = prefixFile.getCanonicalPath();
            } catch (IOException e) {
                // FIXME
                throw new RuntimeException(e);
            }
            if (name.startsWith(path)) {
                return prefixFile;
            }
        }
        throw new RuntimeException("Failed to find source prefix for " + name);
    }

    private String getPackage(JavaFileObject file){
        Iterable<? extends File> prefixes = ((JavacFileManager)fileManager).getLocation(StandardLocation.SOURCE_PATH);

    	// Figure out the package name by stripping the "-src" prefix and
    	// extracting
    	// the package part of the fullname.
        for (File prefixFile : prefixes) {
            String prefix = prefixFile.getPath();
    		if (file.toString().startsWith(prefix)) {
    			String fullname = file.toString().substring(prefix.length());
    			assert fullname.endsWith(".ceylon");
    			fullname = fullname.substring(0, fullname.length() - ".ceylon".length());
    			fullname = fullname.replace(File.separator, ".");
    			if(fullname.startsWith("."))
    				fullname = fullname.substring(1);
    			String packageName = Convert.packagePart(fullname);
    			if (!packageName.equals(""))
    				return packageName;
    		}
    	}
    	return null;
    }
    
    private void printError(RecognitionError le, String message, String key, LineMap map) {
        int pos = map.getStartPosition(le.getLine()) + le.getCharacterInLine();
        log.error(pos, key, message);
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
    
    protected void flow(Env<AttrContext> env, Queue<Env<AttrContext>> results) {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                Context.SourceLanguage.push(Language.CEYLON);
                super.flow(env, results);
                return;
            } finally {
                Context.SourceLanguage.pop();
            }
        }
        super.flow(env, results);   
    }

}

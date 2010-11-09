package com.redhat.ceylon.compiler;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.tools.JavaFileObject;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;

import com.redhat.ceylon.compiler.parser.CeylonLexer;
import com.redhat.ceylon.compiler.codegen.Gen;
import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.Grok;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;

public class LanguageCompiler extends JavaCompiler {

	private Gen gen;
	
	/** Get the JavaCompiler instance for this context. */
    public static JavaCompiler instance(Context context) {
        JavaCompiler instance = context.get(compilerKey);
        if (instance == null)
            instance = new LanguageCompiler(context);
        return instance;
    }

	public LanguageCompiler(Context context) {
		super(context);
		try {
			gen = new Gen(context);
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

	private JCCompilationUnit ceylonParse(JavaFileObject filename,
			CharSequence readSource) {
		try {
			InputStream is = filename.openInputStream();
			ANTLRInputStream input = new ANTLRInputStream(is);
			CeylonLexer lexer = new CeylonLexer(input);

			CommonTokenStream tokens = new CommonTokenStream(lexer);

			CeylonParser parser = new CeylonParser(tokens);
			CeylonParser.compilationUnit_return r = parser.compilationUnit();

			CommonTree t = (CommonTree)r.getTree();

			if (parser.getNumberOfSyntaxErrors() == 0 &&
					lexer.getNumberOfSyntaxErrors() == 0) {
				CeylonTree.CompilationUnit cu = CeylonTree.build(t, filename.getName());
				cu.file = filename;
		    	cu.accept(new Grok());

				char[] chars = filename.getCharContent(true).toString().toCharArray();
				LineMap map = Position.makeLineMap(chars, chars.length, false);
				gen.setMap(map);

				return gen.convert(cu);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		return null;
	}
}

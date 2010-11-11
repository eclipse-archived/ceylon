package com.redhat.ceylon.compiler;

import java.io.InputStream;
import java.util.Queue;

import javax.tools.JavaFileObject;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.codegen.CeylonFileObject;
import com.redhat.ceylon.compiler.codegen.Gen;
import com.redhat.ceylon.compiler.parser.CeylonLexer;
import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.tree.CeylonTree;
import com.redhat.ceylon.compiler.tree.Grok;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
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

    protected JCCompilationUnit parse(JavaFileObject filename,
			CharSequence readSource) {
    	// FIXME
    	if (filename instanceof CeylonFileObject)
    		return ceylonParse(filename, readSource);	
    	else
    		return super.parse(filename, readSource);
    }
    
	 JCCompilationUnit ceylonParse(JavaFileObject filename,
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

				char[] chars = readSource.toString().toCharArray();
				LineMap map = Position.makeLineMap(chars, chars.length, false);
				gen.setMap(map);

				return gen.convert(cu);
			}			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;
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
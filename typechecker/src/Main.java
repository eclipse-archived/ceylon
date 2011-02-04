

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.parser.CeylonLexer;
import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.tree.Tree.TypeDecl;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.TreeBuilder;
import com.redhat.ceylon.compiler.tree.Visitor;
import com.redhat.ceylon.compiler.tree.Walker;

public class Main {

	public static void main(String[] args) throws Exception {
		String path;
		if ( args.length==0 ) {
			path = "corpus";
		}
		else {
			path = args[0];
		}
		fileOrDir( new File(path) );
	}
	
	private static void file(File file) throws Exception {
		if ( file.getName().endsWith(".ceylon") ) {
		
		System.out.println("Parsing " + file.getName());
        InputStream is = new FileInputStream( file );
        ANTLRInputStream input = new ANTLRInputStream(is);
        CeylonLexer lexer = new CeylonLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CeylonParser parser = new CeylonParser(tokens);
        CeylonParser.compilationUnit_return r = parser.compilationUnit();

        CommonTree t = (CommonTree)r.getTree();
        
        CompilationUnit cu = new TreeBuilder().buildCompilationUnit(t);
        Visitor v = new Visitor() {
        	@Override
        	public void visitAny(Node that) {
        		System.out.println(that);
        	}
        };
        Walker.walkCompilationUnit(v, cu);

        if (lexer.getNumberOfSyntaxErrors() != 0) {
            System.out.println("Lexer failed");
        }
        else if (parser.getNumberOfSyntaxErrors() != 0) {
        	System.out.println("Parser failed");
        }
        else {
        	System.out.println("Parser succeeded");
        }
        
		}
        
	}
	
	private static void fileOrDir(File file) throws Exception {
		if (file.isDirectory()) 
			dir(file);
		else
			file(file);
	}

	private static void dir(File dir) throws Exception {
		for (File file: dir.listFiles()) 
			fileOrDir(file);
	}

}

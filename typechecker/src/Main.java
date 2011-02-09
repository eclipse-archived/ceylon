

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.analyzer.TypeVisitor;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.parser.CeylonLexer;
import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.parser.LexError;
import com.redhat.ceylon.compiler.parser.ParseError;
import com.redhat.ceylon.compiler.tree.Builder;
import com.redhat.ceylon.compiler.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.tree.Visitor;

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
    
        	List<LexError> lexerErrors = lexer.getErrors();
        	for (LexError le: lexerErrors) {
                System.out.println("Lexer error: " + le.getMessage(lexer));
        	}

        	List<ParseError> parserErrors = parser.getErrors();
        	for (ParseError pe: parserErrors) {
                System.out.println("Parser error: " + pe.getMessage(parser));
        	}

        	CommonTree t = (CommonTree)r.getTree();
            
            CompilationUnit cu = new Builder().buildCompilationUnit(t);
            
            Visitor v = new PrintVisitor();
            cu.visit(v);
            
            Package p = new Package();
            p.setName(Arrays.asList(new String[]{"test"}));
            Module m = new Module();
            m.setName(Arrays.asList(new String[]{"test"}));
            p.setModule(m);
            m.getPackages().add(p);
            
            DeclarationVisitor dv = new DeclarationVisitor(p);
            cu.visit(dv);
            
            cu.visit(new TypeVisitor(dv.getCompilationUnit()));
    
            cu.visit(new ExpressionVisitor());
            
            cu.visit(v);
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

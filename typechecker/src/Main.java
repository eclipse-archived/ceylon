import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.context.PhasedUnit;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.parser.CeylonLexer;
import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.parser.LexError;
import com.redhat.ceylon.compiler.parser.ParseError;
import com.redhat.ceylon.compiler.tree.Builder;
import com.redhat.ceylon.compiler.tree.Tree.CompilationUnit;

/**
 * @author Gavin King <gavin@hibernate.org>
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Main {
    private static boolean noisy;

    /**
     * Files that are not under a proper module structure are placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        String path;
        if ( args.length==0 ) {
            path = "corpus";
        }
        else {
            path = args[0];
        }
        
        noisy = "true".equals(System.getProperties().getProperty("verbose"));
        
        Context context = new Context();
        final File file = new File(path);

        //ceylon.language must be built (parsed) before any other
        buildLanguageModule(context, file);

        if ( file.isDirectory() ) {
            //root directory is the src dir => start from here
            for ( File subfile : file.listFiles() ) {
                parseFileOrDirectory(subfile, context);
            }
        }
        else {
            //simple file compilation
            //TODO is that really valid?
            parseFileOrDirectory(file, context);
        }
        executePhases(context);
    }

    private static void executePhases(Context context) {
        final List<PhasedUnit> phasedUnits = context.getPhasedUnits();
        for (PhasedUnit pu : phasedUnits) {
            pu.buildModuleImport();
        }

        /*
        At this stage we need to
         - resolve all non local modules (recursively) TODO
         - build the object model of these compiled modules TODO
         - declare a missing module as an error
         - detect circular dependencies
         */
        context.verifyModuleDependencyTree();

        for (PhasedUnit pu : phasedUnits) {
            pu.scanDeclarations();
            pu.validateControlFlow();
            pu.validateSpecification();
        }
        for (PhasedUnit pu : phasedUnits) {
            pu.scanTypeDeclarations();
        }
        for (PhasedUnit pu : phasedUnits) {
            pu.analyseTypes();
        }
        for (PhasedUnit pu : phasedUnits) {
            if (noisy) pu.display();
            pu.runAssertions();
        }
    }

    private static void buildLanguageModule(Context context, File master) throws Exception {
        //ceylon.language must be parsed before any other
        if ( ! ( master.getName().equals("corpus")
                || master.getName().equals("corpus/ceylon")
                || master.getName().equals("corpus/ceylon/language") ) ) {
            File file = new File("corpus/ceylon");
            parseFileOrDirectory(file, context);
        }
    }

    private static void parseFile(File file, Context context) throws Exception {
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

        	Package p = context.getPackage();
            CommonTree t = (CommonTree) r.getTree();
            CompilationUnit cu = new Builder().buildCompilationUnit(t);
            PhasedUnit phasedUnit = new PhasedUnit(file.getName(),cu,p, context);
            context.addStagedUnit(phasedUnit);
            
        }
    }
    
    private static void parseFileOrDirectory(File file, Context context) throws Exception {
        if (file.isDirectory()) {
            processDirectory(file, context);
        }
        else {
            parseFile(file, context);
        }
    }

    private static void processDirectory(File dir, Context context) throws Exception {
        context.push( dir.getName() );
        final File[] files = dir.listFiles();
        for (File file: files) {
            if ( Context.MODULE_FILE.equals( file.getName() ) ) {
                context.defineModule();
            }
        }
        for (File file: files) {
            parseFileOrDirectory(file, context);
        }
        context.pop();
    }

}

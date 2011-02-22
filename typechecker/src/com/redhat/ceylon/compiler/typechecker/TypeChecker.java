package com.redhat.ceylon.compiler.typechecker;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.Builder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Executes type checking upon construction and retrieve a CompilationUnit object for a given File.
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
//TODO make an interface?
public class TypeChecker {

    private final boolean verbose;
    private final List<File> srcDirectories;
    private final Context context;

    //package level
    TypeChecker(List<File> srcDirectories, boolean verbose) {
        this.srcDirectories = srcDirectories;
        this.verbose = verbose;
        context = new Context();
        process(context);
    }

    /**
     * Return the CompilationUnit for a given file.
     * May return null of the CompilationUnit has not been parsed.
     */
    public Tree.CompilationUnit getCompilationUnit(File file) {
        return context.getPhasedUnit(file).getCompilationUnit();
    }

    private void process(Context context) throws RuntimeException {
        for ( File file : srcDirectories ) {
            try {
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
                executePhases( context, file.getPath() );
            }
            catch (RuntimeException e) {
                //let it go
                throw e;
            }
            catch (Exception e) {
                throw new RuntimeException("Error while parsing the source directory: " + file.toString() ,e);
            }
        }
    }

    private void executePhases(Context context, String path) {
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
            if (pu.getPath().startsWith(path)) {
                if (verbose) pu.display();
                pu.runAssertions();
            }
        }
    }

    private void buildLanguageModule(Context context, File master) throws Exception {
        //ceylon.language must be parsed before any other
        if ( ! ( master.getName().equals("corpus")
                || master.getName().equals("corpus/ceylon")
                || master.getName().equals("corpus/ceylon/language") ) ) {
            File file = new File("corpus/ceylon");
            parseFileOrDirectory(file, context);
        }
    }

    private void parseFile(File file, Context context) throws Exception {
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

        	com.redhat.ceylon.compiler.typechecker.model.Package p = context.getPackage();
            CommonTree t = (CommonTree) r.getTree();
            Tree.CompilationUnit cu = new Builder().buildCompilationUnit(t);
            PhasedUnit phasedUnit = new PhasedUnit(file, cu, p, context);
            context.addStagedUnit(file, phasedUnit);

        }
    }

    private void parseFileOrDirectory(File file, Context context) throws Exception {
        if (file.isDirectory()) {
            processDirectory(file, context);
        }
        else {
            parseFile(file, context);
        }
    }

    private void processDirectory(File dir, Context context) throws Exception {
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

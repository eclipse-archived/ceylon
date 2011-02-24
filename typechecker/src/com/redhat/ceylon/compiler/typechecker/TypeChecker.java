package com.redhat.ceylon.compiler.typechecker;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.CustomBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/**
 * Executes type checking upon construction and retrieve a CompilationUnit object for a given File.
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
//TODO make an interface?
public class TypeChecker {

    private final boolean verbose;
    private final List<VirtualFile> srcDirectories;
    private final Context context;
    private final VFS vfs;

    //package level
    TypeChecker(VFS vfs, List<VirtualFile> srcDirectories, boolean verbose) {
        this.vfs = vfs;
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
        return context.getPhasedUnit( vfs.getFromFile( file ) ).getCompilationUnit();
    }

    private void process(Context context) throws RuntimeException {
        //ceylon.language must be built (parsed) before any other

        try {
            //FIXME hack to not parse ceylon.language twice: only works if a single directory is set
            if ( srcDirectories.size() == 1 ) {
                buildLanguageModule( context, srcDirectories.get(0) );
            }
            else {
                buildLanguageModule(context, null);
            }
        }
        catch (RuntimeException e) {
            //let it go
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while parsing the source directory: " + srcDirectories.get(0).toString() ,e);
        }
        for ( VirtualFile file : srcDirectories ) {
            try {
                if ( file.isFolder() ) {
                    //root directory is the src dir => start from here
                    for ( VirtualFile subfile : file.getChildren() ) {
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

    private void buildLanguageModule(Context context, VirtualFile master) throws Exception {
        //ceylon.language must be parsed before any other
        if ( master == null ||
                ! ( master.getName().equals("corpus")
                || master.getName().equals("corpus/ceylon")
                || master.getName().equals("corpus/ceylon/language") ) ) {
            VirtualFile file = vfs.getFromFile( new File("corpus/ceylon") );
            parseFileOrDirectory(file, context);
        }
    }

    private void parseFile(VirtualFile file, Context context) throws Exception {
        if ( file.getName().endsWith(".ceylon") ) {

            System.out.println("Parsing " + file.getName());
            InputStream is = file.getInputStream();
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
            Tree.CompilationUnit cu = new CustomBuilder().buildCompilationUnit(t);
            PhasedUnit phasedUnit = new PhasedUnit(file, cu, p, context);
            context.addPhasedUnit(file, phasedUnit);

        }
    }

    private void parseFileOrDirectory(VirtualFile file, Context context) throws Exception {
        if (file.isFolder()) {
            processDirectory(file, context);
        }
        else {
            parseFile(file, context);
        }
    }

    private void processDirectory(VirtualFile dir, Context context) throws Exception {
        context.push( dir.getName() );
        final List<VirtualFile> files = dir.getChildren();
        for (VirtualFile file: files) {
            if ( Context.MODULE_FILE.equals( file.getName() ) ) {
                context.defineModule();
            }
        }
        for (VirtualFile file: files) {
            parseFileOrDirectory(file, context);
        }
        context.pop();
    }
}

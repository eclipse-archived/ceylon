import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.parser.CeylonLexer;
import com.redhat.ceylon.compiler.parser.CeylonParser;
import com.redhat.ceylon.compiler.parser.LexError;
import com.redhat.ceylon.compiler.parser.ParseError;
import com.redhat.ceylon.compiler.tree.Builder;
import com.redhat.ceylon.compiler.tree.Tree.CompilationUnit;

public class Main {

    private static final String MODULE_FILE = "module.ceylon";
    private static boolean ENABLE_MODULE_AND_PACKAGE = false;

    /**
     * When run with the argument corpus/multisource
     * the code supports proper module and allow for multisource compilation
     *
     * However the corpus does not define the proper ceylon.language module
     * And the compiler does not know how to reverse engineer a .class file into a type model
     */
    public static void main(String[] args) throws Exception {
        String path;
        if ( args.length==0 ) {
            path = "corpus";
        }
        else {
            path = args[0];
        }

        if ( path.equals("corpus/multisource") ) {
            //Today only multisource respects the module / package structure defined in the Ceylon spec
            ENABLE_MODULE_AND_PACKAGE = true;
        }
        Context context = new Context();
        final File file = new File(path);
        buildLanguageImport(context);
        if ( file.isDirectory() ) {
            //root directory is the src dir => start from here
            for ( File subfile : file.listFiles() )
            parseFileOrDirectory(subfile, context);
        }
        else {
            //simple file compilation
            //TODO is that really valid?
            parseFileOrDirectory(file, context);
        }

        executePhases(context);
    }

    private static void executePhases(Context context) {
        final List<PhasedUnit> stagedUnits = context.getPhasedUnits();
        for (PhasedUnit su : stagedUnits) {
            su.visitAndPrint();
        }
        for (PhasedUnit su : stagedUnits) {
            su.visitDeclarations();
            su.visitAssignments();
        }
        for (PhasedUnit su : stagedUnits) {
            su.visitTypes();
        }
        for (PhasedUnit su : stagedUnits) {
            su.visitExpressions();
        }
        //TODO we print before and after, not sure why but Gavin added the call
        for (PhasedUnit su : stagedUnits) {
            su.visitAndPrint();
        }
    }

    private static void buildLanguageImport(Context context) {
        //FIXME this is a hack: need to be read from the module dependency or in this case an implicit dependency
        context.push("ceylon");
        context.push("language");
        context.defineModule();
        //add Module
        Class module = new Class();
        final Package defaultPackage = context.getPackage();
        module.setContainer(defaultPackage);
        module.setName("Module");
        defaultPackage.getMembers().add(module);
        context.setDefaultPackage(defaultPackage);
        context.pop();
        context.pop();
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

        	Package p;

            if (ENABLE_MODULE_AND_PACKAGE) {
                p = context.getPackage();
            }
            else {
                //TODO here for legacy purpose until the corpus is cleanly split into properly formed modules
                p = new Package();
                p.setName(Arrays.asList(new String[]{"test"}));
                Module m = new Module();
                m.setName(Arrays.asList(new String[]{"test"}));
                p.setModule(m);
                m.getPackages().add(p);
            }

            CommonTree t = (CommonTree) r.getTree();
            CompilationUnit cu = new Builder().buildCompilationUnit(t);
            PhasedUnit phasedUnit = new PhasedUnit(cu,p);
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
            if ( MODULE_FILE.equals( file.getName() ) ) {
                context.defineModule();
            }
        }
        for (File file: files) {
            parseFileOrDirectory(file, context);
        }
        context.pop();
    }

}

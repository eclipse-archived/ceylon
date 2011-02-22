import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.Builder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;

/**
 * @author Gavin King <gavin@hibernate.org>
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Main {

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
        
        boolean noisy = "true".equals(System.getProperties().getProperty("verbose"));

        final TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(noisy)
                .addSrcDirectory(new File(path))
                .getTypeChecker();
        //getting the type checker does process all types in the source directory
    }
}

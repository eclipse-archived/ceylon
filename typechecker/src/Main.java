import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.redhat.ceylon.compiler.TypeChecker;
import com.redhat.ceylon.compiler.TypeCheckerBuilder;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.context.PhasedUnit;
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

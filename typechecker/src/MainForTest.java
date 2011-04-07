import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

import java.io.File;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForTest {
    /**
     * Files that are not under a proper module structure are placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {

        TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test") )
                .getTypeChecker();
        Tree.CompilationUnit compilationUnit = typeChecker.getCompilationUnitFromRelativePath("ceylon/language/Object.ceylon");
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for files in .src");
        }
        compilationUnit = typeChecker.getCompilationUnitFromRelativePath("capture/Capture.ceylon");
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for files in real src dir");
        }
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test/capture") )
                .getTypeChecker();
        compilationUnit = typeChecker.getCompilationUnitFromRelativePath("Capture.ceylon");
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for top level files (no package) in real src dir");
        }
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("languagesrc/final") )
                .getTypeChecker();
    }
}

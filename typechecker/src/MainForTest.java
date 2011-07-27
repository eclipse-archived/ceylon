import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.model.Module;
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
        typeChecker.process();
        Tree.CompilationUnit compilationUnit = typeChecker.getCompilationUnitFromRelativePath("ceylon/language/Object.ceylon");
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for files in .src");
        }
        compilationUnit = typeChecker.getCompilationUnitFromRelativePath("capture/Capture.ceylon");
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for files in real src dir");
        }
        compilationUnit = typeChecker.getCompilationUnitFromRelativePath("com/redhat/sample/multisource/Boo.ceylon");
        Module module = compilationUnit.getUnit().getPackage().getModule();
        if ( !"com.redhat.sample.multisource".equals( module.getNameAsString() ) ) {
            throw new RuntimeException("Unable to extract module name");
        }
        if ( !"0.2".equals( module.getVersion() ) ) {
            throw new RuntimeException("Unable to extract module version");
        }
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test/capture") )
                .getTypeChecker();
        typeChecker.process();
        compilationUnit = typeChecker.getCompilationUnitFromRelativePath("Capture.ceylon");
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for top level files (no package) in real src dir");
        }
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("languagesrc/current") )
                .getTypeChecker();
        typeChecker.process();
    }
}

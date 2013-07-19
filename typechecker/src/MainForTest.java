import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
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
     * Files that are not under a proper module structure are 
     * placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        TypeChecker typeChecker = new TypeCheckerBuilder()
                .statistics(true)
                .verbose(false)
                .addSrcDirectory( new File("test/main") )
                .getTypeChecker();
        typeChecker.process();
        Tree.CompilationUnit compilationUnit = typeChecker.getPhasedUnitFromRelativePath("ceylon/language/Object.ceylon").getCompilationUnit();
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for files in .src");
        }
        compilationUnit = typeChecker.getPhasedUnitFromRelativePath("capture/Capture.ceylon").getCompilationUnit();
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for files in real src dir");
        }
        compilationUnit = typeChecker.getPhasedUnitFromRelativePath("com/redhat/sample/multisource/Boo.ceylon").getCompilationUnit();
        Module module = compilationUnit.getUnit().getPackage().getModule();
        if ( !"com.redhat.sample.multisource".equals( module.getNameAsString() ) ) {
            throw new RuntimeException("Unable to extract module name");
        }
        if ( !"0.2".equals( module.getVersion() ) ) {
            throw new RuntimeException("Unable to extract module version");
        }
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test/main/capture") )
                .getTypeChecker();
        typeChecker.process();
        compilationUnit = typeChecker.getPhasedUnitFromRelativePath("Capture.ceylon").getCompilationUnit();
        if ( compilationUnit == null ) {
            throw new RuntimeException("Failed to pass getCompilationUnitFromRelativePath for top level files (no package) in real src dir");
        }

        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test/moduledep1") )
                .addSrcDirectory( new File("test/moduledep2") )
                .addSrcDirectory( new File("test/moduletest") )
                .getTypeChecker();
        typeChecker.process();

        ClosableVirtualFile latestZippedLanguageSourceFile = MainHelper.getLatestZippedLanguageSourceFile();
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( latestZippedLanguageSourceFile )
                .getTypeChecker();
        typeChecker.process();
        latestZippedLanguageSourceFile.close();
        System.out.println("Tests took " + ( (System.nanoTime()-start) / 1000000 ) + " ms");
    }
}

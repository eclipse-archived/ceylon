package main;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForLanguage {
    /**
     * Files that are not under a proper module structure are 
     * placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        ClosableVirtualFile latestZippedLanguageSourceFile = MainHelper.getLatestZippedLanguageSourceFile();
        new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(latestZippedLanguageSourceFile)
                .getTypeChecker()
                .process();
        latestZippedLanguageSourceFile.close();
    }

}

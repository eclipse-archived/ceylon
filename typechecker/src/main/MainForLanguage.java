package main;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.cmr.impl.LeakingLogger;

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
        ClosableVirtualFile latestZippedLanguageSourceFile = 
                MainHelper.getLatestZippedLanguageSourceFile();
        RepositoryManager repositoryManager = CeylonUtils.repoManager()
                .systemRepo("../dist/dist/repo")
                .logger(new LeakingLogger())
                .buildManager();
        new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(latestZippedLanguageSourceFile)
                .setRepositoryManager(repositoryManager)
                .getTypeChecker()
                .process();
        latestZippedLanguageSourceFile.close();
    }

}

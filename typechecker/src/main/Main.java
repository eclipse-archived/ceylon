package main;
import java.io.File;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.io.cmr.impl.LeakingLogger;

/**
 * Entry point for the type checker. Pass the source directory 
 * as a parameter. The source directory is relative to the 
 * startup directory.
 *
 * @author Gavin King <gavin@hibernate.org>
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Main {

    /**
     * Files that are not under a proper module structure are 
     * placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        if ( args.length==0 ) {
            System.err.println("Usage Main <directoryNames>");
            System.exit(-1);
            return;
        }
        
        RepositoryManager repositoryManager = 
                CeylonUtils.repoManager()
                    .systemRepo("../dist/dist/repo")
                    .logger(new LeakingLogger())
                    .buildManager();
        
        String verbose = 
                System.getProperties().getProperty("verbose");
        //ClosableVirtualFile latestZippedLanguageSourceFile = 
        //        MainHelper.getLatestZippedLanguageSourceFile();
        TypeCheckerBuilder tcb = 
                new TypeCheckerBuilder()
                    .setRepositoryManager(repositoryManager)
                    .verbose("true".equals(verbose))
                    .statistics(true);
                //.addSrcDirectory(latestZippedLanguageSourceFile);
        for (String path: args) {
            tcb.addSrcDirectory(new File(path));
        }
        tcb.getTypeChecker().process();
        //latestZippedLanguageSourceFile.close();
    }
}

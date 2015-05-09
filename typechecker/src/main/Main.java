package main;
import java.io.File;

import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

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
        
        boolean noisy = "true".equals(System.getProperties().getProperty("verbose"));
        //ClosableVirtualFile latestZippedLanguageSourceFile = MainHelper.getLatestZippedLanguageSourceFile();
        TypeCheckerBuilder tcb = new TypeCheckerBuilder()
                .verbose(noisy)
                .statistics(true);
                //.addSrcDirectory(latestZippedLanguageSourceFile);
        for (String path: args) {
            tcb.addSrcDirectory(new File(path));
        }
        tcb.getTypeChecker().process();
        //latestZippedLanguageSourceFile.close();
    }
}

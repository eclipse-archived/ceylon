import java.io.File;

import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

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

        new TypeCheckerBuilder()
                .verbose(noisy)
                .addSrcDirectory(new File(path))
                .getTypeChecker();
        //getting the type checker does process all types in the source directory
    }
}

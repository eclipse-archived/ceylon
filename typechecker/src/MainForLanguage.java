import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

import java.io.File;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForLanguage {
    /**
     * Files that are not under a proper module structure are placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {

        final TypeChecker typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory(new File("languagesrc/current"))
                .getTypeChecker();
        System.out.print("done");
    }
}

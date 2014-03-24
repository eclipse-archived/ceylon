import java.io.File;
import java.util.ArrayList;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForJsTest {
    
    public static void main(String[] args) throws Exception {
        final Options opts = new Options().addRepo("build/runtime").outDir("build/test/opt_modules");
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .cwd(opts.getCwd())
                .systemRepo(opts.getSystemRepo())
                .userRepos(opts.getRepos())
                .outRepo(opts.getOutDir())
                .buildManager();
        System.out.println("Typechecking Ceylon test code...");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().verbose(false)
            .moduleManagerFactory(new JsModuleManagerFactory(null))
            .usageWarnings(false);
        final ArrayList<File> excludes = new ArrayList<>();
        for (String dir : args) {
            final File d = new File(dir.startsWith("X:")?dir.substring(2):dir);
            if (dir.startsWith("X:")) {
                excludes.add(d);
            } else {
                tcb.addSrcDirectory(d);
            }
        }
        tcb.setRepositoryManager(repoman);
        TypeChecker typeChecker = tcb.getTypeChecker();
        for (File x : excludes) {
            final String ap = x.getPath();
            for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
                if (pu.getUnit().getFullPath().startsWith(ap)) {
                    typeChecker.getPhasedUnits().removePhasedUnitForRelativePath(pu.getPathRelativeToSrcDir());
                }
            }
        }
        typeChecker.process();
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
        System.out.println("Compiling in prototype style");
        JsCompiler jsc = new JsCompiler(typeChecker, opts).stopOnErrors(true);
        if (!jsc.generate()) {
            jsc.printErrorsAndCount(System.out);
        }
        System.out.println("Compiling in lexical scope style");
        jsc = new JsCompiler(typeChecker, opts.optimize(false).outDir("build/test/modules")).stopOnErrors(false);
        if (!jsc.generate()) {
            jsc.printErrors(System.out);
        }
    }

}

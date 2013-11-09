import java.io.File;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

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
        for (String dir : args) {
            tcb.addSrcDirectory(new File(dir));
        }
        tcb.setRepositoryManager(repoman);
        TypeChecker typeChecker = tcb.getTypeChecker();
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

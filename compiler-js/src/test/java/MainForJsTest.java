import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.model.typechecker.context.ProducedTypeCache;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForJsTest {
    
    public static void main(String[] args) throws Exception {
        final Options opts = new Options().addRepo("build/runtime").outRepo("build/test/proto");
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .cwd(opts.getCwd())
                .systemRepo(opts.getSystemRepo())
                .userRepos(opts.getRepos())
                .outRepo(opts.getOutRepo())
                .buildManager();
        System.out.println("Typechecking Ceylon test code...");
        JsModuleManagerFactory.setVerbose(true);
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().verbose(false)
            .moduleManagerFactory(new JsModuleManagerFactory(null))
            .usageWarnings(false);
        final ArrayList<File> excludes = new ArrayList<>();
        final ArrayList<File> resfiles = new ArrayList<>();
        final ArrayList<File> resdirs = new ArrayList<>();
        for (String dir : args) {
            final File d = new File(dir.charAt(1)==':'?dir.substring(2):dir);
            if (dir.startsWith("X:")) {
                excludes.add(d);
            } else if (dir.startsWith("R:")) {
                resdirs.add(d);
            } else if (dir.startsWith("r:")) {
                resfiles.add(d);
            } else {
                tcb.addSrcDirectory(d);
                opts.addSrcDir(d);
            }
        }
        tcb.setRepositoryManager(repoman);
        TypeChecker typeChecker = tcb.getTypeChecker();
        for (File x : excludes) {
            String ap = x.getPath();
            //Fix for Windows
            if ('/' != File.separatorChar) {
                ap = ap.replace(File.separatorChar, '/');
            }
            for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
                if (pu.getUnit().getFullPath().startsWith(ap)) {
                    typeChecker.getPhasedUnits().removePhasedUnitForRelativePath(pu.getPathRelativeToSrcDir());
                }
            }
        }
        ProducedTypeCache.setEnabled(false);
        typeChecker.process();
        ProducedTypeCache.setEnabled(true);
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
        System.out.println("Compiling in prototype style");
        opts.resourceDirs(resdirs);
        JsCompiler jsc = new JsCompiler(typeChecker, opts).stopOnErrors(true);
        jsc.setResourceFiles(resfiles);
        PrintWriter writer = new PrintWriter(System.out);
        if (!jsc.generate()) {
            jsc.printErrorsAndCount(writer);
        }
        System.out.println("Compiling in lexical scope style");
        jsc = new JsCompiler(typeChecker, opts.optimize(false).outRepo("build/test/lexical")).stopOnErrors(false);
        jsc.setResourceFiles(resfiles);
        if (!jsc.generate()) {
            jsc.printErrors(writer);
        }
    }

}

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.eclipse.ceylon.compiler.typechecker.context.PhasedUnit;
import org.eclipse.ceylon.model.typechecker.context.TypeCache;

import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.compiler.js.loader.JsModuleManagerFactory;
import org.eclipse.ceylon.compiler.js.util.Options;

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
        final TypeChecker typeChecker = tcb.getTypeChecker();
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
        TypeCache.doWithoutCaching(new Runnable() {
            @Override
            public void run() {
                typeChecker.process();
            }
        });
        if (typeChecker.getErrors() > 0) {
            System.exit(1);
        }
        System.out.println("Compiling in prototype style");
        opts.resourceDirs(resdirs);
        JsCompiler jsc = new JsCompiler(typeChecker, opts).stopOnErrors(true);
        ArrayList<File> sources = new ArrayList<>();
        for (String dir : args) {
            //The arg is src/test/ceylon for example
            addFilesToList(new File(dir),sources);
        }
        Collections.sort(sources);
        jsc.setSourceFiles(sources);
        jsc.setResourceFiles(resfiles);
        PrintWriter writer = new PrintWriter(System.out);
        if (!jsc.generate()) {
            jsc.printErrorsAndCount(writer);
        }
    }

    private static void addFilesToList(File dir, ArrayList<File> list) {
        if (dir.exists() && dir.isDirectory()) {
            for (File sd: dir.listFiles()) {
                final String fname = sd.getName();
                if (sd.isFile() && (fname.endsWith(".js") || fname.endsWith(".ceylon"))) {
                    list.add(sd);
                    if (fname.endsWith(".js")) {
                        System.out.println("Adding JS file " + sd);
                    }
                } else {
                    addFilesToList(sd, list);
                }
            }
        }
    }

}

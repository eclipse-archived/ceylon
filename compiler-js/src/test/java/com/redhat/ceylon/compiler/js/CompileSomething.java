package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.common.tool.EnumUtil;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.loader.JsModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.model.typechecker.context.TypeCache;

/** Just a little program to compile something from within the IDE.
 * 
 * @author Enrique Zamudio
 */
public class CompileSomething {

    public static void main(String[] x) throws IOException {
        final Options opts = new Options().outRepo("/tmp/modules").addRepo("build/runtime")
                .addRepo("../ceylon.ast/modules")
                //.addRepo("../ceylon-sdk/modules").addRepo("build/test/proto")
                .optimize(true).verbose("all")
                .generateSourceArchive(false)
                .suppressWarnings(EnumUtil.enumsFromStrings(Warning.class, Arrays.asList("unusedImport")))
                .addSrcDir("/tmp/source");
        final TypeCheckerBuilder tcb = new TypeCheckerBuilder().statistics(false).encoding("UTF-8");
        for (File sd : opts.getSrcDirs()) {
            tcb.addSrcDirectory(sd);
        }
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .systemRepo(opts.getSystemRepo())
                .userRepos(opts.getRepos())
                .outRepo(opts.getOutRepo())
                .buildManager();
        tcb.setRepositoryManager(repoman);
        JsModuleManagerFactory.setVerbose(true);
        tcb.moduleManagerFactory(new JsModuleManagerFactory("UTF-8"));
        final TypeChecker tc = tcb.getTypeChecker();
        TypeCache.setEnabled(false);
        tc.process(true);
        TypeCache.setEnabled(true);
        final JsCompiler jsc = new JsCompiler(tc, opts);
        /*for (File sd : new File("/tmp/source").listFiles()) {
            if (sd.isFile() && sd.getName().endsWith(".js")) {
                System.out.println("Especificando archivos para incluir fuentes js");
                jsc.setSourceFiles(Arrays.asList(new File("/tmp/source").listFiles()));
                break;
            }
        }*/
        jsc.stopOnErrors(true);
        boolean ok = jsc.generate();
        jsc.printErrors(new java.io.PrintWriter(System.out));
        if (ok) {
            System.out.println("OK");
        } else {
            System.out.println("EXIT CODE: " + jsc.getExitCode());
        }
    }

}

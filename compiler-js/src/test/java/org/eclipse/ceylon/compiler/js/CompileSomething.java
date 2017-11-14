/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.common.tool.EnumUtil;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.model.typechecker.context.TypeCache;
import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.compiler.js.loader.JsModuleManagerFactory;
import org.eclipse.ceylon.compiler.js.util.Options;

/** Just a little program to compile something from within the IDE.
 * 
 * @author Enrique Zamudio
 */
public class CompileSomething {

    public static void main(String[] x) throws IOException {
        final Options opts = new Options().outRepo("/tmp/modules").addRepo("compiler-js/build/runtime")
                .addRepo("../ceylon.ast/modules")
                .addRepo("../ceylon-sdk/modules").addRepo("compiler-js/build/test/proto")
                .addRepo("npm:")
                .optimize(true).verbose("all")
                .generateSourceArchive(false)
                .suppressWarnings(EnumUtil.enumsFromStrings(Warning.class, Arrays.asList("unusedImport")))
                //.addSrcDir("/tmp/issue5789/source2").addSrcDir("/tmp/issue5789/source");
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
        ArrayList<File> individualSources = new ArrayList<>();
        for (File srcdir : opts.getSrcDirs()) {
            for (File sd : srcdir.listFiles()) {
                if (sd.isFile() && sd.getName().endsWith(".js") || !individualSources.isEmpty()) {
                    System.out.println("Especificando archivos para incluir fuentes js");
                    individualSources.addAll(Arrays.asList(srcdir.listFiles()));
                    break;
                }
            }
        }
        if (!individualSources.isEmpty()) {
            jsc.setSourceFiles(individualSources);
        }
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

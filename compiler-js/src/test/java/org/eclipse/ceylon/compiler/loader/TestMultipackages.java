/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.loader;

import java.io.IOException;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.compiler.js.loader.JsModuleManagerFactory;
import org.eclipse.ceylon.compiler.js.util.Options;

//Tests for issue 195
public class TestMultipackages {

    private static RepositoryManager repoman;

    @BeforeClass
    public static void sharedSetup() {
        Options options = new Options().addRepo("build/test/test_modules").outRepo("build/test/test_modules")
                .addSrcDir("multi/1.0.0").addSrcDir("src/test/resources/loader");
        repoman = CeylonUtils.repoManager()
                .cwd(options.getCwd())
                .systemRepo(options.getSystemRepo())
                .userRepos(options.getRepos())
                .outRepo(options.getOutRepo())
                .buildManager();
    }

    @Before
    public void setup() throws IOException {
        //Compile module with 2 packages
        System.out.println("Compiling multi");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.addSrcDirectory(new java.io.File("src/test/resources/multi/pass1"));
        tcb.setRepositoryManager(repoman);
        TypeChecker tc = tcb.getTypeChecker();
        tc.process();
        Options options = new Options().addRepo("build/test/test_modules").outRepo("build/test/test_modules")
                .addSrcDir("src/test/resources/multi/pass1");
        JsCompiler compiler = new JsCompiler(tc, options);
        compiler.stopOnErrors(false);
        compiler.generate();
    }

    @Test
    public void test() throws IOException {
        //Compile module with 2 packages
        System.out.println("Compiling usemulti");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.moduleManagerFactory(new JsModuleManagerFactory("UTF-8"));
        tcb.addSrcDirectory(new java.io.File("src/test/resources/multi/pass2"));
        tcb.setRepositoryManager(repoman);
        TypeChecker tc = tcb.getTypeChecker();
        tc.process();
        Options options = new Options().addRepo("build/test/test_modules").outRepo("build/test/test_modules")
                .addSrcDir("src/test/resources/multi/pass2").verbose("");
        JsCompiler compiler = new JsCompiler(tc, options);
        compiler.stopOnErrors(false);
        compiler.generate();
    }

}

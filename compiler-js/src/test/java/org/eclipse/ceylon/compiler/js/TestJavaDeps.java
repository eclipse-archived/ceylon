/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
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

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.junit.Test;
import org.eclipse.ceylon.compiler.js.JsCompiler;
import org.eclipse.ceylon.compiler.js.util.Options;

public class TestJavaDeps {

    @Test
    public void testJavaDependencies() throws IOException {
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .systemRepo("../dist/dist/repo")
                .outRepo("test-modules")
                .buildManager();
        final TypeCheckerBuilder builder = new TypeCheckerBuilder()
            .setRepositoryManager(repoman)
            .addSrcDirectory(new File("src/test/resources/javadeps"));
        final TypeChecker tc = builder.getTypeChecker();
        tc.process();
        final Options opts = new Options()
                .addSrcDir("src/test/resources/javadeps")
                .outRepo("./build")
                .comment(false)
                .generateSourceArchive(false)
                .encoding("UTF-8");
        final JsCompiler comp = new JsCompiler(tc, opts);
        comp.generate();
    }

}

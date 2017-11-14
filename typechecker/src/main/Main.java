/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package main;
import java.io.File;

import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.eclipse.ceylon.compiler.typechecker.io.cmr.impl.LeakingLogger;

/**
 * Entry point for the type checker. Pass the source directory 
 * as a parameter. The source directory is relative to the 
 * startup directory.
 *
 * @author Gavin King <gavin@hibernate.org>
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Main {

    /**
     * Files that are not under a proper module structure are 
     * placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        if ( args.length==0 ) {
            System.err.println("Usage Main <directoryNames>");
            System.exit(-1);
            return;
        }
        
        RepositoryManager repositoryManager = 
                CeylonUtils.repoManager()
                    .systemRepo("../dist/dist/repo")
                    .logger(new LeakingLogger())
                    .buildManager();
        
        String verbose = 
                System.getProperties().getProperty("verbose");
        //ClosableVirtualFile latestZippedLanguageSourceFile = 
        //        MainHelper.getLatestZippedLanguageSourceFile();
        TypeCheckerBuilder tcb = 
                new TypeCheckerBuilder()
                    .setRepositoryManager(repositoryManager)
                    .verbose("true".equals(verbose))
                    .statistics(true);
                //.addSrcDirectory(latestZippedLanguageSourceFile);
        for (String path: args) {
            tcb.addSrcDirectory(new File(path));
        }
        tcb.getTypeChecker().process();
        //latestZippedLanguageSourceFile.close();
    }
}

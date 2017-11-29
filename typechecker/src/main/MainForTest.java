/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
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
import org.eclipse.ceylon.compiler.typechecker.TypeChecker;
import org.eclipse.ceylon.compiler.typechecker.TypeCheckerBuilder;
import org.eclipse.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import org.eclipse.ceylon.compiler.typechecker.io.cmr.impl.LeakingLogger;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.model.typechecker.model.Module;

/**
 * Some hack before a proper unit test harness is put in place
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class MainForTest {
    /**
     * Files that are not under a proper module structure are 
     * placed under a <nomodule> module.
     */
    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        
        RepositoryManager repositoryManager = CeylonUtils.repoManager()
                .systemRepo("../dist/dist/repo")
                .outRepo("test/modules")
                .logger(new LeakingLogger())
                .buildManager();
        
        TypeChecker typeChecker = new TypeCheckerBuilder()
                .statistics(true)
                .verbose(false)
                .addSrcDirectory( new File("test/main") )
                .setRepositoryManager(repositoryManager)
                .getTypeChecker();
        typeChecker.process();
        int errors = typeChecker.getErrors();
        Tree.CompilationUnit compilationUnit = 
                typeChecker.getPhasedUnitFromRelativePath(
                        "ceylon/language/Object.ceylon")
                    .getCompilationUnit();
        if ( compilationUnit == null ) {
            throw new RuntimeException(
                    "Failed to pass getCompilationUnitFromRelativePath for files in .src");
        }
        compilationUnit = 
                typeChecker.getPhasedUnitFromRelativePath(
                        "capture/Capture.ceylon")
                    .getCompilationUnit();
        if ( compilationUnit == null ) {
            throw new RuntimeException(
                    "Failed to pass getCompilationUnitFromRelativePath for files in real src dir");
        }
        compilationUnit = 
                typeChecker.getPhasedUnitFromRelativePath(
                        "org/eclipse/sample/multisource/Boo.ceylon")
                    .getCompilationUnit();
        Module module = compilationUnit.getUnit().getPackage().getModule();
        if ( !"org.eclipse.sample.multisource".equals( module.getNameAsString() ) ) {
            throw new RuntimeException("Unable to extract module name");
        }
        if ( !"0.2".equals( module.getVersion() ) ) {
            throw new RuntimeException("Unable to extract module version");
        }
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test/main/capture") )
                .setRepositoryManager(repositoryManager)
                .getTypeChecker();
        typeChecker.process();
        errors += typeChecker.getErrors();
        compilationUnit = 
                typeChecker.getPhasedUnitFromRelativePath(
                        "Capture.ceylon")
                    .getCompilationUnit();
        if ( compilationUnit == null ) {
            throw new RuntimeException(
                    "Failed to pass getCompilationUnitFromRelativePath for top level files (no package) in real src dir");
        }

        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( new File("test/moduledep1") )
                .addSrcDirectory( new File("test/moduledep2") )
                .addSrcDirectory( new File("test/moduletest") )
                .addSrcDirectory( new File("test/restricted") )
                .setRepositoryManager(repositoryManager)
                .getTypeChecker();
        typeChecker.process();
        errors += typeChecker.getErrors();

        ClosableVirtualFile latestZippedLanguageSourceFile = 
                MainHelper.getLatestZippedLanguageSourceFile();
        typeChecker = new TypeCheckerBuilder()
                .verbose(false)
                .addSrcDirectory( latestZippedLanguageSourceFile )
                .setRepositoryManager(repositoryManager)
                .getTypeChecker();
        typeChecker.process();
        errors += typeChecker.getErrors();
        latestZippedLanguageSourceFile.close();
        System.out.println("Tests took " + ( (System.nanoTime()-start) / 1000000 ) + " ms");
        
        if (errors > 0) {
            System.exit(1);
        }
    }
}

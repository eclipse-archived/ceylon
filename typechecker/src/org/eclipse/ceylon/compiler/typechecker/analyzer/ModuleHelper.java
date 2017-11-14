/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import java.util.LinkedList;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.ceylon.CeylonUtils;
import org.eclipse.ceylon.compiler.typechecker.exceptions.LanguageModuleNotFoundException;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleHelper {
    private ModuleHelper() {}

    public static void buildErrorOnMissingArtifact(
            ArtifactContext artifactContext,
            Module module,
            ModuleImport moduleImport,
            LinkedList<Module> dependencyTree,
            Exception exceptionOnGetArtifact,
            ModuleSourceMapper moduleManagerUtil,
            boolean isError) {
        StringBuilder error = new StringBuilder("cannot find module ");
        if (CeylonUtils.arrayContains(artifactContext.getSuffixes(), ArtifactContext.SRC)) {
            error.append("source ");
        }
        error.append("artifact ");
        error.append("'").append( artifactContext.toString() ).append("'");
        if ( exceptionOnGetArtifact != null ) {
            String message = exceptionOnGetArtifact.getMessage();
            if (message==null) {
                message = exceptionOnGetArtifact.getClass().getName();
            }
            error.append( "\ndue to connection error: " + message );
        }
        
        /*List<String> repos = 
                moduleManagerUtil.getContext()
                    .getRepositoryManager()
                    .getRepositoriesDisplayString();
        error.append("\n\t- in repositories:");
        for (String repo: repos) {
            error.append("\n\t  ");
            error.append(repo);
        }*/
        
        error.append("\n\t- dependency tree: ");
        buildDependencyString(dependencyTree, module, error);
        if ( moduleManagerUtil.getContext().getModules().getLanguageModule() == module) {
            error.append("\n\tget ceylon.language and run 'ant publish' (more information at http://ceylon-lang.org/code/source/#ceylonlanguage_module)");
            //ceylon.language is essential to the type checker
            throw new LanguageModuleNotFoundException(error.toString());
        }
        else {
            //today we attach that to the module dependency
            moduleManagerUtil.attachErrorToDependencyDeclaration(moduleImport, dependencyTree, error.toString(), isError);
        }
    }

    public static void buildDependencyString(LinkedList<Module> dependencyTree, Module module, StringBuilder error) {
        for (Module errorModule : dependencyTree) {
            appendModuleDesc(errorModule, error);
            error.append(" -> ");
        }
        appendModuleDesc(module, error);
    }

    private static void appendModuleDesc(Module module, StringBuilder error) {
        if (module.isDefaultModule()) {
            error.append("default module");
        }
        else {
            error.append("'")
                .append(module.getNameAsString())
                .append("/")
                .append(module.getVersion())
                .append("'");
        }
    }
}

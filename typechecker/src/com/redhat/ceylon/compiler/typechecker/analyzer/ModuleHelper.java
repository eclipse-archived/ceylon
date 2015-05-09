package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.LinkedList;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.typechecker.exceptions.LanguageModuleNotFoundException;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;

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
            ModuleManager moduleManager) {
        StringBuilder error = new StringBuilder("cannot find module ");
        if (CeylonUtils.arrayContains(artifactContext.getSuffixes(), ArtifactContext.SRC)) {
            error.append("source ");
        }
        error.append("artifact ");
        error.append( artifactContext.toString() );
        if ( exceptionOnGetArtifact != null ) {
            error.append( "\ndue to connection error: " + exceptionOnGetArtifact.getMessage() );
        }
        //FIXME add list of repositories when ceylon/ceylon-module-resolver/issues/26 is fixed
//                    error.append("\n\t  in repositories : ");
//                    if (artifactProviders.size() > 0) {
//                        error.append(artifactProviders.get(0));
//                    }
//                    if (artifactProviders.size() > 1) {
//                        for (ArtifactProvider searchedProvider : artifactProviders.subList(1, artifactProviders.size())) {
//                            error.append(", ");
//                            error.append("\n\t");
//                            error.append(searchedProvider);
//                        }
//                    }
        error.append("\n\t- dependency tree: ");
        buildDependencyString(dependencyTree, module, error);
        if ( module.getLanguageModule() == module ) {
            error.append("\n\tget ceylon.language and run 'ant publish' (more information at http://ceylon-lang.org/code/source/#ceylonlanguage_module)");
            //ceylon.language is essential to the type checker
            throw new LanguageModuleNotFoundException(error.toString());
        }
        else {
            //today we attach that to the module dependency
            moduleManager.attachErrorToDependencyDeclaration(moduleImport, dependencyTree, error.toString());
        }
    }

    public static void buildDependencyString(LinkedList<Module> dependencyTree, Module module, StringBuilder error) {
        for (Module errorModule : dependencyTree) {
            error.append(errorModule.getNameAsString())
                .append("/").append(errorModule.getVersion())
                .append(" -> ");
        }
        error.append(module.getNameAsString())
            .append("/").append(module.getVersion());
    }
}

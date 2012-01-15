package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.exceptions.LanguageModuleNotFoundException;
import com.redhat.ceylon.compiler.typechecker.io.VFSArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;

/**
 * Validate module dependency:
 *  - make sure all modules are available
 *  - get modules from local or remote repos if necessary
 *  - parse and process external modules
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleValidator {
    private final Context context;
    private List<PhasedUnits> phasedUnitsOfDependencies;
    private final ModuleManager moduleManager;

    
    public ModuleValidator(Context context, PhasedUnits phasedUnits) {
        this.context = context;
        this.moduleManager = phasedUnits.getModuleManager();
    }

    public List<PhasedUnits> getPhasedUnitsOfDependencies() {
        return phasedUnitsOfDependencies;
    }

    /**
     *  At this stage we need to
     *  - resolve all non local modules (recursively)
     *  - build the object model of these compiled modules
     *  - declare a missing module as an error
     *  - detect circular dependencies
     *  - detect module version conflicts
     */
    public void verifyModuleDependencyTree() {
        phasedUnitsOfDependencies = new ArrayList<PhasedUnits>();
        LinkedList<Module> dependencyTree = new LinkedList<Module>();
        //copy modules collection as new (dependent) modules might be added on the fly.
        final HashSet<Module> modulesCopy = new HashSet<Module>( context.getModules().getListOfModules() );
        for (Module module : modulesCopy) {
            dependencyTree.addLast(module);
            //we don't care about propagated dependency here as top modules are independent from one another
            verifyModuleDependencyTree(module.getImports(), dependencyTree, new ArrayList<Module>());
            dependencyTree.pollLast();
        }
        executeExternalModulePhases();
    }

    private void verifyModuleDependencyTree(
            Collection<ModuleImport> moduleImports,
            LinkedList<Module> dependencyTree,
            List<Module> propagatedDependencies) {
        List<Module> visibleDependencies = new ArrayList<Module>();
        visibleDependencies.add(dependencyTree.getLast()); //first addition => no possible conflict
        for (ModuleImport moduleImport : moduleImports) {
            Module module = moduleImport.getModule();
            if (moduleManager.findModule(module, dependencyTree, true) != null) {
                //circular dependency
                StringBuilder error = new StringBuilder("Circular dependency between modules: ");
                buildDependencyString(dependencyTree, module, error);
                error.append(".");
                //TODO is there a better place than the top level module triggering the error?
                //nested modules might not have representations in the src tree
                moduleManager.addErrorToModule( dependencyTree.getFirst(), error.toString() );
                return;
            }
            Set<String> searchedArtifacts = new HashSet<String>();
            Iterable<String> searchedArtifactExtensions = moduleManager.getSearchedArtifactExtensions();
            
            if ( ! module.isAvailable() ) {
                //try and load the module from the repository
                VirtualFile artifact = null;
                List<ArtifactProvider> artifactProviders = context.getArtifactProviders();
                for (final ArtifactProvider artifactProvider : artifactProviders) {
                    // this really is just for error messages
                    searchedArtifacts.add(VFSArtifactProvider.getArtifactName(module.getName(), 
                            module.getVersion(), "*"));
                    artifact = artifactProvider.getArtifact(module.getName(), 
                            module.getVersion(), 
                            searchedArtifactExtensions);
                    if (artifact != null) {
                        break;
                    }
                }
                if (artifact == null) {
                    //not there => error
                    StringBuilder error = new StringBuilder("Cannot find module artifact(s) : ");
                    List<String> searchedArtifactList = new ArrayList<String>(searchedArtifacts.size());
                    searchedArtifactList.addAll(searchedArtifacts);
                    if (searchedArtifactList.size() > 0) {
                        error.append(searchedArtifactList.get(0));
                    }
                    if (searchedArtifacts.size() > 1) {
                        for (String searchedArtifact : searchedArtifactList.subList(1, searchedArtifactList.size())) {
                            error.append(", ");
                            error.append("\n\t");
                            error.append(searchedArtifact);
                        }
                    }
                    error.append("\n\t  in repositories : ");
                    if (artifactProviders.size() > 0) {
                        error.append(artifactProviders.get(0));
                    }
                    if (artifactProviders.size() > 1) {
                        for (ArtifactProvider searchedProvider : artifactProviders.subList(1, artifactProviders.size())) {
                            error.append(", ");
                            error.append("\n\t");
                            error.append(searchedProvider);
                        }
                    }
                    error.append("\n\tDependency tree: ");
                    buildDependencyString(dependencyTree, module, error);
                    error.append(".");
                    if ( module.getLanguageModule() == module ) {
                        error.append("\n\tGet ceylon.language and run 'ant publish' Get more information at http://ceylon-lang.org/code/source/#ceylonlanguage_module");
                        //ceylon.language is essential to the type checker
                        throw new LanguageModuleNotFoundException(error.toString());
                    }
                    else {
                        //today we attach that to the module dependency
                        moduleManager.attachErrorToDependencyDeclaration(moduleImport, error.toString());
                    }
                }
                else {
                    try {
                        //parse module units and build module dependency and carry on
                        moduleManager.resolveModule(module, artifact, phasedUnitsOfDependencies);
                    } finally {
                        if (artifact instanceof ClosableVirtualFile) {
                            ((ClosableVirtualFile)artifact).close();
                        }
                    }
                }
            }
            dependencyTree.addLast(module);
            List<Module> subModulePropagatedDependencies = new ArrayList<Module>();
            verifyModuleDependencyTree( module.getImports(), dependencyTree, subModulePropagatedDependencies );
            //visible dependency += subModule + subModulePropagatedDependencies
            checkAndAddDependency(visibleDependencies, module, dependencyTree);
            for (Module submodule : subModulePropagatedDependencies) {
                checkAndAddDependency(visibleDependencies, submodule, dependencyTree);
            }
            //propagated dependency += if subModule.export then subModule + subModulePropagatedDependencies
            if (moduleImport.isExport()) {
                checkAndAddDependency(propagatedDependencies, module, dependencyTree);
                for (Module submodule : subModulePropagatedDependencies) {
                    checkAndAddDependency(propagatedDependencies, submodule, dependencyTree);
                }
            }
            dependencyTree.pollLast();
        }
    }

    private void checkAndAddDependency(List<Module> dependencies, Module module, LinkedList<Module> dependencyTree) {
        Module dupe = moduleManager.findModule(module, dependencies, false);
        if (dupe != null && !isSameVersion(module, dupe)) {
            //TODO improve by giving the dependency string leading to these two conflicting modules
            StringBuilder error = new StringBuilder("Module (transitively) imports conflicting versions of ");
            error.append(module.getNameAsString())
                    .append(". Version ").append(module.getVersion())
                    .append(" and version ").append(dupe.getVersion())
                    .append(" found and visible at the same time.");
            moduleManager.addErrorToModule(dependencyTree.getFirst(), error.toString());
        }
        else {
            dependencies.add(module);
        }
    }

    private boolean isSameVersion(Module module, Module dupe) {
        if (module == null || dupe == null) return false;
        if (dupe.getVersion() == null) {
            System.err.println("TypeChecker assertion failure: version should not be null in " +
                    "ModuleValidator.isSameVersion. Please report the issue with a test case");
            return false;
        }
        return dupe.getVersion().equals(module.getVersion());
    }

    private void executeExternalModulePhases() {
        //moduleimport phase already done
        //Already called from within verifyModuleDependencyTree
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            for (PhasedUnit pu : units.getPhasedUnits()) {
                pu.scanDeclarations();
            }
        }
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            for (PhasedUnit pu : units.getPhasedUnits()) {
                pu.scanTypeDeclarations();
            }
        }
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            for (PhasedUnit pu : units.getPhasedUnits()) {
                pu.validateRefinement(); //TODO: only needed for type hierarchy view in IDE!
            }
        }
    }

    private void buildDependencyString(LinkedList<Module> dependencyTree, Module module, StringBuilder error) {
        for (Module errorModule : dependencyTree) {
            error.append(errorModule).append(" -> ");
        }
        error.append(module);
    }
}

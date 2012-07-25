package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
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
        moduleManager.addImplicitImports();
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
                ModuleHelper.buildDependencyString(dependencyTree, module, error);
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
                ArtifactResult artifact = null;
                RepositoryManager repositoryManager = context.getRepositoryManager();
                Exception exceptionOnGetArtifact = null;
                ArtifactContext artifactContext = null;
                for(String extension : searchedArtifactExtensions){
                    artifactContext = new ArtifactContext(module.getNameAsString(), module.getVersion(), "."+extension);
                    try {
                        artifact = repositoryManager.getArtifactResult(artifactContext);
                        if(artifact != null) 
                            break;
                    } catch (Exception e) {
                        exceptionOnGetArtifact = e;
                    }
                }
                if (artifact == null) {
                    //not there => error
                    ModuleHelper.buildErrorOnMissingArtifact(artifactContext, module, moduleImport, dependencyTree, exceptionOnGetArtifact, moduleManager);
                }
                else {
                    //parse module units and build module dependency and carry on
                    moduleManager.resolveModule(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies);
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

    protected void executeExternalModulePhases() {
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
}

package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.exceptions.LanguageModuleNotFoundException;
import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
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
    private final ModuleBuilder moduleBuilder;

    public ModuleValidator(Context context, PhasedUnits phasedUnits) {
        this.context = context;
        this.moduleBuilder = phasedUnits.getModuleBuilder();
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
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            executeExternalModulePhases(units);
        }
    }

    private void verifyModuleDependencyTree(
            Collection<ModuleImport> moduleImports,
            LinkedList<Module> dependencyTree,
            List<Module> propagatedDependencies) {
        List<Module> visibleDependencies = new ArrayList<Module>();
        visibleDependencies.add(dependencyTree.getLast()); //first addition => no possible conflict
        for (ModuleImport moduleImport : moduleImports) {
            Module module = moduleImport.getModule();
            if (moduleBuilder.findModule(module, dependencyTree, true) != null) {
                //circular dependency
                StringBuilder error = new StringBuilder("Circular dependency between modules: ");
                buildDependencyString(dependencyTree, module, error);
                error.append(".");
                //TODO is there a better place than the top level module triggering the error?
                //nested modules might not have representations in the src tree
                moduleBuilder.addErrorToModule( dependencyTree.getFirst(), error.toString() );
                return;
            }
            if ( ! module.isAvailable() ) {
                //try and load the module from the repository
                final ArtifactProvider artifactProvider = context.getArtifactProvider();
                final ClosableVirtualFile src = artifactProvider.getArtifact(module.getName(), module.getVersion(), "src");
                if (src == null) {
                    //not there => error
                    StringBuilder error = new StringBuilder("Cannot find module artifact ");
                    error.append( artifactProvider.getArtifactName(module.getName(), module.getVersion(), "src") )
                            .append(" in local repository ('~/.ceylon/repo')")
                            .append("\n\tDependency tree: ");
                    buildDependencyString(dependencyTree, module, error);
                    error.append(".");
                    if ( module.getLanguageModule() == module ) {
                        error.append("\n\tGet ceylon.language and run 'ant publish' Get more information at http://ceylon-lang.org/code/source/#ceylonlanguage_module");
                        //ceylon.language is essential to the type checker
                        throw new LanguageModuleNotFoundException(error.toString());
                    }
                    else {
                        //today we attach that to the module dependency
                        moduleBuilder.attachErrorToDependencyDeclaration(moduleImport, error.toString());
                    }
                }
                else {
                    //parse module units and build module dependency and carry on
                    PhasedUnits modulePhasedUnit = new PhasedUnits(context);
                    phasedUnitsOfDependencies.add(modulePhasedUnit);
                    modulePhasedUnit.parseUnit(src);
                    src.close();
                    module.setAvailable(true);  // TODO : not necessary anymore ? since at least on module.ceylon 
                                                //        should have been parsed and should be applied buildModuleImport()
                    final List<PhasedUnit> listOfUnits = modulePhasedUnit.getPhasedUnits();
                    //populate module.getDependencies()
                    moduleBuilder.visitModules(listOfUnits);
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
        Module dupe = moduleBuilder.findModule(module, dependencies, false);
        if (dupe != null) {
            //TODO improve by giving the dependency string leading to these two conflicting modules
            StringBuilder error = new StringBuilder("Module (transitively) imports conflicting versions of ");
            error.append(module.getNameAsString())
                    .append(". Version ").append(module.getVersion())
                    .append(" and version ").append(dupe.getVersion())
                    .append(" found and visible at the same time.");
            moduleBuilder.addErrorToModule(dependencyTree.getFirst(), error.toString());
        }
        else {
            dependencies.add(module);
        }
    }

    private void executeExternalModulePhases(PhasedUnits phasedUnits) {
        //moduleimport phase already done
        //Already called from within verifyModuleDependencyTree
        final List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        for (PhasedUnit pu : listOfUnits) {
            pu.scanDeclarations();
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.scanTypeDeclarations();
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.validateRefinement(); //TODO: only needed for type hierarchy view in IDE!
        }
    }

    private void buildDependencyString(LinkedList<Module> dependencyTree, Module module, StringBuilder error) {
        for (Module errorModule : dependencyTree) {
            error.append(errorModule).append(" -> ");
        }
        error.append(module);
    }
}

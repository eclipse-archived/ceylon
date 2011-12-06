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
     */
    public void verifyModuleDependencyTree() {
        phasedUnitsOfDependencies = new ArrayList<PhasedUnits>();
        LinkedList<Module> dependencyTree = new LinkedList<Module>();
        //copy modules collection as new (dependent) modules might be added on the fly.
        final HashSet<Module> modulesCopy = new HashSet<Module>( context.getModules().getListOfModules() );
        for (Module module : modulesCopy) {
            dependencyTree.addLast(module);
            verifyModuleDependencyTree( module.getDependencies(), dependencyTree );
            dependencyTree.pollLast();
        }
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            executeExternalModulePhases(units);
        }
    }

    private void verifyModuleDependencyTree(
            Collection<Module> modules,
            LinkedList<Module> dependencyTree) {
        for (Module module : modules) {
            if ( dependencyTree.contains(module) ) {
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
                final ClosableVirtualFile src = artifactProvider.getArtifact(module.getName(), "0.1", "src");
                if (src == null) {
                    //not there => error
                    StringBuilder error = new StringBuilder("Cannot find module artifact ");
                    error.append( artifactProvider.getArtifactName(module.getName(), "0.1", "src") )
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
                        moduleBuilder.addMissingDependencyError(module, error.toString());
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
            verifyModuleDependencyTree( module.getDependencies(), dependencyTree );
            dependencyTree.pollLast();
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

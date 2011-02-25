package com.redhat.ceylon.compiler.typechecker.context;

import com.redhat.ceylon.compiler.typechecker.io.ArtifactProvider;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.redhat.ceylon.compiler.typechecker.util.PrintUtil.importPathToString;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Context {
    public static final String MODULE_FILE = "module.ceylon";

    private LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module currentModule;
    private Module nomodule;
    private Module languageModule;
    private Set<Module> modules = new HashSet<Module>();
    private ArtifactProvider artifactProvider;

    public Context(VFS vfs) {
        this.artifactProvider = new ArtifactProvider(vfs);

        //build nomodule module: used by any package not declared within a module
        final Package pkg = new Package();
        pkg.setName( new ArrayList<String>(0) );
        packageStack.add(pkg);
        nomodule = new Module();
        final List<String> noModuleName = new ArrayList<String>();
        noModuleName.add("<nomodule>");
        nomodule.setName(noModuleName);
        nomodule.setAvailable(true);
        bindPackageToModule(pkg, nomodule);
        modules.add(nomodule);

        //create language module and add it as a dependency of nomodule
        //since packages outside a module cannot declare dependencies
        final List<String> languageName = new ArrayList<String>();
        languageName.add("ceylon");
        languageName.add("language");
        languageModule = new Module();
        languageModule.setName(languageName);
        modules.add(languageModule);
        nomodule.getDependencies().add(languageModule);
    }

    public Module getOrCreateModule(List<String> moduleName) {
        if (moduleName.size() == 0) {
            throw new RuntimeException("Module cannot be top level");
        }
        Module module = null;
        for (Module current : modules ) {
            final List<String> names = current.getName();
            if ( names.size() == moduleName.size()
                    && moduleName.containsAll(names) ) {
                module = current;
                break;
            }
        }
        if (module == null) {
            module = new Module();
            module.setName(moduleName);
            modules.add(module);
        }
        return module;
    }

    public void push(String path) {
        createPackageAndAddToModule(path);
    }

    private void createPackageAndAddToModule(String path) {
        Package pkg = new Package();
        final Package lastPkg = packageStack.peekLast();
        List<String> parentName = lastPkg.getName();
        final ArrayList<String> name = new ArrayList<String>(parentName.size() + 1);
        name.addAll( parentName );
        name.add(path);
        pkg.setName(name);
        if (currentModule != null) {
            bindPackageToModule(pkg, currentModule);
        }
        else {
            //bind package to nomodule
            bindPackageToModule(pkg, nomodule);
        }
        packageStack.addLast(pkg);
    }

    private void bindPackageToModule(Package pkg, Module module) {
        //undo nomodule setting if necessary
        if (pkg.getModule() != null) {
            pkg.getModule().getPackages().remove(pkg);
            pkg.setModule(null);
        }
        module.getPackages().add(pkg);
        pkg.setModule(module);
    }

    public void pop() {
        removeLastPackageAndModuleIfNecessary();
    }

    private void removeLastPackageAndModuleIfNecessary() {
        packageStack.pollLast();
        final boolean moveAboveModuleLevel = currentModule != null
                && currentModule.getName().size() > packageStack.size() -1; //first package is the empty package
        if (moveAboveModuleLevel) {
            currentModule = null;
        }
    }

    public void defineModule() {
        if ( currentModule == null ) {
            final Package currentPkg = packageStack.peekLast();
            final List<String> moduleName = currentPkg.getName();
            currentModule = getOrCreateModule(moduleName);
            currentModule.setAvailable(true);
            bindPackageToModule(currentPkg, currentModule);
        }
        else {
            StringBuilder error = new StringBuilder("Found two modules within the same hierarchy: '");
            error.append( importPathToString( currentModule.getName() ) )
                .append( "' and '" )
                .append( importPathToString( packageStack.peekLast().getName() ) )
                .append("'");
            System.err.println(error);
        }
    }

    public Package getPackage() {
        return packageStack.peekLast();
    }

    /**
     *  At this stage we need to
     *  - resolve all non local modules (recursively)
     *  - build the object model of these compiled modules
     *  - declare a missing module as an error
     *  - detect circular dependencies
     */
    public void verifyModuleDependencyTree() {
        List<PhasedUnits> phasedUnitsOfDependencies = new ArrayList<PhasedUnits>();
        LinkedList<Module> dependencyTree = new LinkedList<Module>();
        //copy modules collection as new (dependent) modules might be added on the fly.
        final HashSet<Module> modulesCopy = new HashSet<Module>(modules);
        for (Module module : modulesCopy) {
            dependencyTree.addLast(module);
            verifyModuleDependencyTree( module.getDependencies(), dependencyTree, phasedUnitsOfDependencies );
            dependencyTree.pollLast();
        }
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            executeExternalModulePhases(units);
        }
    }

    private void verifyModuleDependencyTree(
            Collection<Module> modules,
            LinkedList<Module> dependencyTree,
            List<PhasedUnits> phasedUnitsOfDependencies) {
        for (Module module : modules) {
            if ( dependencyTree.contains(module) ) {
                //circular dependency
                StringBuilder error = new StringBuilder("Circular dependency between modules: ");
                buildDependencyString(dependencyTree, module, error);
                error.append(".");
                System.err.println(error);
                return;
            }
            if ( ! module.isAvailable() ) {
                //try and load the module from the repository
                final ClosableVirtualFile src = artifactProvider.getArtifact(module.getName(), "0.1", "src");
                if (src == null) {
                    //not there => error
                    StringBuilder error = new StringBuilder("Cannot find module artifact ");
                    error.append( artifactProvider.getArtifactName(module.getName(), "0.1", "src") )
                            .append(" in local repository ('~/.ceylon/repo')")
                            .append("\n\tDependency tree: ");
                    buildDependencyString(dependencyTree, module, error);
                    error.append(".");
                    error.append("\n\tGet ceylon-spec and run 'ant publish' or 'ant publish.language.module'");
                    System.err.println(error);
                    System.exit(1);
                }
                else {
                    //parse module units and build module dependency and carry on
                    PhasedUnits modulePhasedUnit = new PhasedUnits(this);
                    phasedUnitsOfDependencies.add(modulePhasedUnit);
                    modulePhasedUnit.parseUnit(src);
                    src.close();
                    module.setAvailable(true);
                    final List<PhasedUnit> listOfUnits = modulePhasedUnit.getPhasedUnits();
                    //populate module.getDependencies()
                    for (PhasedUnit pu : listOfUnits) {
                        pu.buildModuleImport();
                    }
                }
            }
            dependencyTree.addLast(module);
            verifyModuleDependencyTree( module.getDependencies(), dependencyTree, phasedUnitsOfDependencies );
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
    }

    private void buildDependencyString(LinkedList<Module> dependencyTree, Module module, StringBuilder error) {
        for (Module errorModule : dependencyTree) {
            error.append(errorModule).append(" -> ");
        }
        error.append(module);
    }

    public Module getLanguageModule() {
        return languageModule;
    }
}

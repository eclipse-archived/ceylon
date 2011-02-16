package com.redhat.ceylon.compiler.context;

import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.redhat.ceylon.compiler.util.PrintUtil.importPathToString;

/**
 * Keep compiler contextual information like the package stack and the current module
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class Context {
    public static final String MODULE_FILE = "module.ceylon";

    private LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module currentModule;
    private Package defaultPackage;
    private List<PhasedUnit> phasedUnits = new ArrayList<PhasedUnit>();
    private Set<Module> modules = new HashSet<Module>();

    public Context() {
        final Package pkg = new Package();
        pkg.setName( new ArrayList<String>(0) );
        packageStack.add(pkg);
    }

    public Module getOrCreateModule(List<String> moduleName) {
        if (moduleName.size() == 0) {
            throw new RuntimeException("Module cannot be top level");
        }
        Module module = null;
        for(Module current : modules ) {
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
            bindPackageToCurrentModule(pkg);
        }
        //FIXME this is a total hack, should be an implicit import of the ceylon.language module (unless we compile ceylon.language :) )
        //add ceylon.language elements to the package
        if (defaultPackage != null) {
            final List<Structure> packageMembers = pkg.getMembers();
            for ( Structure structure : defaultPackage.getMembers() ) {
                packageMembers.add(structure);
            }
        }
        packageStack.addLast(pkg);
    }

    private void bindPackageToCurrentModule(Package pkg) {
        currentModule.getPackages().add(pkg);
        pkg.setModule(currentModule);
    }

    public void pop() {
        removeLastPackageAndModuleIfNecessary();
    }

    private void removeLastPackageAndModuleIfNecessary() {
        packageStack.pollLast();
        final boolean moveAboveModuleLevel = currentModule != null && currentModule.getName().size() > packageStack.size();
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
            bindPackageToCurrentModule(currentPkg);
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

    public void setDefaultPackage(Package pkg) {
        defaultPackage = pkg;
    }

    public Package getDefaultPackage() {
        return defaultPackage;
    }

    public void addStagedUnit(PhasedUnit phasedUnit) {
        this.phasedUnits.add(phasedUnit);
    }

    public List<PhasedUnit> getPhasedUnits() {
        return phasedUnits;
    }

    public void verifyModuleDependencyTree() {
        LinkedList<Module> dependencyTree = new LinkedList<Module>();
        for (Module module : modules) {
            dependencyTree.addLast(module);
            verifyModuleDependencyTree( module.getDependencies(), dependencyTree );
            dependencyTree.pollLast();
        }
    }

    private void verifyModuleDependencyTree(Collection<Module> modules, LinkedList<Module> dependencyTree) {
        for (Module module : modules) {
            if ( dependencyTree.contains(module) ) {
                StringBuilder error = new StringBuilder("Circular dependency between modules: ");
                buildDependencyString(dependencyTree, module, error);
                error.append(".");
                System.err.println(error);
                return;
            }
            if ( ! module.isAvailable() ) {
                StringBuilder error = new StringBuilder("Unable to find ");
                error.append(module).append(". Dependency tree ");
                buildDependencyString(dependencyTree, module, error);
                error.append(".");
                System.err.println(error);
            }
            dependencyTree.addLast(module);
            verifyModuleDependencyTree( module.getDependencies(), dependencyTree );
            dependencyTree.pollLast();
        }
    }

    private void buildDependencyString(LinkedList<Module> dependencyTree, Module module, StringBuilder error) {
        for (Module errorModule : dependencyTree) {
            error.append(errorModule).append(" -> ");
        }
        error.append(module);
    }
}

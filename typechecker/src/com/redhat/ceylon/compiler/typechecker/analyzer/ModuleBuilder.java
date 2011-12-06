package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.model.Util.formatPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/**
 * Build modules and packages
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleBuilder {
    public static final String MODULE_FILE = "module.ceylon";
    public static final String PACKAGE_FILE = "package.ceylon";
    private final Context context;
    private final LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module currentModule;
    private Modules modules;
    private final Map<Module,Set<Node>> missingModuleDependencies = new HashMap<Module, Set<Node>>();
    private Map<List<String>, Set<String>> topLevelErrorsPerModuleName = new HashMap<List<String>,Set<String>>();

    public ModuleBuilder(Context context) {
        this.context = context;
    }

    public void initCoreModules() {
        modules = context.getModules();
        if ( modules == null ) {
            modules = new Modules();
            //build empty package
            final Package emptyPackage = new Package();
            emptyPackage.setName(Collections.<String>emptyList());
            packageStack.addLast(emptyPackage);

            //build default module (module in which packages belong to when not explicitly under a module
            final List<String> defaultModuleName = Collections.singletonList("<default module>");
            final Module defaultModule = createModule(defaultModuleName);
            defaultModule.setAvailable(true);
            bindPackageToModule(emptyPackage, defaultModule);
            modules.getListOfModules().add(defaultModule);
            modules.setDefaultModule(defaultModule);

            //create language module and add it as a dependency of defaultModule
            //since packages outside a module cannot declare dependencies
            final List<String> languageName = Arrays.asList("ceylon", "language");
            Module languageModule = createModule(languageName);
            languageModule.setLanguageModule(languageModule);
            languageModule.setAvailable(false); //not available yet
            modules.setLanguageModule(languageModule);
            modules.getListOfModules().add(languageModule);
            defaultModule.getDependencies().add(languageModule);
            defaultModule.setLanguageModule(languageModule);
            context.setModules(modules);
        }
        else {
            modules = context.getModules();
            packageStack.addLast( modules.getDefaultModule().getPackages().get(0) );
        }
    }

    protected Module createModule(List<String> moduleName) {
		Module module = new Module();
		module.setName(moduleName);
		return module;
	}

	public void push(String path) {
        createPackageAndAddToModule(path);
    }

    public void pop() {
        removeLastPackageAndModuleIfNecessary();
    }

    public Package getCurrentPackage() {
        return packageStack.peekLast();
    }

    /**
     * Get or create a module.
     * version == null is considered equal to any version.
     * Likewise a module with no version will match any version passed
     */
    public Module getOrCreateModule(List<String> moduleName, String version) {
        if (moduleName.size() == 0) {
            return null;
        }
        Module module = null;
        final Set<Module> moduleList = context.getModules().getListOfModules();
        for (Module current : moduleList) {
            final List<String> names = current.getName();
            if ( names.size() == moduleName.size()
                    && moduleName.containsAll(names)
                    && compareVersions(version, current.getVersion())) {
                module = current;
                break;
            }
        }
        if (module == null) {
            module = createModule(moduleName);
            module.setVersion(version);
            module.setLanguageModule(modules.getLanguageModule());
            moduleList.add(module);
        }
        return module;
    }

    private boolean compareVersions(String version, String currentVersion) {
        return currentVersion == null || version == null || currentVersion.equals(version);
    }

    public void visitModuleFile() {
        if ( currentModule == null ) {
            final Package currentPkg = packageStack.peekLast();
            final List<String> moduleName = currentPkg.getName();
            //we don't know the version at this stage, will be filled later
            currentModule = getOrCreateModule(moduleName, null);
            if ( currentModule != null ) {
                currentModule.setAvailable(true); // TODO : not necessary anymore ? the phasedUnit will be added. And the buildModuleImport()
                                                  //        function (which calls module.setAvailable()) will be called by the typeChecker
                                                  //        BEFORE the ModuleValidator.verifyModuleDependencyTree() call that uses 
                                                  //        isAvailable()
                bindPackageToModule(currentPkg, currentModule);
            }
            else {
                collectError(new ArrayList<String>(), "A module cannot be defined at the top level of the hierarchy");
            }
        }
        else {
            StringBuilder error = new StringBuilder("Found two modules within the same hierarchy: '");
            error.append( formatPath( currentModule.getName() ) )
                .append( "' and '" )
                .append( formatPath( packageStack.peekLast().getName() ) )
                .append("'");
            collectError(currentModule.getName(), error.toString());
            collectError(packageStack.peekLast().getName(), error.toString());
        }
    }

    private void collectError(List<String> moduleName, String error) {
        Set<String> errors = topLevelErrorsPerModuleName.get(moduleName);
        if (errors == null) {
            errors = new HashSet<String>();
            topLevelErrorsPerModuleName.put(moduleName, errors);
        }
        errors.add(error);
    }

    private void createPackageAndAddToModule(String path) {
        Package pkg = new Package();
        final Package lastPkg = packageStack.peekLast();
        List<String> parentName = lastPkg.getName();
        final ArrayList<String> name = new ArrayList<String>(parentName.size() + 1);
        name.addAll(parentName);
        name.add(path);
        pkg.setName(name);
        if (currentModule != null) {
            bindPackageToModule(pkg, currentModule);
        }
        else {
            //bind package to defaultModule
            bindPackageToModule( pkg, modules.getDefaultModule() );
        }
        packageStack.addLast(pkg);
    }

    private void removeLastPackageAndModuleIfNecessary() {
        packageStack.pollLast();
        final boolean moveAboveModuleLevel = currentModule != null
                && currentModule.getName().size() > packageStack.size() -1; //first package is the empty package
        if (moveAboveModuleLevel) {
            currentModule = null;
        }
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

    public void addModuleDependencyDefinition(Module module, Node definition) {
        Set<Node> moduleDepError = missingModuleDependencies.get(module);
        if (moduleDepError == null) {
            moduleDepError = new HashSet<Node>();
            missingModuleDependencies.put(module, moduleDepError);
        }
        moduleDepError.add(definition);
    }

    public void addMissingDependencyError(Module module, String error) {
        Set<Node> moduleDepError = missingModuleDependencies.get(module);
        if (moduleDepError != null) {
            for ( Node definition :  moduleDepError ) {
                definition.addError(error);
            }
        }
        else {
            System.err.println("This is a type checker bug, please report. \nExpecting to add missing dependency error on non present definition: " + error);
        }
    }

    public void addErrorsToModule(Module module, Node unit) {
        Set<String> errors = topLevelErrorsPerModuleName.get(module.getName());
        if (errors != null) {
            for(String error : errors) {
                unit.addError(error);
            }
        }
    }
}

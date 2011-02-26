package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.*;
import com.redhat.ceylon.compiler.typechecker.model.Package;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.redhat.ceylon.compiler.typechecker.util.PrintUtil.importPathToString;

/**
 * Build modules and packages
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleBuilder {
    public static final String MODULE_FILE = "module.ceylon";
    private final Context context;
    private final LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module currentModule;
    private Modules modules;

    public ModuleBuilder(Context context) {
        this.context = context;
    }

    public void initCoreModules() {
        modules = context.getModules();
        if ( modules == null ) {
            modules = new Modules();
            //build empty package
            final Package emptyPackage = new Package();
            emptyPackage.setName(new ArrayList<String>(0));
            packageStack.addLast(emptyPackage);

            //build default module (module in which packages belong to when not explicitly under a module
            final Module defaultModule = new Module();
            final List<String> defaultModuleName = new ArrayList<String>();
            defaultModuleName.add("<default module>");
            defaultModule.setName(defaultModuleName);
            defaultModule.setAvailable(true);
            bindPackageToModule(emptyPackage, defaultModule);
            modules.getListOfModules().add(defaultModule);
            modules.setDefaultModule(defaultModule);

            //create language module and add it as a dependency of defaultModule
            //since packages outside a module cannot declare dependencies
            final List<String> languageName = new ArrayList<String>();
            languageName.add("ceylon");
            languageName.add("language");
            Module languageModule = new Module();
            languageModule.setName(languageName);
            languageModule.setAvailable(false); //not available yet
            modules.setLanguageModule(languageModule);
            modules.getListOfModules().add(languageModule);
            defaultModule.getDependencies().add(languageModule);
            context.setModules(modules);
        }
        else {
            modules = context.getModules();
            packageStack.addLast( modules.getDefaultModule().getPackages().get(0) );
        }
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

    public Module getOrCreateModule(List<String> moduleName) {
        if (moduleName.size() == 0) {
            throw new RuntimeException("Module cannot be top level");
        }
        Module module = null;
        final Set<Module> moduleList = context.getModules().getListOfModules();
        for (Module current : moduleList) {
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
            moduleList.add(module);
        }
        return module;
    }

    public void visitModuleFile() {
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


}

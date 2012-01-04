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
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

/**
 * Manager modules and packages (build, retrieve, handle errors etc)
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleManager {
    public static final String MODULE_FILE = "module.ceylon";
    public static final String PACKAGE_FILE = "package.ceylon";
    private Context context;
    private final LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module currentModule;
    private Modules modules;
    private final Map<ModuleImport,Set<Node>> moduleImportToNode = new HashMap<ModuleImport, Set<Node>>();
    private Map<List<String>, Set<String>> topLevelErrorsPerModuleName = new HashMap<List<String>,Set<String>>();
    private Map<Module, Node> moduleToNode = new HashMap<Module, Node>();

    public ModuleManager(Context context) {
        this.context = context;
    }
    
    public ModuleManager(){}

    public void initCoreModules() {
        modules = context.getModules();
        if ( modules == null ) {
            modules = new Modules();
            //build empty package
            final Package emptyPackage = new Package();
            emptyPackage.setName(Collections.<String>emptyList());
            packageStack.addLast(emptyPackage);

            //build default module (module in which packages belong to when not explicitly under a module
            final List<String> defaultModuleName = Collections.singletonList(Module.DEFAULT_MODULE_NAME);
            final Module defaultModule = createModule(defaultModuleName);
            defaultModule.setDefault(true);
            defaultModule.setAvailable(true);
            defaultModule.setVersion("unversioned");
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
            defaultModule.getImports().add(new ModuleImport(languageModule, false, false));
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
                addErrorToModule(new ArrayList<String>(), "A module cannot be defined at the top level of the hierarchy");
            }
        }
        else {
            StringBuilder error = new StringBuilder("Found two modules within the same hierarchy: '");
            error.append( formatPath( currentModule.getName() ) )
                .append( "' and '" )
                .append( formatPath( packageStack.peekLast().getName() ) )
                .append("'");
            addErrorToModule(currentModule.getName(), error.toString());
            addErrorToModule(packageStack.peekLast().getName(), error.toString());
        }
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

    public void addModuleDependencyDefinition(ModuleImport moduleImport, Node definition) {
        Set<Node> moduleDepDefinition = moduleImportToNode.get(moduleImport);
        if (moduleDepDefinition == null) {
            moduleDepDefinition = new HashSet<Node>();
            moduleImportToNode.put(moduleImport, moduleDepDefinition);
        }
        moduleDepDefinition.add(definition);
    }

    public void attachErrorToDependencyDeclaration(ModuleImport moduleImport, String error) {
        Set<Node> moduleDepError = moduleImportToNode.get(moduleImport);
        if (moduleDepError != null) {
            for ( Node definition :  moduleDepError ) {
                definition.addError(error);
            }
        }
        else {
            //This probably can happen if the missing dependency is found deep in the dependency structure (ie the binary version of a module)
            //TODO find the nearest src module that triggered the issue
            System.err.println("This might be a type checker bug, please report. \nExpecting to add missing dependency error on non present definition: " + error);
        }
    }

    //must be used *after* addLinkBetweenModuleAndNode has been set ie post ModuleVisitor visit
    public void addErrorToModule(Module module, String error) {
        Node node = moduleToNode.get(module);
        if (node != null) {
            node.addError(error);
        }
        else {
            //might happen if the faulty module is a compiled module
            System.err.println("This is a type checker bug, please report. " +
                    "\nExpecting to add error on non present module node: " + module.toString() + ". Error " + error);
        }
    }

    //only used if we really don't know the version
    private void addErrorToModule(List<String> moduleName, String error) {
        Set<String> errors = topLevelErrorsPerModuleName.get(moduleName);
        if (errors == null) {
            errors = new HashSet<String>();
            topLevelErrorsPerModuleName.put(moduleName, errors);
        }
        errors.add(error);
    }

    public void addLinkBetweenModuleAndNode(Module module, Node unit) {
        //keep link and display errors on modules where we don't know the version of
        Set<String> errors = topLevelErrorsPerModuleName.get(module.getName());
        if (errors != null) {
            for(String error : errors) {
                unit.addError(error);
            }
            errors.clear();
        }
        moduleToNode.put(module,unit);
    }

    public ModuleImport findImport(Module owner, Module dependency) {
        for (ModuleImport modImprt : owner.getImports()) {
            if (equalsForModules(modImprt.getModule(), dependency, true)) return modImprt;
        }
        return null;
    }

    public boolean equalsForModules(Module left, Module right, boolean exactVersionMatch) {
        if (left == right) return true;
        List<String> leftName = left.getName();
        List<String> rightName = right.getName();
        if (leftName.size() != rightName.size()) return false;
        for(int index = 0 ; index < leftName.size(); index++) {
            if (!leftName.get(index).equals(rightName.get(index))) return false;
        }
        if (exactVersionMatch && left.getVersion()!=right.getVersion()) return false;
        return true;
    }

    public Module findModule(Module module, List<Module> listOfModules, boolean exactVersionMatch) {
        for(Module current : listOfModules) {
            if (equalsForModules(module, current, exactVersionMatch)) return current;
        }
        return null;
    }

    public Module findLoadedModule(String moduleName, String searchedVersion) {
        return findLoadedModule(moduleName, searchedVersion, modules);
    }
    
    public Module findLoadedModule(String moduleName, String searchedVersion, Modules modules) {
        for(Module module : modules.getListOfModules()){
            if(module.getNameAsString().equals(moduleName)) {
                if (searchedVersion != null && searchedVersion.equals(module.getVersion())){
                    return module;
                }
            }
        }
        return null;
    }

    public void resolveModule(Module module, VirtualFile artifact, List<PhasedUnits> phasedUnitsOfDependencies) {
        PhasedUnits modulePhasedUnit = new PhasedUnits(context);
        phasedUnitsOfDependencies.add(modulePhasedUnit);
        modulePhasedUnit.parseUnit(artifact);
        //populate module.getDependencies()
        modulePhasedUnit.visitModules();
    }

    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("src");
    }

    public static List<String> splitModuleName(String moduleName) {
        return Arrays.asList(moduleName.split("[\\.]"));
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

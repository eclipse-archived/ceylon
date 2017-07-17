package com.redhat.ceylon.model.typechecker.util;

import static com.redhat.ceylon.common.ModuleUtil.toCeylonModuleName;
import static com.redhat.ceylon.common.Versions.CEYLON_VERSION_NUMBER;
import static com.redhat.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.common.Backends;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;

/**
 * Manager modules and packages (build, retrieve, handle errors etc)
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleManager {

    public static final String MODULE_FILE = "module.ceylon";
    public static final String PACKAGE_FILE = "package.ceylon";
    
    protected Modules modules;

    public ModuleManager() {}
    
    public Package createPackage(String pkgName, Module module) {
        Package pkg = new Package();
        List<String> name = 
                pkgName.isEmpty() ? 
                        asList("") : 
                        splitModuleName(pkgName); 
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }

    public void initCoreModules(Modules initialModules) {
        modules = initialModules;
        if (modules.getLanguageModule() == null) {
            //build empty package
            Package emptyPackage = createPackage("", null);

            //build default module 
            //(module in which packages belong to when not explicitly under a module)
            List<String> defaultModuleName = 
                    singletonList(DEFAULT_MODULE_NAME);
            Module defaultModule = 
                    createModule(defaultModuleName, "unversioned");
//            defaultModule.setDefault(true);
            defaultModule.setAvailable(true);
            bindPackageToModule(emptyPackage, defaultModule);
            modules.getListOfModules().add(defaultModule);
            modules.setDefaultModule(defaultModule);

            //create language module and add it as a dependency of defaultModule
            //since packages outside a module cannot declare dependencies
            List<String> languageName = asList("ceylon", "language");
            Module languageModule = 
                    createModule(languageName, CEYLON_VERSION_NUMBER);
            languageModule.setLanguageModule(languageModule);
            languageModule.setAvailable(false); //not available yet
            modules.setLanguageModule(languageModule);
            modules.getListOfModules().add(languageModule);
            defaultModule.addImport(new ModuleImport(null, languageModule, false, false));
            defaultModule.setLanguageModule(languageModule);
        }
    }

    public void bindPackageToModule(Package pkg, Module module) {
        //undo nomodule setting if necessary
        if (pkg.getModule() != null) {
            pkg.getModule().getPackages().remove(pkg);
            pkg.setModule(null);
        }
        module.getPackages().add(pkg);
        pkg.setModule(module);
    }

    protected Module createModule(List<String> moduleName, String version) {
		Module module = new Module();
		module.setName(moduleName);
		module.setVersion(version);
		return module;
	}

    /**
     * Get or create a module.
     * version == null is considered equal to any version.
     * Likewise a module with no version will match any version passed
     */
    public Module getOrCreateModule(List<String> moduleName, String version) {
        if (moduleName.isEmpty()) {
            return null;
        }
        Module module = null;
        Set<Module> moduleList = modules.getListOfModules();
        for (Module current : moduleList) {
            final List<String> names = current.getName();
            if (moduleName.equals(names)
                    && compareVersions(current, version, 
                            current.getVersion())) {
                module = current;
                break;
            }
        }
        if (module == null) {
            module = createModule(moduleName, version);
            module.setLanguageModule(modules.getLanguageModule());
            moduleList.add(module);
        }
        return module;
    }

    protected boolean compareVersions(Module current, String version, String currentVersion) {
        return currentVersion == null 
            || version == null 
            || currentVersion.equals(version);
    }


    public ModuleImport findImport(Module owner, Module dependency) {
        for (ModuleImport modImprt : owner.getImports()) {
            if (equalsForModules(modImprt.getModule(), dependency, true)) {
                return modImprt;
            }
        }
        return null;
    }

    public boolean equalsForModules(Module left, Module right, boolean exactVersionMatch) {
        if (left == right) {
            return true;
        }
        List<String> leftName = left.getName();
        List<String> rightName = right.getName();
        if (leftName.size() != rightName.size()) {
            return false;
        }
        for (int index = 0 ; index < leftName.size(); index++) {
            if (!leftName.get(index).equals(rightName.get(index))) {
                return false;
            }
        }
        return !exactVersionMatch 
            || left.getVersion() != null 
            && left.getVersion().equals(right.getVersion());
    }

    public Module findModule(Module module, List<Module> listOfModules, boolean exactVersionMatch) {
        for (Module current : listOfModules) {
            if (equalsForModules(module, current, exactVersionMatch)) {
                return current;
            }
        }
        return null;
    }

    public boolean similarForModules(Module left, Module right) {
        if (left == right) {
            return true;
        }
        String leftName = toCeylonModuleName(left.getNameAsString());
        String rightName = toCeylonModuleName(right.getNameAsString());
        return leftName.equals(rightName);
    }

    /**
     * This treats Maven and Ceylon modules as similar: com:foo and com.foo will match
     */
    public Module findSimilarModule(Module module, List<Module> listOfModules) {
        for (Module current : listOfModules) {
            if (similarForModules(module, current)) {
                return current;
            }
        }
        return null;
    }

    public Module findLoadedModule(String moduleName, String searchedVersion) {
        return findLoadedModule(moduleName, searchedVersion, modules);
    }
    
    /**
     * @Deprecated This looks fishy: why would we have an extra Modules parameter?
     */
    @Deprecated
    private final Module findLoadedModule(String moduleName, String searchedVersion, Modules modules) {
        if (moduleName.equals(DEFAULT_MODULE_NAME)) {
            return modules.getDefaultModule();
        }
        for (Module module : modules.getListOfModules()) {
            if (module.getNameAsString().equals(moduleName)
                    && compareVersions(module, searchedVersion, 
                            module.getVersion())) {
                return module;
            }
        }
        return null;
    }

    public Iterable<String> getSearchedArtifactExtensions() {
        return asList("src");
    }

    public static List<String> splitModuleName(String moduleName) {
        List<String> ret = new LinkedList<>();
        for(String part : moduleName.split("[\\.]")){
            if(part.indexOf('-') != -1){
                boolean first = true;
                for(String subpart : part.split("[\\:]")){
                    if(first)
                        first = false;
                    else
                        ret.add("");
                    ret.add(subpart);
                }
            }else{
                ret.add(part);
            }
        }
        return ret;
    }

    public void prepareForTypeChecking() {
        // to be overridden by subclasses
    }

    public void addImplicitImports() {
    }

    public void modulesVisited() {
        // to be overridden by subclasses
    }

    public void visitedModule(Module module, boolean forCompiledModule) {
        // to be overridden by subclasses
    }

    public Module overridesModule(ArtifactResult artifact,
            Module module, ModuleImport moduleImport) {
        String namespace = artifact.namespace();
        String realName = artifact.name();
        String realVersion = artifact.version();
        if (!realName.equals(module.getNameAsString()) ||
            !realVersion.equals(module.getVersion())) {
            if (module != module.getLanguageModule()) {
                Module realModule = 
                        getOrCreateModule(splitModuleName(realName), realVersion);
                moduleImport.override(new ModuleImport(namespace, realModule, 
                        moduleImport.isOptional(), 
                        moduleImport.isExport()));
                return realModule;
            }
        }
        return null;
    }
    
    public Modules getModules() {
        return modules;
    }

    protected void setModules(Modules modules) {
        this.modules = modules;
    }
    /**
     * Returns the set of native backend names supported by
     * this ModuleManager. Returning an empty set signifies
     * that any backend is acceptable.
     */
    public Backends getSupportedBackends() {
        return Backends.ANY;
    }

    /**
     * Return true if the metamodel has already been manually set up. This is only invalidated in some
     * cases in the RuntimeModuleManager.
     */
    public boolean isManualMetamodelSetup() {
        return true;
    }
}

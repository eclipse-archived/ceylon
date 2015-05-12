package com.redhat.ceylon.model.typechecker.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
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
public class ModuleManager implements BackendSupport {

    public static final String MODULE_FILE = "module.ceylon";
    public static final String PACKAGE_FILE = "package.ceylon";
    protected Modules modules;

    public ModuleManager() {
    }
    
    public Package createPackage(String pkgName, Module module) {
        final Package pkg = new Package();
        List<String> name = pkgName.isEmpty() ? Arrays.asList("") : splitModuleName(pkgName); 
        pkg.setName(name);
        if (module != null) {
            module.getPackages().add(pkg);
            pkg.setModule(module);
        }
        return pkg;
    }

    public void initCoreModules(Modules initialModules) {
        modules = initialModules;
        if ( modules.getLanguageModule() == null ) {
            //build empty package
            final Package emptyPackage = createPackage("", null);

            //build default module (module in which packages belong to when not explicitly under a module
            final List<String> defaultModuleName = Collections.singletonList(Module.DEFAULT_MODULE_NAME);
            final Module defaultModule = createModule(defaultModuleName, "unversioned");
            defaultModule.setDefault(true);
            defaultModule.setAvailable(true);
            bindPackageToModule(emptyPackage, defaultModule);
            modules.getListOfModules().add(defaultModule);
            modules.setDefaultModule(defaultModule);

            //create language module and add it as a dependency of defaultModule
            //since packages outside a module cannot declare dependencies
            final List<String> languageName = Arrays.asList("ceylon", "language");
            Module languageModule = createModule(languageName, Versions.CEYLON_VERSION_NUMBER);
            languageModule.setLanguageModule(languageModule);
            languageModule.setAvailable(false); //not available yet
            modules.setLanguageModule(languageModule);
            modules.getListOfModules().add(languageModule);
            defaultModule.addImport(new ModuleImport(languageModule, false, false));
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
        if (moduleName.size() == 0) {
            return null;
        }
        Module module = null;
        final Set<Module> moduleList = modules.getListOfModules();
        for (Module current : moduleList) {
            final List<String> names = current.getName();
            if (moduleName.equals(names)
                    && compareVersions(current, version, current.getVersion())) {
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
        return currentVersion == null || version == null || currentVersion.equals(version);
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
        if (exactVersionMatch && !left.getVersion().equals(right.getVersion())) return false;
        return true;
    }

    public Module findModule(Module module, List<Module> listOfModules, boolean exactVersionMatch) {
        for(Module current : listOfModules) {
            if (equalsForModules(module, current, exactVersionMatch)) return current;
        }
        return null;
    }

    public boolean similarForModules(Module left, Module right) {
        if (left == right) return true;
        String leftName = ModuleUtil.toCeylonModuleName(left.getNameAsString());
        String rightName = ModuleUtil.toCeylonModuleName(right.getNameAsString());
        return leftName.equals(rightName);
    }

    /**
     * This treats Maven and Ceylon modules as similar: com:foo and com.foo will match
     */
    public Module findSimilarModule(Module module, List<Module> listOfModules) {
        for(Module current : listOfModules) {
            if (similarForModules(module, current)) return current;
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
    public Module findLoadedModule(String moduleName, String searchedVersion, Modules modules) {
        if(moduleName.equals(Module.DEFAULT_MODULE_NAME))
            return modules.getDefaultModule();
        for(Module module : modules.getListOfModules()){
            if(module.getNameAsString().equals(moduleName)
                    && compareVersions(module, searchedVersion, module.getVersion())){
                return module;
            }
        }
        return null;
    }

    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("src");
    }

    public static List<String> splitModuleName(String moduleName) {
        return Arrays.asList(moduleName.split("[\\.]"));
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
        String realName = artifact.name();
        String realVersion = artifact.version();
        if (! realName.equals(module.getNameAsString()) ||
            ! realVersion.equals(module.getVersion())) {
            if (module != module.getLanguageModule()) {
                Module realModule = getOrCreateModule(splitModuleName(realName), realVersion);
                moduleImport.override(new ModuleImport(realModule, moduleImport.isOptional(), moduleImport.isExport()));
                return realModule;
            }
        }
        return null;
    }
    
    public Modules getModules(){
        return modules;
    }

    @Override
    public boolean supportsBackend(Backend backend) {
        return backend != Backend.None;
    }
}

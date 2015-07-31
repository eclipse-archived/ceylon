package com.redhat.ceylon.model.loader.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/**
 * ModuleManager which can load artifacts from jars and cars.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class LazyModuleManager extends ModuleManager {

    public LazyModuleManager() {
        super();
    }

    protected void setupIfJDKModule(LazyModule module) {
        // Make sure that the java modules are set up properly.
        // Bad jdk versions will not be made available and the module validator
        // will fail to load their artifacts, and the error is properly handled by the lazy module manager in
        // attachErrorToDependencyDeclaration()
        String nameAsString = module.getNameAsString();
        String version = module.getVersion();
        if(version != null
                && AbstractModelLoader.isJDKModule(nameAsString)){
            if(JDKUtils.jdk.providesVersion(version)){
                module.setAvailable(true);
                module.setJava(true);
                module.setNativeBackend(Backend.Java.nativeAnnotation);
            }
        }
    }


    /**
     * To be overriden by reflection module manager, because reflection requires even types of private members
     * of imported modules to be in the classpath, and those could be of unimported modules (since they're private
     * that's allowed).
     */
    public boolean shouldLoadTransitiveDependencies(){
        return false;
    }
    
    @Override
    protected abstract Module createModule(List<String> moduleName, String version);
    
    public abstract AbstractModelLoader getModelLoader();

    /**
     * Return true if this module should be loaded from source we are compiling
     * and not from its compiled artifact at all. Returns false by default, so
     * modules will be laoded from their compiled artifact.
     */
    public boolean isModuleLoadedFromSource(String moduleName){
        return false;
    }
    
    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("car", "jar");
    }

    @Override
    public Set<String> supportedBackends() {
        return Collections.singleton(Backend.Java.nativeAnnotation);
    }
    
    @Override
    public void addImplicitImports() {
        Module languageModule = modules.getLanguageModule();
        for(Module m : modules.getListOfModules()){
            // Java modules don't depend on ceylon.language
            if((m instanceof LazyModule == false || !((LazyModule)m).isJava()) && !m.equals(languageModule)) {
                // add ceylon.language if required
                ModuleImport moduleImport = findImport(m, languageModule);
                if (moduleImport == null) {
                    moduleImport = new ModuleImport(languageModule, false, true);
                    m.addImport(moduleImport);
                }
            }
        }
    }
    
    @Override
    protected boolean compareVersions(Module current, String version, String currentVersion) {
        String name = current.getNameAsString();
        if(JDKUtils.isJDKModule(name) || JDKUtils.isOracleJDKModule(name)){
            // if we're running JDK8, pretend that it provides JDK7 modules
            if(JDKUtils.jdk.providesVersion(version)
                    && JDKUtils.jdk.providesVersion(currentVersion))
                return true;
        }
        return currentVersion == null || version == null || currentVersion.equals(version);
    }
}

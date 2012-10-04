/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.loader.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;

/**
 * ModuleManager which can load artifacts from jars and cars.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class LazyModuleManager extends ModuleManager {

    public LazyModuleManager(Context ceylonContext) {
        super(ceylonContext);
    }
    
    @Override
    public void resolveModule(ArtifactResult artifact, Module module, ModuleImport moduleImport, 
            LinkedList<Module> dependencyTree, List<PhasedUnits> phasedUnitsOfDependencies) {
        String moduleName = module.getNameAsString();
        // check for an already loaded module with the same name but different version
        for(Module loadedModule : getContext().getModules().getListOfModules()){
            if(loadedModule.getNameAsString().equals(moduleName)
                    && !loadedModule.getVersion().equals(module.getVersion())){
                getModelLoader().logDuplicateModuleError(module, loadedModule);
                // abort
                return;
            }
        }
        
        if(isModuleLoadedFromSource(moduleName)){
            super.resolveModule(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies);
        }else{
            getModelLoader().addModuleToClassPath(module, artifact); // To be able to load it from the corresponding archive
            Module compiledModule = getModelLoader().loadCompiledModule(moduleName);
            if(compiledModule == null && !module.isDefault()){
                // we didn't find module.class so it must be a java module if it's not the default module
                ((LazyModule)module).setJava(true);
                ((LazyModule)module).loadPackageList(artifact);
            }
            if(compiledModule != null){
                // it must be a Ceylon module
                // default modules don't have any module descriptors so we can't check them
                if(compiledModule.getMajor() != Versions.JVM_BINARY_MAJOR_VERSION
                        || compiledModule.getMinor() != Versions.JVM_BINARY_MINOR_VERSION){
                    attachErrorToDependencyDeclaration(moduleImport,
                            dependencyTree,
                            "This module was compiled for an incompatible version of the Ceylon compiler ("+compiledModule.getMajor()+"."+compiledModule.getMinor()+")."
                                    +"\nThis compiler supports "+Versions.JVM_BINARY_MAJOR_VERSION+"."+Versions.JVM_BINARY_MINOR_VERSION+"."
                                    +"\nPlease try to recompile your module using a compatible compiler."
                                    +"\nBinary compatibility will only be supported after Ceylon 1.0.");
                }
            }
        }
    }

    @Override
    protected abstract Module createModule(List<String> moduleName, String version);
    
    protected abstract AbstractModelLoader getModelLoader();

    /**
     * Return true if this module should be loaded from source we are compiling
     * and not from its compiled artifact at all. Returns false by default, so
     * modules will be laoded from their compiled artifact.
     */
    protected boolean isModuleLoadedFromSource(String moduleName){
        return false;
    }
    
    @Override
    public Iterable<String> getSearchedArtifactExtensions() {
        return Arrays.asList("car", "jar");
    }

    @Override
    public void addImplicitImports() {
        Module languageModule = getContext().getModules().getLanguageModule();
        for(Module m : getContext().getModules().getListOfModules()){
            // Java modules don't depend on ceylon.language
            if(m instanceof LazyModule == false || !((LazyModule)m).isJava()){
                // add ceylon.language if required
                ModuleImport moduleImport = findImport(m, languageModule);
                if (moduleImport == null) {
                    moduleImport = new ModuleImport(languageModule, false, true);
                    m.getImports().add(moduleImport);
                }
            }
        }
    }
    
    @Override
    public void attachErrorToDependencyDeclaration(ModuleImport moduleImport, List<Module> dependencyTree, String error) {
        // special case for the java modules, which we only get when using the wrong version
        String name = moduleImport.getModule().getNameAsString();
        if(AbstractModelLoader.isJDKModule(name)){
            error = "Invalid Java module version: only " + AbstractModelLoader.JDK_MODULE_VERSION + " is supported";
        }
        super.attachErrorToDependencyDeclaration(moduleImport, dependencyTree, error);
    }
}

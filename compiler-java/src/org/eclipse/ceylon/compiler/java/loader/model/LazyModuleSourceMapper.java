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

package org.eclipse.ceylon.compiler.java.loader.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.ceylon.cmr.api.Overrides;
import org.eclipse.ceylon.cmr.api.RepositoryManager;
import org.eclipse.ceylon.cmr.api.VersionComparator;
import org.eclipse.ceylon.cmr.ceylon.loader.ModuleNotFoundException;
import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.ModuleUtil;
import org.eclipse.ceylon.common.StatusPrinter;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.compiler.java.loader.CompilerModuleLoader;
import org.eclipse.ceylon.compiler.java.tools.CeylonLog;
import org.eclipse.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.compiler.typechecker.context.Context;
import org.eclipse.ceylon.compiler.typechecker.context.PhasedUnits;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.langtools.tools.javac.util.Log.WriterKind;
import org.eclipse.ceylon.model.cmr.ArtifactResult;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.eclipse.ceylon.model.cmr.ModuleScope;
import org.eclipse.ceylon.model.loader.AbstractModelLoader;
import org.eclipse.ceylon.model.loader.JdkProvider;
import org.eclipse.ceylon.model.loader.model.LazyModule;
import org.eclipse.ceylon.model.loader.model.LazyModuleManager;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;
import org.eclipse.ceylon.model.typechecker.util.ModuleManager;

import java.util.Set;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class LazyModuleSourceMapper extends ModuleSourceMapper {
    private final String encoding;

    private StatusPrinter statusPrinter;
    private boolean verbose;
    private CeylonLog log;

    public LazyModuleSourceMapper(Context context, LazyModuleManager moduleManager, 
            StatusPrinter statusPrinter, boolean verbose, CeylonLog log) {
        this(context, moduleManager, statusPrinter, verbose, log, null);
    }

    public LazyModuleSourceMapper(Context context, LazyModuleManager moduleManager, 
            StatusPrinter statusPrinter, boolean verbose, CeylonLog log, String encoding) {
        super(context, moduleManager);
        this.statusPrinter = statusPrinter;
        this.verbose = verbose;
        this.log = log;
        this.encoding = encoding;
    }

    @Override
    protected PhasedUnits createPhasedUnits() {
        PhasedUnits units = super.createPhasedUnits();
        if (encoding != null) {
            units.setEncoding(encoding);
        }
        return units;
    }

    protected LazyModuleManager getModuleManager(){
        return (LazyModuleManager) super.getModuleManager();
    }
    
    @Override
    public void resolveModule(ArtifactResult artifact, Module module, ModuleImport moduleImport, 
            LinkedList<Module> dependencyTree, List<PhasedUnits> phasedUnitsOfDependencies, boolean forCompiledModule) {
        String moduleName = module.getNameAsString();
        LazyModuleManager moduleManager = getModuleManager();
        boolean moduleLoadedFromSource = moduleManager.isModuleLoadedFromSource(moduleName);
        boolean isLanguageModule = module == module.getLanguageModule();
        AbstractModelLoader modelLoader = moduleManager.getModelLoader();
        if(modelLoader.getJdkProviderModule() == module){
            modelLoader.setupAlternateJdk(module, artifact);
        }
        
        // if this is for a module we're compiling, or for an indirectly imported module, we need to check because the
        // module in question will be in the classpath
        if(moduleLoadedFromSource || forCompiledModule){
            if(shouldAbortOnDuplicateModule(module, moduleName, modelLoader, dependencyTree))
                return;
        }
        
        if(moduleLoadedFromSource){
            super.resolveModule(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies, forCompiledModule);
        }else if(forCompiledModule || isLanguageModule || moduleManager.shouldLoadTransitiveDependencies()){
            // we only add stuff to the classpath and load the modules if we need them to compile our modules
            modelLoader.addModuleToClassPath(module, artifact); // To be able to load it from the corresponding archive
            LazyModule lazyModule = (LazyModule) module;

            if(!module.isDefaultModule()){
                if(artifact.groupId() != null)
                    module.setGroupId(artifact.groupId());
                if(artifact.artifactId() != null)
                    module.setArtifactId(artifact.artifactId());
                if(artifact.classifier() != null)
                    module.setClassifier(artifact.classifier());

                if(!modelLoader.loadCompiledModule(module)){
                    setupJavaModule(moduleImport, lazyModule, modelLoader, moduleManager, artifact);
                }else{
                    setupCeylonModule(moduleImport, lazyModule, modelLoader, moduleManager, artifact, dependencyTree);
                }
            }
            // module is now available
            module.setAvailable(true);
        }
    }

    private void setupCeylonModule(ModuleImport moduleImport, LazyModule module, 
            AbstractModelLoader modelLoader, ModuleManager moduleManager,
            ArtifactResult artifact, LinkedList<Module> dependencyTree) {
        // it must be a Ceylon module
        // default modules don't have any module descriptors so we can't check them
        overrideModuleImports(module, artifact);
        if(!Versions.isJvmBinaryVersionSupported(module.getJvmMajor(), module.getJvmMinor())){
            attachErrorToDependencyDeclaration(moduleImport,
                    dependencyTree,
                    "version '"+ module.getVersion() + "' of module '" + module.getNameAsString() + 
                    "' was compiled by an incompatible version of the compiler (binary version " +
                    module.getJvmMajor() + "." + module.getJvmMinor() + 
                    " of module is not compatible with binary version " + 
                    Versions.JVM_BINARY_MAJOR_VERSION + "." + Versions.JVM_BINARY_MINOR_VERSION +
                    " of this compiler)",
                    true);
        }
    }

    private void setupJavaModule(ModuleImport moduleImport, LazyModule module, 
            AbstractModelLoader modelLoader, ModuleManager moduleManager,
            ArtifactResult artifact) {
        
        // we didn't find module.class so it must be a java module if it's not the default module
        module.setJava(true);
        module.setNativeBackends(Backend.Java.asSet());
        
        modelLoader.loadJava9Module(module, artifact.artifact());
        
        List<ArtifactResult> deps = artifact.dependencies();
        boolean forceExport = ModuleUtil.isMavenModule(module.getNameAsString())
                && modelLoader.isFullyExportMavenDependencies();
        for (ArtifactResult dep : deps) {
            // forget runtime, test and even provided
            if(dep.moduleScope() != ModuleScope.COMPILE)
                continue;
            // Never load optional dependencies of Java modules, since those are supposed to not
            // re-export them anyway
            if(dep.optional())
                continue;
            Module dependency = moduleManager.getOrCreateModule(ModuleManager.splitModuleName(dep.name()), dep.version());

            ModuleImport depImport = moduleManager.findImport(module, dependency);
            if (depImport == null) {
                moduleImport = new ModuleImport(dep.namespace(), dependency, 
                        dep.optional(), 
                        // allow forcing export but not for optional modules
                        dep.exported() || forceExport && !dep.optional(),
                        Backend.Java);
                module.addImport(moduleImport);
            }
        }
    }

    private boolean shouldAbortOnDuplicateModule(Module module, String moduleName,
            AbstractModelLoader modelLoader, LinkedList<Module> dependencyTree) {
        String standardisedModuleName = ModuleUtil.toCeylonModuleName(moduleName);
        // check for an already loaded module with the same name but different version
        for(Module loadedModule : getContext().getModules().getListOfModules()){
            String loadedModuleName = loadedModule.getNameAsString();
            String standardisedLoadedModuleName = ModuleUtil.toCeylonModuleName(loadedModuleName);
            boolean sameModule = loadedModuleName.equals(moduleName);
            boolean similarModule = standardisedLoadedModuleName.equals(standardisedModuleName);
            if((sameModule || similarModule)
                    && !loadedModule.getVersion().equals(module.getVersion())
                    && modelLoader.isModuleInClassPath(loadedModule)){
                // abort
                // we need this error thrown rather than the typechecker error because the typechecker currently
                // allows more than we do, such as having direct imports of the same module with different versions
                // as long as they are not reexported, but we don't support that since they all go in the same
                // classpath (direct imports of compiled modules)

                if(sameModule){
                    String[] versions = VersionComparator.orderVersions(module.getVersion(), loadedModule.getVersion());
                    String error = "source code imports two different versions of module '" + 
                            moduleName + "': "+
                            "version '" + versions[0] + "' and version '" + versions[1] +
                            "'";
                    addConflictingModuleErrorToModule(moduleName, dependencyTree.getFirst(), error);
                }else{
                    String moduleA;
                    String moduleB;
                    if(loadedModuleName.compareTo(moduleName) < 0){
                        moduleA = ModuleUtil.makeModuleName(loadedModuleName, loadedModule.getVersion());
                        moduleB = ModuleUtil.makeModuleName(moduleName, module.getVersion());
                    }else{
                        moduleA = ModuleUtil.makeModuleName(moduleName, module.getVersion());
                        moduleB = ModuleUtil.makeModuleName(loadedModuleName, loadedModule.getVersion());
                    }
                    String error = "source code imports two different versions of similar modules '" + 
                            moduleA + "' and '"+ moduleB + "'";
                    addConflictingModuleWarningToModule(moduleA, dependencyTree.getFirst(), Warning.similarModule, error);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void attachErrorToDependencyDeclaration(ModuleImport moduleImport, List<Module> dependencyTree, 
            String error, boolean isError) {
        // special case for the java modules, which we only get when using the wrong version
        String name = moduleImport.getModule().getNameAsString();
        JdkProvider jdkProvider = getJdkProvider();
        if(jdkProvider != null && jdkProvider.isJDKModule(name)){
            error = "imported module '" + name + "' depends on JDK version '\"" + 
                    moduleImport.getModule().getVersion() +
                    "\"' and compiler is using Java " + jdkProvider.getJDKVersion();
        }
        super.attachErrorToDependencyDeclaration(moduleImport, dependencyTree, error, isError);
    }

    @Override
    public void addModuleDependencyDefinition(ModuleImport moduleImport, Node definition) {
        super.addModuleDependencyDefinition(moduleImport, definition);
        Module module = moduleImport.getModule();
        if(module == null)
            return;
        String nameAsString = module.getNameAsString();
        String version = module.getVersion();
        if(version != null
                && (JDKUtils.isJDKModule(nameAsString) || JDKUtils.isOracleJDKModule(nameAsString))){
            // Add a warning if we're using a lower JDK than the one we're running on
            // FIXME: this does not work for JDK9 or Android
            if(JDKUtils.jdk.isLowerVersion(version)){
                definition.addUsageWarning(Warning.importsOtherJdk, "You import an earlier JDK ("+version+"), which is provided by the current JDK ("+
                        JDKUtils.jdk.version+") you are running on, but"+
                        " we cannot check that you are not using any of the current JDK-specific classes or methods. Upgrade your import to the current JDK if you depend on"+
                        " the current JDK classes or methods.", Backend.Java);
            }
        }
    }
    
    @Override
    public Module getJdkModule() {
        return getModuleManager().getModelLoader().getJDKBaseModule();
    }

    @Override
    public Module getJdkProviderModule() {
        return getModuleManager().getModelLoader().getJdkProviderModule();
    }

    @Override
    public JdkProvider getJdkProvider() {
        return getModuleManager().getModelLoader().getJdkProvider();
    }
    
    @Override
    public void preResolveDependenciesIfRequired(RepositoryManager repositoryManager) {
        AbstractModelLoader modelLoader = getModuleManager().getModelLoader();
        if(!modelLoader.isFullyExportMavenDependencies())
            return;
        if(statusPrinter != null){
            statusPrinter.clearLine();
            statusPrinter.log("Pre-resolving dependencies");
        }
        if(verbose){
            log.printRawLines(WriterKind.NOTICE, "[Pre-resolving dependencies]");
        }
        Set<Module> compiledModules = getCompiledModules();
        Map<String,String> modules = new HashMap<>();
        ModuleImport anyImport = null;
        for (Module module : compiledModules) {
            for (ModuleImport imp : module.getImports()) {
                if(imp.getModule() == null
                        || !compiledModules.contains(imp.getModule())){
                    if(anyImport == null)
                        anyImport = imp;
                    String name = imp.getModule().getNameAsString();
                    if(imp.getNamespace() != null)
                        name = imp.getNamespace() + ":" + name;
                    modules.put(name, imp.getModule().getVersion());
                }
            }
        }
        if(statusPrinter != null){
            statusPrinter.clearLine();
            statusPrinter.log("Pre-resolving found "+modules.size()+" to pre-resolve");
        }
        if(verbose){
            log.printRawLines(WriterKind.NOTICE, "[Pre-resolving "+modules.size()+" modules]");
        }
        if(modules.isEmpty())
            return;
        Entry<String, String> first = modules.entrySet().iterator().next();
        CompilerModuleLoader ml = new CompilerModuleLoader(repositoryManager, null, modules, verbose, statusPrinter, log);
        boolean giveup = false;
        try {
            ml.loadModule(first.getKey(), first.getValue(), ModuleScope.COMPILE);
        } catch (ModuleNotFoundException e) {
            attachErrorToDependencyDeclaration(anyImport, "Pre-resolving of module failed: "+e.getMessage(), true);
            giveup = true;
        }
        if(statusPrinter != null){
            statusPrinter.clearLine();
            // don't try to read the module count if pre-resolving failed
            if(giveup)
                statusPrinter.log("Pre-resolving failed");
            else
                statusPrinter.log("Pre-resolving resolved "+ml.getModuleCount());
        }
        if(verbose){
            // don't try to read the module count if pre-resolving failed
            if(giveup)
                log.printRawLines(WriterKind.NOTICE, "[Pre-resolved failed]");
            else
                log.printRawLines(WriterKind.NOTICE, "[Pre-resolved "+ml.getModuleCount()+" modules]");
        }
        if(giveup)
            return;
        Overrides overrides = repositoryManager.getOverrides();
        if(overrides == null){
            overrides = Overrides.create();
            repositoryManager.setOverrides(overrides);
        }
        ml.setupOverrides(overrides);
        ml.cleanup();
    }
}

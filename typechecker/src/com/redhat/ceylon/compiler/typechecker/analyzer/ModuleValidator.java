package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getNativeBackend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.VersionComparator;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportModule;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.typechecker.context.TypeCache;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/**
 * Validate module dependency:
 * 
 *  - make sure all modules are available
 *  - get modules from local or remote repos if necessary
 *  - parse and process external modules
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleValidator {
    private final Context context;
    private List<PhasedUnits> phasedUnitsOfDependencies;
    private final ModuleManager moduleManager;
    private final ModuleSourceMapper moduleManagerUtil;
    private Map<Module, ArtifactResult> searchedArtifacts = new HashMap<Module, ArtifactResult>();

    public static interface ProgressListener {
        void retrievingModuleArtifact(Module module, ArtifactContext artifactContext);
        void resolvingModuleArtifact(Module module, ArtifactResult artifactResult);
    }
    private ProgressListener listener = new ProgressListener() {
        @Override
        public void resolvingModuleArtifact(Module module,
                ArtifactResult artifactResult) {
        }

        @Override
        public void retrievingModuleArtifact(Module module,
                ArtifactContext artifactContext) {
        }
    };
    
    public ModuleValidator(Context context, PhasedUnits phasedUnits) {
        this.context = context;
        this.moduleManager = phasedUnits.getModuleManager();
        this.moduleManagerUtil = phasedUnits.getModuleSourceMapper();
    }

    public void setListener (ProgressListener listener) {
        this.listener = listener;
    }

    public List<PhasedUnits> getPhasedUnitsOfDependencies() {
        return phasedUnitsOfDependencies;
    }

    /**
     *  At this stage we need to
     *  - resolve all non local modules (recursively)
     *  - build the object model of these compiled modules
     *  - declare a missing module as an error
     *  - detect circular dependencies
     *  - detect module version conflicts
     */
    public void verifyModuleDependencyTree() {
        TypeCache.doWithoutCaching(new Runnable() {
            @Override
            public void run() {
                phasedUnitsOfDependencies = new ArrayList<PhasedUnits>();
                LinkedList<Module> dependencyTree = new LinkedList<Module>();
                // only verify modules we compile (and default/language), as that makes us traverse their dependencies anyways
                Set<Module> compiledModules = moduleManagerUtil.getCompiledModules();
                List<Module> modules = new ArrayList<Module>(compiledModules.size()+2);
                // we must resolve the language module first because it contains definitions that must be in the classpath
                // before any other JVM class is loaded, including the module descriptor annotations themselves
                modules.add(context.getModules().getLanguageModule());
                modules.add(context.getModules().getDefaultModule());
                modules.addAll(compiledModules);
                for (Module module : modules) {
                    dependencyTree.addLast(module);
                    //we don't care about propagated dependency here as top modules are independent from one another
                    verifyModuleDependencyTree(module.getImports(), dependencyTree, new ArrayList<Module>(), ImportDepth.First, searchedArtifacts);
                    dependencyTree.pollLast();
                }
                for (Module module : compiledModules) {
                    verifyNative(module);
                }
                moduleManager.addImplicitImports();
            }
        });
        executeExternalModulePhases();
    }

    public final long numberOfModulesNotAlreadySearched() {
        long result = 0;
        for (Module m : context.getModules().getListOfModules()) {
            if (! m.isAvailable() && !searchedArtifacts.containsKey(m)) {
                result ++;
            }
        }
        return result;
    }
    
    public final long numberOfModulesAlreadySearched() {
        return searchedArtifacts.size();
    }

    /**
     * Used to represent import links with compiled modules
     */
    private enum ImportDepth {
        /**
         * Represents a module directly imported by a compiled module
         */
        First {
            @Override
            public ImportDepth forModuleImport(ModuleImport moduleImport) {
                return Required;
            }

            @Override
            public boolean isVisibleToCompiledModules() {
                return true;
            }
        }, 
        /**
         * Represents a module indirectly imported by a compiled module, through a chain of exported/shared
         * modules imported by a module imported directly by a compiled module
         */
        Required {
            @Override
            public ImportDepth forModuleImport(ModuleImport moduleImport) {
                return moduleImport.isExport() ? Required : Transitive;
            }

            @Override
            public boolean isVisibleToCompiledModules() {
                return true;
            }
        }, 
        /**
         * Represents a module indirectly imported by a compiled module, but not visible to it because
         * it was not shared/exported to it
         */
        Transitive {
            @Override
            public ImportDepth forModuleImport(ModuleImport moduleImport) {
                return Transitive;
            }

            @Override
            public boolean isVisibleToCompiledModules() {
                return false;
            }
        };

        /**
         * Returns a new ImportDepth for the given module import
         */
        public abstract ImportDepth forModuleImport(ModuleImport moduleImport);

        /**
         * Returns true if this import is visible to the compiled modules
         */
        public abstract boolean isVisibleToCompiledModules();
    }
    
    private void verifyModuleDependencyTree(
            Collection<ModuleImport> moduleImports,
            LinkedList<Module> dependencyTree,
            List<Module> propagatedDependencies, 
            ImportDepth importDepth,
            Map<Module, ArtifactResult> alreadySearchedArtifacts) {
        List<Module> visibleDependencies = new ArrayList<Module>();
        visibleDependencies.add(dependencyTree.getLast()); //first addition => no possible conflict
        for (ModuleImport moduleImport : moduleImports) {
            if (moduleImport.isNative() && !moduleManager.supportsBackend(Backend.fromAnnotation(moduleImport.getNativeBackend()))) {
                //import is not for this backend
                continue;
            }
            Module module = moduleImport.getModule();
            if (moduleManager.findModule(module, dependencyTree, true) != null) {
                //circular dependency: stop right here
                return;
            }
            Iterable<String> searchedArtifactExtensions = moduleManager.getSearchedArtifactExtensions();
            ImportDepth newImportDepth = importDepth.forModuleImport(moduleImport);
            
            boolean forCompiledModule = newImportDepth.isVisibleToCompiledModules();
            if ( ! module.isAvailable()) {
                ArtifactResult artifact = null;
                if (alreadySearchedArtifacts.containsKey(module)) {
                    artifact = alreadySearchedArtifacts.get(module);
                } else {
                    //try and load the module from the repository
                    RepositoryManager repositoryManager = context.getRepositoryManager();
                    Exception exceptionOnGetArtifact = null;
                    ArtifactContext artifactContext = new ArtifactContext(module.getNameAsString(), module.getVersion(), getArtifactSuffixes(searchedArtifactExtensions));
                    listener.retrievingModuleArtifact(module, artifactContext);
                    try {
                        artifact = repositoryManager.getArtifactResult(artifactContext);
                    } catch (Exception e) {
                        exceptionOnGetArtifact = catchIfPossible(e);
                    }
                    if (artifact == null) {
                        //not there => error
                        ModuleHelper.buildErrorOnMissingArtifact(artifactContext, module, moduleImport, dependencyTree, exceptionOnGetArtifact, moduleManagerUtil);
                    }
                    alreadySearchedArtifacts.put(module, artifact);
                }
                
                if (artifact != null) {
                    //parse module units and build module dependency and carry on
                    listener.resolvingModuleArtifact(module, artifact);
                    Module moduleOverride = moduleManager.overridesModule(artifact, module, moduleImport);
                    if (moduleOverride != null) {
                        module = moduleOverride;
                        if (importDepth.equals(ImportDepth.First)) {
                            moduleManagerUtil.attachErrorToDependencyDeclaration(moduleImport, dependencyTree, 
                                    "The module import should not be overriden, since it is explicitely imported by a project source module");
                        }
                    }
                    moduleManagerUtil.resolveModule(artifact, module, moduleImport, dependencyTree, phasedUnitsOfDependencies, forCompiledModule);
                }
            }
            moduleManager.visitedModule(module, forCompiledModule);
            dependencyTree.addLast(module);
            List<Module> subModulePropagatedDependencies = new ArrayList<Module>();
            verifyModuleDependencyTree( module.getImports(), dependencyTree, subModulePropagatedDependencies, newImportDepth, alreadySearchedArtifacts);
            //visible dependency += subModule + subModulePropagatedDependencies
            checkAndAddDependency(visibleDependencies, module, dependencyTree);
            for (Module submodule : subModulePropagatedDependencies) {
                checkAndAddDependency(visibleDependencies, submodule, dependencyTree);
            }
            //propagated dependency += if subModule.export then subModule + subModulePropagatedDependencies
            if (moduleImport.isExport()) {
                checkAndAddDependency(propagatedDependencies, module, dependencyTree);
                for (Module submodule : subModulePropagatedDependencies) {
                    checkAndAddDependency(propagatedDependencies, submodule, dependencyTree);
                }
            }
            dependencyTree.pollLast();
        }
    }

    protected Exception catchIfPossible(Exception e) {
        return e;
    }

    private String[] getArtifactSuffixes(Iterable<String> extensions) {
        ArrayList<String> suffixes = new ArrayList<String>();
        for (String ext : extensions) {
            suffixes.add("." + ext);
        }
        return suffixes.toArray(new String[suffixes.size()]);
    }
    
    private void checkAndAddDependency(List<Module> dependencies, Module module, LinkedList<Module> dependencyTree) {
        Module dupe = moduleManager.findModule(module, dependencies, false);
        boolean isDupe = dupe != null;
        if(dupe == null)
            dupe = moduleManager.findSimilarModule(module, dependencies);
        if (dupe != null && !isSameVersion(module, dupe)) {
            //TODO improve by giving the dependency string leading to these two conflicting modules
            if(isDupe){
                StringBuilder error = new StringBuilder("module (transitively) imports conflicting versions of dependency '");
                error.append(module.getNameAsString()).append("': ");
                String[] versions = VersionComparator.orderVersions(module.getVersion(), dupe.getVersion());
                error.append("version '").append(versions[0]).append("' and version '").append(versions[1]).append("'");
                moduleManagerUtil.addErrorToModule(dependencyTree.getFirst(), error.toString());
            }else {
                // just possibly a dupe
                String moduleA;
                String moduleB;
                String moduleName = module.getNameAsString();
                String duplicateModuleName = dupe.getNameAsString();
                if(duplicateModuleName.compareTo(moduleName) < 0){
                    moduleA = ModuleUtil.makeModuleName(duplicateModuleName, dupe.getVersion());
                    moduleB = ModuleUtil.makeModuleName(moduleName, module.getVersion());
                }else{
                    moduleA = ModuleUtil.makeModuleName(moduleName, module.getVersion());
                    moduleB = ModuleUtil.makeModuleName(duplicateModuleName, dupe.getVersion());
                }
                String error = "module (transitively) imports conflicting versions of similar dependencies '" + 
                        moduleA + "' and '"+ moduleB + "'";
                moduleManagerUtil.addWarningToModule(dependencyTree.getFirst(), Warning.similarModule, error);
            }
        }
        else {
            dependencies.add(module);
        }
    }

    private boolean isSameVersion(Module module, Module dupe) {
        if (module == null || dupe == null) return false;
        if (dupe.getVersion() == null) {
            System.err.println("TypeChecker assertion failure: version should not be null in " +
                    "ModuleValidator.isSameVersion. Please report the issue with a test case");
            return false;
        }
        return dupe.getVersion().equals(module.getVersion());
    }

    private void verifyNative(Module module) {
        for (ImportModule imp : moduleManagerUtil.retrieveModuleImportNodes(module)) {
            Module importedModule;
            Node node;
            if (imp.getImportPath()!=null) {
                node = imp.getImportPath();
                importedModule = (Module)imp.getImportPath().getModel();
            } else {
                node = imp.getQuotedLiteral();
                importedModule = moduleManagerUtil.getModuleForNode(node);
                if (importedModule == null) continue;
            }
            String be = getNativeBackend(imp.getAnnotationList(), imp.getUnit());
            if (be == null) {
                if (importedModule.isNative() && !module.isNative()) {
                    node.addError(new ModuleSourceMapper.ModuleDependencyAnalysisError(node, "native import for cross-platform module" +
                            " (mark either the module or the import as native)"));
                }
            } else if (importedModule.isNative() && !be.equals(importedModule.getNativeBackend())) {
                node.addError(new ModuleSourceMapper.ModuleDependencyAnalysisError(node, "native backend name conflicts with imported module: '\"" + 
                        be + "\"' is not '\"" + importedModule.getNativeBackend() + "\"'"));
            }
        }
    }
    
    protected void executeExternalModulePhases() {
        //moduleimport phase already done
        //Already called from within verifyModuleDependencyTree
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            for (PhasedUnit pu : units.getPhasedUnits()) {
                pu.scanDeclarations();
            }
        }
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            for (PhasedUnit pu : units.getPhasedUnits()) {
                pu.scanTypeDeclarations();
            }
        }
        for (PhasedUnits units : phasedUnitsOfDependencies) {
            for (PhasedUnit pu : units.getPhasedUnits()) {
                pu.validateRefinement();
            }
        }
    }
}

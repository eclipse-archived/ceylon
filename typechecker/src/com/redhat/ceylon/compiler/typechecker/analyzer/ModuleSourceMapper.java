package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.formatPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.JdkProvider;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

public class ModuleSourceMapper {
	
	JdkProvider defaultJdkProvider = new JdkProvider();

    public static class ModuleDependencyAnalysisError extends AnalysisError {
        
        public ModuleDependencyAnalysisError(Node treeNode, String message, int code) {
            super(treeNode, message, code);
        }
        
        public ModuleDependencyAnalysisError(Node treeNode, String message, int code, Backend backend) {
            super(treeNode, message, code, backend);
        }
        
        public ModuleDependencyAnalysisError(Node treeNode, String message) {
            super(treeNode, message);
        }
        
        public ModuleDependencyAnalysisError(Node treeNode, String message, Backend backend) {
            super(treeNode, message, backend);
        }
    }

    private final LinkedList<Package> packageStack = new LinkedList<Package>();
    private Module currentModule;
    private final Map<ModuleImport,WeakHashMap<Node, Object>> moduleImportToNode = new HashMap<ModuleImport, WeakHashMap<Node, Object>>();
    private Map<List<String>, Set<String>> topLevelErrorsPerModuleName = new HashMap<List<String>,Set<String>>();
    private Map<Module, Node> moduleToNode = new TreeMap<Module, Node>();
    private ModuleManager moduleManager;
    private Context context;
    private Modules modules;
    private static Object PRESENT = new Object();

    public ModuleSourceMapper(Context context, ModuleManager moduleManager) {
        this.context = context;
        this.moduleManager = moduleManager;
        this.modules = context.getModules();
    }

    public void initCoreModules(){
        moduleManager.initCoreModules(modules);
        packageStack.clear();
        packageStack.addLast( modules.getDefaultModule().getPackages().get(0) );
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

    public void visitModuleFile() {
        if ( currentModule == null ) {
            final Package currentPkg = packageStack.peekLast();
            final List<String> moduleName = currentPkg.getName();
            //we don't know the version at this stage, will be filled later
            currentModule = moduleManager.getOrCreateModule(moduleName, null);
            if ( currentModule != null ) {
                currentModule.setAvailable(true); // TODO : not necessary anymore ? the phasedUnit will be added. And the buildModuleImport()
                                                  //        function (which calls module.setAvailable()) will be called by the typeChecker
                                                  //        BEFORE the ModuleValidator.verifyModuleDependencyTree() call that uses 
                                                  //        isAvailable()
                moduleManager.bindPackageToModule(currentPkg, currentModule);
            }
            else {
                addErrorToModule(new ArrayList<String>(), 
                        "module may not be defined at the top level of the hierarchy");
            }
        }
        else {
            StringBuilder error = new StringBuilder("two modules within the same hierarchy: '");
            error.append( formatPath( currentModule.getName() ) )
                .append( "' and '" )
                .append( formatPath( packageStack.peekLast().getName() ) )
                .append("'");
            addErrorToModule(currentModule.getName(), error.toString());
            addErrorToModule(packageStack.peekLast().getName(), error.toString());
        }
    }

    private void createPackageAndAddToModule(String path) {
        final Package lastPkg = packageStack.peekLast();
        List<String> parentName = lastPkg.getName();
        final ArrayList<String> name = new ArrayList<String>(parentName.size() + 1);
        name.addAll(parentName);
        name.add(path);
        
        Package pkg = moduleManager.createPackage(formatPath(name), 
                currentModule != null ? currentModule : modules.getDefaultModule());
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

    public void addModuleDependencyDefinition(ModuleImport moduleImport, Node definition) {
        WeakHashMap<Node, Object> moduleDepDefinition = moduleImportToNode.get(moduleImport);
        if (moduleDepDefinition == null) {
            moduleDepDefinition = new WeakHashMap<Node, Object>();
            moduleImportToNode.put(moduleImport, moduleDepDefinition);
        }
        moduleDepDefinition.put(definition, PRESENT);
    }

    public void attachErrorToDependencyDeclaration(ModuleImport moduleImport, List<Module> dependencyTree, String error) {
        if (!attachErrorToDependencyDeclaration(moduleImport, error)) {
            //This probably can happen if the missing dependency is found deep in the dependency structure (ie the binary version of a module)
            // in theory the first item in the dependency tree is the compiled module, and the second one is the import we have to add
            // the error to
            if(dependencyTree.size() >= 2){
                Module rootModule = dependencyTree.get(0);
                Module originalImportedModule = dependencyTree.get(1);
                // find the original import
                for(ModuleImport imp : rootModule.getImports()){
                    if(imp.getModule() == originalImportedModule){
                        // found it, try to attach the error
                        if(attachErrorToDependencyDeclaration(imp, error)){
                            // we're done
                            return;
                        }else{
                            // failed
                            break;
                        }
                    }
                }
            }
            System.err.println("This might be a type checker bug, please report. \nExpecting to add missing dependency error on non present definition: " + error);
        }
    }

    private boolean attachErrorToDependencyDeclaration(ModuleImport moduleImport, String error) {
        WeakHashMap<Node, Object> moduleDepError = moduleImportToNode.get(moduleImport);
        if (moduleDepError != null) {
            for ( Node definition :  moduleDepError.keySet() ) {
                definition.addError(new ModuleDependencyAnalysisError(definition, error));
            }
            return true;
        }
        return false;
    }

    public Iterable<ModuleImport> retrieveModuleImports(Module module) {
        List<ModuleImport> imports = new ArrayList<>();
        for (ModuleImport imp : moduleImportToNode.keySet()) {
            if (imp.getModule() == module) {
                imports.add(imp);
            }
        }
        return imports;
    }
    
    public Iterable<Tree.ImportModule> retrieveModuleImportNodes(Module module) {
        List<Tree.ImportModule> nodes = new ArrayList<>();
        for (ModuleImport imp : module.getImports()) {
            WeakHashMap<Node, Object> moduleDepDefinition = moduleImportToNode.get(imp);
            if (moduleDepDefinition != null) {
                for (Node node : moduleDepDefinition.keySet()) {
                    if (node instanceof Tree.ImportModule) {
                        nodes.add((Tree.ImportModule)node);
                    }
                }
            }
        }
        return nodes;
    }
    
    public Module getModuleForNode(Node importNode) {
        for (ModuleImport imp : moduleImportToNode.keySet()) {
            WeakHashMap<Node, Object> moduleDepDefinition = moduleImportToNode.get(imp);
            if (moduleDepDefinition != null) {
                for (Node node : moduleDepDefinition.keySet()) {
                    if (node == importNode) {
                        return imp.getModule();
                    }
                }
            }
        }
        return null;
    }
    
    public void attachErrorToModuleImport(ModuleImport moduleImport, String error){
        WeakHashMap<Node, Object> errors = moduleImportToNode.get(moduleImport);
        if(errors != null){
            for ( Node definition :  errors.keySet() ) {
                definition.addError(new ModuleDependencyAnalysisError(definition, error));
            }
        }
    }

    public void attachErrorToOriginalModuleImport(Module module, String error){
        if(getCompiledModules().contains(module)){
            // we're compiling it, just add it to the module node then
            addErrorToModule(module, error);
        }else{
            // we must be importing it
            for(Entry<ModuleImport, WeakHashMap<Node, Object>> entry : moduleImportToNode.entrySet()){
                if(entry.getKey().getModule() == module){
                    for ( Node definition :  entry.getValue().keySet() ) {
                        definition.addError(new ModuleDependencyAnalysisError(definition, error));
                    }
                }
            }
        }
    }

    //must be used *after* addLinkBetweenModuleAndNode has been set ie post ModuleVisitor visit
    public void addErrorToModule(Module module, String error) {
        Node node = moduleToNode.get(module);
        if (node != null) {
            node.addError(new ModuleDependencyAnalysisError(node, error));
        }
        else {
            //might happen if the faulty module is a compiled module
            System.err.println("This is a type checker bug, please report. " +
                    "\nExpecting to add error on non present module node: " + module.toString() + ". Error " + error);
        }
    }

    //must be used *after* addLinkBetweenModuleAndNode has been set ie post ModuleVisitor visit
    public void addWarningToModule(Module module, Warning warningType, String error) {
        Node node = moduleToNode.get(module);
        if (node != null) {
            node.addUsageWarning(warningType, error);
        }
        else {
            //might happen if the faulty module is a compiled module
            System.err.println("This is a type checker bug, please report. " +
                    "\nExpecting to add error on non present module node: " + module.toString() + ". Error " + error);
        }
    }

    //only used if we really don't know the version
    protected void addErrorToModule(List<String> moduleName, String error) {
        Set<String> errors = topLevelErrorsPerModuleName.get(moduleName);
        if (errors == null) {
            errors = new HashSet<String>();
            topLevelErrorsPerModuleName.put(moduleName, errors);
        }
        errors.add(error);
    }

    public void addLinkBetweenModuleAndNode(Module module, ModuleDescriptor descriptor) {
        //keep link and display errors on modules where we don't know the version of
        Set<String> errors = topLevelErrorsPerModuleName.get(module.getName());
        if (errors != null) {
            for(String error : errors) {
                descriptor.addError(new ModuleDependencyAnalysisError(descriptor, error));
            }
            errors.clear();
        }
        moduleToNode.put(module,descriptor);
    }
    
    public Set<Module> getCompiledModules(){
        return moduleToNode.keySet();
    }
    
    public void resolveModule(ArtifactResult artifact, Module module, ModuleImport moduleImport, LinkedList<Module> dependencyTree, List<PhasedUnits> phasedUnitsOfDependencies, boolean forCompiledModule) {
        //This implementation relies on the ability to read the model from source
        //the compiler for example subclasses this to read lazily and from the compiled model
        ArtifactContext artifactContext = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.SRC);
        RepositoryManager repositoryManager = context.getRepositoryManager();
        Exception exceptionOnGetArtifact = null;
        ArtifactResult sourceArtifact = null;
        try {
            sourceArtifact = repositoryManager.getArtifactResult(artifactContext);
        } catch (Exception e) {
            exceptionOnGetArtifact = e;
        }
        if ( sourceArtifact == null ) {
            ModuleHelper.buildErrorOnMissingArtifact(artifactContext, module, moduleImport, dependencyTree, exceptionOnGetArtifact, this);
        }
        else {
            
            PhasedUnits modulePhasedUnits = createPhasedUnits();
            ClosableVirtualFile virtualArtifact= null;
            try {
                virtualArtifact = context.getVfs().getFromZipFile(sourceArtifact.artifact());
                modulePhasedUnits.parseUnit(virtualArtifact);
                //populate module.getDependencies()
                modulePhasedUnits.visitModules();
                addToPhasedUnitsOfDependencies(modulePhasedUnits, phasedUnitsOfDependencies, module);
            } catch (Exception e) {
                StringBuilder error = new StringBuilder("unable to read source artifact for ");
                error.append(artifactContext.toString());
                error.append( "\ndue to connection error: ").append(e.getMessage());
                attachErrorToDependencyDeclaration(moduleImport, dependencyTree, error.toString());
            } finally {
                if (virtualArtifact != null) {
                    virtualArtifact.close();
                }
            }
        }
    }

    protected void addToPhasedUnitsOfDependencies(PhasedUnits modulePhasedUnits, List<PhasedUnits> phasedUnitsOfDependencies, Module module) {
        phasedUnitsOfDependencies.add(modulePhasedUnits);
    }
    
    protected PhasedUnits createPhasedUnits() {
        return new PhasedUnits(context);
    }
    
    protected ModuleManager getModuleManager(){
        return moduleManager;
    }
    
    protected Context getContext(){
        return context;
    }

	public Module getJdkModule() {
		return null;
	}

	public JdkProvider getJdkProvider() {
		return defaultJdkProvider;
	}
}

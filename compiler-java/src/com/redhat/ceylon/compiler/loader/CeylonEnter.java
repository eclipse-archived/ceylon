package com.redhat.ceylon.compiler.loader;

import java.io.File;
import java.io.IOException;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import com.redhat.ceylon.compiler.codegen.BoxingDeclarationVisitor;
import com.redhat.ceylon.compiler.codegen.BoxingVisitor;
import com.redhat.ceylon.compiler.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.codegen.CodeGenError;
import com.redhat.ceylon.compiler.tools.CeylonPhasedUnit;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Paths;

public class CeylonEnter extends Enter {

    public static CeylonEnter instance(Context context) {
        CeylonEnter instance = (CeylonEnter)context.get(enterKey);
        if (instance == null){
            instance = new CeylonEnter(context);
            context.put(enterKey, instance);
        }
        return instance;
    }

    private CeylonTransformer gen;
    private boolean hasRun = false;
    private PhasedUnits phasedUnits;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private Log log;
    private CeylonModelLoader modelLoader;
    private Options options;
    private Paths paths;
    private CeyloncFileManager fileManager;
    private JavaCompiler compiler;
    
    protected CeylonEnter(Context context) {
        super(context);
        try {
            gen = CeylonTransformer.getInstance(context);
        } catch (Exception e) {
            // FIXME
            e.printStackTrace();
        }
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        log = Log.instance(context);
        modelLoader = CeylonModelLoader.instance(context);
        options = Options.instance(context);
        paths = Paths.instance(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
        compiler = LanguageCompiler.instance(context);
    }

    @Override
    public void main(List<JCCompilationUnit> trees) {
        // complete the javac AST with a completed ceylon model
        completeCeylonTrees(trees);
        super.main(trees);
    }

    @Override
    protected Type classEnter(JCTree tree, Env<AttrContext> env) {
        if(tree instanceof CeylonCompilationUnit){
            Context.SourceLanguage.push(Language.CEYLON);
            try{
                return super.classEnter(tree, env);
            }finally{
                Context.SourceLanguage.pop();
            }
        }else
            return super.classEnter(tree, env);
    }
    
    private void printModules() {
        for(Module module : ceylonContext.getModules().getListOfModules()){
            System.err.println("Found module: "+module.getNameAsString());
            for(Package pkg : module.getPackages()){
                System.err.println(" Found package: "+pkg.getNameAsString());
                for(Declaration decl : pkg.getMembers()){
                    System.err.println("  Found Decl: "+decl);
                }
            }
        }
    }

    public void completeCeylonTrees(List<JCCompilationUnit> trees) {
        if (hasRun)
            throw new RuntimeException("Waaaaa, running twice!!!");
        // load the standard modules
        modelLoader.loadStandardModules();
        // load the modules we are compiling first
        loadCompiledModules();
        hasRun = true;
        // make sure we don't load the files we are compiling from their class files
        modelLoader.setupSourceFileObjects(trees);
        // resolve module dependencies
        resolveModuleDependencies();
        // run the type checker
        typeCheck();
        // some debugging
        //printModules();
        /*
         * Here we convert the ceylon tree to its javac AST, after the typechecker has run
         */
        for (JCCompilationUnit tree : trees) {
            if (tree instanceof CeylonCompilationUnit) {
                CeylonCompilationUnit ceylonTree = (CeylonCompilationUnit) tree;
                gen.setMap(ceylonTree.lineMap);
                ceylonTree.defs = gen.transformAfterTypeChecking(ceylonTree.ceylonTree).toList();
                if(options.get(OptionName.VERBOSE) != null){
                    System.err.println("Java code generated for "+tree.getSourceFile());
                    System.err.println(ceylonTree);
                }
            }
        }
        printGeneratorErrors();
    }

    // FIXME: this needs to be replaced when we deal with modules
    private void resolveModuleDependencies() {
        Modules modules = ceylonContext.getModules();
        // every module depends on java.lang implicitely
        Module javaModule = modelLoader.findOrCreateModule("java.lang");
        // make sure java.lang is available
        modelLoader.findOrCreatePackage(javaModule, "java.lang");
        for(Module m : modules.getListOfModules()){
            if(!m.getName().equals("java")){
                m.getDependencies().add(javaModule);
            }
        }
    }

    private void loadCompiledModules() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        // FIXME: handle loading modules from compiled class
        for (PhasedUnit pu : listOfUnits) {
            pu.buildModuleImport();
        }
        Modules modules = ceylonContext.getModules();
        // at this point every module should be available
        for(Module m : modules.getListOfModules()){
            m.setAvailable(true);
        }
        // now make sure the phase units have their modules and packages set correctly
        for (PhasedUnit pu : listOfUnits) {
            Package pkg = pu.getPackage();
            // skip it if we already resolved the package
            if(pkg.getModule() != null)
                continue;
            String pkgName = pkg.getQualifiedNameString();
            Module module = null;
            // do we have a module for this package?
            // FIXME: is this true? what if we have a module.ceylon at toplevel?
            if(pkgName.isEmpty())
                module = modules.getDefaultModule();
            else{
                for(Module m : modules.getListOfModules()){
                    if(pkgName.startsWith(m.getNameAsString())){
                        module = m;
                        break;
                    }
                }
                if(module == null){
                    module = loadModuleFromSource(pkgName);
                }
                if(module == null){
                    // no declaration for it, must be the default module
                    module = modules.getDefaultModule();
                }
            }
            // bind module and package together
            pkg.setModule(module);
            module.getPackages().add(pkg);
            // automatically add this module's jar to the classpath if it exists
            addModuleToClassPath(module);
        }
    }

    private Module loadModuleFromSource(String pkgName) {
        if(pkgName.isEmpty())
            return null;
        String moduleClassName = pkgName + ".module";
        JavaFileObject fileObject;
        try {
            System.err.println("Trying to load module "+moduleClassName);
            fileObject = fileManager.getJavaFileForInput(StandardLocation.SOURCE_PATH, moduleClassName, Kind.SOURCE);
            System.err.println("Got file object: "+fileObject);
        } catch (IOException e) {
            e.printStackTrace();
            return loadModuleFromSource(getParentPackage(pkgName));
        }
        if(fileObject != null){
            CeylonCompilationUnit ceylonCompilationUnit = (CeylonCompilationUnit) compiler.parse(fileObject);
            // parse the module info from there
            ceylonCompilationUnit.phasedUnit.buildModuleImport();
            // now try to obtain the parsed module
            Module module = getModule(pkgName);
            if(module != null){
                ceylonCompilationUnit.phasedUnit.getPackage().setModule(module);
                return module;
            }
        }
        return loadModuleFromSource(getParentPackage(pkgName));
    }

    private String getParentPackage(String pkgName) {
        int lastDot = pkgName.lastIndexOf(".");
        if(lastDot == -1)
            return "";
        return pkgName.substring(0, lastDot);
    }

    private Module getModule(String pkgName) {
        Modules modules = ceylonContext.getModules();
        for(Module m : modules.getListOfModules()){
            if(pkgName.equals(m.getNameAsString())){
                return m;
            }
        }
        return null;
    }

    private void addModuleToClassPath(Module module) {
        Paths.Path classPath = paths.getPathForLocation(StandardLocation.CLASS_PATH);
        Iterable<? extends File> location = fileManager.getLocation(StandardLocation.CLASS_OUTPUT);
        File classDir = location.iterator().next();
        File moduleJar = new File(classDir, Util.getJarName(module));
        classPath.addFile(moduleJar, false);
    }

    private void typeCheck() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();

        final ModuleValidator moduleValidator = new ModuleValidator(ceylonContext);
        // FIXME: this breaks because it tries to load dependencies on its own
        // moduleValidator.verifyModuleDependencyTree();
        // FIXME: what's that for?
        java.util.List<PhasedUnits> phasedUnitsOfDependencies = moduleValidator.getPhasedUnitsOfDependencies();
        for (PhasedUnit pu : listOfUnits) {
            pu.validateTree();
            pu.scanDeclarations();
        }
        for (PhasedUnit pu : listOfUnits) { 
            pu.scanTypeDeclarations(); 
        } 
        for (PhasedUnit pu: listOfUnits) { 
            pu.validateRefinement();
        }
        for (PhasedUnit pu : listOfUnits) { 
            pu.analyseTypes(); 
        }
        for (PhasedUnit pu : listOfUnits) { 
            pu.analyseFlow();
        }
        BoxingDeclarationVisitor boxingDeclarationVisitor = new BoxingDeclarationVisitor(gen);
        BoxingVisitor boxingVisitor = new BoxingVisitor(gen);
        // Extra phases for the compiler
        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(boxingDeclarationVisitor);
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(boxingVisitor);
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(new JavacAssertionVisitor((CeylonPhasedUnit) pu){
                @Override
                protected void out(UnexpectedError err) {
                    logError(getPosition(err.getTreeNode()), err.getMessage());
                }
                @Override
                protected void out(AnalysisError err) {
                    logError(getPosition(err.getTreeNode()), err.getMessage());
                }
                @Override
                protected void out(AnalysisWarning err) {
                    logWarning(getPosition(err.getTreeNode()), err.getMessage());
                }
                @Override
                protected void out(Node that, String message) {
                    logError(getPosition(that), message);
                }
            });
        }
    }

    private void printGeneratorErrors() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();

        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(new JavacAssertionVisitor((CeylonPhasedUnit) pu){
                @Override
                protected void out(UnexpectedError err) {
                    if(err instanceof CodeGenError){
                        CodeGenError error = ((CodeGenError)err);
                        logError(getPosition(err.getTreeNode()), "Compiler error: "+error.getCause());
                        error.getCause().printStackTrace();
                    }
                }
                // Ignore those
                @Override
                protected void out(AnalysisError err) {}
                @Override
                protected void out(AnalysisWarning err) {}
                @Override
                protected void out(Node that, String message) {}
            });
        }
    }

    protected void logError(int position, String message) {
        boolean prev = log.multipleErrors;
        // we want multiple errors for Ceylon
        log.multipleErrors = true;
        try{
            log.error(position, "ceylon", message);
        }finally{
            log.multipleErrors = prev;
        }
    }

    protected void logWarning(int position, String message) {
        boolean prev = log.multipleErrors;
        // we want multiple errors for Ceylon
        log.multipleErrors = true;
        try{
            log.warning(position, "ceylon", message);
        }finally{
            log.multipleErrors = prev;
        }
    }

    private class JavacAssertionVisitor extends AssertionVisitor {
        private CeylonPhasedUnit cpu;
        JavacAssertionVisitor(CeylonPhasedUnit cpu){
            this.cpu = cpu;
        }
        protected int getPosition(Node node) {
            int pos = cpu.getLineMap().getStartPosition(node.getToken().getLine())
            + node.getToken().getCharPositionInLine();
            log.useSource(cpu.getFileObject());
            return pos;
        }
    }
    
    public boolean hasRun(){
        return hasRun;
    }
}

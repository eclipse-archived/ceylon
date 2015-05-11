package com.redhat.ceylon.compiler.java.tools;

import com.redhat.ceylon.compiler.java.loader.UnknownTypeCollector;
import com.redhat.ceylon.compiler.java.loader.model.CompilerModuleManager;
import com.redhat.ceylon.compiler.java.loader.model.LazyModuleSourceMapper;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler.CompilerDelegate;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public final class CeyloncCompilerDelegate implements
        CompilerDelegate {
    private final Context context;
    private CompilerModuleManager moduleManager;
    private LazyModuleSourceMapper moduleSourceMapper;

    public CeyloncCompilerDelegate(Context context) {
        this.context = context;
    }

    @Override
    public CompilerModuleManager getModuleManager() {
        if(moduleManager == null){
            com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
            moduleManager = new CompilerModuleManager(ceylonContext, context);
        }
        return moduleManager;
    }

    @Override
    public ModuleSourceMapper getModuleSourceMapper() {
        if(moduleSourceMapper == null){
            com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
            moduleSourceMapper = new LazyModuleSourceMapper(ceylonContext, getModuleManager());
        }
        return moduleSourceMapper;
    }

    @Override
    public PhasedUnit getExternalSourcePhasedUnit(
            VirtualFile srcDir, VirtualFile file) {
        return null;
    }

    @Override
    public void typeCheck(java.util.List<PhasedUnit> listOfUnits) {
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

        for (PhasedUnit pu : listOfUnits) { 
            pu.analyseUsage();
        }
        
        UnknownTypeCollector utc = new UnknownTypeCollector();
        for (PhasedUnit pu : listOfUnits) { 
            pu.getCompilationUnit().visit(utc);
        }
    }

    @Override
    public void visitModules(PhasedUnits phasedUnits) {
        phasedUnits.visitModules();
    }

    @Override
    public void loadPackageDescriptors(AbstractModelLoader modelLoader) {
        modelLoader.loadPackageDescriptors();
    }

    @Override
    public void resolveModuleDependencies(PhasedUnits phasedUnits) {
        com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        ModuleValidator validator = new ModuleValidator(ceylonContext, phasedUnits);
        validator.verifyModuleDependencyTree();
    }

    @Override
    public void setupSourceFileObjects(List<JCCompilationUnit> trees, AbstractModelLoader modelLoader) {
        modelLoader.setupSourceFileObjects(trees);
    }

    @Override
    public void loadStandardModules(AbstractModelLoader modelLoader) {
        com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        Module languageModule = ceylonContext.getModules().getLanguageModule();
        if (languageModule.getVersion() == null) {
            languageModule.setVersion(TypeChecker.LANGUAGE_MODULE_VERSION);
        }
        modelLoader.loadStandardModules();
    }
}
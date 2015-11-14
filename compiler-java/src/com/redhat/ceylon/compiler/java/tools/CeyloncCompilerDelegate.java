package com.redhat.ceylon.compiler.java.tools;

import com.redhat.ceylon.common.StatusPrinter;
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
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Options;

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
        StatusPrinter sp = getStatusPrinter();

        int size = listOfUnits.size();
        int i=1;
        for (PhasedUnit pu : listOfUnits) {
            if(sp != null)
                progress(sp, 1, i++, size, pu);
            pu.validateTree();
            pu.scanDeclarations();
        }
        i=1;
        for (PhasedUnit pu : listOfUnits) { 
            if(sp != null)
                progress(sp, 2, i++, size, pu);
            pu.scanTypeDeclarations(); 
        } 
        i=1;
        for (PhasedUnit pu: listOfUnits) { 
            if(sp != null)
                progress(sp, 3, i++, size, pu);
            pu.validateRefinement();
        }
        
        i=1;
        for (PhasedUnit pu : listOfUnits) { 
            if(sp != null)
                progress(sp, 4, i++, size, pu);
            pu.analyseTypes(); 
        }
        
        i=1;
        for (PhasedUnit pu : listOfUnits) { 
            if(sp != null)
                progress(sp, 5, i++, size, pu);
            pu.analyseFlow();
        }

        i=1;
        for (PhasedUnit pu : listOfUnits) { 
            if(sp != null)
                progress(sp, 6, i++, size, pu);
            pu.analyseUsage();
        }
        
        i=1;
        UnknownTypeCollector utc = new UnknownTypeCollector();
        for (PhasedUnit pu : listOfUnits) { 
            if(sp != null)
                progress(sp, 7, i++, size, pu);
            pu.getCompilationUnit().visit(utc);
        }
    }

    private StatusPrinter getStatusPrinter() {
        Options options = Options.instance(context);
        boolean isProgressPrinted = options.get(OptionName.CEYLONPROGRESS) != null && StatusPrinter.canPrint();
        if(isProgressPrinted){
            return LanguageCompiler.getStatusPrinterInstance(context);
        }else{
            return null;
        }
    }

    private void progress(StatusPrinter sp, int phase, int i, int size, PhasedUnit pu) {
        sp.clearLine();
        sp.log("Typechecking "+phase+"/7 ["+i+"/"+size+"] ");
        sp.log(pu.getPathRelativeToSrcDir());
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
        final StatusPrinter sp = getStatusPrinter();
        com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        final ModuleValidator validator = new ModuleValidator(ceylonContext, phasedUnits);
        if(sp != null){
            validator.setListener(new StatusPrinterProgressListener(validator, sp));
            sp.clearLine();
            sp.log("Starting resolving");
        }
        validator.verifyModuleDependencyTree();
        if(sp != null){
            sp.clearLine();
            sp.log("Done resolving");
        }
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
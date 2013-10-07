package com.redhat.ceylon.compiler.java.tools;

import com.redhat.ceylon.compiler.java.loader.CeylonEnter;
import com.redhat.ceylon.compiler.java.loader.UnknownTypeCollector;
import com.redhat.ceylon.compiler.java.loader.model.CompilerModuleManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler.CompilerDelegate;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

public final class CeyloncCompilerDelegate implements
        CompilerDelegate {
    private final Context context;

    public CeyloncCompilerDelegate(Context context) {
        this.context = context;
    }

    @Override
    public ModuleManager getModuleManager() {
        com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        return new CompilerModuleManager(ceylonContext, context);
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
    public void prepareForTypeChecking(List<JCCompilationUnit> trees) {
        CeylonEnter.instance(context).prepareForTypeChecking(trees);
    }
}
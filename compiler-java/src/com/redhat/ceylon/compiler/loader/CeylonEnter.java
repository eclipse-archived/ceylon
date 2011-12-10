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

package com.redhat.ceylon.compiler.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;

import com.redhat.ceylon.compiler.codegen.BoxingDeclarationVisitor;
import com.redhat.ceylon.compiler.codegen.BoxingVisitor;
import com.redhat.ceylon.compiler.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.codegen.CodeGenError;
import com.redhat.ceylon.compiler.tools.CeylonLocation;
import com.redhat.ceylon.compiler.tools.CeylonLog;
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
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
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
        // make sure it's loaded first
        CeylonClassReader.instance(context);
        try {
            gen = CeylonTransformer.getInstance(context);
        } catch (Exception e) {
            // FIXME
            e.printStackTrace();
        }
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        log = CeylonLog.instance(context);
        modelLoader = CeylonModelLoader.instance(context);
        options = Options.instance(context);
        paths = Paths.instance(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
        compiler = LanguageCompiler.instance(context);
        // now superclass init
        init(context);
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
        hasRun = true;
        // make sure we don't load the files we are compiling from their class files
        modelLoader.setupSourceFileObjects(trees);
        // resolve module dependencies
        resolveModuleDependencies();
        // now load package descriptors
        modelLoader.loadPackageDescriptors();
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
                    System.err.println("Model tree for "+tree.getSourceFile());
                    System.err.println(ceylonTree.ceylonTree);
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
        
        ModuleValidator validator = new ModuleValidator(ceylonContext, phasedUnits);
        validator.verifyModuleDependencyTree();

        // every module depends on java.lang implicitely
        Module javaModule = modelLoader.findOrCreateModule("java.lang");
        // make sure java.lang is available
        modelLoader.findOrCreatePackage(javaModule, "java.lang");
        for(Module m : modules.getListOfModules()){
            if(!m.getName().equals("java")){
                ModuleImport moduleImport = phasedUnits.getModuleManager().findImport(m, javaModule);
                if (moduleImport == null) {
                    moduleImport = new ModuleImport(javaModule, false, true);
                    m.getImports().add(moduleImport);
                }
            }
        }
    }

    public void addModuleToClassPath(Module module, boolean errorIfMissing) {
        Paths.Path classPath = paths.getPathForLocation(StandardLocation.CLASS_PATH);
        
        Iterable<? extends File> outputLocation = fileManager.getLocation(StandardLocation.CLASS_OUTPUT);
        if (outputLocation != null && outputLocation.iterator().hasNext()) {
            File outputRepository = outputLocation.iterator().next();
            if (addModuleFromRepository(module, outputRepository, classPath)) {
                return;
            }
        }
        
        Iterable<? extends File> repositories = fileManager.getLocation(CeylonLocation.REPOSITORY);
        for(File repository : repositories){
            if (addModuleFromRepository(module, repository, classPath)) {
                return;
            }
        }
        if(errorIfMissing)
            log.error("ceylon", "Failed to find module "+module.getNameAsString()+"/"+module.getVersion()+" in repositories");
    }

    private boolean addModuleFromRepository(Module module, File repository, Paths.Path classPath) {
        File moduleDir = Util.getModulePath(repository, module);
        File moduleJar = new File(moduleDir, Util.getModuleArchiveName(module));
        if(moduleJar.exists()){
            classPath.addFile(moduleJar, false);
            return true;
        }
        return false;
    }
    
    private void typeCheck() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();

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

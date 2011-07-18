package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleValidator;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;

public class CeylonEnter extends Enter {

    public static Enter instance(Context context) {
        Enter instance = context.get(enterKey);
        if (instance == null){
            instance = new CeylonEnter(context);
            context.put(enterKey, instance);
        }
        return instance;
    }

    private Gen2 gen;
    private boolean hasRun = false;
    private PhasedUnits phasedUnits;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private Log log;
    private CeylonModelLoader modelLoader;
    
    protected CeylonEnter(Context context) {
        super(context);
        try {
            gen = Gen2.getInstance(context);
        } catch (Exception e) {
            // FIXME
            e.printStackTrace();
        }
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        log = Log.instance(context);
        modelLoader = CeylonModelLoader.instance(context);
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
        hasRun = true;
        // load modules required by the typechecker
        modelLoader.loadRequiredModules(trees);
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
                ceylonTree.defs = gen.convertAfterTypeChecking(ceylonTree.ceylonTree).toList();
                System.err.println(ceylonTree);
            }
        }
    }

    private void typeCheck() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        for (PhasedUnit pu : listOfUnits) {
            pu.buildModuleImport();
        }

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
        for (PhasedUnit pu : listOfUnits) { 
            pu.display(); 
            pu.getCompilationUnit().visit(new AssertionVisitor(){
                // FIXME: use regular javac log.error. We must define a localisation key for that, and 
                // be able to access the javac AST we generated for a given CU because it holds the 
                // source file object for javac 
                @Override
                protected void out(AnalysisError err) {
                    log.rawError(0, err.getMessage() + "\n at " + 
                            err.getTreeNode().getAntlrTreeNode().getLine() + ":" +
                            err.getTreeNode().getAntlrTreeNode().getCharPositionInLine() + " of " +
                            err.getTreeNode().getUnit().getFilename());
                }
                @Override
                protected void out(Node that, String message) {
                    log.rawError(0, message + "\n at " + 
                            that.getAntlrTreeNode().getLine() + ":" +
                            that.getAntlrTreeNode().getCharPositionInLine() + " of " +
                            that.getUnit().getFilename());
                }
            });
        }
        /*
        */
    }
}

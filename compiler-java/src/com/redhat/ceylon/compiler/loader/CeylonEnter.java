package com.redhat.ceylon.compiler.loader;

import com.redhat.ceylon.compiler.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.tools.CeylonPhasedUnit;
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
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Context.SourceLanguage.Language;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;

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
        // load the modules we are compiling first
        loadCompiledModules();
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
                ceylonTree.defs = gen.transformAfterTypeChecking(ceylonTree.ceylonTree).toList();
                if(options.get(OptionName.VERBOSE) != null){
                    System.err.println("Java code generated for "+tree.getSourceFile());
                    System.err.println(ceylonTree);
                }
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
                    // no declaration for it, must be the default module
                    module = modules.getDefaultModule();
                }
            }
            // bind module and package together
            pkg.setModule(module);
            module.getPackages().add(pkg);
        }
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
        for (PhasedUnit pu : listOfUnits) {
            final CeylonPhasedUnit cpu = (CeylonPhasedUnit) pu;
            pu.getCompilationUnit().visit(new AssertionVisitor(){
                private int getPosition(Node node) {
                    int pos = cpu.getLineMap().getStartPosition(node.getAntlrTreeNode().getLine())
                    + node.getAntlrTreeNode().getCharPositionInLine();
                    log.useSource(cpu.getFileObject());
                    return pos;
                }
                @Override
                protected void out(UnexpectedError err) {
                    log.error(getPosition(err.getTreeNode()), "ceylon", err.getMessage());
                }
                @Override
                protected void out(AnalysisError err) {
                    log.error(getPosition(err.getTreeNode()), "ceylon", err.getMessage());
                }
                @Override
                protected void out(AnalysisWarning err) {
                    log.warning(getPosition(err.getTreeNode()), "ceylon", err.getMessage());
                }
                @Override
                protected void out(Node that, String message) {
                    log.error(getPosition(that), "ceylon", message);
                }
            });
        }
    }
    
    public boolean hasRun(){
        return hasRun;
    }
}

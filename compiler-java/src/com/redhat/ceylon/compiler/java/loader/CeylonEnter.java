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

package com.redhat.ceylon.compiler.java.loader;

import java.io.File;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.InvalidArchiveException;
import com.redhat.ceylon.compiler.java.codegen.BoxingDeclarationVisitor;
import com.redhat.ceylon.compiler.java.codegen.BoxingVisitor;
import com.redhat.ceylon.compiler.java.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.java.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.java.codegen.CodeGenError;
import com.redhat.ceylon.compiler.java.codegen.CompilerBoxingDeclarationVisitor;
import com.redhat.ceylon.compiler.java.codegen.CompilerBoxingVisitor;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.CeylonPhasedUnit;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler.PhasedUnitsManager;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.Annotate;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.comp.Todo;
import com.sun.tools.javac.file.Paths;
import com.sun.tools.javac.main.JavaCompiler;
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
    private PhasedUnitsManager phasedUnitsManager;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private Log log;
    private AbstractModelLoader modelLoader;
    private Options options;
    private Timer timer;
    private Paths paths;
    private CeyloncFileManager fileManager;
    private JavaCompiler compiler;
    private boolean allowWarnings;
    private boolean verbose;
    private Check chk;
    private Types types;
    private Symtab symtab;
    private Todo todo;
    private boolean isBootstrap;
    private Annotate annotate;
    
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
        phasedUnitsManager = LanguageCompiler.getPhasedUnitsManagerInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        log = CeylonLog.instance(context);
        modelLoader = CeylonModelLoader.instance(context);
        options = Options.instance(context);
        timer = Timer.instance(context);
        paths = Paths.instance(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
        compiler = LanguageCompiler.instance(context);
        allowWarnings = com.redhat.ceylon.compiler.Util.allowWarnings(context);
        verbose = options.get(OptionName.VERBOSE) != null;
        isBootstrap = options.get(OptionName.BOOTSTRAPCEYLON) != null;
        chk = Check.instance(context);
        types = Types.instance(context);
        symtab = Symtab.instance(context);
        todo = Todo.instance(context);
        annotate = Annotate.instance(context);

        // now superclass init
        init(context);
    }

    @Override
    public void main(List<JCCompilationUnit> trees) {
        // complete the javac AST with a completed ceylon model
        timer.startTask("prepareForTypeChecking");
        prepareForTypeChecking(trees);
        timer.endTask();
        List<JCCompilationUnit> javaTrees = List.nil();
        List<JCCompilationUnit> ceylonTrees = List.nil();
        if (modelLoader instanceof CeylonModelLoader) {
            // split them in two sets: java and ceylon
            for(JCCompilationUnit tree : trees){
                if(tree instanceof CeylonCompilationUnit)
                    ceylonTrees = ceylonTrees.prepend(tree);
                else
                    javaTrees = javaTrees.prepend(tree);
            }
            timer.startTask("Enter on Java trees");
            // enter java trees first to set up their ClassSymbol objects for ceylon trees to use during type-checking
            if(isBootstrap){
                super.main(trees);
            }else if(!javaTrees.isEmpty()){
                super.main(javaTrees);
            }
            // now we can type-check the Ceylon code
            completeCeylonTrees(trees);
            
            if(isBootstrap){
                // bootstrapping the language module is a bit more complex
                resetAndRunEnterAgain(trees);
            }else{
                timer.startTask("Enter on Ceylon trees");
                // and complete their new trees
                super.main(ceylonTrees);
                timer.endTask();
            }
        }
        else {
            completeCeylonTrees(trees);
            super.main(trees);
        }
    }

    private void resetAndRunEnterAgain(List<JCCompilationUnit> trees) {
        timer.startTask("Resetting all trees for bootstrap");
        
        // get rid of some caches and state
        chk.compiled.clear();
        types.reset();
        annotate.reset();
        super.reset();
        
        // reset all class symbols
        for(ClassSymbol classSymbol : symtab.classes.values()){
            if(Util.isLoadedFromSource(classSymbol) 
                    || (classSymbol.sourcefile != null && classSymbol.sourcefile.getKind() == Kind.SOURCE)){
                PackageSymbol pkg = classSymbol.packge();
                String name = pkg.getQualifiedName().toString();
                if(name.startsWith("ceylon.language") || name.startsWith("com.redhat.ceylon.compiler.java"))
                    resetClassSymbol(classSymbol);
            }
        }
        
        // reset the trees
        JCTypeResetter jcTypeResetter = new JCTypeResetter();
        for(JCCompilationUnit tree : trees){
            tree.accept(jcTypeResetter);
        }
        
        // and reset the list of things to compile, because we got rid of the Env key we used to look them up
        // so they'd appear as extra things to compile when we do Enter
        todo.reset();
        timer.endTask();
        
        timer.startTask("Enter on Java+Ceylon trees");
        // now do Enter on all the java+ceylon code
        super.main(trees);
        timer.endTask();
    }

    /**
     * This resets a ClassSymbol recursively, for bootstrap
     */
    private void resetClassSymbol(ClassSymbol classSymbol) {
        // look for inner classes
        if(classSymbol.members_field != null){
            for(Symbol member : classSymbol.getEnclosedElements()){
                if(member instanceof ClassSymbol){
                    resetClassSymbol((ClassSymbol)member);
                }
            }
        }
        
        // reset its type, we need to keep it
        Type.ClassType classType = (ClassType) classSymbol.type;
        classType.all_interfaces_field = null;
        classType.interfaces_field = null;
        classType.supertype_field = null;
        classType.typarams_field = null;
        
        // reset its members and completer
        classSymbol.members_field = null;
        classSymbol.completer = null;
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

    public void prepareForTypeChecking(List<JCCompilationUnit> trees) {
        if (hasRun)
            throw new RuntimeException("Waaaaa, running twice!!!");
        //By now le language module version should be known (as local)
        //or we should use the default one.
        Module languageModule = ceylonContext.getModules().getLanguageModule();
        if (languageModule.getVersion() == null) {
            languageModule.setVersion(TypeChecker.LANGUAGE_MODULE_VERSION);
        }
        // load the standard modules
        modelLoader.loadStandardModules();
        // load the modules we are compiling first
        hasRun = true;
        // make sure we don't load the files we are compiling from their class files
        modelLoader.setupSourceFileObjects(trees);
        // resolve module dependencies
        phasedUnitsManager.resolveDependencies();
        // now load package descriptors
        modelLoader.loadPackageDescriptors();
    }
    
    public void completeCeylonTrees(List<JCCompilationUnit> trees){
        // run the type checker
        timer.startTask("Ceylon type checking");
        typeCheck();
        // some debugging
        //printModules();
        timer.startTask("Ceylon code generation");
        /*
         * Here we convert the ceylon tree to its javac AST, after the typechecker has run
         */
        Timer nested = timer.nestedTimer();
        for (JCCompilationUnit tree : trees) {
            if (tree instanceof CeylonCompilationUnit) {
                CeylonCompilationUnit ceylonTree = (CeylonCompilationUnit) tree;
                gen.setMap(ceylonTree.lineMap);
                gen.setFileObject(((CeylonPhasedUnit)ceylonTree.phasedUnit).getFileObject());
                nested.startTask("Ceylon code generation for " + ((CeylonPhasedUnit)ceylonTree.phasedUnit).getUnitFile().getName());
                ceylonTree.defs = gen.transformAfterTypeChecking(ceylonTree.ceylonTree).toList();
                nested.endTask();
                if(isVerbose("ast")){
                    System.err.println("Model tree for "+tree.getSourceFile());
                    System.err.println(ceylonTree.ceylonTree);
                }
                if(isVerbose("code")){
                    System.err.println("Java code generated for "+tree.getSourceFile());
                    System.err.println(ceylonTree);
                }
            }
        }
        timer.startTask("Ceylon error generation");
        printGeneratorErrors();
        timer.endTask();
        // write some stats
        if(verbose)
            modelLoader.printStats();
    }

    private boolean isVerbose(String key) {
        return verbose || options.get(OptionName.VERBOSE + ":" + key) != null;
    }

    public void addOutputModuleToClassPath(Module module){
        RepositoryManager repositoryManager = fileManager.getOutputRepositoryManager();
        ArtifactResult artifact = null;
        try {
            ArtifactContext ctx = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.CAR);
            artifact = repositoryManager.getArtifactResult(ctx);
            if(artifact == null){
                // try again for a jar
                ctx.setSuffix(ArtifactContext.JAR);
                artifact = repositoryManager.getArtifactResult(ctx);
            }
        } catch (InvalidArchiveException e) {
            log.error("ceylon", "Module car " + e.getPath()
                    +" obtained from repository " + e.getRepository()
                    +" has an invalid SHA1 signature: you need to remove it and rebuild the archive, since it"
                    +" may be corrupted.");
        } catch (Exception e) {
            String moduleName = module.getNameAsString();
            if(!module.isDefault())
                moduleName += "/" + module.getVersion();
            log.error("ceylon", "Exception occured while trying to resolve module "+moduleName);
            e.printStackTrace();
        }
        addModuleToClassPath(module, false, artifact);
    }
    
    public void addModuleToClassPath(Module module, boolean errorIfMissing, ArtifactResult result) {
        if(verbose)
            Log.printLines(log.noticeWriter, "[Adding module to classpath: "+module.getNameAsString()+"/"+module.getVersion()+"]");        
        
        Paths.Path classPath = paths.getPathForLocation(StandardLocation.CLASS_PATH);

        File artifact = null;
        try {
            artifact = result != null ? result.artifact() : null;
        } catch (Exception e) {
            String moduleName = module.getNameAsString();
            if(!module.isDefault())
                moduleName += "/" + module.getVersion();
            log.error("ceylon", "Exception occured while trying to resolve module "+moduleName);
            e.printStackTrace();
        }
        
        if(verbose){
            if(artifact != null)
                Log.printLines(log.noticeWriter, "[Found module at : "+artifact.getPath()+"]");
            else
                Log.printLines(log.noticeWriter, "[Could not find module]");
        }

        if(artifact != null && artifact.exists())
            classPath.add(artifact);
        else if(errorIfMissing)
            log.error("ceylon", "Failed to find module "+module.getNameAsString()+"/"+module.getVersion()+" in repositories");
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
        
        Iterable<PhasedUnit> phasedUnitsForExtraPhase = phasedUnitsManager.getPhasedUnitsForExtraPhase(listOfUnits);
        for (PhasedUnit pu : phasedUnitsForExtraPhase) {
            Unit unit = pu.getUnit();
            final CompilationUnit compilationUnit = pu.getCompilationUnit();
            for (Declaration d: unit.getDeclarations()) {
                if (d instanceof TypedDeclaration && !(d instanceof Setter)) {
                    compilationUnit.visit(new ValueVisitor((TypedDeclaration) d));
                }
            }
        }
        
        BoxingDeclarationVisitor boxingDeclarationVisitor = new CompilerBoxingDeclarationVisitor(gen);
        BoxingVisitor boxingVisitor = new CompilerBoxingVisitor(gen);
        // Extra phases for the compiler
        for (PhasedUnit pu : phasedUnitsForExtraPhase) {
            pu.getCompilationUnit().visit(boxingDeclarationVisitor);
        }
        for (PhasedUnit pu : phasedUnitsForExtraPhase) {
            pu.getCompilationUnit().visit(boxingVisitor);
        }
        
        phasedUnitsManager.extraPhasesApplied();
        
        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(new JavacAssertionVisitor((CeylonPhasedUnit) pu){
                @Override
                protected void out(UnexpectedError err) {
                    logError(getPosition(err.getTreeNode()), err.getMessage());
                }
                @Override
                protected void out(AnalysisError err) {
                    Node node = getIdentifyingNode(err.getTreeNode());
                    logError(getPosition(node), err.getMessage());
                }
                @Override
                protected void out(AnalysisWarning err) {
                    Node node = getIdentifyingNode(err.getTreeNode());
                    if (allowWarnings) {
                        logWarning(getPosition(node), err.getMessage());
                    } else {
                        logError(getPosition(node), err.getMessage());
                    }
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
        protected Node getIdentifyingNode(Node node) {
            if (node instanceof Tree.Declaration) {
                return ((Tree.Declaration) node).getIdentifier();
            }
            else if (node instanceof Tree.ModuleDescriptor) {
                return ((Tree.ModuleDescriptor) node).getImportPath();
            }
            else if (node instanceof Tree.PackageDescriptor) {
                return ((Tree.PackageDescriptor) node).getImportPath();
            }
            else if (node instanceof Tree.NamedArgument) {
                return ((Tree.NamedArgument) node).getIdentifier();
            }
            else if (node instanceof Tree.StaticMemberOrTypeExpression) {
                return ((Tree.StaticMemberOrTypeExpression) node).getIdentifier();
            }
            else if (node instanceof Tree.ExtendedTypeExpression) {
                //TODO: whoah! this is really ugly!
                return ((Tree.SimpleType) ((Tree.ExtendedTypeExpression) node).getChildren().get(0))
                        .getIdentifier();
            }
            else if (node instanceof Tree.SimpleType) {
                return ((Tree.SimpleType) node).getIdentifier();
            }
            else if (node instanceof Tree.ImportMemberOrType) {
                return ((Tree.ImportMemberOrType) node).getIdentifier();
            }
            else {    
                return node;
            }
        }
        protected int getPosition(Node node) {
            int pos;
            if(node.getToken() != null)
                pos = cpu.getLineMap().getStartPosition(node.getToken().getLine())
                + node.getToken().getCharPositionInLine();
            else
                pos = -1;
            log.useSource(cpu.getFileObject());
            return pos;
        }
        @Override
        protected void initExpectingError(java.util.List<CompilerAnnotation> annotations) {
            // don't act on @error
        }
    }
    
    public boolean hasRun(){
        return hasRun;
    }
}

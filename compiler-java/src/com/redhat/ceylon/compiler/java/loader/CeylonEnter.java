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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.antlr.runtime.Token;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.impl.InvalidArchiveException;
import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.StatusPrinter;
import com.redhat.ceylon.compiler.java.codegen.AnnotationDeclarationVisitor;
import com.redhat.ceylon.compiler.java.codegen.AnnotationModelVisitor;
import com.redhat.ceylon.compiler.java.codegen.BoxingDeclarationVisitor;
import com.redhat.ceylon.compiler.java.codegen.BoxingVisitor;
import com.redhat.ceylon.compiler.java.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.java.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.java.codegen.CodeGenError;
import com.redhat.ceylon.compiler.java.codegen.CompilerBoxingDeclarationVisitor;
import com.redhat.ceylon.compiler.java.codegen.CompilerBoxingVisitor;
import com.redhat.ceylon.compiler.java.codegen.DeferredVisitor;
import com.redhat.ceylon.compiler.java.codegen.DefiniteAssignmentVisitor;
import com.redhat.ceylon.compiler.java.codegen.EeVisitor;
import com.redhat.ceylon.compiler.java.codegen.InterfaceVisitor;
import com.redhat.ceylon.compiler.java.codegen.JvmMissingNativeVisitor;
import com.redhat.ceylon.compiler.java.codegen.SmallDeclarationVisitor;
import com.redhat.ceylon.compiler.java.codegen.SmallVisitor;
import com.redhat.ceylon.compiler.java.codegen.TypeParameterCaptureVisitor;
import com.redhat.ceylon.compiler.java.codegen.UnsupportedVisitor;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.CeylonPhasedUnit;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler.CompilerDelegate;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UnsupportedError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.TreeUtil;
import com.redhat.ceylon.compiler.typechecker.tree.UnexpectedError;
import com.redhat.ceylon.compiler.typechecker.util.AssertionVisitor;
import com.redhat.ceylon.compiler.typechecker.util.WarningSuppressionVisitor;
import com.redhat.ceylon.javax.tools.JavaFileManager;
import com.redhat.ceylon.javax.tools.JavaFileObject.Kind;
import com.redhat.ceylon.javax.tools.StandardLocation;
import com.redhat.ceylon.langtools.source.util.TaskEvent;
import com.redhat.ceylon.langtools.source.util.TaskListener;
import com.redhat.ceylon.langtools.tools.javac.code.Kinds;
import com.redhat.ceylon.langtools.tools.javac.code.Symbol;
import com.redhat.ceylon.langtools.tools.javac.code.Symbol.ClassSymbol;
import com.redhat.ceylon.langtools.tools.javac.code.Symtab;
import com.redhat.ceylon.langtools.tools.javac.code.Type.ClassType;
import com.redhat.ceylon.langtools.tools.javac.code.TypeTag;
import com.redhat.ceylon.langtools.tools.javac.code.Types;
import com.redhat.ceylon.langtools.tools.javac.comp.Annotate;
import com.redhat.ceylon.langtools.tools.javac.comp.AttrContext;
import com.redhat.ceylon.langtools.tools.javac.comp.Check;
import com.redhat.ceylon.langtools.tools.javac.comp.Enter;
import com.redhat.ceylon.langtools.tools.javac.comp.Env;
import com.redhat.ceylon.langtools.tools.javac.comp.Todo;
import com.redhat.ceylon.langtools.tools.javac.main.Option;
import com.redhat.ceylon.langtools.tools.javac.tree.EndPosTable;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree;
import com.redhat.ceylon.langtools.tools.javac.tree.JCTree.JCCompilationUnit;
import com.redhat.ceylon.langtools.tools.javac.util.Abort;
import com.redhat.ceylon.langtools.tools.javac.util.Context;
import com.redhat.ceylon.langtools.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import com.redhat.ceylon.langtools.tools.javac.util.JCDiagnostic.SimpleDiagnosticPosition;
import com.redhat.ceylon.langtools.tools.javac.util.List;
import com.redhat.ceylon.langtools.tools.javac.util.Log;
import com.redhat.ceylon.langtools.tools.javac.util.Log.WriterKind;
import com.redhat.ceylon.langtools.tools.javac.util.Options;
import com.redhat.ceylon.langtools.tools.javac.util.Position;
import com.redhat.ceylon.langtools.tools.javac.util.SourceLanguage;
import com.redhat.ceylon.langtools.tools.javac.util.SourceLanguage.Language;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.Timer;
import com.redhat.ceylon.model.loader.model.LazyModule;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

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
    private CompilerDelegate compilerDelegate;
    private Log log;
    private AbstractModelLoader modelLoader;
    private Options options;
    private Timer timer;
    private CeyloncFileManager fileManager;
    private boolean verbose;
    private Check chk;
    private Types types;
    private Symtab symtab;
    private Todo todo;
    private boolean isBootstrap;
    private Annotate annotate;
    private Set<Module> modulesAddedToClassPath = new HashSet<Module>();
    private Set<ModuleSpec> modulesAddedToAptClassPath = new HashSet<ModuleSpec>();
    private TaskListener taskListener;
    private SourceLanguage sourceLanguage;
    private StatusPrinter sp;
    private boolean hasJavaAndCeylonSources;
    private LanguageCompiler compiler;

    
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
        compilerDelegate = LanguageCompiler.getCompilerDelegate(context);
        log = CeylonLog.instance(context);
        modelLoader = CeylonModelLoader.instance(context);
        options = Options.instance(context);
        timer = com.redhat.ceylon.compiler.java.util.Timer.instance(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
        verbose = options.get(Option.VERBOSE) != null;
        isBootstrap = options.get(Option.BOOTSTRAPCEYLON) != null;
        chk = Check.instance(context);
        types = Types.instance(context);
        symtab = Symtab.instance(context);
        todo = Todo.instance(context);
        annotate = Annotate.instance(context);
        taskListener = context.get(TaskListener.class);
        sourceLanguage = SourceLanguage.instance(context);
        compiler = (LanguageCompiler) LanguageCompiler.instance(context);

        // now superclass init
        init(context);
        
        boolean isProgressPrinted = options.get(Option.CEYLONPROGRESS) != null && StatusPrinter.canPrint();
        if(isProgressPrinted && taskListener == null){
            sp = LanguageCompiler.getStatusPrinterInstance(context);
        }else{
            sp = null;
        }
    }

    @Override
    public void main(List<JCCompilationUnit> trees) {
        // complete the javac AST with a completed ceylon model
        timer.startTask("prepareForTypeChecking");
        prepareForTypeChecking(trees);
        timer.endTask();
        List<JCCompilationUnit> javaTrees = List.nil();
        List<JCCompilationUnit> ceylonTrees = List.nil();
        // split them in two sets: java and ceylon
        for(JCCompilationUnit tree : trees){
            if(tree instanceof CeylonCompilationUnit)
                ceylonTrees = ceylonTrees.prepend(tree);
            else
                javaTrees = javaTrees.prepend(tree);
        }
        timer.startTask("Enter on Java trees");
        boolean needsModelReset = isBootstrap;
        // enter java trees first to set up their ClassSymbol objects for ceylon trees to use during type-checking
        if(!javaTrees.isEmpty()){
            setupImportedPackagesForJavaTrees(javaTrees);
            hasJavaAndCeylonSources = true;
            needsModelReset = true;
        }
        // this is false if we're in an APT round where we did not generate the trees
        if(!compiler.isAddModuleTrees()){
            setupImportedPackagesForJavaTrees(ceylonTrees);
        }
        if(isBootstrap || hasJavaAndCeylonSources){
            super.main(trees);
        }
        // now we can type-check the Ceylon code
        List<JCCompilationUnit> packageInfo = completeCeylonTrees(trees);
        trees = trees.prependList(packageInfo);
        ceylonTrees = ceylonTrees.prependList(packageInfo);
        
        if(compiler.isHadRunTwiceException()){
            needsModelReset = true;
        }
        if(needsModelReset){
            // bootstrapping the language module is a bit more complex
            resetAndRunEnterAgain(trees);
        }else{
            timer.startTask("Enter on Ceylon trees");
            // and complete their new trees
            try {
                sourceLanguage.push(Language.CEYLON);
                super.main(ceylonTrees);
            } finally {
                sourceLanguage.pop();
            }
            timer.endTask();
        }
    }

    private void setupImportedPackagesForJavaTrees(List<JCCompilationUnit> javaTrees) {
        JCTree.Visitor visitor = new ImportScanner(modelLoader);
        for(JCCompilationUnit unit : javaTrees){
            unit.accept(visitor);
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
        try {
            sourceLanguage.push(Language.CEYLON);
            super.main(trees);
        } finally {
            sourceLanguage.pop();
        }
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
        com.redhat.ceylon.langtools.tools.javac.code.Type.ClassType classType = (ClassType) classSymbol.type;
        classType.all_interfaces_field = null;
        classType.interfaces_field = null;
        classType.supertype_field = null;
        classType.typarams_field = null;
        // make sure we give erroneous types a second chance
        if(classType.getTag() == TypeTag.ERROR){
            // This is how ClassSymbol's constructor builds them, so make'em brand new
            classSymbol.type = new ClassType(com.redhat.ceylon.langtools.tools.javac.code.Type.noType, null, null);
            classSymbol.type.tsym = classSymbol;
        }
        
        // reset its members and completer
        classSymbol.members_field = null;
        classSymbol.completer = null;
        // make sure we revert the class symbol kind to a new state by removing ERR kinds
        classSymbol.kind = Kinds.TYP;
        classSymbol.flags_field = 0;
    }

    @Override
    protected com.redhat.ceylon.langtools.tools.javac.code.Type classEnter(JCTree tree, Env<AttrContext> env) {
        if(tree instanceof CeylonCompilationUnit){
            sourceLanguage.push(Language.CEYLON);
            try{
                return super.classEnter(tree, env);
            }finally{
                sourceLanguage.pop();
            }
        }else
            return super.classEnter(tree, env);
    }
    
    public void prepareForTypeChecking(List<JCCompilationUnit> trees) {
        if (hasRun)
            throw new RuntimeException("Waaaaa, running twice!!!");
        //By now le language module version should be known (as local)
        //or we should use the default one.
        int numParserErrors = log.nerrors;
        // load the standard modules
        timer.startTask("loadStandardModules");
        compilerDelegate.loadStandardModules(modelLoader);
        timer.endTask();

        // load the modules we are compiling first
        hasRun = true;
        
        // make sure we don't load the files we are compiling from their class files
        timer.startTask("setupSourceFileObjects");
        compilerDelegate.setupSourceFileObjects(trees, modelLoader);
        timer.endTask();
       
        // resolve module dependencies
        timer.startTask("verifyModuleDependencyTree");
        compilerDelegate.resolveModuleDependencies(phasedUnits);
        timer.endTask();
        
        // now load package descriptors
        timer.startTask("loadPackageDescriptors");
        compilerDelegate.loadPackageDescriptors(modelLoader);
        timer.endTask();
        
        // at this point, abort if we had any errors logged due to module descriptors
        timer.startTask("collectTreeErrors");
        collectTreeErrors(false, false);
        timer.endTask();
        // check if we abort on errors or not
        if (options.get(Option.CEYLONCONTINUE) == null) {
            // if we didn't have any errors on module descriptors, 
            // we can go on, none were logged so
            // they can't be re-logged and duplicated later on
            if(log.nerrors - numParserErrors > 0)
                throw new Abort();
        }
    }

    
    public List<JCCompilationUnit> completeCeylonTrees(List<JCCompilationUnit> trees){
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
        if(sp != null){
            sp.clearLine();
            sp.log("Generating AST");
        }
        int i=1;
        int size = countCeylonFiles(trees);
        List<JCCompilationUnit> packageInfos = List.<JCCompilationUnit>nil();
        for (JCCompilationUnit tree : trees) {
            if (tree instanceof CeylonCompilationUnit) {
                CeylonCompilationUnit ceylonTree = (CeylonCompilationUnit) tree;
                gen.setMap(ceylonTree.lineMap);
                CeylonPhasedUnit phasedUnit = (CeylonPhasedUnit)ceylonTree.phasedUnit;

                if(sp != null){
                    sp.clearLine();
                    sp.log("Generating ["+(i++)+"/"+size+"] ");
                    sp.log(phasedUnit.getPathRelativeToSrcDir());
                }

                gen.setFileObject(phasedUnit.getFileObject());
                nested.startTask("Ceylon code generation for " + phasedUnit.getUnitFile().getName());
                TaskEvent event = new TaskEvent(TaskEvent.Kind.PARSE, tree);
                if (taskListener != null) {
                    taskListener.started(event);
                }
                ceylonTree.defs = gen.transformAfterTypeChecking(ceylonTree.ceylonTree).toList();
                if (taskListener != null) {
                    taskListener.finished(event);
                }
                packageInfos = packageInfos.prependList(gen.transformPackageInfo(ceylonTree));
                nested.endTask();
                if(isVerbose("ast")){
                    log.printRawLines(WriterKind.ERROR, "Model tree for "+tree.getSourceFile());
                    log.printRawLines(WriterKind.ERROR, ceylonTree.ceylonTree.toString());
                }
                if(isVerbose("code")){
                    log.printRawLines(WriterKind.ERROR, "Java code generated for "+tree.getSourceFile());
                    log.printRawLines(WriterKind.ERROR, ceylonTree.toString());
                }
            }
        }
        if(isVerbose("code")){
            for (JCCompilationUnit packageInfo : packageInfos) {
                log.printRawLines(WriterKind.ERROR, packageInfo.toString());
            }
        }
        
        if(sp != null){
            sp.clearLine();
        }
        timer.startTask("Ceylon error generation");
        printGeneratorErrors();
        timer.endTask();
        // write some stats
        if(verbose)
            modelLoader.printStats();
        return packageInfos;
    }

    private int countCeylonFiles(List<JCCompilationUnit> trees) {
        int cnt = 0;
        for (JCCompilationUnit tree : trees) {
            if (tree instanceof CeylonCompilationUnit) {
                cnt++;
            }
        }
        return cnt;
    }
    
    private boolean isVerbose(String key) {
        return verbose || options.get(Option.VERBOSE.text + ":" + key) != null;
    }

    public void addOutputModuleToClassPath(Module module){
        RepositoryManager repositoryManager = fileManager.getOutputRepositoryManager();
        ArtifactResult artifact = null;
        try {
            ArtifactContext ctx = new ArtifactContext(null, module.getNameAsString(), module.getVersion(), ArtifactContext.CAR, ArtifactContext.JAR);
            artifact = repositoryManager.getArtifactResult(ctx);
        } catch (InvalidArchiveException e) {
            log.warning("ceylon", "Module car " + e.getPath()
                    +" obtained from repository " + e.getRepository()
                    +" has an invalid SHA1 signature:"
                    +" it will be overwritten but if the problem"
                    +" persists you need to remove it and rebuild the module, since it may be corrupted.");
        } catch (Exception e) {
            String moduleName = module.getNameAsString();
            if(!module.isDefaultModule())
                moduleName += "/" + module.getVersion();
            log.error("ceylon", "Exception occured while trying to resolve module "+moduleName);
            e.printStackTrace();
        }
        if (artifact == null || JarUtils.isValidJar(artifact.artifact())) {
            addModuleToClassPath(module, false, artifact);
        } else {
            log.warning("ceylon", "Module car " + artifact.artifact()
            +" obtained from repository " + artifact.repository()
            +" could not be read:"
            +" it will be overwritten but if the problem"
            +" persists you need to remove it and rebuild the module, since it may be corrupted.");
        }
    }
    
    public boolean isModuleInClassPath(Module module){
        return modulesAddedToClassPath.contains(module);
    }
    
    public void addModuleToClassPath(Module module, boolean errorIfMissing, ArtifactResult result) {
        if(verbose)
            log.printRawLines(WriterKind.NOTICE, "[Adding module to classpath: "+module.getNameAsString()+"/"+module.getVersion()+"]");        
        
        Collection<File> classPath = fileManager.getLocations().getLocation(StandardLocation.CLASS_PATH);
        
        File artifact = null;
        try {
            artifact = result != null ? result.artifact() : null;
        } catch (Exception e) {
            String moduleName = module.getNameAsString();
            if(!module.isDefaultModule())
                moduleName += "/" + module.getVersion();
            log.error("ceylon", "Exception occured while trying to resolve module "+moduleName);
            e.printStackTrace();
        }
        
        if(verbose){
            if(artifact != null)
                log.printRawLines(WriterKind.NOTICE, "[Found module at : "+artifact.getPath()+"]");
            else
                log.printRawLines(WriterKind.NOTICE, "[Could not find module]");
        }

        if(modulesAddedToClassPath.add(module)){
            if(artifact != null && artifact.exists()){
                ArrayList<File> newClassPath = new ArrayList<File>(classPath);
                newClassPath.add(artifact);
                try {
                    fileManager.getLocations().setLocation(StandardLocation.CLASS_PATH, newClassPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ((LazyModule)module).loadPackageList(result);
            }else if(errorIfMissing){
                log.error("ceylon", "Failed to find module "+module.getNameAsString()+"/"+module.getVersion()+" in repositories");
            }
        }else if(verbose){
            log.printRawLines(WriterKind.NOTICE, "[Module already added to classpath]");
        }
    }

    public void addModuleToAptPath(ModuleSpec module, ArtifactResult result) {
        if(verbose)
            log.printRawLines(WriterKind.NOTICE, "[Adding APT module to APT classpath: "+module+"]");        
        
        Collection<File> classPath = fileManager.getLocations().getLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH);
        
        File artifact = null;
        try {
            artifact = result != null ? result.artifact() : null;
        } catch (Exception e) {
            log.error("ceylon", "Exception occured while trying to resolve APT module "+module);
            e.printStackTrace();
        }
        
        if(verbose){
            if(artifact != null)
                log.printRawLines(WriterKind.NOTICE, "[Found APT module at : "+artifact.getPath()+"]");
            else
                log.printRawLines(WriterKind.NOTICE, "[Could not find APT module]");
        }

        if(modulesAddedToAptClassPath.add(module)){
            if(artifact != null && artifact.exists()){
                ArrayList<File> newClassPath = classPath == null
                        ? new ArrayList<File>(1)
                        : new ArrayList<File>(classPath);
                newClassPath.add(artifact);
                try {
                    fileManager.getLocations().setLocation(StandardLocation.ANNOTATION_PROCESSOR_PATH, newClassPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                log.error("ceylon", "Failed to find APT module "+module+" in repositories");
            }
        }else if(verbose){
            log.printRawLines(WriterKind.NOTICE, "[APT Module already added to APT classpath]");
        }
    }

    private void typeCheck() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();
        
        Module jdk = modelLoader.getJDKBaseModule();
        Package javaLangPackage = jdk.getPackage("java.lang");
        for (PhasedUnit pu : listOfUnits) {
        	pu.getUnit().setJavaLangPackage(javaLangPackage);
        }
        
        // Delegate to an external typechecker (e.g. the IDE build)
        compilerDelegate.typeCheck(listOfUnits);

        if(sp != null){
            sp.clearLine();
            sp.log("Preparation phase");
        }

        int size = listOfUnits.size();
        
        int i=1;
        // This phase is proper to the Java backend 
        ForcedCaptureVisitor fcv = new ForcedCaptureVisitor();
        
        for (PhasedUnit pu : listOfUnits) {
            if(sp != null)
                progressPreparation(1, i++, size, pu);
            Unit unit = pu.getUnit();
            final CompilationUnit compilationUnit = pu.getCompilationUnit();
            compilationUnit.visit(fcv);
            for (Declaration d: unit.getDeclarations()) {
                if (d instanceof TypedDeclaration 
                        && !(d instanceof Setter)
                        // skip already captured members
                        && !d.isCaptured()) {
                    compilationUnit.visit(new MethodOrValueReferenceVisitor((TypedDeclaration) d));
                }
            }
        }
        
        EeVisitor eeVisitor = gen.getEeVisitor();
        UnsupportedVisitor uv = new UnsupportedVisitor(eeVisitor);
        JvmMissingNativeVisitor mnv = new JvmMissingNativeVisitor(/*modelLoader*/);
        BoxingDeclarationVisitor boxingDeclarationVisitor = new CompilerBoxingDeclarationVisitor(gen);
        BoxingVisitor boxingVisitor = new CompilerBoxingVisitor(gen);
        SmallDeclarationVisitor smallDeclarationVisitor = new SmallDeclarationVisitor();
        SmallVisitor smallVisitor = new SmallVisitor();
        DeferredVisitor deferredVisitor = new DeferredVisitor();
        AnnotationDeclarationVisitor adv = new AnnotationDeclarationVisitor(gen);
        AnnotationModelVisitor amv = new AnnotationModelVisitor(gen);
        DefiniteAssignmentVisitor dav = new DefiniteAssignmentVisitor();
        TypeParameterCaptureVisitor tpCaptureVisitor = new TypeParameterCaptureVisitor();
        InterfaceVisitor localInterfaceVisitor = new InterfaceVisitor();
        
        // Extra phases for the compiler
        
        // boxing visitor depends on boxing decl
        i=1;
        for (PhasedUnit pu : listOfUnits) {
            if(sp != null)
                progressPreparation(2, i++, size, pu);
            pu.getCompilationUnit().visit(eeVisitor);
            pu.getCompilationUnit().visit(uv);
            pu.getCompilationUnit().visit(adv);
        }
        i=1;
        for (PhasedUnit pu : listOfUnits) {
            if(sp != null)
                progressPreparation(3, i++, size, pu);
            pu.getCompilationUnit().visit(boxingDeclarationVisitor);
            pu.getCompilationUnit().visit(smallDeclarationVisitor);
            pu.getCompilationUnit().visit(amv);
            
        }
        i=1;
        // the others can run at the same time
        for (PhasedUnit pu : listOfUnits) {
            if(sp != null)
                progressPreparation(4, i++, size, pu);
            CompilationUnit compilationUnit = pu.getCompilationUnit();
            compilationUnit.visit(mnv);
            compilationUnit.visit(boxingVisitor);
            compilationUnit.visit(smallVisitor);
            compilationUnit.visit(deferredVisitor);
            compilationUnit.visit(dav);
            compilationUnit.visit(tpCaptureVisitor);
            compilationUnit.visit(localInterfaceVisitor);
        }
        
        i=1;
        for (PhasedUnit pu : listOfUnits) {
            if(sp != null)
                progressPreparation(5, i++, size, pu);
            CompilationUnit compilationUnit = pu.getCompilationUnit();
            compilationUnit.visit(new WarningSuppressionVisitor<Warning>(Warning.class, pu.getSuppressedWarnings()));
        }
        
        collectTreeErrors(true, true);
    }

    private void progressPreparation(int phase, int i, int size, PhasedUnit pu) {
        sp.clearLine();
        sp.log("Preparing "+phase+"/5 ["+(i++)+"/"+size+"] ");
        sp.log(pu.getPathRelativeToSrcDir());
    }

    private void collectTreeErrors(boolean runAssertions, final boolean reportWarnings) {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();

        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(new JavacAssertionVisitor((CeylonPhasedUnit) pu, runAssertions){
                @Override
                protected void out(UnexpectedError err) {
                    setSource();
                    logError(getPosition(err.getTreeNode()), "ceylon", err.getMessage());
                }
                @Override
                protected void out(AnalysisError err) {
                    if(err.isWarning()){
                        if (reportWarnings) {
                            setSource();
                            Node node = getIdentifyingNode(err.getTreeNode());
                            logWarning(getPosition(node), "ceylon", err.getMessage());
                        }
                    }else{
                        setSource();
                        Node node = getIdentifyingNode(err.getTreeNode());
                        logError(getPosition(node), "ceylon", err.getMessage());
                    }
                }
                @Override
                protected void out(Node that, LexError err) {
                    setSource();
                    DiagnosticPosition pos = getPosition((err.getLine()), err.getCharacterInLine());
                    logError(pos, "ceylon", err.getMessage());
                }
                @Override
                protected void out(Node that, ParseError err) {
                    setSource();
                    DiagnosticPosition pos = getPosition((err.getLine()), err.getCharacterInLine());
                    logError(pos, "ceylon", err.getMessage());
                }
                @Override
                protected void out(UnsupportedError err) {
                    setSource();
                    Node node = getIdentifyingNode(err.getTreeNode());
                    logError(getPosition(node), "ceylon", err.getMessage());
                }
                @Override
                protected void out(UsageWarning warning) {
                    if (reportWarnings && !warning.isSuppressed()) {
                        setSource();
                        Node node = getIdentifyingNode(warning.getTreeNode());
                        logWarning(getPosition(node), "ceylon", warning.getMessage());
                    }
                }
                @Override
                protected void out(Node that, String message) {
                    setSource();
                    logError(getPosition(that), "ceylon", message);
                }
            });
        }
    }

    /**
     * Visits the nodes of each unit calling 
     * {@link #logError(int, String, String)} for each {@link CodeGenError}
     */
    private void printGeneratorErrors() {
        final java.util.List<PhasedUnit> listOfUnits = phasedUnits.getPhasedUnits();

        for (PhasedUnit pu : listOfUnits) {
            pu.getCompilationUnit().visit(new JavacAssertionVisitor((CeylonPhasedUnit) pu, false){
                @Override
                protected void out(UnexpectedError err) {
                    if(err instanceof CodeGenError){
                        setSource();
                        CodeGenError error = ((CodeGenError)err);
                        String location = locationInfo(error);
                        logError(getPosition(err.getTreeNode()), 
                                "ceylon.codegen.exception", 
                                "compiler bug: "+error.getMessage()+" at " + location);
                    }
                }
                private String locationInfo(CodeGenError error) {
                    if (error.getCause() != null
                            && error.getCause().getStackTrace() != null
                            && error.getCause().getStackTrace().length > 0) {
                        return error.getCause().getStackTrace()[0].toString();
                    } else {
                        return "unknown";
                    }
                }
                // Ignore those
                @Override
                protected void out(AnalysisError err) {}
                @Override
                protected void out(UnsupportedError err) {}
                @Override
                protected void out(UsageWarning warn) {}
                @Override
                protected void out(Node that, ParseError err) {}
                @Override
                protected void out(Node that, LexError err) {}
                @Override
                protected void out(Node that, String message) {}
            });
        }
    }

    protected void logError(int position, String key, String message) {
        logError(toDiagnosticPosition(position), key, message);
    }

    protected void logError(DiagnosticPosition position, String key, String message) {
        boolean prev = log.multipleErrors;
        // we want multiple errors for Ceylon
        log.multipleErrors = true;
        try{
            log.error(position, key, message);
        }finally{
            log.multipleErrors = prev;
        }
    }

    protected void logWarning(int position, String key, String message) {
        logWarning(toDiagnosticPosition(position), key, message);
    }

    protected void logWarning(DiagnosticPosition position, String key, String message) {
        boolean prev = log.multipleErrors;
        // we want multiple errors for Ceylon
        log.multipleErrors = true;
        try{
            log.warning(position, key, message);
        }finally{
            log.multipleErrors = prev;
        }
    }
    
    protected void logStackTrace(String key, Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        String tracktrace = sw.toString();
        boolean prev = log.multipleErrors;
        // we want multiple errors for Ceylon
        log.multipleErrors = true;
        try{
            log.printLines(WriterKind.ERROR, key, tracktrace);
        }finally{
            log.multipleErrors = prev;
        }
    }

    protected DiagnosticPosition toDiagnosticPosition(int pos) {
        return (pos == Position.NOPOS ? null : new SimpleDiagnosticPosition(pos));
    }

    private class JavacAssertionVisitor extends AssertionVisitor {
        private CeylonPhasedUnit cpu;
        protected final boolean runAssertions;
        JavacAssertionVisitor(CeylonPhasedUnit cpu, boolean runAssertions){
            this.cpu = cpu;
            this.runAssertions = runAssertions;
            this.includeUsageWarnings(true);
        }
        
        @Override
        protected void checkType(Statement that, Type type, Node typedNode) {
            if(runAssertions)
                super.checkType(that, type, typedNode);
        }
        
        @Override
        protected boolean includeError(Message err, int phase) {
            return err.getBackend() == null ||
                    ModelUtil.isForBackend(err.getBackend().asSet(), Backend.Java);
        }

        protected Node getIdentifyingNode(Node node) {
            return TreeUtil.getIdentifyingNode(node);
        }
        
        protected DiagnosticPosition getPosition(int line, int characterInLine) {
            int pos = getPositionAsInt(line, characterInLine);
            return toDiagnosticPosition(pos);
        }
        
        protected int getPositionAsInt(int line, int characterInLine) {
            if (line<1) {
                return -1;
            }
            try {
                return cpu.getLineMap().getPosition(line,characterInLine);
            }
            catch (ArrayIndexOutOfBoundsException aie) {
                if (line<2) {
                    return -1;
                }
                return cpu.getLineMap().getPosition(line-1, 0);
            }
        }
        
        protected DiagnosticPosition getPosition(Node node) {
            if(node != null) {
                Token token = node.getToken();
                if (token != null) {
                    final int startOffset = node.getStartIndex();
                    final int endOffset = node.getStopIndex();
                    final int startLine = token.getLine();
                    final int startCol = token.getCharPositionInLine();
                    
                    if (startOffset < 0 || endOffset < 0) {
                        return getPosition(startLine, 
                                startCol);
                    } else {
                        return new DiagnosticPosition() {
                            @Override
                            public JCTree getTree() {
                                return null;
                            }
                            
                            @Override
                            public int getStartPosition() {
                                return startOffset;
                            }
                            
                            @Override
                            public int getPreferredPosition() {
                                return startOffset;
                            }
                            
                            @Override
                            public int getEndPosition(EndPosTable endPosTable) {
                                return endOffset;
                            }
                        };
                    }
                }
            }
            return null;
        }
        
        protected void setSource() {
            log.useSource(cpu.getFileObject());
        }
        
        @Override
        protected void initExpectingError(java.util.List<CompilerAnnotation> annotations) {
            if (runAssertions) {
                super.initExpectingError(annotations);
            }
        }
    }
    
    public boolean hasRun(){
        return hasRun;
    }

    public boolean isCompilingJavaAndCeylonSources() {
        return hasJavaAndCeylonSources;
    }
}

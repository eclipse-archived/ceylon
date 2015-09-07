/*
 * Copyright (c) 1999, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 */

package com.redhat.ceylon.compiler.java.tools;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getNativeBackend;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isForBackend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import com.redhat.ceylon.cmr.util.JarUtils;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.StatusPrinter;
import com.redhat.ceylon.compiler.java.codegen.CeylonClassWriter;
import com.redhat.ceylon.compiler.java.codegen.CeylonCompilationUnit;
import com.redhat.ceylon.compiler.java.codegen.CeylonFileObject;
import com.redhat.ceylon.compiler.java.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.java.loader.CeylonEnter;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.parser.RecognitionError;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportModule;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;
import com.redhat.ceylon.compiler.typechecker.util.NewlineFixingStringStream;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.comp.AttrContext;
import com.sun.tools.javac.comp.Env;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.TreeInfo;
import com.sun.tools.javac.util.Abort;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Convert;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
import com.sun.tools.javac.util.Pair;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;
import com.sun.tools.javac.util.SourceLanguage;
import com.sun.tools.javac.util.SourceLanguage.Language;

public class LanguageCompiler extends JavaCompiler {

    /** The context key for the phasedUnits. */
    protected static final Context.Key<PhasedUnits> phasedUnitsKey = new Context.Key<PhasedUnits>();
    public static final Context.Key<CompilerDelegate> compilerDelegateKey = new Context.Key<CompilerDelegate>();

    /** The context key for the ceylon context. */
    public static final Context.Key<com.redhat.ceylon.compiler.typechecker.context.Context> ceylonContextKey = new Context.Key<com.redhat.ceylon.compiler.typechecker.context.Context>();

    /** The context key for the StatusPrinter. */
    public static final Context.Key<StatusPrinter> statusPrinterKey = new Context.Key<StatusPrinter>();

    private final CeylonTransformer gen;
    private final PhasedUnits phasedUnits;
    private final CompilerDelegate compilerDelegate;
    private final com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private final VFS vfs;

	private AbstractModelLoader modelLoader;

    private CeylonEnter ceylonEnter;

    private Options options;
    
    private Timer timer;
    private boolean isBootstrap;
    private boolean addedDefaultModuleToClassPath;
    private boolean treatLikelyBugsAsErrors = false;
    private Set<Module> modulesLoadedFromSource = new HashSet<Module>();
    private List<JavaFileObject> resourceFileObjects;
    private Map<String,CeylonFileObject> moduleNamesToFileObjects = new HashMap<String,CeylonFileObject>();
    private SourceLanguage sourceLanguage;

    /** Get the PhasedUnits instance for this context. */
    public static PhasedUnits getPhasedUnitsInstance(final Context context) {
        PhasedUnits phasedUnits = context.get(phasedUnitsKey);
        if (phasedUnits == null) {
            com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = getCeylonContextInstance(context);
            phasedUnits = new PhasedUnits(ceylonContext, new ModuleManagerFactory(){
                @Override
                public ModuleManager createModuleManager(com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext) {
                    CompilerDelegate phasedUnitsManager = getCompilerDelegate(context);
                    return phasedUnitsManager.getModuleManager();
                }

                @Override
                public ModuleSourceMapper createModuleManagerUtil(com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext, ModuleManager moduleManager) {
                    CompilerDelegate phasedUnitsManager = getCompilerDelegate(context);
                    return phasedUnitsManager.getModuleSourceMapper();
                }
            });
            context.put(phasedUnitsKey, phasedUnits);
        }
        return phasedUnits;
    }

    public static CompilerDelegate getCompilerDelegate(final Context context) {
        CompilerDelegate compilerDelegate = context.get(compilerDelegateKey);
        if (compilerDelegate == null) {
            compilerDelegate = new CeyloncCompilerDelegate(context);
            context.put(compilerDelegateKey, compilerDelegate);
        }
        return compilerDelegate;
    }
    
    /** Get the Ceylon context instance for this context. */
    public static com.redhat.ceylon.compiler.typechecker.context.Context getCeylonContextInstance(Context context) {
        com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext = context.get(ceylonContextKey);
        if (ceylonContext == null) {
            CeyloncFileManager fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
            VFS vfs = new VFS();
            ceylonContext = new com.redhat.ceylon.compiler.typechecker.context.Context(fileManager.getRepositoryManager(), vfs);
            context.put(ceylonContextKey, ceylonContext);
        }
        return ceylonContext;
    }

    /** Get the StatusPrinter instance for this context. */
    public static StatusPrinter getStatusPrinterInstance(Context context) {
        StatusPrinter statusPrinter = context.get(statusPrinterKey);
        if (statusPrinter == null) {
            statusPrinter = new StatusPrinter();
            context.put(statusPrinterKey, statusPrinter);
        }
        return statusPrinter;
    }

    /** Get the JavaCompiler instance for this context. */
    public static JavaCompiler instance(Context context) {
        Options options = Options.instance(context);
        options.put("-Xprefer", "source");
        // make sure it's registered
        CeylonLog.instance(context);
        CeylonEnter.instance(context);
        CeylonClassWriter.instance(context);
        JavaCompiler instance = context.get(compilerKey);
        if (instance == null)
            instance = new LanguageCompiler(context);
        return instance;
    }

    public LanguageCompiler(Context context) {
        super(context);
        ceylonContext = getCeylonContextInstance(context);
        vfs = ceylonContext.getVfs();
        compilerDelegate = getCompilerDelegate(context);
        phasedUnits = getPhasedUnitsInstance(context);
        try {
            gen = CeylonTransformer.getInstance(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        modelLoader = CeylonModelLoader.instance(context);
        ceylonEnter = CeylonEnter.instance(context);
        options = Options.instance(context);
        isBootstrap = options.get(OptionName.BOOTSTRAPCEYLON) != null;
        timer = Timer.instance(context);
        sourceLanguage = SourceLanguage.instance(context);
        boolean isProgressPrinted = options.get(OptionName.CEYLONPROGRESS) != null && StatusPrinter.canPrint();
        if(isProgressPrinted && taskListener == null){
            taskListener = new StatusPrinterTaskListener(getStatusPrinterInstance(context));
        }
    }

    @Override
    public void compile(List<JavaFileObject> fileObjects,
            List<String> classnames,
            Iterable<? extends Processor> processors)
    {
        // Now we first split the files into sources/modules and resources
        List<JavaFileObject> sourceFiles = List.nil();
        List<JavaFileObject> resourceFiles = List.nil();
        for (JavaFileObject fo : fileObjects) {
            if (isResource(fo)) {
                resourceFiles = resourceFiles.append(fo);
            } else {
                sourceFiles = sourceFiles.append(fo);
            }
        }
        this.resourceFileObjects = resourceFiles;
        // Add any module files for the resources (if needed)
        sourceFiles = addModuleDescriptors(sourceFiles, resourceFiles);
        // And then continue to the compilation of the source files
        super.compile(sourceFiles, classnames, processors);
    }

    private boolean isResource(JavaFileObject fo) {
        // make sure we get a proper normalized abslute path
        String fileName = FileUtil.absoluteFile(new File(fo.toUri().getPath())).getPath();
        // now see if it's in any of the resource paths
        JavacFileManager dfm = (JavacFileManager) fileManager;
        for (File dir : dfm.getLocation(CeylonLocation.RESOURCE_PATH)) {
            String prefix = FileUtil.absoluteFile(dir).getPath();
            if (fileName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    
    // This is a bit of a hack, but if we got passed a list of resources
    // without any accompaning source files we'll not be able to determine
    // the module to which the resource files belong. So to try to fix that
    // we see if a module file exists in the source folders and add it to
    // the list of source files
    private List<JavaFileObject> addModuleDescriptors(List<JavaFileObject> sourceFiles, List<JavaFileObject> resourceFiles) {
        List<JavaFileObject> result = sourceFiles;
        JavacFileManager dfm = (JavacFileManager) fileManager;
        for (JavaFileObject fo : resourceFiles) {
            String resName = JarUtils.toPlatformIndependentPath(dfm.getLocation(CeylonLocation.RESOURCE_PATH), fo.getName());
            JavaFileObject moduleFile = findModuleDescriptorForFile(new File(resName));
            if (moduleFile != null && !result.contains(moduleFile)) {
                result = result.append(moduleFile);
            }
        }
        return result;
    }

    private JavaFileObject findModuleDescriptorForFile(File file) {
        JavacFileManager dfm = (JavacFileManager) fileManager;
        File dir = file.getParentFile();
        while (dir != null) {
            try {
                String name = dir.getPath() + "/module";
                JavaFileObject fo = dfm.getJavaFileForInput(StandardLocation.SOURCE_PATH, name, Kind.SOURCE);
                if (fo != null) {
                    return fo;
                }
            } catch (IOException e) {
                // Ignore
            }
            dir = dir.getParentFile();
        }
        return null;
    }

    @Override
    public void close(boolean disposeNames) {
        if (resourceFileObjects != null) {
            addResources();
            resourceFileObjects = null;
        }
        super.close(disposeNames);
    }

    private void addResources() throws Abort {
       HashSet<String> written = new HashSet<String>();
        try {
            for (JavaFileObject fo : resourceFileObjects) {
                CeyloncFileManager dfm = (CeyloncFileManager) fileManager;
                String jarFileName = JarUtils.toPlatformIndependentPath(dfm.getLocation(CeylonLocation.RESOURCE_PATH), fo.getName());
                if (!written.contains(jarFileName)) {
                    dfm.setModule(modelLoader.findModuleForFile(new File(jarFileName)));
                    FileObject outFile = dfm.getFileForOutput(StandardLocation.CLASS_OUTPUT, "", jarFileName, null);
                    OutputStream out = outFile.openOutputStream();
                    try {
                        InputStream in = new FileInputStream(new File(fo.getName()));
                        try {
                            JarUtils.copy(in, out);
                        } finally {
                            in.close();
                        }
                    } finally {
                        out.close();
                    }
                    written.add(jarFileName);
                }
            }
        } catch (IOException ex) {
            throw new Abort(ex);
        }
    }
    
    /**
     * Parse contents of file.
     * @param filename The name of the file to be parsed.
     */
    public JCTree.JCCompilationUnit parse(JavaFileObject filename) {
        JavaFileObject prev = log.useSource(filename);
        try {
            JCTree.JCCompilationUnit t;
            if (filename.getName().endsWith(".java")) {

                t = parse(filename, readSource(filename));
            } else {
                t = ceylonParse(filename, readSource(filename));
            }
            if (t.endPositions != null)
                log.setEndPosTable(filename, t.endPositions);
            return t;
        } finally {
            log.useSource(prev);
        }
    }

    protected JCCompilationUnit parse(JavaFileObject filename, CharSequence readSource) {
        // FIXME
        if (filename instanceof CeylonFileObject)
            return ceylonParse(filename, readSource);
        else
            return super.parse(filename, readSource);
    }


    public static interface CompilerDelegate {
        
        ModuleManager getModuleManager();
        ModuleSourceMapper getModuleSourceMapper();
        PhasedUnit getExternalSourcePhasedUnit(VirtualFile srcDir, VirtualFile file);
        void typeCheck(java.util.List<PhasedUnit> listOfUnits);
        void visitModules(PhasedUnits phasedUnits);
        void loadStandardModules(AbstractModelLoader modelLoader);
        void setupSourceFileObjects(List<JCCompilationUnit> trees, AbstractModelLoader modelLoader);
        void resolveModuleDependencies(PhasedUnits phasedUnits);
        void loadPackageDescriptors(AbstractModelLoader modelLoader);
    }
    
    private static class RunTwiceException extends RuntimeException {

        public RunTwiceException(String string) {
            super(string);
        }
        
    }
    
    private JCCompilationUnit ceylonParse(JavaFileObject filename, CharSequence readSource) {
        if(ceylonEnter.hasRun())
            throw new RunTwiceException("Trying to load new source file after CeylonEnter has been called: "+filename);
        try {
            ModuleManager moduleManager = phasedUnits.getModuleManager();
            ModuleSourceMapper moduleSourceMapper = phasedUnits.getModuleSourceMapper();
            File sourceFile = new File(filename.getName());
            // FIXME: temporary solution
            VirtualFile file = vfs.getFromFile(sourceFile);
            VirtualFile srcDir = vfs.getFromFile(getSrcDir(sourceFile));
            
            String source = readSource.toString();
            char[] chars = source.toCharArray();
            LineMap map = Position.makeLineMap(chars, chars.length, false);
            
            PhasedUnit phasedUnit = null;
            
            PhasedUnit externalPhasedUnit = compilerDelegate.getExternalSourcePhasedUnit(srcDir, file);
            
            String suppressWarnings = options.get(OptionName.CEYLONSUPPRESSWARNINGS);
            final EnumSet<Warning> suppressedWarnings;
            if (suppressWarnings != null) {
                if (suppressWarnings.trim().isEmpty()) {
                    suppressedWarnings = EnumSet.allOf(Warning.class);
                } else {
                    suppressedWarnings = EnumSet.noneOf(Warning.class);
                    for (String name : suppressWarnings.trim().split(" *, *")) {
                        suppressedWarnings.add(Warning.valueOf(name));
                    }
                }
            } else {
                suppressedWarnings = EnumSet.noneOf(Warning.class);
            }
            
            if (externalPhasedUnit != null) {
                phasedUnit = new CeylonPhasedUnit(externalPhasedUnit, filename, map);
                phasedUnit.setSuppressedWarnings(suppressedWarnings);
                phasedUnits.addPhasedUnit(externalPhasedUnit.getUnitFile(), phasedUnit);
                gen.setMap(map);
                
                String pkgName = phasedUnit.getPackage().getQualifiedNameString();
                if ("".equals(pkgName)) {
                    pkgName = null;
                }
                return gen.makeJCCompilationUnitPlaceholder(phasedUnit.getCompilationUnit(), filename, pkgName, phasedUnit);
            }
            if (phasedUnit == null) {
                ANTLRStringStream input = new NewlineFixingStringStream(source);
                CeylonLexer lexer = new CeylonLexer(input);

                CommonTokenStream tokens = new CommonTokenStream(lexer);

                CeylonParser parser = new CeylonParser(tokens);
                CompilationUnit cu = parser.compilationUnit();

                java.util.List<LexError> lexerErrors = lexer.getErrors();
                for (LexError le : lexerErrors) {
                    printError(le, le.getMessage(), "ceylon.lexer", map);
                }

                java.util.List<ParseError> parserErrors = parser.getErrors();
                for (ParseError pe : parserErrors) {
                    printError(pe, pe.getMessage(), "ceylon.parser", map);
                }

                // if we continue and it's not a descriptor, we don't care about errors
                if ((options.get(OptionName.CEYLONCONTINUE) != null
                        && !ModuleManager.MODULE_FILE.equals(sourceFile.getName())
                        && !ModuleManager.PACKAGE_FILE.equals(sourceFile.getName()))
                        // otherwise we care about errors
                        || (lexerErrors.size() == 0 
                            && parserErrors.size() == 0)) {
                    // FIXME: this is bad in many ways
                    String pkgName = getPackage(filename);
                    // make a Package with no module yet, we will resolve them later
                    /*
                     * Stef: see javadoc for findOrCreateModulelessPackage() for why this is here.
                     */
                    com.redhat.ceylon.model.typechecker.model.Package p = modelLoader.findOrCreateModulelessPackage(pkgName == null ? "" : pkgName);
                    phasedUnit = new CeylonPhasedUnit(file, srcDir, cu, p, moduleManager, moduleSourceMapper, ceylonContext, filename, map);
                    phasedUnit.setSuppressedWarnings(suppressedWarnings);
                    phasedUnits.addPhasedUnit(file, phasedUnit);
                    gen.setMap(map);

                    return gen.makeJCCompilationUnitPlaceholder(cu, filename, pkgName, phasedUnit);
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JCCompilationUnit result = make.TopLevel(List.<JCAnnotation> nil(), null, List.<JCTree> of(make.Erroneous()));
        result.sourcefile = filename;
        return result;
    }

    @Override
    public List<JCCompilationUnit> parseFiles(Iterable<JavaFileObject> fileObjects) {
        timer.startTask("parse");
        /*
         * Stef: see javadoc for fixDefaultPackage() for why this is here.
         */
        modelLoader.fixDefaultPackage();
        List<JCCompilationUnit> trees = super.parseFiles(fileObjects);
        timer.startTask("loadCompiledModules");
        LinkedList<JCCompilationUnit> moduleTrees = new LinkedList<JCCompilationUnit>();
        // now load modules and associate their moduleless packages with the corresponding modules
        trees = loadCompiledModules(trees, moduleTrees);
        /*
         * Stef: see javadoc for cacheModulelessPackages() for why this is here.
         */
        modelLoader.cacheModulelessPackages();
        timer.endTask();
        return trees;
    }

    private List<JCCompilationUnit> loadCompiledModules(List<JCCompilationUnit> trees, LinkedList<JCCompilationUnit> moduleTrees) {
        checkInvalidNativeModules();
        compilerDelegate.visitModules(phasedUnits);
        Modules modules = ceylonContext.getModules();
        // now make sure the phase units have their modules and packages set correctly
        for (PhasedUnit pu : phasedUnits.getPhasedUnits()) {
            Package pkg = pu.getPackage();
            loadModuleFromSource(pkg, modules, moduleTrees, trees);
        }
        checkInvalidNativeImports();
        // also make sure we have packages and modules set up for every Java file we compile
        for(JCCompilationUnit cu : trees){
            // skip Ceylon CUs
            if(cu instanceof CeylonCompilationUnit)
                continue;
            String packageName = ""; 
            if(cu.pid != null)
                packageName = TreeInfo.fullName(cu.pid).toString();
            /*
             * Stef: see javadoc for findOrCreateModulelessPackage() for why this is here.
             */
            Package pkg = modelLoader.findOrCreateModulelessPackage(packageName);
            loadModuleFromSource(pkg, modules, moduleTrees, trees);
        }
        for(PhasedUnit phasedUnit : phasedUnits.getPhasedUnits()){
            for(Tree.ModuleDescriptor modDescr : phasedUnit.getCompilationUnit().getModuleDescriptors()){
                String name = phasedUnit.getPackage().getNameAsString();
                CeylonPhasedUnit cpu = (CeylonPhasedUnit) phasedUnit;
                CeylonFileObject cfo = (CeylonFileObject) cpu.getFileObject();
                moduleNamesToFileObjects .put(name, cfo);
            }
        }
        for (JCCompilationUnit moduleTree : moduleTrees) {
            trees = trees.append(moduleTree);
        }
        return trees;
    }

    private void checkInvalidNativeModules() {
        for (PhasedUnit pu : phasedUnits.getPhasedUnits()) {
            ModuleDescriptor md = pu.findModuleDescriptor();
            if (md != null) {
                String be = getNativeBackend(md.getAnnotationList(), md.getUnit());
                if (be != null) {
                    if (be.isEmpty()) {
                        md.addError("missing backend argument for native annotation on module: " + formatPath(md.getImportPath().getIdentifiers()), Backend.Java);
                    } else if (!isForBackend(be, Backend.Java)) {
                        md.addError("module not meant for this backend: " + formatPath(md.getImportPath().getIdentifiers()), Backend.Java);
                    }
                }
            }
        }
    }
    
    private void checkInvalidNativeImports() {
        for (PhasedUnit pu : phasedUnits.getPhasedUnits()) {
            ModuleDescriptor md = pu.findModuleDescriptor();
            if (md != null) {
                for (ImportModule im : md.getImportModuleList().getImportModules()) {
                    String be = getNativeBackend(im.getAnnotationList(), im.getUnit());
                    if (im.getImportPath() != null) {
                        Module m = (Module)im.getImportPath().getModel();
                        if (be != null && m.isNative() && !be.equals(m.getNativeBackend())) {
                            im.addError("native backend name conflicts with imported module: '\"" + 
                                    be + "\"' is not '\"" + m.getNativeBackend() + "\"'", Backend.Java);
                        }
                    }
                }
            }
        }
    }
    
    private void loadModuleFromSource(Package pkg, Modules modules, LinkedList<JCCompilationUnit> moduleTrees, List<JCCompilationUnit> parsedTrees) {
        // skip it if we already resolved the package
        if(pkg.getModule() != null){
            // make sure the default module is always added to the classpath, it will be the only one to have a module
            if(!addedDefaultModuleToClassPath && pkg.getModule().isDefault()){
                addedDefaultModuleToClassPath = true;
                ceylonEnter.addOutputModuleToClassPath(pkg.getModule());
            }
            return;
        }
        String pkgName = pkg.getQualifiedNameString();
        Module module = null;
        // do we have a module for this package?
        // FIXME: is this true? what if we have a module.ceylon at toplevel?
        if(pkgName.isEmpty())
            module = modules.getDefaultModule();
        else{
            for(Module m : modulesLoadedFromSource){
                if(JvmBackendUtil.isSubPackage(m.getNameAsString(), pkgName)){
                    module = m;
                    break;
                }
            }
            if(module == null){
                module = loadModuleFromSource(pkgName, moduleTrees, parsedTrees);
            }
            else if (! module.isAvailable()) {
                loadModuleFromSource(pkgName, moduleTrees, parsedTrees);
            }

            if(module == null){
                // no declaration for it, must be the default module, unless we're bootstrapping the language module,
                // because we have some com.redhat.ceylon packages that must go in the language module
                if(isBootstrap)
                    module = modules.getLanguageModule();
                else
                    module = modules.getDefaultModule();
            }
        }
        // bind module and package together
        pkg.setModule(module);
        if (!module.getPackages().contains(pkg)) {
            module.getPackages().add(pkg);
        }
        // automatically add this module's jar to the classpath if it exists
        ceylonEnter.addOutputModuleToClassPath(module);
    }

    private Module loadModuleFromSource(String pkgName, LinkedList<JCCompilationUnit> moduleTrees, List<JCCompilationUnit> parsedTrees) {
        if(pkgName.isEmpty())
            return null;
        String moduleClassName = pkgName + ".module";
        JavaFileObject fileObject;
        try {
            if(options.get(OptionName.VERBOSE) != null){
                Log.printLines(log.noticeWriter, "[Trying to load module "+moduleClassName+"]");
            }
            fileObject = fileManager.getJavaFileForInput(StandardLocation.SOURCE_PATH, moduleClassName, Kind.SOURCE);
            if(options.get(OptionName.VERBOSE) != null){
                Log.printLines(log.noticeWriter, "[Got file object: "+fileObject+"]");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return loadModuleFromSource(getParentPackage(pkgName), moduleTrees, parsedTrees);
        }
        if(fileObject != null){
            // first make sure we're not already compiling it: this can happen if we have several versions of the
            // same module already loaded: we will get one which isn't the one we compile, but that's not the one
            // we really want to compile.
            for(JCCompilationUnit parsedTree : parsedTrees){
                if(parsedTree.sourcefile.equals(fileObject)
                        && parsedTree instanceof CeylonCompilationUnit){
                    // same file! we already parsed it, let's return this one's module
                    PhasedUnit phasedUnit = ((CeylonCompilationUnit)parsedTree).phasedUnit;
                    // the module visitor does load the module but does not set the unit's package module
                    if(phasedUnit.getPackage().getModule() == null){
                        // so find the module it created
                        for(Module mod : ceylonContext.getModules().getListOfModules()){
                            // we recognise it with the unit
                            if(mod.getUnit() == phasedUnit.getUnit()){
                                // set the package's module
                                Package pkg = phasedUnit.getPackage();
                                pkg.setModule(mod);
                                mod.getPackages().add(pkg);
                                modulesLoadedFromSource.add(mod);
                                break;
                            }
                        }
                    }
                    // now return it
                    return phasedUnit.getPackage().getModule();
                }
            }
            JCCompilationUnit javaCompilationUnit = parse(fileObject);
            Module module;
            if(javaCompilationUnit instanceof CeylonCompilationUnit){
                CeylonCompilationUnit ceylonCompilationUnit = (CeylonCompilationUnit) javaCompilationUnit;
                moduleTrees.add(ceylonCompilationUnit);
                // parse the module info from there
                module = ceylonCompilationUnit.phasedUnit.visitSrcModulePhase();
                ceylonCompilationUnit.phasedUnit.visitRemainingModulePhase();
                // now set the module
                if(module != null){
                    ceylonCompilationUnit.phasedUnit.getPackage().setModule(module);
                }
            }else{
                // there was a syntax error in the module descriptor, make a pretend module so that we can
                // correctly mark all declarations as part of that module, but we won't generate any code
                // for it
                ModuleManager moduleManager = phasedUnits.getModuleManager();
                module = moduleManager.getOrCreateModule(Arrays.asList(pkgName.split("\\.")), "bogus");
            }
            // now remember it
            if(module != null){
                modulesLoadedFromSource.add(module);
                return module;
            }
        }
        return loadModuleFromSource(getParentPackage(pkgName), moduleTrees, parsedTrees);
    }

    private String getParentPackage(String pkgName) {
        int lastDot = pkgName.lastIndexOf(".");
        if(lastDot == -1)
            return "";
        return pkgName.substring(0, lastDot);
    }

    // FIXME: this function is terrible, possibly refactor it with getPackage?
    private File getSrcDir(File sourceFile) throws IOException {
        Iterable<? extends File> prefixes = ((JavacFileManager)fileManager).getLocation(StandardLocation.SOURCE_PATH);
        File srcDirFile = FileUtil.selectPath(prefixes, sourceFile.getPath());
        if (srcDirFile != null) {
            return srcDirFile;
        } else {
            // This error should have been caught by the tool chain
            throw new RuntimeException(sourceFile.getPath() + " is not in the current source path");
        }
    }

    private String getPackage(JavaFileObject file) throws IOException{
        Iterable<? extends File> prefixes = ((JavacFileManager)fileManager).getLocation(StandardLocation.SOURCE_PATH);

    	// Figure out the package name by stripping the "-src" prefix and
    	// extracting
    	// the package part of the fullname.
        
        String filePath = file.toUri().getPath();
        // go absolute
        filePath = new File(filePath).getCanonicalPath();
        
        int srcDirLength = 0;
        for (File prefixFile : prefixes) {
            String prefix = prefixFile.getCanonicalPath();
            if (filePath.startsWith(prefix) && prefix.length() > srcDirLength) {
                srcDirLength = prefix.length();
            }
    	}
        
        if (srcDirLength > 0) {
            String fullname = filePath.substring(srcDirLength);
            assert fullname.endsWith(".ceylon");
            fullname = fullname.substring(0, fullname.length() - ".ceylon".length());
            fullname = fullname.replace(File.separator, ".");
            if(fullname.startsWith("."))
                fullname = fullname.substring(1);
            String packageName = Convert.packagePart(fullname);
            if (!packageName.equals(""))
                return packageName;
        }
    	return null;
    }
    
    private void printError(RecognitionError le, String message, String key, LineMap map) {
        int pos = -1;
        if (le.getLine() > 0) {
            /* does not seem to be a way to determine the max line number so we do an ugly try-catch */
            try {
                pos = map.getStartPosition(le.getLine()) + le.getCharacterInLine();
            } catch (Exception e) { }
        }
        log.error(pos, key, message);
    }

    public Env<AttrContext> attribute(Env<AttrContext> env) {
        if (env.toplevel.sourcefile instanceof CeylonFileObject || isBootstrap) {
            try {
                sourceLanguage.push(Language.CEYLON);
                return super.attribute(env);
            } finally {
                sourceLanguage.pop();
            }
        }
        return super.attribute(env);
    }

    @Override
    protected JavaFileObject genCode(Env<AttrContext> env, JCClassDecl cdef) throws IOException {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                sourceLanguage.push(Language.CEYLON);
                // call our own genCode
                return genCodeUnlessError(env, cdef);
            } finally {
                sourceLanguage.pop();
            }
        }
        return super.genCode(env, cdef);
    }

    @Override
    protected boolean shouldStop(CompileState cs) {
        // we override this to make sure we don't stop because of errors, because we want to generate
        // code for classes with no errors
        boolean result;
        if (shouldStopPolicy == null)
            result = false;
        else
            result = cs.ordinal() > shouldStopPolicy.ordinal();
        if (!result && super.shouldStop(cs)) {
            treatLikelyBugsAsErrors = true;
        }
        return result;
    }
    
    public boolean getTreatLikelyBugsAsErrors() {
        return treatLikelyBugsAsErrors;
    }
    
    private JavaFileObject genCodeUnlessError(Env<AttrContext> env, JCClassDecl cdef) throws IOException {
        CeylonFileObject sourcefile = (CeylonFileObject) env.toplevel.sourcefile;
        try {
            // do not look at the global number of errors but only those for this file
            if (super.gen.genClass(env, cdef)){
                String packageName = cdef.sym.packge().getQualifiedName().toString();
                Package pkg = modelLoader.findPackage(packageName);
                if(pkg == null)
                    throw new RuntimeException("Failed to find package: "+packageName);
                Module module = pkg.getModule();
                if(!module.isDefault()){
                    String moduleName = module.getNameAsString();
                    CeylonFileObject moduleFileObject = moduleNamesToFileObjects.get(moduleName);
                    // if there's no module source file object it means the module descriptor had parse errors
                    if(moduleFileObject == null || moduleFileObject.hasError()){
                        // we do not produce any class files for modules with errors
                        if(options.get(OptionName.VERBOSE) != null){
                            Log.printLines(log.noticeWriter, "[Not writing class "+cdef.sym.className()
                                    +" because its module has errors: "+moduleName+"]");
                        }
                        return null;
                    }
                }
                return writer.writeClass(cdef.sym);
            }
        } catch (ClassWriter.PoolOverflow ex) {
            log.error(cdef.pos(), "limit.pool");
        } catch (ClassWriter.StringOverflow ex) {
            log.error(cdef.pos(), "limit.string.overflow",
                    ex.value.substring(0, 20));
        } catch (CompletionFailure ex) {
            chk.completionError(cdef.pos(), ex);
        } catch (AssertionError e) {
            throw new RuntimeException("Error generating bytecode for " + sourcefile.getName(), e);
        }
        return null;
    }

    protected void desugar(final Env<AttrContext> env, Queue<Pair<Env<AttrContext>, JCClassDecl>> results) {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                sourceLanguage.push(Language.CEYLON);
                super.desugar(env, results);
                return;
            } finally {
                sourceLanguage.pop();
            }
        }
        super.desugar(env, results);
    }
    
    protected void flow(Env<AttrContext> env, Queue<Env<AttrContext>> results) {
        if (env.toplevel.sourcefile instanceof CeylonFileObject) {
            try {
                sourceLanguage.push(Language.CEYLON);
                super.flow(env, results);
                return;
            } finally {
                sourceLanguage.pop();
            }
        }
        super.flow(env, results);   
    }

    @Override
    public void initProcessAnnotations(Iterable<? extends Processor> processors) {
        // don't do anything, which will leave the "processAnnotations" field to false
    }

    @Override
    public void complete(ClassSymbol c) throws CompletionFailure {
        try {
            sourceLanguage.push(Language.JAVA);
            super.complete(c);
        } catch (RunTwiceException e) {
            throw new CompletionFailure(c, e.getLocalizedMessage());
        } finally {
            sourceLanguage.pop();
        }
    }
    
    @Override
    public void generate(Queue<Pair<Env<AttrContext>, JCClassDecl>> queue, Queue<JavaFileObject> results) {
        timer.startTask("Generate");
        super.generate(queue, results);
        timer.endTask();
    }
}

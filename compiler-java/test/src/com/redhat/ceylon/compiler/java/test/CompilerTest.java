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
package com.redhat.ceylon.compiler.java.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.regex.Matcher;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;

import org.junit.Assert;
import org.junit.Before;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.ArtifactResultType;
import com.redhat.ceylon.cmr.api.ImportType;
import com.redhat.ceylon.cmr.api.RepositoryException;
import com.redhat.ceylon.cmr.api.VisibilityType;
import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer;
import com.redhat.ceylon.compiler.java.codegen.JavaPositionsRetriever;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState.CeylonState;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.java.util.RepositoryLister;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskEvent.Kind;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.file.ZipFileIndexCache;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

public abstract class CompilerTest {

    protected final static String dir = "test/src";
    protected final static String destDirGeneral = "build/test-cars";
    protected final static String cacheDirGeneral = "build/test-cache";
    public static final String LANGUAGE_MODULE_CAR = "../ceylon.language/ide-dist/ceylon.language-"+TypeChecker.LANGUAGE_MODULE_VERSION+".car";
    protected final String destDir;
    protected final String cacheDir;
    protected final String moduleName;
    protected final List<String> defaultOptions;
    
    /**
     * See run() for why this is needed.
     */
    public static final Object RUN_LOCK = new Object();

    public CompilerTest() {
        // for comparing with java source
        Package pakage = getClass().getPackage();
        moduleName = pakage.getName();
        
        
        int lastDot = moduleName.lastIndexOf('.');
        if(lastDot == -1){
            destDir = destDirGeneral + File.separator + transformDestDir(moduleName);
        } else {
            destDir = destDirGeneral + File.separator + transformDestDir(moduleName.substring(lastDot+1));
        }
        if(lastDot == -1){
            cacheDir = cacheDirGeneral + File.separator + transformDestDir(moduleName);
        } else {
            cacheDir = cacheDirGeneral + File.separator + transformDestDir(moduleName.substring(lastDot+1));
        }
        defaultOptions = Arrays.asList("-out", destDir, "-cacherep", cacheDir);
    }

    // for subclassers 
    protected String transformDestDir(String name) {
        return name;
    }

    protected CeyloncTool makeCompiler(){
        try {
            return new CeyloncTool();
        } catch (VerifyError e) {
            System.err.println("ERROR: Cannot run tests! Did you maybe forget to configure the -Xbootclasspath/p: parameter?");
            throw e;
        }
    }

    protected String getPackagePath() {
        Package pakage = getClass().getPackage();
        String pkg = pakage == null ? "" : moduleName.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
        return dir + File.separator + pkg + File.separator;
    }
    
    protected CeyloncFileManager makeFileManager(CeyloncTool compiler, DiagnosticListener<? super FileObject> diagnosticListener){
        return (CeyloncFileManager)compiler.getStandardFileManager(diagnosticListener, null, null);
	}
	
	protected void compareWithJavaSource(String name) {
		compareWithJavaSource(name+".src", name+".ceylon");
	}
	
    protected void compareWithJavaSourceNoOpt(String name) {
        List<String> options = new ArrayList<String>();
        options.add("-disableOptimization");
        for (String option : defaultOptions) {
            options.add(option);
        }
        compareWithJavaSource(options, name+".noopt.src", name+".ceylon");
    }

	@Before
	public void cleanCars() {
	    cleanCars(destDir);
        cleanCars(cacheDir);
	}
	
    public void cleanCars(String repo) {
        File destFile = new File(repo);
        List<String> extensionsToDelete = Arrays.asList("");
        new RepositoryLister(extensionsToDelete).list(destFile, new RepositoryLister.Actions() {
            @Override
            public void doWithFile(File path) {
                path.delete();
            }

            public void exitDirectory(File path) {
                if (path.list().length == 0) {
                    path.delete();
                }
            }
        });
    }

    protected void assertErrors(String ceylon, CompilerError... expectedErrors) {
        assertErrors(ceylon, defaultOptions, null, expectedErrors);
    }
    
    protected void assertErrors(String ceylon, Throwable expectedException, CompilerError... expectedErrors) {
        assertErrors(ceylon, defaultOptions, expectedException, expectedErrors);
    }
    
    protected void assertErrors(String ceylon, List<String> options, Throwable expectedException, CompilerError... expectedErrors) {
        assertErrors(new String[] {ceylon+".ceylon"}, options, expectedException, expectedErrors);
    }
    
    protected void assertErrors(String[] ceylonFiles, List<String> options, Throwable expectedException, CompilerError... expectedErrors) {
        // make a compiler task
        // FIXME: runFileManager.setSourcePath(dir);
        ErrorCollector collector = new ErrorCollector();
        
        CeyloncTaskImpl task = getCompilerTask(options, collector, ceylonFiles);

        // now compile it all the way
        Throwable ex = null;
        ExitState exitState = task.call2();
        switch (exitState.ceylonState) {
        case OK:
            Assert.fail("Compilation successful (it should have failed!)");
            break;
        case BUG:
            if (expectedException == null) {
                throw new AssertionError("Compiler bug", exitState.abortingException);
            }
            ex = exitState.abortingException;
            break;
        case ERROR:
            break;
        case SYS:
            Assert.fail("System error");
            break;
        default:
            Assert.fail("Unknown exit state");
        }

        if (ex != null) {
            if (expectedException == null) {
                throw new AssertionError("Compilation terminated with unexpected error", ex);
            } else if (expectedException.getClass() != ex.getClass() || !eq(expectedException.getMessage(), ex.getMessage())) {
                throw new AssertionError("Compilation terminated with a different error than the expected " + expectedException, ex);
            }
        } else if (expectedException != null) {
            Assert.fail("Expected compiler exception " + expectedException);
        }
        
        TreeSet<CompilerError> actualErrors = collector.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors, expectedErrors);
    }
    
    private boolean eq(Object o1, Object o2) {
        return (o1 == o2) || o1.equals(o2);
    }
    
    protected void compareErrors(TreeSet<CompilerError> actualErrors, CompilerError... expectedErrors) {
        TreeSet<CompilerError> expectedErrorSet = new TreeSet<CompilerError>(Arrays.asList(expectedErrors));
        // first dump the actual errors
        for(CompilerError actualError : actualErrors){
            System.err.println(actualError.lineNumber+": "+actualError.message);
        }
        
        // make sure we have all those we expect
        for(CompilerError expectedError : expectedErrorSet){
            Assert.assertTrue("Missing expected error: "+expectedError, actualErrors.contains(expectedError));
        }
        //  make sure we don't have unexpected ones
        for(CompilerError actualError : actualErrors){
            Assert.assertTrue("Unexpected error: "+actualError, expectedErrorSet.contains(actualError));
        }
    }
    
    protected void compareWithJavaSourceWithPositions(String name) {
        // make a compiler task
        // FIXME: runFileManager.setSourcePath(dir);
        CeyloncTaskImpl task = getCompilerTask(new String[] {name+".ceylon"});

        // grab the CU after we've completed it
        class Listener implements TaskListener{
            JCCompilationUnit compilationUnit;
            private String compilerSrc;
            private JavaPositionsRetriever javaPositionsRetriever = new JavaPositionsRetriever();

            @Override
            public void started(TaskEvent e) {
                AbstractTransformer.trackNodePositions(javaPositionsRetriever);
            }

            @Override
            public void finished(TaskEvent e) {
                if(e.getKind() == Kind.ENTER){
                    if(compilationUnit == null) {
                        compilationUnit = (JCCompilationUnit) e.getCompilationUnit();
                        // for some reason compilationUnit is full here in the listener, but empty as soon
                        // as the compile task is done. probably to clean up for the gc?
                        javaPositionsRetriever.retrieve(compilationUnit);
                        compilerSrc = normalizeLineEndings(javaPositionsRetriever.getJavaSourceCodeWithCeylonPositions());
                        AbstractTransformer.trackNodePositions(null);
                    }
                }
            }
        }
        Listener listener = new Listener();
        task.setTaskListener(listener);

        // now compile it all the way
        ExitState exitState = task.call2();
        Assert.assertEquals("Compilation failed", exitState.ceylonState, CeylonState.OK);

        // now look at what we expected
        String expectedSrc = normalizeLineEndings(readFile(new File(getPackagePath(), name+".src"))).trim();
        String compiledSrc = listener.compilerSrc.trim();
        Assert.assertEquals("Source code differs", expectedSrc, compiledSrc);
    }

    protected void compareWithJavaSourceWithLines(String name) {
        // make a compiler task
        // FIXME: runFileManager.setSourcePath(dir);
        CeyloncTaskImpl task = getCompilerTask(new String[] {name+".ceylon"});

        // grab the CU after we've completed it
        class Listener implements TaskListener{
            JCCompilationUnit compilationUnit;
            private String compilerSrc;
            private JavaPositionsRetriever javaPositionsRetriever = new JavaPositionsRetriever();

            @Override
            public void started(TaskEvent e) {
            }

            @Override
            public void finished(TaskEvent e) {
                if(e.getKind() == Kind.ENTER){
                    if(compilationUnit == null) {
                        compilationUnit = (JCCompilationUnit) e.getCompilationUnit();
                        // for some reason compilationUnit is full here in the listener, but empty as soon
                        // as the compile task is done. probably to clean up for the gc?
                        javaPositionsRetriever.retrieve(compilationUnit);
                        compilerSrc = normalizeLineEndings(javaPositionsRetriever.getJavaSourceCodeWithCeylonLines());
                        AbstractTransformer.trackNodePositions(null);
                    }
                }
            }
        }
        Listener listener = new Listener();
        task.setTaskListener(listener);

        // now compile it all the way
        ExitState exitState = task.call2();
        Assert.assertEquals("Compilation failed", exitState.ceylonState == CeylonState.OK);

        // now look at what we expected
        String expectedSrc = normalizeLineEndings(readFile(new File(getPackagePath(), name+".src"))).trim();
        String compiledSrc = listener.compilerSrc.trim();
        Assert.assertEquals("Source code differs", expectedSrc, compiledSrc);
    }

    protected void compareWithJavaSource(String java, String... ceylon) {
        compareWithJavaSource(defaultOptions, java, ceylon);
    }
    
    protected void compareWithJavaSource(List<String> options, String java, String... ceylon) {
        // make a compiler task
        // FIXME: runFileManager.setSourcePath(dir);
        ErrorCollector collector = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, collector, ceylon);

        // grab the CU after we've completed it
        class Listener implements TaskListener{
            JCCompilationUnit compilationUnit;
            private String compilerSrc;
            @Override
            public void started(TaskEvent e) {
            }

            @Override
            public void finished(TaskEvent e) {
                if(e.getKind() == Kind.ENTER){
                    if(compilationUnit == null) {
                        compilationUnit = (JCCompilationUnit) e.getCompilationUnit();
                        // for some reason compilationUnit is full here in the listener, but empty as soon
                        // as the compile task is done. probably to clean up for the gc?
                        compilerSrc = normalizeLineEndings(compilationUnit.toString());
                    }
                }
            }
        }
        Listener listener = new Listener();
        task.setTaskListener(listener);

        // now compile it all the way
        assertCompilesOk(collector, task.call2());
        
        // now look at what we expected
        File expectedSrcFile = new File(getPackagePath(), java);
        String expectedSrc = normalizeLineEndings(readFile(expectedSrcFile)).trim();
        String compiledSrc = listener.compilerSrc.trim();
        
        // THIS IS FOR INTERNAL USE ONLY!!!
        // Can be used to do batch updating of known correct tests
        // Uncomment only when you know what you're doing!
//        if (expectedSrc != null && compiledSrc != null && !expectedSrc.equals(compiledSrc)) {
//            writeFile(expectedSrcFile, compiledSrc);
//            expectedSrc = compiledSrc;
//        }
        
        Assert.assertEquals("Source code differs", expectedSrc, compiledSrc);
    }

    protected void assertCompilesOk(ErrorCollector collector, ExitState exitState)
            throws AssertionError {
        switch (exitState.ceylonState) {
        case OK:
            break;
        case BUG:
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            if (exitState.abortingException != null) {
                pw.println();
                pw.println("With Javac error");
                exitState.abortingException.printStackTrace(pw);
            }
            pw.flush();
            Assert.fail(collector.getAssertionFailureMessage() + sw.toString());
            break;
        case ERROR:
            Assert.fail(collector.getAssertionFailureMessage());
            break;
        case SYS:
            Assert.fail("System error");
            break;
        default:
            Assert.fail("Unknown exit state");
        }
    }
    


    protected String readFile(File file) {
        try{
            Reader reader = new FileReader(file);
            StringBuilder strbuf = new StringBuilder();
            try{
                char[] buf = new char[1024];
                int read;
                while((read = reader.read(buf)) > -1)
                    strbuf.append(buf, 0, read);
            }finally{
                reader.close();
            }
            return strbuf.toString();
        }catch(IOException x){
            throw new RuntimeException(x);
        }
    }

    // THIS IS FOR INTERNAL USE ONLY!!!
    private void writeFile(File file, String src) {
        try{
            Writer writer = new FileWriter(file);
            try{
                writer.write(src);
            }finally{
                writer.close();
            }
        }catch(IOException x){
            throw new RuntimeException(x);
        }
    }

    protected String normalizeLineEndings(String txt) {
        String result = txt.replaceAll("\r\n", "\n"); // Windows
        result = result.replaceAll("\r", "\n"); // Mac (OS<=9)
        return result;
    }

    protected void compile(String... ceylon) {
        ErrorCollector c = new ErrorCollector();
        assertCompilesOk(c, getCompilerTask(c, ceylon).call2());
    }
    
    protected void compilesWithoutWarnings(String... ceylon) {
        ErrorCollector dl = new ErrorCollector();
        ExitState exitState = getCompilerTask(defaultOptions, dl, ceylon).call2();
        Assert.assertEquals(exitState.ceylonState, CeylonState.OK);
        Assert.assertEquals("The code compiled with javac warnings", 
                0, dl.get(Diagnostic.Kind.WARNING).size() + dl.get(Diagnostic.Kind.MANDATORY_WARNING).size());
    }

    protected Object compileAndRun(String main, String... ceylon) {
        compile(ceylon);
        return run(main);
    }

    protected Object run(String main) {
        return run(main, getDestModuleWithArtifact());
    }
    
    public class ModuleWithArtifact {
        private String module;
        private String version;
        private File file;
        public ModuleWithArtifact(String module, String version) {
            this.module = module;
            this.version = version;
            this.file = getModuleArchive(module,version);
        }
    }
    
    public static ArtifactResult makeArtifactResult(final File file) {
        return new ArtifactResult() {
            
            @Override
            public VisibilityType visibilityType() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String version() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ArtifactResultType type() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String name() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ImportType importType() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public List<ArtifactResult> dependencies() throws RepositoryException {
                return Collections.emptyList();
            }
            
            @Override
            public File artifact() throws RepositoryException {
                return file;
            }
        };
    }

    protected Class<?> loadClass(String className, ModuleWithArtifact... modules) {
        synchronized(RUN_LOCK){
            // the module initialiser code needs to run in a protected section because the language module Util is not loaded by
            // the test classloader but by our own classloader, which may be shared with other tests running in parallel, so if
            // we set up the module system while another thread is setting it up for other modules we're toast
            try{
                // make sure we load the stuff from the Car
                NonCachingURLClassLoader loader = getClassLoader(className, modules);
                
                java.lang.Class<?> klass = java.lang.Class.forName(className, true, loader);
                
                return klass;
            }catch(Exception x){
                throw new RuntimeException(x);
            }
        }
    }
    
    protected Object run(String main, ModuleWithArtifact... modules) {
        synchronized(RUN_LOCK){
            // the module initialiser code needs to run in a protected section because the language module Util is not loaded by
            // the test classloader but by our own classloader, which may be shared with other tests running in parallel, so if
            // we set up the module system while another thread is setting it up for other modules we're toast
            Object result = null;
            try{
                // make sure we load the stuff from the Car

                NonCachingURLClassLoader loader = getClassLoader(main, modules);
                String mainClass = main;
                String mainMethod = main.replaceAll("^.*\\.", "");
                if (Util.isInitialLowerCase(mainMethod)) {
                    mainClass = main + "_";
                }
                java.lang.Class<?> klass = java.lang.Class.forName(mainClass, true, loader);
                if (Util.isInitialLowerCase(mainMethod)) {
                    // A method, we need to quote the name though
                    if ("main".equals(mainMethod)
                            || "finalize".equals(mainMethod)) {
                        mainMethod = "$" + mainMethod;
                    }
                    Method m = klass.getDeclaredMethod(mainMethod);
                    m.setAccessible(true);
                    result = m.invoke(null);
                } else {
                    // A main class
                    final Constructor<?> ctor = klass.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    result = ctor.newInstance();
                }
                loader.clearCache();
                return result;
            }catch(Exception x){
                throw new RuntimeException(x);
            }
        }
    }

    private NonCachingURLClassLoader getClassLoader(String main,
            ModuleWithArtifact... modules) throws MalformedURLException {
        List<URL> urls = getClassPathAsUrls(modules);
        System.err.println("Running " + main +" with classpath" + urls);
        NonCachingURLClassLoader loader = new NonCachingURLClassLoader(urls.toArray(new URL[urls.size()]));
        // set up the runtime module system
        Metamodel.resetModuleManager();
        Metamodel.loadModule("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, makeArtifactResult(new File(LANGUAGE_MODULE_CAR)), loader);
        for (ModuleWithArtifact module : modules) {
            Metamodel.loadModule(module.module, module.version, makeArtifactResult(module.file), loader);
        }
        return loader;
    }
    
    @SuppressWarnings("deprecation")
    protected List<URL> getClassPathAsUrls(ModuleWithArtifact... modules)
            throws MalformedURLException {
        List<File> files = getClassPathAsFiles(modules);
        List<URL> urls = new ArrayList<URL>(files.size());
        for (File file : files) {
            urls.add(file.toURL());
        }
        return urls;
    }
    
    protected List<File> getClassPathAsFiles(ModuleWithArtifact... modules) {
        List<File> files = new ArrayList<File>(modules.length);
        for (ModuleWithArtifact module : modules) {
            File car = module.file;
            Assert.assertTrue(car + " does not exist", car.exists());
            files.add(car);
        }
        return files;
    }

    public static class NonCachingURLClassLoader extends URLClassLoader {

        public NonCachingURLClassLoader(URL[] urls) {
            super(urls);
        }

        public void clearCache() {
            try {
                Class<?> klass = java.net.URLClassLoader.class;
                Field ucp = klass.getDeclaredField("ucp");
                ucp.setAccessible(true);
                Object sunMiscURLClassPath = ucp.get(this);
                Field loaders = sunMiscURLClassPath.getClass().getDeclaredField("loaders");
                loaders.setAccessible(true);
                Object collection = loaders.get(sunMiscURLClassPath);
                for (Object sunMiscURLClassPathJarLoader : ((Collection<?>) collection).toArray()) {
                    try {
                        Field loader = sunMiscURLClassPathJarLoader.getClass().getDeclaredField("jar");
                        loader.setAccessible(true);
                        Object jarFile = loader.get(sunMiscURLClassPathJarLoader);
                        ((JarFile) jarFile).close();
                    } catch (Throwable t) {
                        // not a JAR loader?
                        t.printStackTrace();
                    }
                }
            } catch (Throwable t) {
                // Something's wrong
                t.printStackTrace();
            }
            return;
        }
    }

    protected CeyloncTaskImpl getCompilerTask(String... sourcePaths){
        return getCompilerTask(defaultOptions, null, sourcePaths);
    }
    
    protected CeyloncTaskImpl getCompilerTask(DiagnosticListener<? super FileObject> diagnosticListener, String... sourcePaths){
        return getCompilerTask(defaultOptions, diagnosticListener, sourcePaths);
    }

    protected CeyloncTaskImpl getCompilerTask(List<String> options, String... sourcePaths){
        return getCompilerTask(options, null, sourcePaths);
    }

    protected CeyloncTaskImpl getCompilerTask(List<String> options, DiagnosticListener<? super FileObject> diagnosticListener, 
            String... sourcePaths){
        return getCompilerTask(options, diagnosticListener, null, sourcePaths);
    }
    
    protected CeyloncTaskImpl getCompilerTask(List<String> initialOptions, DiagnosticListener<? super FileObject> diagnosticListener, 
            List<String> modules, String... sourcePaths){
        // make sure we get a fresh jar cache for each compiler run
        // FIXME: make this only get rid of the jars we produce, to win 2s out of 17s
        ZipFileIndexCache.getSharedInstance().clearCache();
        java.util.List<File> sourceFiles = new ArrayList<File>(sourcePaths.length);
        for(String file : sourcePaths){
            sourceFiles.add(new File(getPackagePath(), file));
        }

        CeyloncTool runCompiler = makeCompiler();
        CeyloncFileManager runFileManager = makeFileManager(runCompiler, diagnosticListener);

        // make sure the destination repo exists
        new File(destDir).mkdirs();

        List<String> options = new LinkedList<String>();
        options.addAll(initialOptions);
        if(!options.contains("-src"))
            options.addAll(Arrays.asList("-src", getSourcePath()));
        if(!options.contains("-cacherep"))
            options.addAll(Arrays.asList("-cacherep", getCachePath()));
        boolean hasVerbose = false;
        for(String option : options){
            if(option.startsWith("-verbose")){
                hasVerbose = true;
                break;
            }
        }
        if(!hasVerbose)
            options.add("-verbose:ast,code");
        Iterable<? extends JavaFileObject> compilationUnits1 =
                runFileManager.getJavaFileObjectsFromFiles(sourceFiles);
        return runCompiler.getTask(null, runFileManager, diagnosticListener, 
                options, modules, compilationUnits1);
    }

    protected String getSourcePath() {
        return dir;
    }

    protected String getCachePath() {
        return cacheDir;
    }

    protected String getOutPath() {
        return destDir;
    }

    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact(Module.DEFAULT_MODULE_NAME, null);
    }

    protected File getModuleArchive(String moduleName, String version) {
        return getModuleArchive(moduleName, version, destDir);
    }

    protected static File getModuleArchive(String moduleName, String version, String destDir) {
        return getArchiveName(moduleName, version, destDir, "car");
    }

    protected File getSourceArchive(String moduleName, String version) {
        return getArchiveName(moduleName, version, destDir, "src");
    }
    
    protected static File getSourceArchive(String moduleName, String version, String destDir) {
        return getArchiveName(moduleName, version, destDir, "src");
    }

    protected static File getArchiveName(String moduleName, String version, String destDir, String extension) {
        String modulePath = moduleName.replace('.', File.separatorChar);
        if(version != null)
            modulePath += File.separatorChar+version;
        modulePath += File.separator;
        String artifactName = modulePath+moduleName;
        if(version != null)
            artifactName += "-"+version;
        artifactName += "."+extension;
        File archiveFile = new File(destDir, artifactName);
        return archiveFile;
    }
}

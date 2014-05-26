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
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;

import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.runtime.Main;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyClass;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.tools.classpath.CeylonClasspathTool;
import com.sun.tools.javac.util.Context;

/**
 * JUnit Runner for a Ceylon module.
 * 
 * <ul>
 * <li>Works on a class annotated with {@link TestModule} and 
 * {@link org.junit.runner.RunWith RunWith(CeylonModuleRunner.class)} in the 
 * root package of the module to be tested.</li>
 * <li>The top-level classes are
 * scanned for {@code shared} methods with the {@code @test} compiler 
 * annotation.</li>
 * <li>The module is compiled</li>
 * <li>The test classes are loaded. Any which can't be loaded are treated as
 * test failures</li>
 * <li>The test methods on the test classes are executed (by the {@link CeylonTestRunner}</li>
 * <li>Exceptions thrown by the test methods are treated as failures</li>
 * </ol> 
 * @author tom
 */
public class CeylonModuleRunner extends ParentRunner<Runner> {

    private HashMap<Runner, Description> children;

    private boolean errorIfNoTests;
    
    private File outRepo;
    
    private TestLoader testLoader;

    public CeylonModuleRunner(Class<?> moduleSuiteClass) throws InitializationError {
        super(moduleSuiteClass);
        try {
            TestModule testModule = getTestModuleAnno(moduleSuiteClass);
            this.errorIfNoTests = testModule.errorIfNoTests();
            this.testLoader = testModule.testLoader().newInstance();
            this.children = new LinkedHashMap<Runner, Description>();
            
            File srcDir = new File(testModule.srcDirectory());
            outRepo = Files.createTempDirectory("ceylon-module-runner").toFile();
            
            String[] modules = testModule.modules();
            if(modules.length == 0)
                modules = new String[]{ moduleSuiteClass.getPackage().getName() };
            
            Set<String> removeAtRuntime = new HashSet<String>();
            Collections.addAll(removeAtRuntime, testModule.removeAtRuntime());
            compileAndRun(srcDir, outRepo, modules, testModule.dependencies(), removeAtRuntime);
            
            for(ModuleSpecifier module : testModule.runModulesInNewJvm()){
                makeModuleRunnerInNewJvm(module);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void makeModuleRunnerInNewJvm(final ModuleSpecifier module) {
        final Description description = Description.createTestDescription(getClass(), "Run "+module.module()+" in new JVM");
        
        Runner runner = new Runner(){
            @Override
            public Description getDescription() {
                return description;
            }

            @Override
            public void run(RunNotifier notifier) {
                notifier.fireTestStarted(description);
                try{
                    String moduleName = module.module();
                    String version = Module.DEFAULT_MODULE_NAME.equals(moduleName) ? null : module.version();
                    String runClass = module.runClass();
                    if(runClass.isEmpty())
                        runClass = moduleName + ".run_";
                    runModuleInNewJvm(moduleName, version, runClass);
                }catch(Exception x){
                    x.printStackTrace();
                    notifier.fireTestFailure(new Failure(description, x));
                }
                notifier.fireTestFinished(description);
            }
        };
        children.put(runner, description);
    }

    protected void runModuleInNewJvm(String module, String version, String runClass) throws Exception {
        String path = System.getProperty("java.home")
                + File.separator + "bin" + File.separator + "java";
        
        String moduleSpec = Module.DEFAULT_MODULE_NAME.equals(module) ? module : (module+"/"+version);

        CeylonClasspathTool cpTool = new CeylonClasspathTool();
        cpTool.setModule(moduleSpec);
        cpTool.setRepositoryAsStrings(Arrays.asList(outRepo.getAbsolutePath()));
        cpTool.setSystemRepository("../ceylon-dist/dist/repo");
        cpTool.setNoDefRepos(true);
        cpTool.setOffline(true);
        StringBuilder sb = new StringBuilder();
        cpTool.setOut(sb);
        cpTool.run();

        String classpath = sb.toString();
        
        ProcessBuilder processBuilder = 
                new ProcessBuilder(path, "-cp", 
                classpath, 
                Main.class.getName(),
                moduleSpec, runClass);
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exit = process.waitFor();
        Assert.assertTrue(exit == 0);
    }

    private void compileAndRun(File srcDir, File outRepo, String[] modules, String[] dependencies, Set<String> removeAtRuntime) throws Exception {
        // Compile all the .ceylon files into a .car
        Context context = new Context();
        final ErrorCollector listener = new ErrorCollector();
        CeyloncFileManager.preRegister(context); // can't create it until Log
        // has been set up
        CeylonLog.preRegister(context);
        context.put(DiagnosticListener.class, listener);
        
        com.redhat.ceylon.compiler.java.launcher.Main compiler = new com.redhat.ceylon.compiler.java.launcher.Main("ceylonc");
        List<String> args = new ArrayList<>();
//            args.add("-verbose:code");
        args.add("-src"); 
        args.add(srcDir.getCanonicalPath());
        args.add("-out");
        args.add(outRepo.getAbsolutePath());
        for(String module : modules)
            args.add(module);
        for(String module : dependencies)
            args.add(module);
        
        compiler.compile(args.toArray(new String[args.size()]), context);

        TreeSet<CompilerError> errors = listener.get(Kind.ERROR);
        if(!errors.isEmpty()){
            List<Runner> errorRunners = new LinkedList<Runner>();
            for (final CompilerError compileError : errors) {
                createFailingTest(errorRunners, compileError.filename, new CompilationException(compileError.toString()));
            }
            for(Runner errorRunner : errorRunners){
                children.put(errorRunner, errorRunner.getDescription());
            }
        }
        // remove what we need for runtime
        for(String module : removeAtRuntime){
            File moduleFolder = new File(outRepo, module.replace('.', File.separatorChar));
            FileUtil.delete(moduleFolder);
        }

        for(String module : modules){
            postCompile(context, listener, module, srcDir, dependencies, removeAtRuntime);
        }
    }
    
    private void postCompile(Context context, ErrorCollector listener, String moduleName, File srcDir, String[] dependencies, Set<String> removeAtRuntime) throws Exception {
        // now fetch stuff from the context
        PhasedUnits phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        
        List<Runner> moduleRunners = new LinkedList<Runner>();

        // Get a class loader for the car
        // XXX Need to use CMR if the module has dependencies
        URL[] carUrls = getCarUrls(moduleName, dependencies, removeAtRuntime, outRepo);
        URLClassLoader cl = classLoaderForModule(carUrls);
        Runnable moduleInitialiser = getModuleInitialiser(moduleName, carUrls, dependencies, removeAtRuntime, cl);
        
        if (cl != null) {
            loadCompiledTests(moduleRunners, srcDir, cl, phasedUnits, moduleName);
        }
        CeylonTestGroup ceylonTestGroup = new CeylonTestGroup(moduleRunners, moduleName, moduleInitialiser);
        children.put(ceylonTestGroup, ceylonTestGroup.getDescription());
    }

    private static class CompilationException extends Exception {
        public CompilationException(String message) {
            super(message);
        }
    }
    
    void createFailingTest(List<Runner> moduleRunners, final String testName, final Exception ex) {
        final Description description = Description.createTestDescription(getClass(), testName);
        Runner runner = new Runner() {
            @Override
            public Description getDescription() {
                return description;
            }
            @Override
            public void run(RunNotifier notifier) {
                notifier.fireTestStarted(description);
                notifier.fireTestFailure(new Failure(description, ex));
                notifier.fireTestFinished(description);
            }
        };
        moduleRunners.add(runner);
    }
    
    private void loadCompiledTests(List<Runner> moduleRunners, File srcDir, URLClassLoader cl, PhasedUnits phasedUnits, String moduleName)
                throws InitializationError {
        Map<String, List<String>> testMethods = testLoader.loadTestMethods(moduleRunners, this, phasedUnits, moduleName);
        if (testMethods.isEmpty() && errorIfNoTests) {
            createFailingTest(moduleRunners, "No tests!", new Exception("contains no tests"));
        }
        Method failureCountGetter = null;
        if(ModuleUtil.isDefaultModule(moduleName)){
            failureCountGetter = getFailureCountGetter(moduleRunners, cl);
            // check if an error was produced
            if(failureCountGetter == null)
                return;
        }

        for (Map.Entry<String, List<String>> entry : testMethods.entrySet()) {
            final String className = entry.getKey();
            
            Class<?> testClass;
            try {
                testClass = cl.loadClass(className);
            } catch (ClassNotFoundException e) {
                testClass = null;
            }
            if (testClass == null) {
                // Create a fake (failing) test for classes we couldn't load
                // presumably because they had compile errors
                createFailingTest(moduleRunners, className, new CompilationException("Test class " + className + " didn't compile"));
            } else {
                List<String> testMethodNames = entry.getValue();
                CeylonTestRunner classTestRunner = new CeylonTestRunner(testClass, failureCountGetter, testMethodNames);
                moduleRunners.add(classTestRunner);
            }
        }
    }
    
    private Method getFailureCountGetter(List<Runner> moduleRunners, URLClassLoader cl) {
        Class<?> failureCountClass;
        try {
            failureCountClass = cl.loadClass("failureCount_");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            failureCountClass = null;
        }
        if(failureCountClass == null) {
            // Create a fake (failing) test for classes we couldn't find the failure count
            createFailingTest(moduleRunners, "Initialisation error", new CompilationException("Count not find test.failureCount class"));
            return null;
        }
        // get the method for getting the failure count
        Method failureCountGetter = null;
        try {
            failureCountGetter = failureCountClass.getDeclaredMethod("get_");
            failureCountGetter.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e1) {
            // Create a fake (failing) test for classes we couldn't find the failure count
            createFailingTest(moduleRunners, "Initialisation error", new CompilationException("Could not find getter for failure count: "+e1));
            return null;
        }
        return failureCountGetter;
    }

    public void noTests() {
        Assert.fail("No tests found");
    }

    private URL urlForModule(File repo, String moduleName) throws MalformedURLException {
        File moduleDir = new File(repo, moduleName.replace(".", "/"));
        if (!moduleDir.exists()) {
            moduleDir = new File(repo, "default");
            if (!moduleDir.exists()) {
                throw new RuntimeException(moduleDir + " doesn't exist");
            }
        }
        String[] files = moduleDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return new File(dir, name).isDirectory() || name.endsWith(".car");
            }
        });
        if (files.length == 0) {
            return null;
        } else if (files.length > 1) {
            throw new RuntimeException("Unexpectedly more than one car file in " + moduleDir);
        }
        String version = files[0];
        final File carFile;
        if (version.equals("default.car")) {
            carFile = new File(moduleDir, version);
            version = "unversioned";
        } else {
            carFile = new File(moduleDir, 
                    version + File.separator + moduleName + "-" + version+ ".car");
        }
        if (!carFile.exists()) {
            throw new RuntimeException(carFile + " doesn't exist");
        }
        URL outCar = carFile.toURI().toURL();
        return outCar;
    }

    private URL[] getCarUrls(final String moduleName, final String[] deps, Set<String> removeAtRuntime, File repo)
            throws MalformedURLException {
        final URL[] carUrls = new URL[1 + (deps != null ? deps.length : 0) - removeAtRuntime.size()];
        
        URL carUrl = urlForModule(repo, moduleName);
        carUrls[0] = carUrl;
        if (deps != null) {
            for (int dep = 0, url = 1; dep < deps.length; dep++) {
                if(!removeAtRuntime.contains(deps[dep]))
                    carUrls[url++] = urlForModule(repo, deps[dep]);
            }
        }
        return carUrls;
    }
    
    private URLClassLoader classLoaderForModule(URL[] carUrls) {
        return new URLClassLoader(carUrls, getClass().getClassLoader());
    }

    private Runnable getModuleInitialiser(final String moduleName, final URL[] carUrls, final String[] dependencies, final Set<String> removeAtRuntime, final ClassLoader cl) throws URISyntaxException{
        final File carFile = new File(carUrls[0].toURI());
        final String version;
        if (carFile.getName().equals("default.car")) {
            version = "unversioned";
        } else {
            Matcher matcher = Pattern.compile(Pattern.quote(moduleName+"-") + "(.*)" + Pattern.quote(".car")).matcher(carFile.getName());
            if (!matcher.matches()) {
                throw new RuntimeException(carFile.getName());
            }
            version = matcher.group(1);
        }

        return new Runnable(){
            @Override
            public void run() {
                // set up the runtime module system
                Metamodel.resetModuleManager();
                Metamodel.loadModule("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, CompilerTest.makeArtifactResult(new File("../ceylon.language/ide-dist/ceylon.language-"+TypeChecker.LANGUAGE_MODULE_VERSION+".car")), cl);
                Metamodel.loadModule("com.redhat.ceylon.typechecker", TypeChecker.LANGUAGE_MODULE_VERSION, CompilerTest.makeArtifactResult(new File("../ceylon-dist/dist/repo/com/redhat/ceylon/typechecker/"+TypeChecker.LANGUAGE_MODULE_VERSION+"/com.redhat.ceylon.typechecker-"+TypeChecker.LANGUAGE_MODULE_VERSION+".jar")), cl);
                Metamodel.loadModule(AbstractModelLoader.JAVA_BASE_MODULE_NAME, JDKUtils.jdk.version, CompilerTest.makeArtifactResult(null), cl);
                Metamodel.loadModule(moduleName, version, CompilerTest.makeArtifactResult(carFile), cl);
                // dependencies
                for (int dep = 0, c = 1; dep < dependencies.length; dep++) {
                    try {
                        String name = dependencies[dep];
                        if(removeAtRuntime.contains(name))
                            continue;
                        File car = new File(carUrls[c++].toURI());
                        String namePart = car.getName();
                        String version;
                        if(namePart.startsWith(name+"-")){
                            // remove name prefix
                            version = namePart.substring(name.length()+1);
                            // remove .car extension
                            version = version.substring(0, version.length()-4);
                        }else
                            throw new RuntimeException("Failed to find dependency module version for "+name);
                        Metamodel.loadModule(name,  version, CompilerTest.makeArtifactResult(car), cl);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        };
    }
    
    private TestModule getTestModuleAnno(Class<?> moduleSuiteClass) {
        TestModule testModule = (TestModule)moduleSuiteClass.getAnnotation(TestModule.class);
        if (testModule == null) {
            throw new RuntimeException("@RunWith(" + getClass().getSimpleName() + ".class) requires @" + TestModule.class.getSimpleName() + " annotation");
        }
        return testModule;
    }

    public static interface TestLoader {
        /**
         * Loads test methods
         * @param phasedUnits
         * @return A map of class names to test methods contained in that class
         */
        public Map<String, List<String>> loadTestMethods(List<Runner> moduleRunners, final CeylonModuleRunner moduleRunner, PhasedUnits phasedUnits, String moduleName);
    }

    public static class StandardLoader implements TestLoader {
        @Override
        public Map<String, List<String>> loadTestMethods(final List<Runner> moduleRunners, final CeylonModuleRunner moduleRunner, final PhasedUnits phasedUnits, final String moduleName) {
            // Find all the top level classes/functions annotated @test
            // Unfortunately doing it like this
            final Map<String, List<String>> testMethods = new TreeMap<String, List<String>>();

            for (PhasedUnit pu : phasedUnits.getPhasedUnits()) {
                CompilationUnit cu = pu.getCompilationUnit();
                cu.visit(new Visitor() {
                    private String testClassName = null;
                    @Override
                    public void visit(Declaration that) {
                        if (that instanceof AnyClass
                                && ((AnyClass)that).getDeclarationModel().getName().endsWith("Test")
                                && !((AnyClass)that).getDeclarationModel().isAbstract()
                                && ((AnyClass)that).getDeclarationModel().isToplevel()
                                && ((AnyClass)that).getDeclarationModel().getParameterLists().get(0).getParameters().size() == 0) {
                            testClassName = Decl.className(that.getDeclarationModel());
                            that.visitChildren(this);
                            testClassName = null;
                        }
                        for (CompilerAnnotation ca : that.getCompilerAnnotations()) {
                            Identifier identifier = ca.getIdentifier();
                            if ("test".equals(identifier.getToken().getText())) {
                                final com.redhat.ceylon.compiler.typechecker.model.Declaration decl = that.getDeclarationModel();
                                if (moduleName.isEmpty() || Decl.getModule(decl).getNameAsString().equals(moduleName)) {
                                    boolean added = false;
                                    if (testClassName != null || decl.isToplevel()) {
                                        if (decl instanceof com.redhat.ceylon.compiler.typechecker.model.Method) {
                                            com.redhat.ceylon.compiler.typechecker.model.Method method = (com.redhat.ceylon.compiler.typechecker.model.Method)decl;
                                            String methodName = method.getName();
                                            if (method.getParameterLists().size() == 1
                                                    && method.getParameterLists().get(0).getParameters().size() == 0) {
                                                String clsNm = (testClassName != null) ? testClassName : Decl.className(method);
                                                List<String> methods = testMethods.get(clsNm);
                                                if (methods == null) {
                                                    methods = new ArrayList<String>(3);
                                                    testMethods.put(clsNm, methods);
                                                }
                                                methods.add(methodName);
                                                added = true;
                                            }
                                        }
                                    } 
                                    if (!added) {
                                        moduleRunner.createFailingTest(moduleRunners, Decl.className(decl),
                                                new Exception("@test should only be used on methods of concrete top level classes whose name ends with 'Test' or on toplevel methods"));
                                    }
                                }
                            }
                        }
                    }
                });
            }
            return testMethods;
        }
    }
    
    private void delete(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                delete(child);
            }
        }
        file.delete();
    }
    
    @Override
    protected Description describeChild(Runner child) {
        return children.get(child);
    }

    @Override
    protected List<Runner> getChildren() {
        return new ArrayList<Runner>(children.keySet());
    }

    @Override
    protected void runChild(Runner child, RunNotifier notifier) {
        Description description = children.get(child);
        notifier.fireTestStarted(description);
        try {
            child.run(notifier);
        } finally {
            notifier.fireTestFinished(description);
        }
    }
    
    @Override
    public void run(RunNotifier notifier) {
        try {
            super.run(notifier);
        } finally {
            delete(outRepo);
        }
    }
    
    @Override
    public int testCount() {
        int ret = 0;
        for(Runner child : children.keySet()){
            ret += child.testCount();
        }
        return ret;
    }
}

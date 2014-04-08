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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyClass;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
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
    
    private int numTests;

    private File outRepo;
    
    private TestLoader testLoader;

    private Runnable moduleInitialiser;
    
    private static void scan(File relativeTo, List<String> list, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                scan(relativeTo, list, child);
            }
        } else if (file.isFile()
                && file.getName().endsWith(".ceylon")) {
            list.add(file.getAbsolutePath());
        }
    }
    
    public CeylonModuleRunner(Class<?> moduleSuiteClass) throws InitializationError {
        super(moduleSuiteClass);
        try {
            TestModule testModule = getTestModuleAnno(moduleSuiteClass);
            this.errorIfNoTests = testModule.errorIfNoTests();
            this.testLoader = testModule.testLoader().newInstance();
            File srcDir = new File(testModule.srcDirectory());
            outRepo = Files.createTempDirectory("ceylon-module-runner").toFile();
            
            for (String dependency : testModule.dependencies()) {
                compile(srcDir, outRepo, dependency, null, false);
            }
            String moduleName = !testModule.module().isEmpty() ? testModule.module() : moduleSuiteClass.getPackage().getName();
            compile(srcDir, outRepo, moduleName, testModule.dependencies(), true);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void compile(File srcDir, File outRepo, String moduleName, String[] deps, boolean runTests) throws Exception {
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
        if (ModuleUtil.isDefaultModule(moduleName)) {
            scan(srcDir.getCanonicalFile(), args, srcDir.getCanonicalFile());
        } else {
            args.add(moduleName);
        }
        
        int sc = compiler.compile(args.toArray(new String[args.size()]), 
                context);
        
        if (runTests) {
            postCompile(context, listener, moduleName, srcDir, deps);
        }
    }
    
    private void postCompile(Context context, ErrorCollector listener, String moduleName, File srcDir, String[] deps) throws Exception {
        // now fetch stuff from the context
        PhasedUnits phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        
        this.children = new LinkedHashMap<Runner, Description>();
        
        for (final CompilerError compileError : listener.get(Kind.ERROR)) {
            createFailingTest(compileError.filename, new CompilationException(compileError.toString()));
        }

        // Get a class loader for the car
        // XXX Need to use CMR if the module has dependencies
        URLClassLoader cl = classLoaderForModule(moduleName, deps, outRepo);
        if (cl != null) {
            loadCompiledTests(srcDir, cl, phasedUnits, moduleName);
        }
    }

    private static class CompilationException extends Exception {
        public CompilationException(String message) {
            super(message);
        }
    }
    
    void createFailingTest(final String testName, final Exception ex) {
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
        numTests += runner.testCount();
        children.put(runner,
                runner.getDescription());
    }
    
    private void loadCompiledTests(File srcDir, URLClassLoader cl, PhasedUnits phasedUnits, String moduleName)
                throws InitializationError {
        Map<String, List<String>> testMethods = testLoader.loadTestMethods(this, phasedUnits, moduleName);
        if (testMethods.isEmpty() && errorIfNoTests) {
            createFailingTest("No tests!", new Exception("contains no tests"));
        }
        Method failureCountGetter = null;
        if(ModuleUtil.isDefaultModule(moduleName)){
            failureCountGetter = getFailureCountGetter(cl);
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
                createFailingTest(className, new CompilationException("Test class " + className + " didn't compile"));
            } else {
                final Runner runner;
                final Description description;
                description = Description.createTestDescription(testClass, className);
                List<String> testMethodNames = entry.getValue();
                CeylonTestRunner classTestRunner = new CeylonTestRunner(testClass, failureCountGetter, testMethodNames);
                // Add child descriptions to my description
                for (Method testMethod : classTestRunner.getChildren()) {
                    description.addChild(classTestRunner.describeChild(testMethod));
                }
                if (testMethodNames == null
                        || testMethodNames.isEmpty()) {
                    try {
                        description.addChild(classTestRunner.describeChild(CeylonModuleRunner.class.getMethod("noTests")));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                }
                runner = classTestRunner;
                numTests += runner.testCount();
                children.put(runner,
                        description);
            }
        }
    }
    
    private Method getFailureCountGetter(URLClassLoader cl) {
        Class<?> failureCountClass;
        try {
            failureCountClass = cl.loadClass("failureCount_");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            failureCountClass = null;
        }
        if(failureCountClass == null) {
            // Create a fake (failing) test for classes we couldn't find the failure count
            createFailingTest("Initialisation error", new CompilationException("Count not find test.failureCount class"));
            return null;
        }
        // get the method for getting the failure count
        Method failureCountGetter = null;
        try {
            failureCountGetter = failureCountClass.getDeclaredMethod("get_");
            failureCountGetter.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException e1) {
            // Create a fake (failing) test for classes we couldn't find the failure count
            createFailingTest("Initialisation error", new CompilationException("Could not find getter for failure count: "+e1));
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
        final String moduleVersion = version;
        if (!carFile.exists()) {
            throw new RuntimeException(carFile + " doesn't exist");
        }
        URL outCar = carFile.toURI().toURL();
        return outCar;
    }
    
    private URLClassLoader classLoaderForModule(final String moduleName, final String[] deps, File repo)
            throws URISyntaxException, MalformedURLException {
        final URL[] carUrls = new URL[1 + (deps != null ? deps.length : 0)];
        
        URL carUrl = urlForModule(repo, moduleName);
        final File carFile = new File(carUrl.toURI());
        carUrls[0] = carUrl;
        if (deps != null) {
            for (int ii = 0; ii < deps.length; ii++) {
                carUrls[1+ii] = urlForModule(repo, deps[ii]);
            }
        }
        
        final URLClassLoader cl = new URLClassLoader(carUrls, 
                getClass().getClassLoader());
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
        
        moduleInitialiser = new Runnable(){
            @Override
            public void run() {
                // set up the runtime module system
                Metamodel.resetModuleManager();
                Metamodel.loadModule("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, CompilerTest.makeArtifactResult(new File("../ceylon.language/ide-dist/ceylon.language-"+TypeChecker.LANGUAGE_MODULE_VERSION+".car")), cl);
                Metamodel.loadModule("com.redhat.ceylon.typechecker", TypeChecker.LANGUAGE_MODULE_VERSION, CompilerTest.makeArtifactResult(new File("../ceylon-dist/dist/repo/com/redhat/ceylon/typechecker/"+TypeChecker.LANGUAGE_MODULE_VERSION+"/com.redhat.ceylon.typechecker-"+TypeChecker.LANGUAGE_MODULE_VERSION+".jar")), cl);
                Metamodel.loadModule(AbstractModelLoader.JAVA_BASE_MODULE_NAME, AbstractModelLoader.JDK_MODULE_VERSION, CompilerTest.makeArtifactResult(null), cl);
                Metamodel.loadModule(moduleName, version, CompilerTest.makeArtifactResult(carFile), cl);
                // dependencies
                for (int i = 0; i < deps.length; i++) {
                    // url is in carUrls[1+i]
                    try {
                        File car = new File(carUrls[1+i].toURI());
                        String name = deps[i];
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
        return cl;
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
        public Map<String, List<String>> loadTestMethods(final CeylonModuleRunner moduleRunner, PhasedUnits phasedUnits, String moduleName);
    }

    public static class StandardLoader implements TestLoader {
        @Override
        public Map<String, List<String>> loadTestMethods(final CeylonModuleRunner moduleRunner, final PhasedUnits phasedUnits, final String moduleName) {
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
                                        moduleRunner.createFailingTest(Decl.className(decl),
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
        synchronized(CompilerTest.RUN_LOCK){
            // the module initialiser code needs to run in a protected section because the language module Util is not loaded by
            // the test classloader but by our own classloader, which may be shared with other tests running in parallel, so if
            // we set up the module system while another thread is setting it up for other modules we're toast
            moduleInitialiser.run();
            try {
                super.run(notifier);
            } finally {
                delete(outRepo);
            }
        }
    }
    
    @Override
    public int testCount() {
        return numTests;
    }
}

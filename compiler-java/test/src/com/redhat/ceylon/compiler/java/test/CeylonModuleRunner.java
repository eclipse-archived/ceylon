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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;

import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
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
            String moduleName = !testModule.module().isEmpty() ? testModule.module() : moduleSuiteClass.getPackage().getName();
            outRepo = File.createTempFile("ceylon-module-runner", ".out.d");
            outRepo.delete();
            outRepo.mkdirs();
            
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
            if (moduleName.equals("default")) {
                scan(srcDir.getCanonicalFile(), args, srcDir.getCanonicalFile());
            } else {
                args.add(moduleName);
            }
            
            int sc = compiler.compile(args.toArray(new String[args.size()]), 
                    context);
            
            // now fetch stuff from the context
            PhasedUnits phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
            
            this.children = new LinkedHashMap<Runner, Description>();
            
            for (final CompilerError compileError : listener.get(Kind.ERROR)) {
                createFailingTest(compileError.filename, new CompilationException(compileError.toString()));
            }

            // Get a class loader for the car
            // XXX Need to use CMR if the module has dependencies
            URLClassLoader cl = classLoaderForModule(moduleName, outRepo);
            if (cl != null) {
                loadCompiledTests(srcDir, cl, phasedUnits);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    
    private void loadCompiledTests(File srcDir, URLClassLoader cl, PhasedUnits phasedUnits)
                throws InitializationError {
            Map<String, List<String>> testMethods = testLoader.loadTestMethods(this, phasedUnits);
        if (testMethods.isEmpty() && errorIfNoTests) {
            createFailingTest("No tests!", new Exception("contains no tests"));
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
                CeylonTestRunner classTestRunner = new CeylonTestRunner(testClass, testMethodNames);
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
    
    public void noTests() {
        Assert.fail("No tests found");
    }

    private URLClassLoader classLoaderForModule(final String moduleName, File repo)
            throws MalformedURLException {
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
        final URLClassLoader cl = new URLClassLoader(new URL[]{outCar}, 
                getClass().getClassLoader());
        
        moduleInitialiser = new Runnable(){
            @Override
            public void run() {
                // set up the runtime module system
                Metamodel.resetModuleManager();
                Metamodel.loadModule("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, CompilerTest.makeArtifactResult(new File("../ceylon.language/ide-dist/ceylon.language-"+TypeChecker.LANGUAGE_MODULE_VERSION+".car")), cl);
                Metamodel.loadModule(moduleName, moduleVersion, CompilerTest.makeArtifactResult(carFile), cl);
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
        public Map<String, List<String>> loadTestMethods(final CeylonModuleRunner moduleRunner, PhasedUnits phasedUnits);
    }

    public static class StandardLoader implements TestLoader {
        @Override
        public Map<String, List<String>> loadTestMethods(final CeylonModuleRunner moduleRunner, PhasedUnits phasedUnits) {
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
                            if (moduleRunner.errorIfNoTests 
                                    && (testMethods.get(testClassName) == null
                                        || testMethods.get(testClassName).isEmpty())) {
                                final Unit unit = that.getUnit();
                                moduleRunner.createFailingTest(testClassName, new Exception("contains no tests"));
                            }
                            testClassName = null;
                        }
                        for (CompilerAnnotation ca : that.getCompilerAnnotations()) {
                            Identifier identifier = ca.getIdentifier();
                            if ("test".equals(identifier.getToken().getText())) {
                                boolean added = false;
                                final com.redhat.ceylon.compiler.typechecker.model.Declaration decl = that.getDeclarationModel();
                                if (testClassName != null) {
                                    if (decl instanceof com.redhat.ceylon.compiler.typechecker.model.Method) {
                                        com.redhat.ceylon.compiler.typechecker.model.Method method = (com.redhat.ceylon.compiler.typechecker.model.Method)decl;
                                        String methodName = method.getName();
                                        if (method.getParameterLists().size() == 1
                                                && method.getParameterLists().get(0).getParameters().size() == 0) {
                                            List<String> methods = testMethods.get(testClassName);
                                            if (methods == null) {
                                                methods = new ArrayList<String>(3);
                                                testMethods.put(testClassName, methods);
                                            }
                                            methods.add(methodName);
                                            added = true;
                                        }
                                    }
                                } 
                                if (!added) {
                                    moduleRunner.createFailingTest(Decl.className(decl),
                                            new Exception("@test should only be used on methods of concrete top level classes whose name ends with 'Test'"));
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

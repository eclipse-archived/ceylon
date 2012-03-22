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
package com.redhat.ceylon.compiler.java.test.runtime;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.redhat.ceylon.compiler.java.Main;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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

    private int numTests;

    private File outRepo;
    
    
    public CeylonModuleRunner(Class<?> moduleSuiteClass) throws InitializationError {
        super(moduleSuiteClass);
        try {
            TestModule testModule = getTestModuleAnno(moduleSuiteClass);
            File srcDir = new File(testModule.srcDirectory());
            String moduleName = moduleSuiteClass.getPackage().getName();
            
            outRepo = File.createTempFile("ceylon-module-runner", ".out.d");
            outRepo.delete();
            outRepo.mkdirs();
            
            // Compile all the .ceylon files into a .car
            Main.compile(new String[]{
                    //"-verbose",
                    "-src", srcDir.getAbsolutePath(),
                    "-out", outRepo.getAbsolutePath(),
                    moduleName});

            // Get a class loader for the car
            // XXX Need to use CMR if the module has dependencies
            URLClassLoader cl = classLoaderForModule(moduleName, outRepo);
            
            Map<String, List<String>> testMethods = loadTestMethods(srcDir);
            
            this.children = new HashMap<Runner, Description>(testMethods.size());
            for (Map.Entry<String, List<String>> entry : testMethods.entrySet()) {
                final String className = entry.getKey();
                final Runner runner;
                final Description description;
                Class<?> testClass;
                try {
                    testClass = cl.loadClass(className);
                } catch (ClassNotFoundException e) {
                    testClass = null;
                }
                if (testClass == null) {
                    // Create a fake (failing) test for classes we couldn't load
                    // presumably because they had compile errors
                    description = Description.createTestDescription(getClass(), className);
                    runner = new Runner() {
                        @Override
                        public Description getDescription() {
                            return description;
                        }
                        @Override
                        public void run(RunNotifier notifier) {
                            notifier.fireTestStarted(description);
                            notifier.fireTestFailure(new Failure(description, new Exception(
                                    "Test class " + className + " didn't compile")));
                            notifier.fireTestFinished(description);
                        }
                    };
                } else {
                    description = Description.createTestDescription(testClass, className);
                    List<String> testMethodNames = entry.getValue();
                    CeylonTestRunner classTestRunner = new CeylonTestRunner(testClass, testMethodNames);
                    // Add child descriptions to my description
                    for (Method testMethod : classTestRunner.getChildren()) {
                        description.addChild(classTestRunner.describeChild(testMethod));
                    }
                    runner = classTestRunner;
                }
                numTests += runner.testCount();
                children.put(runner,
                        description);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private URLClassLoader classLoaderForModule(String moduleName, File repo)
            throws MalformedURLException {
        File moduleDir = new File(repo, moduleName.replace(".", "/"));
        String[] files = moduleDir.list();
        if (files.length != 1) {
            throw new RuntimeException();
        }
        String version = files[0];
        File carFile = new File(moduleDir, 
                version + File.separator + moduleName + "-" + version+ ".car");
        URL outCar = carFile.toURI().toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{outCar}, 
                getClass().getClassLoader());
        return cl;
    }

    private TestModule getTestModuleAnno(Class<?> moduleSuiteClass) {
        TestModule testModule = (TestModule)moduleSuiteClass.getAnnotation(TestModule.class);
        if (testModule == null) {
            throw new RuntimeException("@RunWith(" + getClass().getSimpleName() + ".class) requires @" + TestModule.class.getSimpleName() + " annotation");
        }
        return testModule;
    }

    /**
     * Loads test methods
     * @param srcDir
     * @return A map of class names to test methods contained in that class
     */
    private static Map<String, List<String>> loadTestMethods(File srcDir) {
        // Find all the top level classes/functions annotated @test
        // Unfortunately doing it like this 
        final Map<String, List<String>> testMethods = new TreeMap<String, List<String>>();
        TypeCheckerBuilder typeCheckerBuilder = new TypeCheckerBuilder();
        typeCheckerBuilder.addSrcDirectory(srcDir);
        TypeChecker typeChecker = typeCheckerBuilder.getTypeChecker();
        
        typeChecker.process();
        
        for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
            CompilationUnit cu = pu.getCompilationUnit();
            cu.visit(new Visitor() {
                @Override
                public void visit(Declaration that) {
                    for (CompilerAnnotation ca : that.getCompilerAnnotations()) {
                        Identifier identifier = ca.getIdentifier();
                        if ("test".equals(identifier.getToken().getText())) {
                            String className;
                            Scope parentScope = that.getScope().getContainer();
                            if (parentScope instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
                                className = ((com.redhat.ceylon.compiler.typechecker.model.Class)parentScope).getQualifiedNameString();
                                String methodName = that.getIdentifier().getToken().getText();
                                List<String> methods = testMethods.get(className);
                                if (methods == null) {
                                    methods = new ArrayList<String>(3);
                                    testMethods.put(className, methods);
                                }
                                methods.add(methodName);
                            }
                        }
                    }
                    that.visitChildren(this);
                }
            });
        }
        return testMethods;
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
        return numTests;
    }
}

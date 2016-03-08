package com.redhat.ceylon.compiler.java.runtime;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.compiler.java.runtime.launcher.ToolsTestRunner;
import com.redhat.ceylon.compiler.java.runtime.tools.Backend;
import com.redhat.ceylon.compiler.java.runtime.tools.CeylonToolProvider;
import com.redhat.ceylon.compiler.java.runtime.tools.CompilationListener;
import com.redhat.ceylon.compiler.java.runtime.tools.Compiler;
import com.redhat.ceylon.compiler.java.runtime.tools.CompilerOptions;
import com.redhat.ceylon.compiler.java.runtime.tools.Runner;
import com.redhat.ceylon.compiler.java.runtime.tools.RunnerOptions;
import com.redhat.ceylon.compiler.java.runtime.tools.impl.JavaRunnerImpl;

public class ToolsTest {
    
    private static final String BuildToolsBuildDir = "build/toolsTest";
    private static final String BuildToolsClassesDir = BuildToolsBuildDir + "/classes"; // we generate this one in initFlatRepo()
    private static final String BuildToolsRunnerClassesDir = BuildToolsBuildDir + "/launcher-classes"; // this one is where the IDE/build puts ToolsTestRunner
    private static final String OutputRepository = BuildToolsBuildDir + "/modules";
    private static String SystemRepo = BuildToolsBuildDir + "/repo";
    private static String FlatRepoLib = BuildToolsBuildDir + "/lib";
    private static String FlatRepoOverrides = BuildToolsBuildDir + "/overrides";
    
    public static String getCurrentPackagePath() {
        return "test/"+getCurrentPackagePathPart();
    }

    public static String getCurrentPackagePathPart() {
        return ToolsTest.class.getPackage().getName().replace('.', '/');
    }

    @Rule 
    public TestName name = new TestName();

    @BeforeClass
    public static void initFlatRepo() throws IOException{
        final File repo = new File("../dist/dist/repo");

        // create the output repo
        File outputRepo = new File(OutputRepository);
        FileUtil.delete(outputRepo);
        outputRepo.mkdirs();

        // create the flat repo with Ceylon distrib
        File systemRepo = new File(SystemRepo);
        FileUtil.delete(systemRepo);
        systemRepo.mkdirs();
        
        // copy the distrib repo as is
        final Path systemRepoPath = systemRepo.toPath();
        Files.walkFileTree(repo.toPath(), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.toString().endsWith(".jar") 
                        || file.toString().endsWith(".car")
                        || (file.toString().endsWith(".js") && file.getFileName().toString().startsWith("ceylon.language-"))){
                    File relativeFile = FileUtil.relativeFile(repo, file.toFile());
                    Path target = systemRepoPath.resolve(relativeFile.toPath());
                    Files.createDirectories(target.getParent());
                    Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        
        // create the flat repo with override
        File overridesRepo = new File(FlatRepoOverrides);
        FileUtil.delete(overridesRepo);
        overridesRepo.mkdirs();

        // add the override
        String override = "foo.user-1-module.xml";
        FileUtil.copyAll(new File(getCurrentPackagePath(), override), overridesRepo);
        
        // add the Dependency override
        String dependencyOverrides = "overrides.xml";
        FileUtil.copyAll(new File(getCurrentPackagePath(), dependencyOverrides), overridesRepo);

        // now make the flat repo with Vertx lib (mockup)
        File flatRepo2 = new File(FlatRepoLib);
        FileUtil.delete(flatRepo2);
        flatRepo2.mkdirs();

        File providerJar = compileAndJar("foo.provider", "Provider");
        try{
            File providerJarInRepo = new File(flatRepo2, "foo.provider-1.jar");
            Files.copy(providerJar.toPath(), providerJarInRepo.toPath());
            File userJar = compileAndJar("foo.user", "User", providerJarInRepo);
            try{
                Files.copy(userJar.toPath(), new File(flatRepo2, "foo.user-1.jar").toPath());
            }finally{
                userJar.delete();
            }
        }finally{
            providerJar.delete();
        }

        // we only want this class and the MainTest one, otherwise it would pollute our classpath
        File toolsTestClasses = new File(BuildToolsClassesDir);
        FileUtil.delete(toolsTestClasses);
        toolsTestClasses.mkdirs();
        File toolsTestClassesDest = new File(toolsTestClasses, getCurrentPackagePathPart());
        toolsTestClassesDest.mkdirs();
        final Path toolsTestClassesDestPath = toolsTestClassesDest.toPath();

        File dir = new File("build/test/"+getCurrentPackagePathPart());
        Files.walkFileTree(dir.toPath(), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String name = file.getFileName().toString();
                if(name.endsWith(".class")
                        && (name.startsWith("ToolsTest")
                                || name.startsWith("MainTest"))){
                    Files.copy(file, toolsTestClassesDestPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // we're ready to compile!
    }
    
    private void runInNewJVM() throws Exception{
        String java = System.getProperty("java.home")
                + File.separator + "bin" + File.separator + "java";
        
        String classpath = BuildToolsRunnerClassesDir;
        ProcessBuilder pb = new ProcessBuilder(java, "-cp", classpath, ToolsTestRunner.class.getName(), name.getMethodName());
        pb.inheritIO();
        Process process = pb.start();
        int exit = process.waitFor();
        Assert.assertEquals(0, exit);
        
        // to run in this JVM:
//        Method method;
//        try {
//            method = ToolsTest.class.getDeclaredMethod(name.getMethodName()+"_");
//            method.invoke(new ToolsTest());
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
    }
    
    @Test
    public void testJavaCompiler() throws Exception {
        runInNewJVM();
    }
    
    private void testJavaCompiler_() throws IOException { 
        Compiler javaCompiler = CeylonToolProvider.getCompiler(Backend.Java);
        testCompiler(javaCompiler, "modules.usesProvided", "1");
    }

    @Test
    public void testJavaCompilerWithDependencyOverrides() throws Exception {
        runInNewJVM();
    }
    
    @SuppressWarnings("unused")
    private void testJavaCompilerWithDependencyOverrides_() throws IOException { 
        Compiler javaCompiler = CeylonToolProvider.getCompiler(Backend.Java);
        testCompiler(javaCompiler, "modules.usesProvided", "1", true);
    }

    @Test
    public void testJavaCompilerErrors() throws Exception{
        runInNewJVM();
    }
    
    @SuppressWarnings("unused")
    private void testJavaCompilerErrors_() throws IOException {
        Compiler javaCompiler = CeylonToolProvider.getCompiler(Backend.Java);
        testCompiler(javaCompiler, "modules.errors", "1");
    }

    @Test
    public void testJavaScriptCompiler() throws Exception{
        runInNewJVM();
    }
    
    private void testJavaScriptCompiler_() throws IOException {
        Compiler javaScriptCompiler = CeylonToolProvider.getCompiler(Backend.JavaScript);
        testCompiler(javaScriptCompiler, "modules.hello", "1");
    }

    @Test
    public void testJavaScriptCompilerErrors() throws Exception{
        runInNewJVM();
    }
    
    @SuppressWarnings("unused")
    private void testJavaScriptCompilerErrors_() throws IOException {
        Compiler javaScriptCompiler = CeylonToolProvider.getCompiler(Backend.JavaScript);
        testCompiler(javaScriptCompiler, "modules.errors", "1");
    }

    @Test
    public void testJavaScriptRunner() throws Exception{
        runInNewJVM();
    }
    
    @SuppressWarnings("unused")
    private void testJavaScriptRunner_() throws IOException{
        // depend on compilation
        testJavaScriptCompiler_();
        testCompiler(CeylonToolProvider.getCompiler(Backend.JavaScript), "modules.extra", "1");

        RunnerOptions options = new RunnerOptions();
        options.setSystemRepository(SystemRepo);
        options.addUserRepository("flat:"+FlatRepoLib);
        options.addUserRepository(OutputRepository);
        options.addExtraModule("modules.extra", "1");

        Runner runner = CeylonToolProvider.getRunner(Backend.JavaScript, options, "modules.hello", "1");
        runner.run();
        runner.cleanup();
    }

    @Test
    public void testJavaRunner() throws Exception{
        runInNewJVM();
    }
    
    @SuppressWarnings("unused")
    private void testJavaRunner_() throws IOException{
        // depend on compilation
        testJavaCompiler_();
        testCompiler(CeylonToolProvider.getCompiler(Backend.Java), "modules.extra", "1");
        
        RunnerOptions options = new RunnerOptions();
        options.setSystemRepository(SystemRepo);
        options.addUserRepository("flat:"+FlatRepoLib);
        options.addUserRepository("flat:"+FlatRepoOverrides);
        options.addUserRepository(OutputRepository);
        options.addExtraModule("modules.extra", "1");
        
        Runner runner = CeylonToolProvider.getRunner(Backend.Java, options, "modules.usesProvided", "1");
        runner.run();
        // make sure we only got our two modules in the CL
        Assert.assertTrue(runner instanceof JavaRunnerImpl);
        Assert.assertEquals(2, ((JavaRunnerImpl) runner).getClassLoaderURLs().length);
        runner.cleanup();
    }

    private void testCompiler(Compiler compiler, String module, String expectedVersion) throws IOException {
        testCompiler(compiler, module, expectedVersion, false);
    }
    
    private void testCompiler(Compiler compiler, String module, String expectedVersion, boolean useDependenciesOverrides) throws IOException {
        File moduleStart = new File("test/"+module.replace('.', '/'));
        final List<File> ceylonFiles = new LinkedList<File>();
        Files.walkFileTree(moduleStart.toPath(), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                if(path.toString().endsWith(".ceylon"))
                    ceylonFiles.add(path.toFile());
                return FileVisitResult.CONTINUE;
            }
        });
        final String[] compiledModule = new String[1];
        final String[] compiledVersion = new String[1];
        final Set<String> errors = new HashSet<String>();
        CompilationListener listener = new CompilationListener(){

            @Override
            public void error(File file, long line, long column, String message) {
                String desc = FileUtil.relativeFile(file)+" at "+line+":"+column+" -> "+message;
                errors.add(desc);
                System.err.println("GOT ERROR: "+desc);
            }

            @Override
            public void warning(File file, long line, long column, String message) {
                System.err.println("GOT WARNING: "+file+" at "+line+":"+column+" -> "+message);
            }

            @Override
            public void moduleCompiled(String module, String version) {
                System.err.println("MODULE COMPILED: "+module+"/"+version);
                compiledModule[0] = module;
                compiledVersion[0] = version;
            }
        };
        CompilerOptions options = new CompilerOptions();
        options.addSourcePath(new File("test"));
        options.setSystemRepository(SystemRepo);
        options.addUserRepository("flat:"+FlatRepoLib);
        if (useDependenciesOverrides) {
            options.setOverrides(new File(FlatRepoOverrides, "overrides.xml").getAbsolutePath());
        } else {
            options.addUserRepository("flat:"+FlatRepoOverrides);
        }
        options.setOutputRepository(OutputRepository);
        options.setFiles(ceylonFiles);
        boolean ret = compiler.compile(options, listener);
        Assert.assertEquals(module, compiledModule[0]);
        Assert.assertEquals(expectedVersion, compiledVersion[0]);
        if(module.endsWith(".errors")){
            Assert.assertEquals(2, errors.size());
            Assert.assertTrue(errors.contains("test/modules/errors/run.ceylon at 1:8 -> type declaration does not exist: 'Error'"));
            Assert.assertTrue(errors.contains("test/modules/errors/run.ceylon at 1:14 -> does not definitely return: 'run' has branches which do not end in a 'return' statement"));
            Assert.assertFalse(ret);
        }else{
            Assert.assertTrue(ret);
        }
    }

    public static File compileAndJar(String javaModule, String javaClassName) throws IOException{
        return compileAndJar(javaModule, javaClassName, null);
    }
    
    public static File compileAndJar(String javaModule, String javaClassName, File addToClassPath) throws IOException{
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        
        String javaModulePath = javaModule.replace('.', '/');
        File moduleFile = new File("test/"+javaModulePath, javaClassName+".java");
        Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(moduleFile);
        File destDir = new File("build/javaModules");
        FileUtil.delete(destDir);
        destDir.mkdirs();
        List<String> options = new LinkedList<String>();
        options.add("-d");
        options.add(destDir.getPath());
        if(addToClassPath != null){
            options.add("-cp");
            options.add(addToClassPath.getPath());
        }
        CompilationTask task = compiler.getTask(null, null, null, options, null, units);
        Boolean result = task.call();
        assertTrue(result != null && result.booleanValue());

        File compiledModuleFile = new File(destDir, javaModulePath+"/"+javaClassName+".class");
        assertTrue(compiledModuleFile.isFile());
        
        return jar(compiledModuleFile, javaModulePath);
    }

    private static File jar(File sourceFile, String destDir) throws IOException {
        File jarFile = File.createTempFile("ceylonlang-testmain-", ".jar");
        try {
            JarOutputStream jar = new JarOutputStream(new FileOutputStream(jarFile));
            String dirName = destDir.isEmpty() ? sourceFile.getName() : destDir+"/"+sourceFile.getName();
            ZipEntry entry = new ZipEntry(dirName);
            jar.putNextEntry(entry);
            // relative to this file
            IOUtils.copyStream(new FileInputStream(sourceFile), jar, true, true);
        } catch (Exception ex) {
            FileUtil.deleteQuietly(jarFile);
            throw ex;
        }
        return jarFile;
    }
}

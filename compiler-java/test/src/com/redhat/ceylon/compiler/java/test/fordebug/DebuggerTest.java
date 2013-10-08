package com.redhat.ceylon.compiler.java.test.fordebug;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class DebuggerTest extends CompilerTest {
    
    protected String getClassPathAsPath(ModuleWithArtifact... modules) {
        List<File> files = getClassPathAsFiles(modules);
        files.add(new File(System.getProperty("user.home"), ".ceylon/repo/ceylon/language/1.0.0/ceylon.language-1.0.0.car"));
        return toPath(files);
    }

    private String toPath(List<File> files) {
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            sb.append(file.getAbsolutePath()).append(File.pathSeparator);
        }
        return sb.toString();
    }
    
    public Tracer tracer(String mainClass) throws Exception {
        return tracer(mainClass, getDestModuleWithArtifact());
    }
    
    /**
     * Reflectively instantiate the TracerImpl which was compiled
     * by {@link #compileTracerImpl()}.
     * @see #compileTracerImpl()
     */
    public Tracer tracer(String mainClass, ModuleWithArtifact module) throws Exception {
        Class<? extends Tracer> tracerClass = (Class)Class.forName("com.redhat.ceylon.compiler.java.test.trace.TracerImpl", false, tracerClassLoader);
        Constructor<? extends Tracer> ctor = tracerClass.getConstructor(String.class, String.class);
        return ctor.newInstance(mainClass, getClassPathAsPath(module));
    }
    
    protected void assertSameTrace(Tracer tracer, String traceFile) {
        File expectedSrcFile = new File(getPackagePath(), traceFile);
        String expectedTrace = normalizeLineEndings(readFile(expectedSrcFile)).trim();
        String actualTrace = normalizeLineEndings(tracer.getTrace()).trim();
        
        // THIS IS FOR INTERNAL USE ONLY!!!
        // Can be used to do batch updating of known correct tests
        // Uncomment only when you know what you're doing!
//        if (expectedSrc != null && compiledSrc != null && !expectedSrc.equals(compiledSrc)) {
//            writeFile(expectedSrcFile, compiledSrc);
//            expectedSrc = compiledSrc;
//        }
        
        Assert.assertEquals("Traces differ", expectedTrace, actualTrace);
    }
    
    private static ClassLoader tracerClassLoader;
    
    /**
     * Tries to find {@code tools.jar}:
     * <ol>
     * <li>If it exists within the {@code lib} directory of parent of 
     * the java installation directory, as specified by the 
     * {@code java.home} system property.</li>
     * <li>Look for {@code javac} in the {@code $PATH}. It if exists, 
     * canonicalise that path , and look in the lib directory of its 
     * grandparent directory.<li>
     * <li>Give up, and return null</li>
     * </ol> 
     * @return
     */
    private static String findToolsJar() {
        try {
            String javaHome = System.getProperty("java.home");
            if (javaHome != null) {
                File toolsJar = new File(new File(javaHome).getCanonicalFile().getParentFile(), "lib/tools.jar");
                if (toolsJar.exists()
                        && toolsJar.canRead()) {
                    return toolsJar.getCanonicalPath();
                }
            }
            String osPath = System.getenv("PATH");
            if (osPath != null) {
                String[] paths = osPath.split(java.util.regex.Pattern.quote(File.pathSeparator));
                for (String path : paths) {
                    File file = new File(path, "javac");
                    if (file.exists() && file.canExecute()) {
                        File tools = new File(file.getCanonicalFile().getParentFile().getParentFile(), "lib/tools.jar");
                        if (tools.exists() && tools.canRead()) {
                            return tools.getCanonicalPath();
                        }
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * <p>Find the jar file containing the JDI implementation:</p>
     * <ol>
     * <li>If the system property {@code jdi.jar} is set use that
     * <li>Try to find {@code tools.jar} using {@link #findToolsJar()}
     * </ol>
     */
    private static String findJdiJar() {
        String jdiJar = System.getProperty("jdi.jar");
        if (jdiJar == null) {
            jdiJar = findToolsJar();
        }
        if (jdiJar == null) {
            throw new RuntimeException("System property jdi.jar was not set, and could not find tools.jar");
        }
        File file = new File(jdiJar);
        if (!file.exists()
                || !file.canRead()) {
            throw new RuntimeException("Found jdi.jar " + file.getAbsolutePath() + " but it doesn't exist or cannot be read");
        }
        System.out.println("Found JDI in " + jdiJar);
        return jdiJar;
    }

    /**
     * <p>The tracer tests need to use JDI to execute a ceylon program under a 
     * debugger. The JDK ships with a JDI implementation, but it's in 
     * {@code tools.jar} (at least for OpenJDK). We can't just put 
     * {@code tools.jar} on the classpath because it also contains the classes 
     * for javac, but the ceylon compile contains versions of those classes 
     * too, so the compiler breaks.</p>
     * 
     * <p>Therefore we:</p>
     * <ol>
     * <li>Split the {@link Tracer} into a JDI-independent set of interfaces and 
     *     a separate implementation ({@code TracerImpl}).
     * <li>Keep the {@code TraceImpl} source files out of an Eclipse
     *     source path, so Eclipse doesn't need tools.jar on the classpath
     * <li>Compile the {@code TraceImpl} here and obtain an instance 
     *     reflectively using a ClassLoader pointing at the class we compiled.
     * </ol> 
     */
    @BeforeClass
    public static void compileTracerImpl() throws Exception {
        String srcDir = "test/trace";
        String jdiJar = findJdiJar();
        
        Javac javac = new Javac();
        javac.appendSourcePath(srcDir);
        javac.addSourceFiles(new FileCollector().addFiles(srcDir, FileCollector.JAVA_SOURCE_FILES));
        javac.appendClassPath(new FileCollector().addFiles("lib", FileCollector.JAR_FILES));
        javac.appendClassPath(jdiJar);
        javac.appendClassPath("build/classes");
        System.out.println("Compiling test classes: "+ javac);
        javac.exec();
        
        tracerClassLoader = new URLClassLoader(
                new URL[]{
                        new File(srcDir).toURI().toURL(),
                        new File(jdiJar).toURI().toURL()},
                DebuggerTest.class.getClassLoader());
    }

}

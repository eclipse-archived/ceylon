package com.redhat.ceylon.compiler.java.test.compat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class CompatTests extends CompilerTests {

    /** The location of the ceylon 1.2.0 distribution (as downloaded). */
    private static String ceylon120Dist;
    
    /** The location of the ceylon 1.2.1 distribution. */
    private static String ceylon121Dist;
    
    
    protected String get120DistPath() {
        synchronized (CompatTests.class) { 
            if (ceylon120Dist == null) {
            ceylon120Dist = System.getProperty("ceylon120Dist");
            if (ceylon120Dist == null) {
                ceylon120Dist = "/home/tom/Desktop/ceylon-1.2.0";
            }
            checkVersion(ceylon120Dist, "ceylon version 1.2.0");
            }
        }
        return ceylon120Dist;
    }
    protected String get121DistPath() {
        synchronized (CompatTests.class) { 
            if (ceylon121Dist == null) {
                ceylon121Dist = System.getProperty("ceylon121Dist");
                if (ceylon121Dist == null) {
                    ceylon121Dist = "../dist/dist/";
                }
                //checkVersion(ceylon121Dist, "ceylon version 1.2.1");
            }
        }
        return ceylon121Dist;
    }
    
    protected final String[] getAll12xDistPaths() {
        return new String[]{get120DistPath(), get121DistPath()};
    }

    protected static void checkVersion(String path, String expect) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    path+"/bin/ceylon",
                    "--version");
            pb.redirectInput(Redirect.INHERIT);
            pb.redirectError(Redirect.INHERIT);
            File out = File.createTempFile("ceylon--version", ".out");
            pb.redirectOutput(out);
            assert(0 == pb.start().waitFor());
            try (BufferedReader r = new BufferedReader(new FileReader(out))) {
                String line = r.readLine();
                if (line == null) {
                    throw new RuntimeException("No output from " + path+"/bin/ceylon --version");
                }
                if (!line.startsWith(expect)) {
                    throw new RuntimeException("Output from " + path+"/bin/ceylon --version was " +line + " not the expected "+ expect);
                }
                
            }
            out.delete();
        } catch (Exception e) {
            throw new RuntimeException("Error while checking the version of distribution " + path, e);
        }
    }
    
    /**
     * Tests we can run a module that was compiled using the 1.2.0 compiler in 
     * a 1.2.1 runtime. 
     * 
     * The source is in
     * <pre>
     *   /ceylon-compiler/test/src/com/redhat/ceylon/compiler/java/test/compat/source/source/compat120
     * </pre>
     * but the the module we execute,
     * <pre>  
     *   /ceylon-compiler/test/src/com/redhat/ceylon/compiler/java/test/compat/modules/compat120/1.0.0/compat120-1.0.0.car
     * </pre>
     * was compiled with the real 1.2.0 compiler
     */
    @Test
    public void runCompiled120CarIn121() throws Throwable {
        runInJBossModules("run", "compiled120", Arrays.asList(
                "--offline",
                "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled120::runOn121"));
    }
    
    @Test
    public void runCompiled120CarIn121FlatClasspath() throws Throwable {
        runInJBossModules("run", "compiled120", Arrays.asList(
                "--offline",
                "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled120::runOn121",
                "--flat-classpath"));
    }
    @Test
    public void runCompiled120CarIn121MainApi() throws Throwable {
        runInMainApi("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec("compiled120", "1.0.0"), "compiled120.runOn121_", 
                Collections.<String>emptyList());
    }
    
    @Test
    public void compileAndRunDepends120With121() throws Throwable {
        ProcessBuilder pb = new ProcessBuilder(
                get121DistPath()+"/bin/ceylon",
                "compile",
                "--offline",
                "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                "--src=test/src/com/redhat/ceylon/compiler/java/test/compat/source",
                "depends120");
        assert(0 == pb.inheritIO().start().waitFor());
        
        // with the default upgrade dist behaviour
        runInJBossModules("run", "depends120", Arrays.asList("--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules"));
        runInJBossModules("run", "depends120", Arrays.asList("--flat-classpath", "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules"));
        runInMainApi("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec("depends120", "1.0.0"), "depends120.run_", Collections.<String>emptyList());
    }
    
    
    protected void assertFileContainsLine(File err, String expectedLine) throws IOException, FileNotFoundException {
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(err))) {
            String line = reader.readLine();
            while(line != null) {
                System.err.println(line);
                if (line.equals(expectedLine)) {
                    found = true;
                    break;
                }
                line = reader.readLine();
            }
            if (!found) {
                Assert.fail("missing expected line");
            }
        }
    }
    
    @Test
    public void runCompiled1299CarIn121() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOn121"),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99 (invalid version?)";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled1299CarIn121Downgrade() throws Throwable {
        //File err = File.createTempFile("compattest", "err");
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOn121",
                            "--link-with-current-distribution"),
                    null, out);
            // Check it returned OK
            Assert.assertEquals(0, sc);
            String expectedLine = "Running on 1.2.1 (Irregular Apocalypse) according to language.version";
            assertFileContainsLine(out, expectedLine);
        } finally {
            out.delete();
        }
    }
    
    
    @Test
    public void runCompiled1299Depends121With121NoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends121", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends121::runOn121"),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99 (invalid version?)";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled1299Depends121With121Downgrade() throws Throwable {
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends121", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends121::runOn121",
                            "--link-with-current-distribution"),
                    null, out);
            // Check it returned an error status code
            Assert.assertEquals(0, sc);
            String expectedLine = "Running on 1.2.1 (Irregular Apocalypse) according to language.version";
            assertFileContainsLine(out, expectedLine);
        } finally {
            out.delete();
        }
    }
    
    @Test
    public void runCompiled1299Depends120With121NoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends120", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends121::runOn121"),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99 (invalid version?)";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled1299Depends120With121Downgrade() throws Throwable {
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends120", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends120::runOn121",
                            "--link-with-current-distribution"),
                    null, out);
            // Check it returned an error status code
            Assert.assertEquals(0, sc);
            String expectedLine = "Running on 1.2.1 (Irregular Apocalypse) according to language.version";
            assertFileContainsLine(out, expectedLine);
        } finally {
            out.delete();
        }
    }
    
    
    // TODO FlatClasspath downgrade
    
    @Test
    public void runCompiled1299CarIn121MainApi() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            mainApiClasspath("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                    new ModuleSpec("compiled1299", "1.0.0"), 1, err);
            assertFileContainsLine(err, "Module ceylon.language version 1.2.99 not found in the following repositories:");
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled1299With121FlatClassPathNoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList("--flat-classpath", 
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOn121"),
                    err, null);
            // Check it returned an error status code
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99";
            assertFileContainsLine(err, expectedLine);
            Assert.assertEquals(1, sc);
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled1299With121FlatClassPathDowngrade() throws Throwable {
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList("--flat-classpath", 
                            "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOn121",
                            "--link-with-current-distribution"),
                    null, out);
            // Check it returned an error status code
            assertFileContainsLine(out, "Running on 1.2.1 (Irregular Apocalypse) according to language.version");
            Assert.assertEquals(0, sc);
        } finally {
            out.delete();
        }
    }
    
    @Test
    public void classpathCompiled1299With121NoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        mainApiClasspath("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", new ModuleSpec("compiled1299", "1.0.0"), 1, err);
        assertFileContainsLine(err, "Module ceylon.language version 1.2.99 not found in the following repositories:");
    }
    
    @Test
    public void classpathCompiled1299With121Downgrade() throws Throwable {
        // TODO pass --dist-downgrade to ceylon classpath and check it works
        mainApiClasspath("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", new ModuleSpec("compiled1299", "1.0.0"), true);
    }
    
    @Test
    public void runCompiled1299With121MainApiDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            mainApiClasspath("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                    new ModuleSpec("compiled1299", "1.0.0"), 1, err);
            assertFileContainsLine(err, "Module ceylon.language version 1.2.99 not found in the following repositories:");
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled121Depends120In121() throws Throwable {
        runInJBossModules("run", "compiled121depends120", Arrays.asList(
                "--offline",
                "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled121depends120::runOn121"));
    }
    
    @Test
    public void runCompiled121Depends120In121FlatClasspath() throws Throwable {
        runInJBossModules("run", "compiled121depends120", Arrays.asList(
                "--offline",
                "--flat-classpath",
                "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled121depends120::runOn121"));
    }
    
    @Test
    public void runCompiled1299Depends120In121FlatClasspath() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            runInJBossModules("run", "compiled1299depends120", Arrays.asList(
                    "--offline",
                    "--flat-classpath",
                    "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                    "--run", "compiled1299depends120::runOn121"), err, null);
            assertFileContainsLine(err, "ceylon run: Could not find module: ceylon.language/1.2.99");
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled1299Depends120In121FlatClasspathDowngrade() throws Throwable {
        runInJBossModules("run", "compiled1299depends120", Arrays.asList(
                "--offline",
                "--flat-classpath",
                "--link-with-current-distribution",
                "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled1299depends120::runOn121"));
    }
    
    @Test
    public void runCompiled121Depends120In121MainApi() throws Throwable {
        runInMainApi("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec("compiled121depends120", "1.0.0"), "compiled121depends120.runOn121_", 
                Collections.<String>emptyList());
    }
    
    @Test
    public void bug5935() throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder(
                get121DistPath()+"/bin/ceylon",
                "doc",
                "--src=test/src",
                "com.redhat.ceylon.compiler.java.test.compat.bug5935");
        assert(0 == pb.inheritIO().start().waitFor());
    }

}

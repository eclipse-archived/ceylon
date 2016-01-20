package com.redhat.ceylon.compiler.java.test.compat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
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
                checkVersion(ceylon121Dist, "ceylon version 1.2.1");
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
    public void run120CarIn121() throws Throwable {
        runInJBossModules("run", "compat120", Arrays.asList("--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules"));
    }
    @Test
    public void run120CarIn121FlatClasspath() throws Throwable {
        runInJBossModules("run", "compat120", Arrays.asList("--flat-classpath", "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules"));
    }
    @Test
    public void run120CarIn121MainApi() throws Throwable {
        runInMainApi("test/src/com/redhat/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec("compat120", "1.0.0"), "compat120.run_", Collections.<String>emptyList());
    }
    
    protected void assertFileContainsLine(File err, String expectedLine) throws IOException, FileNotFoundException {
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(err))) {
            String line = reader.readLine();
            while(line != null) {
                
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
    public void run1299CarIn121() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compat120", 
                    Arrays.asList("--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules1299"),
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
    public void run1299CarIn121FlatClasspath() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compat120", 
                    Arrays.asList("--flat-classpath", "--rep", "test/src/com/redhat/ceylon/compiler/java/test/compat/modules1299"),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    @Test
    public void run1299CarIn121MainApi() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            mainApiClasspath("test/src/com/redhat/ceylon/compiler/java/test/compat/modules1299", 
                    new ModuleSpec("compat120", "1.0.0"), 1, err);
            assertFileContainsLine(err, "Module ceylon.language version 1.2.99 not found in the following repositories:");
        } finally {
            err.delete();
        }
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

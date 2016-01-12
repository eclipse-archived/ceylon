package com.redhat.ceylon.compiler.java.test.compat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class CompatTests extends CompilerTests {

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
    public void ceylonTargetVersion() {
        ArrayList<String> options = new ArrayList<String>(defaultOptions);
        options.add("-ceylon-target");
        options.add("1.2.0");
        compile(options, "target/CeylonTargetVersion.ceylon");
    }

}

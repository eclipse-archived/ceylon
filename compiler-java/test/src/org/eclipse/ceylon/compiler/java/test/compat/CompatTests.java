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
package org.eclipse.ceylon.compiler.java.test.compat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.Versions;
import org.eclipse.ceylon.common.tool.ToolFactory;
import org.eclipse.ceylon.common.tool.ToolLoader;
import org.eclipse.ceylon.common.tool.ToolModel;
import org.eclipse.ceylon.common.tools.CeylonTool;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.tools.TestingToolLoader;
import org.eclipse.ceylon.tools.classpath.CeylonClasspathTool;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

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
                    Assume.assumeTrue(false);
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
                    Assume.assumeTrue(false);
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
     *   /ceylon-compiler/test/src/org/eclipse/ceylon/compiler/java/test/compat/source/source/compiled120
     * </pre>
     * but the the module we execute,
     * <pre>  
     *   /ceylon-compiler/test/src/org/eclipse/ceylon/compiler/java/test/compat/modules/compiled120/1.0.0/compiled120-1.0.0.car
     * </pre>
     * was compiled with the real 1.2.0 compiler
     */
    @Test
    public void runCompiled120CarIn121() throws Throwable {
        runInJBossModules("run", "compiled120", Arrays.asList(
                "--offline",
                "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled120::runOnLatest"),
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), null, null);
    }
    
    @Test
    public void runCompiled120CarIn121FlatClasspath() throws Throwable {
        runInJBossModules("run", "compiled120", Arrays.asList(
                "--offline",
                "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled120::runOnLatest",
                "--flat-classpath"),
                Arrays.<String>asList(Versions.CEYLON_VERSION_NUMBER), null, null
                );
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled120CarIn121MainApi() throws Throwable {
        runInMainApi("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec(null, "compiled120", "1.0.0"), "compiled120.runOnLatest_", 
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), false);
    }
    
    @Test
    public void compileAndRunDepends120With121() throws Throwable {
        ProcessBuilder pb = new ProcessBuilder(
                get121DistPath()+"/bin/ceylon",
                "compile",
                "--offline",
                "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                "--src=test/src/org/eclipse/ceylon/compiler/java/test/compat/source",
                "depends120");
        assert(0 == pb.inheritIO().start().waitFor());
        
        // with the default upgrade dist behaviour
        runInJBossModules("run", "depends120", 
                Arrays.asList("--rep", "modules", "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules"),
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), null, null);
        runInJBossModules("run", "depends120", 
                Arrays.asList("--flat-classpath", "--rep", "modules", "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules"),
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), null, null);
        runInMainApi("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec(null, "depends120", "1.0.0"), "depends120.run_", Arrays.asList(Versions.CEYLON_VERSION_NUMBER), false);
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299CarIn121() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOn121"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99 (invalid version?)";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299CarIn121Downgrade() throws Throwable {
        //File err = File.createTempFile("compattest", "err");
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOnLatest",
                            "--link-with-current-distribution"),
                    Arrays.<String>asList(Versions.CEYLON_VERSION_NUMBER),
                    null, out);
            // Check it returned OK
            Assert.assertEquals(0, sc);
            String expectedLine = "Running on "+Versions.CEYLON_VERSION_NUMBER+" ("+Versions.CEYLON_VERSION_NAME+") according to language.version";
            assertFileContainsLine(out, expectedLine);
        } finally {
            out.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299Depends121With121NoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends121", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends121::runOn121"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99 (invalid version?)";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299Depends121With121Downgrade() throws Throwable {
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends121", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends121::runOnLatest",
                            "--link-with-current-distribution"),
                    Arrays.<String>asList(Versions.CEYLON_VERSION_NUMBER),
                    null, out);
            // Check it returned an error status code
            Assert.assertEquals(0, sc);
            String expectedLine = "Running on "+Versions.CEYLON_VERSION_NUMBER+" ("+Versions.CEYLON_VERSION_NAME+") according to language.version";
            assertFileContainsLine(out, expectedLine);
        } finally {
            out.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299Depends120With121NoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends120", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends121::runOn121"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(1, sc);
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99 (invalid version?)";
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299Depends120With121Downgrade() throws Throwable {
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299depends120", 
                    Arrays.asList(
                            "--offline",
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299depends120::runOnLatest",
                            "--link-with-current-distribution"),
                    Arrays.<String>asList(Versions.CEYLON_VERSION_NUMBER),
                    null, out);
            // Check it returned an error status code
            Assert.assertEquals(0, sc);
            String expectedLine = "Running on "+Versions.CEYLON_VERSION_NUMBER+" ("+Versions.CEYLON_VERSION_NAME+") according to language.version";
            assertFileContainsLine(out, expectedLine);
        } finally {
            out.delete();
        }
    }
    
    
    // TODO FlatClasspath downgrade
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299CarIn121MainApi() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            mainApiClasspath("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                    new ModuleSpec(null, "compiled1299", "1.0.0"), Collections.<ModuleSpec>emptyList(), 1, err, false);
            assertFileContainsLine(err, "Module org.eclipse.ceylon.typechecker/1.2.99 not found in the following repositories:");
        } finally {
            err.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299With121FlatClassPathNoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList("--flat-classpath", 
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOn121"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            String expectedLine = "ceylon run: Could not find module: ceylon.language/1.2.99";
            assertFileContainsLine(err, expectedLine);
            Assert.assertEquals(1, sc);
        } finally {
            err.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299With121FlatClassPathDowngrade() throws Throwable {
        File out = File.createTempFile("compattest", "out");
        try {
            int sc = runInJBossModules("run", "compiled1299", 
                    Arrays.asList("--flat-classpath", 
                            "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules",
                            "--run", "compiled1299::runOnLatest",
                            "--link-with-current-distribution"),
                    Arrays.<String>asList(Versions.CEYLON_VERSION_NUMBER),
                    null, out);
            // Check it returned an error status code
            assertFileContainsLine(out, "Running on "+Versions.CEYLON_VERSION_NUMBER+" ("+Versions.CEYLON_VERSION_NAME+") according to language.version");
            Assert.assertEquals(0, sc);
        } finally {
            out.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void classpathCompiled1299With121NoDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            mainApiClasspath("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                    new ModuleSpec(null, "compiled1299", "1.0.0"), Collections.<ModuleSpec>emptyList(), 1, err, false);
            assertFileContainsLine(err, "Module org.eclipse.ceylon.typechecker/1.2.99 not found in the following repositories:");
        } finally {
            err.delete();
        }
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void classpathCompiled1299With121Downgrade() throws Throwable {
        // TODO pass --dist-downgrade to ceylon classpath and check it works
        mainApiClasspath("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec(null, "compiled1299", "1.0.0"), Collections.<ModuleSpec>emptyList(), true, false);
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299With121MainApiDowngrade() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            mainApiClasspath("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                    new ModuleSpec(null, "compiled1299", "1.0.0"), Collections.<ModuleSpec>emptyList(), 1, err, false);
            assertFileContainsLine(err, "Module org.eclipse.ceylon.typechecker/1.2.99 not found in the following repositories:");
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void runCompiled121Depends120In121() throws Throwable {
        runInJBossModules("run", "compiled121depends120", Arrays.asList(
                "--offline",
                "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled121depends120::runOnLatest"),
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), null, null);
    }
    
    @Test
    public void runCompiled121Depends120In121FlatClasspath() throws Throwable {
        runInJBossModules("run", "compiled121depends120", Arrays.asList(
                "--offline",
                "--flat-classpath",
                "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled121depends120::runOnLatest"),
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), null, null);
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled1299Depends120In121FlatClasspath() throws Throwable {
        File err = File.createTempFile("compattest", "out");
        try {
            runInJBossModules("run", "compiled1299depends120", Arrays.asList(
                    "--offline",
                    "--flat-classpath",
                    "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                    "--run", "compiled1299depends120::runOn121"),
                    Arrays.<String>asList(),
                    err, null);
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
                "--rep", "test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                "--run", "compiled1299depends120::runOnLatest"),
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), null, null);
    }
    
    @Ignore("Removed until we can create compiled modules with alternate versions")
    @Test
    public void runCompiled121Depends120In121MainApi() throws Throwable {
        runInMainApi("test/src/org/eclipse/ceylon/compiler/java/test/compat/modules", 
                new ModuleSpec(null, "compiled121depends120", "1.0.0"), "compiled121depends120.runOnLatest_", 
                Arrays.asList(Versions.CEYLON_VERSION_NUMBER), false);
    }
    
    @Test
    public void bug5935() throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder(
                get121DistPath()+"/bin/ceylon",
                "doc",
                "--src=test/src",
                "org.eclipse.ceylon.compiler.java.test.compat.bug5935");
        assert(0 == pb.inheritIO().start().waitFor());
    }

}

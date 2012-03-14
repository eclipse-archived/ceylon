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
package com.redhat.ceylon.itest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public abstract class AntBasedTest {

    protected static final String EXEC_CEYLONC = "script.ceylonc";
    protected static final String EXEC_CEYLOND = "script.ceylond";
    protected static final String EXEC_CEYLON = "script.ceylon";
    protected static final String ARG_VERBOSE = "arg.verbose";
    protected static final String ARG_SRC = "arg.src";
    protected static final String ARG_OUT = "arg.out";
    
    protected final Method mainMethod;
    protected final File buildfile;
    private Class<?> securityManagerClass;
    private ByteArrayOutputStream redirectedStdout;
    private ByteArrayOutputStream redirectedStderr;
    private PrintStream savedStderr;
    private PrintStream savedStdout;
    private SecurityManager savedSecurityManager;
    private Properties savedProperties;
    private File out;

    public AntBasedTest(String buildfileResource) throws Exception {
        buildfile = new File(buildfileResource);
        String antHome = System.getProperty("ant.home");
        if (antHome == null) {
            throw new Exception("ant.home not set, cannot run ant integration tests");
        }
        File[] antJars = new File(antHome, "lib").listFiles(new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().matches("ant(-launcher)?\\.jar");
            }
        });
        /*
         * Create a class loader with just the jars we need to run ant
         * make the parent the bootstrap class loader, because ant is on
         * the CLASSPATH (in eclipse at least) and we want to run the tests
         * using the ant version given by ant.home system property
         * Note this uses the ant.jar from build/lib, so doesn't pick up
         * changes done in eclipse without ant recompiling them
         */
        URL[] antJarUrls = new URL[antJars.length+1];
        int ii = 0;
        for (File antJar : antJars) {
            antJarUrls[ii] = antJar.toURI().toURL();
            ii++;
        }
        antJarUrls[antJarUrls.length-1] = new File(System.getProperty("build.lib", "build/lib"), "ant.jar").toURI().toURL();
        ClassLoader antClassloader = new URLClassLoader(antJarUrls, ClassLoader.getSystemClassLoader().getParent());
        
        Class<?> antMain = antClassloader.loadClass("org.apache.tools.ant.Main");
        mainMethod = antMain.getMethod("main", String[].class);
        securityManagerClass = antClassloader.loadClass("org.apache.tools.ant.util.optional.NoExitSecurityManager");
    }
    
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") > -1;
    }
    
    @Before
    public void saveProperties() throws Exception {
        savedProperties = new Properties(System.getProperties());
        String scriptDir = System.getProperty("build.bin", "build/bin");
        String scriptExt = isWindows() ? ".bat" : "";
        System.setProperty(EXEC_CEYLONC, scriptDir + "/ceylonc" + scriptExt);
        System.setProperty(EXEC_CEYLOND, scriptDir + "/ceylond" + scriptExt);
        System.setProperty(EXEC_CEYLON, "../ceylon-runtime/dist/bin/ceylon" + scriptExt);
        System.setProperty(ARG_VERBOSE, "false");
        System.setProperty(ARG_SRC, "test-src/com/redhat/ceylon/itest");
        out = File.createTempFile("ceylon-ant-test.", ".out.d");
        out.delete();
        out.mkdirs();
        System.setProperty(ARG_OUT, out.getPath());
        System.setProperty("basedir", new File("x").getAbsoluteFile().getParent());
    }
    
    @After
    public void restoreProperties() {
        System.setProperties(savedProperties);
    }
    
    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursively(child);
            }
        }
        System.out.println("deleting: "+file.getPath());
        file.delete();
    }
    
    @After
    public void deleteOut() {
        deleteRecursively(out);
    }
    
    protected void saveGlobalState() throws Exception {
        // Redirect stdout and stderr temporarily so we can capture the ant output
        if (savedStdout != null || savedStderr != null
                || redirectedStderr != null || redirectedStdout != null) {
            throw new RuntimeException();
        }
        savedStdout = System.out;
        redirectedStdout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(redirectedStdout));
        savedStderr = System.err;
        redirectedStderr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(redirectedStdout));
        
        savedSecurityManager = System.getSecurityManager();
        System.setSecurityManager((SecurityManager)securityManagerClass.newInstance());
    }
    
    protected AntResult restoreGlobalState(int sc) {
        System.setSecurityManager(savedSecurityManager);
        
        System.err.flush();
        System.setErr(savedStderr);
        savedStderr = null;
        String stderr = new String(redirectedStderr.toByteArray());
        System.err.print(stderr);
        redirectedStderr = null;
        
        System.out.flush();
        System.setOut(savedStdout);
        savedStdout = null;
        String stdout = new String(redirectedStdout.toByteArray());
        System.out.print(stdout);
        redirectedStdout = null;
        
        return new AntResult(sc, stdout, stderr, this.out);
    }
    
    protected AntResult ant(String goal) throws Exception {
        String[] antArgs = new String[]{
                "-buildfile", buildfile.getPath(),
                "-verbose",
                goal};
        
        /*
         * Don't let ant call System.exit(): Use a security manager to prevent 
         * this, and also capture the argument that was passed, to make 
         * assertions against
         */
        int sc = 0;
        AntResult result;
        try {
            saveGlobalState();
            mainMethod.invoke(null, new Object[]{antArgs});
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            try {
                sc = (Integer)cause.getClass().getMethod("getStatus").invoke(cause);
            } catch (Exception e2) {
                throw e;
            }
        }finally {
            result = restoreGlobalState(sc);
        }
        return result;
    }
    
    public static class AntResult {
        private int statusCode;
        private String stdout;
        private String stderr;
        private File out;
        private AntResult(int statusCode,
                String stdout,
                String stderr,
                File out) {
            this.statusCode = statusCode;
            this.stdout = stdout;
            this.stderr = stderr;
            this.out = out;
        }
        public int getStatusCode() {
            return statusCode;
        }
        public String getStdout() {
            return stdout;
        }
        public String getStderr() {
            return stderr;
        }
        public File getOut() {
            return out;
        }
    }
    
    protected final void assertContains(String string, String sought) {
        Assert.assertTrue(string.contains(sought));
    }
    
    protected final void assertContainsMatch(String string, String pattern) {
        Assert.assertTrue(Pattern.compile(pattern).matcher(string).find());   
    }
    
    protected final void assertContainsMatch(String string, Pattern pattern) {
        Assert.assertTrue(pattern.matcher(string).find());   
    }
    
    protected final void assertNotContains(String string, String sought) {
        Assert.assertFalse(string.contains(sought));
    }
    
    protected final void assertNotContainsMatch(String string, String pattern) {
        Assert.assertFalse(Pattern.compile(pattern).matcher(string).find());   
    }
    
    protected final void assertNotContainsMatch(String string, Pattern pattern) {
        Assert.assertFalse(pattern.matcher(string).find());   
    }
}

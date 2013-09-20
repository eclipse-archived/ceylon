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
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class AntBasedTest {

    protected static final String EXEC_CEYLONC = "script.ceylonc";
    protected static final String EXEC_CEYLOND = "script.ceylond";
    protected static final String EXEC_CEYLON = "script.ceylon";
    protected static final String EXEC_CEY = "script.cey";
    protected static final String ARG_VERBOSE = "arg.verbose";
    protected static final String ARG_SRC = "arg.src";
    protected static final String ARG_OUT = "arg.out";
    
    protected Method mainMethod;
    protected final File originalBuildfile;
    protected File actualBuildFile;
    private Class<?> securityManagerClass;
    private ByteArrayOutputStream redirectedStdout;
    private ByteArrayOutputStream redirectedStderr;
    private PrintStream savedStderr;
    private PrintStream savedStdout;
    private SecurityManager savedSecurityManager;
    private Properties savedProperties;
    private File out;
    private final URL[] antJarUrls;

    public AntBasedTest(String buildfileResource) throws Exception {
        originalBuildfile = new File(buildfileResource);
        String antHome = System.getProperty("ant.home");
        if (antHome == null) {
            throw new Exception("ant.home not set, cannot run ant integration tests\n"
                    +"Find the path to you ant binary: `which ant` (mine is /usr/bin/ant)\n"
                    +"Check that it's not a symlink: `ls -lsa /usr/bin/ant` (mine points to /usr/share/ant/bin/ant)\n"
                    +"Remove 'bin/ant' from the end (mine is /usr/share/ant)\n"
                    +"Set it in Eclipse's Ant run configuration for these tests: \n"
                    +" Right-click on AntBasedTest.java > Run as > Run configurations...\n"
                    +" Arguments > VM Arguments: -Dant.home=/usr/share/ant");

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
        antJarUrls = new URL[antJars.length+1];
        int ii = 0;
        for (File antJar : antJars) {
            antJarUrls[ii] = antJar.toURI().toURL();
            ii++;
        }
        antJarUrls[antJarUrls.length-1] = getCeylonAntJar().toURI().toURL();
    }
    
    private void initMain() throws Exception {
        ClassLoader antClassloader = new URLClassLoader(antJarUrls, ClassLoader.getSystemClassLoader().getParent());
        
        Class<?> antMain = antClassloader.loadClass("org.apache.tools.ant.Main");
        mainMethod = antMain.getMethod("main", String[].class);
        securityManagerClass = antClassloader.loadClass("org.apache.tools.ant.util.optional.NoExitSecurityManager");
    }

    private void search(File file, List<File> matches) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                search(child, matches);
            }
        } else {
            if (file.getName().contains("-ant")
                    && file.getName().endsWith(".jar")) {
                matches.add(file);
            }
        }
    }
    
    private File getCeylonAntJar() throws Exception {
        String property = System.getProperty("ceylon.ant.lib");
        if (property != null) {
            File possibleResult = new File(property);
            if (possibleResult.exists()) {
                return possibleResult;
            } else {
                throw new Exception("File specified by ceylon.ant.lib system property " + possibleResult.getAbsolutePath() + " does not exist");
            }
        }
        File dist = new File(System.getProperty("build.dist", "build/lib"));
        ArrayList<File> matches = new ArrayList<File>(1);
        search(dist, matches);
        if (matches.size() == 0) {
            throw new Exception("Could not *ant*.jar file below " + dist.getAbsolutePath());
        } else if (matches.size() > 1) {
            throw new Exception("Found several *ant*.jar files below " + dist.getAbsolutePath());
        }
        return matches.get(0);
    }
    
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") > -1;
    }
    
    @Before
    public void rewriteBuildFile() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document document = parser.parse(this.originalBuildfile);
        if (isWindows()) {
            /*
             * On windows you can't just use the <exec> task on a .bat file
             * you have to exec "cmd -c foo.bat". To avoid a maintainance 
             * nightmare in the test build files, we just rewrite the build 
             * files on the fly.
             */
            XPathFactory xpfactory = XPathFactory.newInstance();
            NodeList execTasks = (NodeList)xpfactory.newXPath().evaluate("//exec", document, XPathConstants.NODESET);
            for (int ii = 0; ii < execTasks.getLength(); ii++) {
                Element exec = (Element)execTasks.item(ii);
                
                String executable = exec.getAttribute("executable");
                exec.setAttribute("executable", "cmd");
                
                Element argExecutable = document.createElement("arg");
                argExecutable.setAttribute("value", executable);
                exec.insertBefore(argExecutable,  exec.getFirstChild());
            
                Element argOptiopnC = document.createElement("arg");
                argOptiopnC.setAttribute("value", "/c");
                exec.insertBefore(argOptiopnC,  exec.getFirstChild());
            }
        }
        actualBuildFile = File.createTempFile("ceylon-ant-test.", "build.xml");
        TransformerFactory.newInstance().newTransformer().transform(
                new DOMSource(document), 
                new StreamResult(actualBuildFile));
    
    }
    
    @Before
    public void saveProperties() throws Exception {
        savedProperties = new Properties(System.getProperties());
        String scriptDir = System.getProperty("build.bin", "build/bin");
        String scriptExt = isWindows() ? ".bat" : "";
        System.setProperty(EXEC_CEYLONC, scriptDir + "/ceylonc" + scriptExt);
        System.setProperty(EXEC_CEYLOND, scriptDir + "/ceylond" + scriptExt);
        System.setProperty(EXEC_CEYLON, "../ceylon-runtime/build/dist/bin/ceylon" + scriptExt);
        System.setProperty(EXEC_CEY, "../ceylon-dist/dist/bin/ceylon" + scriptExt);
        System.setProperty(ARG_VERBOSE, "false");
        System.setProperty(ARG_SRC, "test/src/com/redhat/ceylon/itest");
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
        deleteRecursively(actualBuildFile);    
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
        initMain();
        String[] antArgs = new String[]{
                "-buildfile", actualBuildFile.getPath(),
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
    
    protected final void assertZipEntryNewer(File zipFile, 
            String olderEntry, String newerEntry) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<? extends ZipEntry> entries = zip.entries();
        Long newer = null;
        Long older = null;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equals(newerEntry)) {
                newer = entry.getTime();
            }
            if (entry.getName().equals(olderEntry)) {
                older = entry.getTime();
            }
        }
        if (newer != null
                && older != null) {
            if (newer <= older) {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
                Assert.fail("Entry " + newerEntry + 
                        " (mtime: " + fmt.format(new Date(newer)) + ") " +
                		"was not newer than " + olderEntry + 
                		" (mtime: " +fmt.format(new Date(older)) + ") " +
        				"in archive " + zipFile);
            }
        } else if (newer != null) {
            Assert.fail("Couldn't find entry " + olderEntry + " in archive " + zipFile);
        } else if (older != null) {
            Assert.fail("Couldn't find entry " + newerEntry + " in archive " + zipFile);
        } else {
            Assert.fail("Couldn't find entries " + newerEntry + " and " + olderEntry + " in archive " + zipFile);
        }
    }
}

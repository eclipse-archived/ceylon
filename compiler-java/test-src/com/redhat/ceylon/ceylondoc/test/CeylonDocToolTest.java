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
package com.redhat.ceylon.ceylondoc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.ceylondoc.CeylonDocTool;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.sun.source.util.JavacTask;

public class CeylonDocToolTest {

    private CeylonDocTool tool(String pathname, String testName, String moduleName, String... repositories)
            throws IOException {
        CeylonDocTool tool = new CeylonDocTool(Arrays.asList(new File(pathname)), 
                Arrays.asList(repositories), 
                Arrays.asList(moduleName));
        File dir = new File(System.getProperty("java.io.tmpdir"), "CeylonDocToolTest/" + testName);
        if (dir.exists()) {
            rm(dir);
        }
        tool.setDestDir(dir.getAbsolutePath());
        return tool;
    }

    private void rm(File dir) throws IOException {
        for (File child : dir.listFiles()) {
            if (child.isDirectory()) {
                rm(child);
            } else {
                if (!child.delete()) {
                    throw new IOException("Couldn't delete " + child);
                }
            }
        }
        if (!dir.delete()) {
            throw new IOException("Couldn't delete " + dir);
        }
    }

    protected void assertFileExists(File destDir, String path) {
        File file = new File(destDir, path);
        Assert.assertTrue(file + " doesn't exist", file.exists());
        Assert.assertTrue(file + " exists but is not a file", file.isFile());
    }
    
    protected void assertFileNotExists(File destDir, String path) {
        File file = new File(destDir, path);
        Assert.assertFalse(file + " does exist", file.exists());
    }
    
    protected void assertDirectoryExists(File destDir, String path) {
        File file = new File(destDir, path);
        Assert.assertTrue(file + " doesn't exist", file.exists());
        Assert.assertTrue(file + " exist but isn't a directory", file.isDirectory());
    }
    
    static interface GrepAsserter {

        void makeAssertions(Matcher matcher);

    }
    
    static GrepAsserter AT_LEAST_ONE_MATCH = new GrepAsserter() {

        @Override
        public void makeAssertions(Matcher matcher) {
            Assert.assertTrue("Zero matches for " + matcher.pattern().pattern(), matcher.find());
        }
        
    };
    
    static GrepAsserter NO_MATCHES = new GrepAsserter() {

        @Override
        public void makeAssertions(Matcher matcher) {
            boolean found = matcher.find();
            if (found) {
                Assert.fail("Unexpected match for " + matcher.pattern().pattern() + ": " + matcher.group(0));
            }
        }
        
    };
    
    protected void assertMatchInFile(File destDir, String path, Pattern pattern, GrepAsserter asserter) throws IOException {
        assertFileExists(destDir, path);
        Charset charset = Charset.forName("UTF-8");
        
        File file = new File(destDir, path);
        FileInputStream stream = new FileInputStream(file);
        try  {
            FileChannel channel = stream.getChannel();
            try {
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                CharBuffer chars = charset.decode(map);
                Matcher matcher = pattern.matcher(chars);
                asserter.makeAssertions(matcher);
            } finally {
                channel.close();
            }
        } finally {
            stream.close();
        }
    }
    
    protected void assertMatchInFile(File destDir, String path, Pattern pattern) throws IOException {
        assertMatchInFile(destDir, path, pattern, AT_LEAST_ONE_MATCH);
    }
    
    protected void assertNoMatchInFile(File destDir, String path, Pattern pattern) throws IOException {
        assertMatchInFile(destDir, path, pattern, NO_MATCHES);
    }
    
    @Test
    public void moduleA() throws IOException {
        String pathname = "test-src/com/redhat/ceylon/ceylondoc/test/modules";
        String testName = "moduleA";
        CeylonDocTool tool = tool(pathname, testName, "a");
        tool.setShowPrivate(false);
        tool.setOmitSource(false);
        tool.makeDoc();
        
        Module module = new Module();
        module.setName(Arrays.asList("a"));
        module.setVersion("3.1.4");
        File destDir = tool.getOutputFolder(module);
        assertDirectoryExists(destDir, ".resources");
        assertFileExists(destDir, ".resources/index.js");
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "search.html");
        assertFileExists(destDir, "interface_Types.html");
        assertFileNotExists(destDir, "class_PrivateClass.html");
        assertFileExists(destDir, "class_SharedClass.html");
        assertFileExists(destDir, "class_CaseSensitive.html");
        assertFileNotExists(destDir, "class_caseSensitive.html");
        assertFileExists(destDir, "object_caseSensitive.html");
        
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("This is a <strong>test</strong> module"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("This is a <strong>test</strong> package"));
        
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='sharedAttribute'.*?>"));
        assertNoMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='privateAttribute'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='sharedGetter'.*?>"));
        assertNoMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='privateGetter'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='sharedMethod'.*?>"));
        assertNoMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='privateMethod'.*?>"));
    }
    
    @Test
    public void moduleAWithPrivate() throws IOException {
        String pathname = "test-src/com/redhat/ceylon/ceylondoc/test/modules";
        String testName = "moduleAWithPrivate";
        CeylonDocTool tool = tool(pathname, testName, "a");
        tool.setShowPrivate(true);
        tool.setOmitSource(false);
        tool.makeDoc();
        
        Module module = new Module();
        module.setName(Arrays.asList("a"));
        module.setVersion("3.1.4");
        File destDir = tool.getOutputFolder(module);
        assertDirectoryExists(destDir, ".resources");
        assertFileExists(destDir, ".resources/index.js");
        assertFileExists(destDir, "index.html");
        assertFileExists(destDir, "search.html");
        assertFileExists(destDir, "interface_Types.html");
        assertFileExists(destDir, "class_PrivateClass.html");
        assertFileExists(destDir, "class_SharedClass.html");
        assertFileExists(destDir, "class_CaseSensitive.html");
        assertFileNotExists(destDir, "class_caseSensitive.html");
        assertFileExists(destDir, "object_caseSensitive.html");
        
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("This is a <strong>test</strong> module"));
        assertMatchInFile(destDir, "index.html", 
                Pattern.compile("This is a <strong>test</strong> package"));
        
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='sharedAttribute'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='privateAttribute'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='sharedGetter'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='privateGetter'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='sharedMethod'.*?>"));
        assertMatchInFile(destDir, "class_SharedClass.html", 
                Pattern.compile("<.*? id='privateMethod'.*?>"));
    }
    
    @Test
    public void dependentOnBinaryModule() throws IOException {
        String pathname = "test-src/com/redhat/ceylon/ceylondoc/test/modules";
        String testName = "dependentOnBinaryModule";
        
        // compile the b module
        compile(pathname, "b");
        
        CeylonDocTool tool = tool(pathname+"/dependency", testName, "c", "build/ceylon-cars");
        tool.makeDoc();
    }

    private void compile(String pathname, String string) throws IOException {
        CeyloncTool compiler = new CeyloncTool();
        List<String> options = Arrays.asList("-src", pathname, "-out", "build/ceylon-cars");
        JavacTask task = compiler.getTask(null, null, null, options, Arrays.asList("b"), null);
        Boolean ret = task.call();
        Assert.assertEquals("Compilation failed", Boolean.TRUE, ret);
    }
}

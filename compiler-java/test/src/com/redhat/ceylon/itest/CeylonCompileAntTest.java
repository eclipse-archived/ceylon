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

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class CeylonCompileAntTest extends AntBasedTest {

    public CeylonCompileAntTest() throws Exception {
        super("test/src/com/redhat/ceylon/itest/ceylon-compile-ant.xml");
    }
    
    @Test
    public void testCompileModuleFooBadExecutable() throws Exception {
        System.setProperty("script.cey", "some/nonsense/path");
        AntResult result = ant("foo-alone");
        Assert.assertEquals(1, result.getStatusCode());
        assertContains(result.getStdout(), "Failed to find 'ceylon' executable in some/nonsense/path");
    }
    
    @Test
    public void testCompileModuleFoo() throws Exception {
        AntResult result = ant("foo-alone");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src.sha1").exists());
        Assert.assertEquals(1, new File(result.getOut(), "com/example").list().length);
        
        assertNotContainsMatch(result.getStdout(), Pattern.compile("^  \\[ceylon-compile\\] Model tree for .*?com.example.foo.foo\\.ceylon$", Pattern.MULTILINE));
    }
    
    @Test
    public void testCompileModuleFooTwice() throws Exception {
        AntResult result = ant("foo-alone");
        Assert.assertEquals(0, result.getStatusCode());
        File car = new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car");
        Assert.assertTrue(car.exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src.sha1").exists());
        Assert.assertEquals(1, new File(result.getOut(), "com/example").list().length);
        assertNotContainsMatch(result.getStdout(), Pattern.compile("^  \\[ceylon-compile\\] Model tree for .*?com.example.foo.foo\\.ceylon$", Pattern.MULTILINE));
        assertNotContains(result.getStdout(), "[ceylon-compile] No need to compile com.example.foo, it's up to date");
        assertNotContains(result.getStdout(), "[ceylon-compile] Everything's up to date");
        final long lastModified = car.lastModified();
        
        result = ant("foo-alone");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "[ceylon-compile] No need to compile com.example.foo, it's up to date");
        assertContains(result.getStdout(), "[ceylon-compile] Everything's up to date");
        Assert.assertEquals(lastModified, car.lastModified());
    }
    
    @Test
    public void testCompileModuleFooVerbosely() throws Exception {
        System.setProperty("arg.verbose", "true");
        
        AntResult result = ant("foo-alone");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src.sha1").exists());
        Assert.assertEquals(1, new File(result.getOut(), "com/example").list().length);
        
        assertContainsMatch(result.getStdout(), Pattern.compile("^\\[ceylon-compile\\] Model tree for .*?com/example/foo/a/foo\\.ceylon\\]$", Pattern.MULTILINE));
    }
    
    @Test
    public void testCompileModuleFooVerboseFlag() throws Exception {
        System.setProperty("arg.verbose", "benchmark");
        
        AntResult result = ant("foo-alone");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src.sha1").exists());
        Assert.assertEquals(1, new File(result.getOut(), "com/example").list().length);
        
        assertContains(result.getStdout(), "[ceylon-compile] [0ms] Program start");
    }
    
    @Test
    public void testNoModuleOrFiles() throws Exception {
        AntResult result = ant("no-module-or-files");
        Assert.assertEquals(1, result.getStatusCode());
        assertContains(result.getStdout(), "You must specify a <module>, <moduleset> and/or <files>");
    }
    
    @Test
    public void testCompileFileFromFoo() throws Exception {
        AntResult result = ant("foo-file");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car").exists());
    }
    
    @Test
    public void testCompileFileFromFooTwice() throws Exception {
        AntResult result = ant("foo-file");
        Assert.assertEquals(0, result.getStatusCode());
        assertNotContains(result.getStdout(), "[ceylon-compile] No need to compile com.example.foo, it's up to date");
        assertNotContains(result.getStdout(), "[ceylon-compile] Everything's up to date");
        File car = new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car");
        Assert.assertTrue(car.exists());
        long lastModified = car.lastModified();
        
        result = ant("foo-file-mtime");
        Assert.assertEquals(0, result.getStatusCode());
        assertContainsMatch(result.getStdout(), Pattern.compile("^\\[ceylon-compile\\] No need to compile .*?/test/src/com/redhat/ceylon/itest/com/example/foo/a/foo.ceylon, it's up to date$", Pattern.MULTILINE));
        assertContains(result.getStdout(), "[ceylon-compile] Everything's up to date");
        Assert.assertEquals(lastModified, car.lastModified());
    }
    
    @Test
    public void testCompileModuleFooClasspath() throws Exception {
        // TODO Test with a <ceylon-compile classpath="">
    }
    
    @Test
    public void testCompileModuleFooClasspathref() throws Exception {
        // TODO Test with a <ceylon-compile classpathref="">
    }
    
    @Test
    public void testCompileModuleFooRep() throws Exception {
        // TODO Test with a <ceylon-compile><rep/>
    }
    
    @Test
    public void testCompileModuleFooThenBar() throws Exception {
        // First compile foo (since it's a dependency), so it's in the output repo
        AntResult result = ant("foo-alone");
        Assert.assertEquals(0, result.getStatusCode());
        
        result = ant("bar-alone");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.src.sha1").exists());
    }
    
    @Test
    public void testCompileModuleFooAndFileFromBar() throws Exception {
        // First compile foo and bar
        AntResult result = ant("foo-and-bar");
        Assert.assertEquals(0, result.getStatusCode());
        
        // Now compile foo and a single file (bar.ceylon containing class C)
        // from bar
        result = ant("foo-and-file-from-bar");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.src.sha1").exists());
        assertZipEntryNewer(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car"),
                "com/example/bar/b/m_.class",
                "com/example/bar/b/C.class");
    }
    
    @Test
    public void testCompileModuleFooAndBarTogether() throws Exception {
        AntResult result = ant("foo-and-bar");
        Assert.assertEquals(0, result.getStatusCode());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/foo/1.0/com.example.foo-1.0.src.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.car.sha1").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.src").exists());
        Assert.assertTrue(new File(result.getOut(), "com/example/bar/1.0/com.example.bar-1.0.src.sha1").exists());
    }
    
    @Test
    public void testFailOnError() throws Exception {
        AntResult result = ant("bad-fail-on-error");
        Assert.assertEquals(0, result.getStatusCode());
        assertContainsMatch(result.getStdout(), "I'll carry on regardless");
        assertContainsMatch(result.getStdout(), "continued: true");
    }

}

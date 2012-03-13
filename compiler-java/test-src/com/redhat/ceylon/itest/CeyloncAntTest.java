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

public class CeyloncAntTest extends AntBasedTest {

    public CeyloncAntTest() throws Exception {
        super("test-src/com/redhat/ceylon/itest/ceylonc-ant.xml");
    }
    
    @Test
    public void testCompileModuleFooBadExecutable() throws Exception {
        System.setProperty("script.ceylonc", "some/nonsense/path");
        AntResult result = ant("foo-alone");
        Assert.assertEquals(1, result.getStatusCode());
        assertContains(result.getStdout(), "Failed to find 'ceylonc' executable in some/nonsense/path");
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
        
        assertNotContainsMatch(result.getStdout(), Pattern.compile("^  \\[ceylonc\\] Model tree for .*?com.example.foo.foo\\.ceylon$", Pattern.MULTILINE));
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
        
        assertContainsMatch(result.getStdout(), Pattern.compile("^  \\[ceylonc\\] Model tree for .*?com.example.foo.a.foo\\.ceylon$", Pattern.MULTILINE));
    }
    
    @Test
    public void testCompileFileFromFoo() throws Exception {
        AntResult result = ant("foo-file");
        Assert.assertEquals(0, result.getStatusCode());
    }
    
    @Test
    public void testCompileModuleFooClasspath() throws Exception {
        // TODO Test with a <ceylonc classpath="">
    }
    
    @Test
    public void testCompileModuleFooClasspathref() throws Exception {
        // TODO Test with a <ceylonc classpathref="">
    }
    
    @Test
    public void testCompileModuleFooRep() throws Exception {
        // TODO Test with a <ceylonc><rep/>
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

}

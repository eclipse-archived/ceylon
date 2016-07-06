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
package com.redhat.ceylon.compiler.java.test.issues;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;


public class IssuesTests_6000_6499 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues.bug62xx", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-6000-6499";
    }

    @Test
    public void testBug6005() throws Throwable {
    	compile("bug60xx/bug6005/run.ceylon");
    	runInJBossModules("run", "com.redhat.ceylon.compiler.java.test.issues.bug60xx.bug6005/1", 
    			Arrays.asList("--flat-classpath"));
    	runInJBossModules("run", "com.redhat.ceylon.compiler.java.test.issues.bug60xx.bug6005/1", 
    			Arrays.asList("--overrides", getPackagePath()+"/bug60xx/bug6005/overrides.xml"));
    }
    
    @Test
    public void testBug6037() throws Throwable {
        ProcessBuilder pb = new ProcessBuilder("ceylon", "compile", "--src=test/src", "com.redhat.ceylon.compiler.java.test.issues.bug60xx.bug6037");
        pb.inheritIO();
        Process p = pb.start();
        //compile("bug60xx/bug6037/run.ceylon");
        Assert.assertEquals(0, p.waitFor());
    }
    
    @Test
    public void testBug6078() {
        compareWithJavaSource("bug60xx/Bug6078");
    }
    
    @Test
    public void testBug6096() {
        compareWithJavaSource("bug60xx/Bug6096");
    }
    
    @Test
    public void testBug6129() {
        compareWithJavaSource("bug61xx/Bug6129");
    }


    @Test
    public void testBug6153() throws Throwable {
        compile("bug61xx/Bug6153.ceylon");
        File err = File.createTempFile("compattest", "out");
        try {
            String expectedLine = "ceylon run: HAH";

            // normal
            int sc = runInJBossModules("run", "com.redhat.ceylon.compiler.java.test.issues/1", 
                    Arrays.asList("--run", "com.redhat.ceylon.compiler.java.test.issues.bug61xx::bug6153"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(2, sc);
            assertFileContainsLine(err, expectedLine);

            // flat
            sc = runInJBossModules("run", "com.redhat.ceylon.compiler.java.test.issues/1", 
                    Arrays.asList("--flat-classpath", "--run", "com.redhat.ceylon.compiler.java.test.issues.bug61xx::bug6153"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(2, sc);
            assertFileContainsLine(err, expectedLine);
        } finally {
            err.delete();
        }
    }
    
    @Test
    public void testBug6174() {
        compareWithJavaSource("bug61xx/Bug6174");
    }
    
    @Test
    public void testBug6176() {
        compareWithJavaSource("bug61xx/Bug6176");
    }
    
    @Test
    public void testBug6178() {
        compareWithJavaSource("bug61xx/Bug6178");
    }
    
    @Test
    public void testBug6180() {
        compareWithJavaSource("bug61xx/Bug6180");
    }
    
    @Test
    public void testBug6207() {
        compile("bug62xx/Bug6207.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug62xx.bug6207");
    }
    
    @Test
    public void testBug6213A() {
        compareWithJavaSource("bug62xx/Bug6213A");
    }
    @Test
    public void testBug6213B() {
        compareWithJavaSource("bug62xx/Bug6213B");
    }
    
    @Test
    public void testBug6215() {
        compareWithJavaSource("bug62xx/Bug6215");
    }
    
    @Test
    public void testBug6231() {
        
        compile("bug62xx/bug6231/static/run.ceylon");
        
        ArrayList<String> options = new ArrayList<String>(defaultOptions);
        options.add("-res");
        options.add("test/src");
        
        compileAndRun(options,
                "com.redhat.ceylon.compiler.java.test.issues.bug62xx.bug6231.$static.run",
                "bug62xx/bug6231/static/run.ceylon");
    }
    
    @Test
    public void testBug6234() {
        assertErrors("bug62xx/Bug6234", 
                new CompilerError(15, "forward declaration may not occur in declaration section: 'count'"));
    }
    
    @Test
    public void testBug6239() {
        compareWithJavaSource("bug62xx/Bug6239");
    }
    
    @Test
    public void testBug6253() {
        compile("bug62xx/Bug6253.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug62xx.bug6253run");
    }
    
    @Test
    public void testBug6267() {
        compareWithJavaSource("bug62xx/Bug6267");
    }
    
    @Test
    public void testBug6272() {
        assertErrors("bug62xx/Bug6272",
                new CompilerError(8, "argument of unknown type assigned to inferred type parameter: 'Item' of 'HashMap'"),
                new CompilerError(8, "function or value does not exist: 'bar'"));
    }
    
    @Test
    public void testBug6277() {
        compareWithJavaSource("bug62xx/Bug6277");
    }
    
    @Test
    public void testBug6283() {
        compareWithJavaSource("bug62xx/Bug6283");
    }

    @Test
    public void testBug6344() {
        compareWithJavaSource("bug63xx/Bug6344");
    }
}

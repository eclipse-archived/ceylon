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
package org.eclipse.ceylon.compiler.java.test.issues;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.test.ErrorCollector;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;


public class IssuesTests_6000_6499 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-6000-6499";
    }

    @Test
    public void testBug6005() throws Throwable {
        compile("bug60xx/bug6005/run.ceylon");
        runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.issues.bug60xx.bug6005/1", 
                Arrays.asList("--flat-classpath"));
        runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.issues.bug60xx.bug6005/1", 
                Arrays.asList("--overrides", getPackagePath()+"/bug60xx/bug6005/overrides.xml"));
    }
    
    @Test
    public void testBug6037() throws Throwable {
        ProcessBuilder pb = new ProcessBuilder(script(), "compile", "--src=test/src", "org.eclipse.ceylon.compiler.java.test.issues.bug60xx.bug6037");
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
            int sc = runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.issues/1", 
                    Arrays.asList("--run", "org.eclipse.ceylon.compiler.java.test.issues.bug61xx::bug6153"),
                    Arrays.<String>asList(),
                    err, null);
            // Check it returned an error status code
            Assert.assertEquals(2, sc);
            assertFileContainsLine(err, expectedLine);

            // flat
            sc = runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.issues/1", 
                    Arrays.asList("--flat-classpath", "--run", "org.eclipse.ceylon.compiler.java.test.issues.bug61xx::bug6153"),
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
        run("org.eclipse.ceylon.compiler.java.test.issues.bug62xx.bug6207",
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues.bug62xx", "1"));
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
                "org.eclipse.ceylon.compiler.java.test.issues.bug62xx.bug6231.$static.run",
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues.bug62xx", "1"),
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
        run("org.eclipse.ceylon.compiler.java.test.issues.bug62xx.bug6253run",
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.issues.bug62xx", "1"));
    }
    
    @Test
    public void testBug6267() {
        compareWithJavaSource("bug62xx/Bug6267");
    }
    
    @Test
    public void testBug6272() {
        assertErrors("bug62xx/Bug6272",
                new CompilerError(8, "argument of unknown type assigned to inferred type parameter: 'Item' of 'HashMap'"),
                new CompilerError(8, "function or value is not defined: 'bar' might be misspelled or is not imported"));
    }
    
    @Test
    public void testBug6277() {
        compareWithJavaSource("bug62xx/Bug6277");
    }
    
    @Test
    public void testBug6287() {
        assertErrors("bug62xx/Bug6287",
                new CompilerError(1, "imported declaration not found: 'string' might be misspelled or does not belong to the package 'ceylon.language' (did you mean 'stringify'?)"),
                new CompilerError(4, "type is not defined: 'string' might be misspelled or is not imported"),
                new CompilerError(5, "function or value is not defined: 'string' might be misspelled or is not imported"));
    }
    
    @Test
    public void testBug6283() {
        compareWithJavaSource("bug62xx/Bug6283");
    }

    @Test
    public void testBug6304() {
        compareWithJavaSource("bug63xx/Bug6304");
    }

    @Test
    public void testBug6310() {
        compile("bug63xx/bug6310/Bug6310.ceylon", "bug63xx/bug6310/module.ceylon", "bug63xx/bug6310/Bug6310Java.java");
    }

    @Test
    public void testBug6323() {
        compile("bug63xx/Bug6323Java.java");
        compareWithJavaSource("bug63xx/Bug6323");
    }

    @Test
    public void testBug6324() {
        compareWithJavaSource("bug63xx/Bug6324");
    }

    @Test
    public void testBug6325() {
        compareWithJavaSource("bug63xx/Bug6325");
    }

    @Test
    public void testBug6330() {
        compareWithJavaSource("bug63xx/Bug6330");
    }

    @Test
    public void testBug6344() {
        compareWithJavaSource("bug63xx/Bug6344");
    }

    @Test
    public void testBug6354() {
        compareWithJavaSource("bug63xx/Bug6354");
    }

    @Test
    public void testBug6358() {
        compile("bug63xx/Bug6358.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.issues.bug63xx.bug6358");
    }

    @Test
    public void testBug6360() {
        compareWithJavaSource("bug63xx/Bug6360");
    }

    @Test
    public void testBug6362() throws Throwable {
        compareWithJavaSource("bug63xx/Bug6362");
        runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.issues/1", 
                Arrays.asList("--run", "org.eclipse.ceylon.compiler.java.test.issues.bug63xx::bug6362"));
    }

    @Test
    public void testBug6365() {
        compareWithJavaSource("bug63xx/Bug6365");
    }

    @Test
    public void testBug6377() {
        compile("bug63xx/Bug6377Java.java");
        compareWithJavaSource("bug63xx/Bug6377");
    }

    @Test
    public void testBug6379() {
        compareWithJavaSource("bug63xx/Bug6379");
    }

    @Test
    public void testBug6391() {
        compareWithJavaSource("bug63xx/Bug6391");
    }

    @Test
    public void testBug6392() {
        compareWithJavaSource("bug63xx/Bug6392");
    }

    @Test
    public void testBug6401() {
        compareWithJavaSource("bug64xx/Bug6401");
    }

    @Test
    public void testBug6404() {
        compareWithJavaSource("bug64xx/Bug6404");
        run("org.eclipse.ceylon.compiler.java.test.issues.bug64xx.bug6404");
    }

    @Test
    public void testBug6409() {
        compareWithJavaSource("bug64xx/Bug6409");
    }

    @Test
    public void testBug6420() {
        compareWithJavaSource("bug64xx/Bug6420");
    }

    @Test
    public void testBug6421() {
        compareWithJavaSource("bug64xx/Bug6421");
    }

    @Ignore("Requires Android")
    @Test
    public void testBug6422() {
        String project = "../../../AndroidStudioProjects/MyApplication2/app";
        String src = project+"/src/main/ceylon";
        List<String> options = Arrays.asList("-jdk-provider", "android/23", "-src", src,
                "-rep", project+"/build/intermediates/ceylon-android/repository");
        ErrorCollector c = new ErrorCollector();
        List<String> modules = Arrays.asList("com.example.android.myapplication");
        assertCompilesOk(c, getCompilerTask(options, c, modules).call2());
    }

    @Test
    public void testBug6433() {
        assertErrors("bug64xx/Bug6433",
                new CompilerError(22, "overloaded formal member 'append(CharSequence?)' of 'Appendable' not implemented in class hierarchy"),
                new CompilerError(22, "overloaded formal member 'append(CharSequence?, Integer, Integer)' of 'Appendable' not implemented in class hierarchy")
                );
    }

    @Test
    public void testBug6435() {
        compareWithJavaSource("bug64xx/Bug6435");
    }

    @Ignore("Disabled due to binary compat being broken")
    @Test
    public void testBug6442() throws Throwable {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8
                || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        compile("bug64xx/bug6442/run.ceylon");
        runInJBossModules("run", "org.eclipse.ceylon.compiler.java.test.issues.bug64xx.bug6442/1", 
                Arrays.asList("--run", "org.eclipse.ceylon.compiler.java.test.issues.bug64xx.bug6442::run",
                        "--overrides", getPackagePath()+"/bug64xx/bug6442/overrides.xml"));
    }

    @Test
    public void testBug6447() {
        compareWithJavaSource("bug64xx/Bug6447");
    }

    @Test
    public void testBug6450() {
        compareWithJavaSource("bug64xx/Bug6450");
    }

    @Test
    public void testBug6459() {
        compareWithJavaSource("bug64xx/Bug6459");
    }

    @Test
    public void testBug6467() {
        compile("bug64xx/Bug6467Java.java", "bug64xx/Bug6467PsiElement.java");
        compareWithJavaSource("bug64xx/Bug6467");
    }
}

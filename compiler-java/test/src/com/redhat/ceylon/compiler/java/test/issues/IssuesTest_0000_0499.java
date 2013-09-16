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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;

public class IssuesTest_0000_0499 extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }

    @Override
    protected String transformDestDir(String name) {
        return name + "-0000-0499";
    }

    @Test
    public void testBug41() throws IOException {
        compile("bug00xx/Bug41.ceylon");
        List<String> options = new ArrayList<String>();
        options.addAll(defaultOptions);
        options.add("-verbose");
        options.add("-cp");
        // If this test is failing, make sure you have done "ant publish"
        // of ceylon.language
        String repoDir = Repositories.get().getUserRepoDir().getCanonicalPath();
        options.add(dir+File.pathSeparator+getModuleArchive("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, repoDir));
        Boolean result = getCompilerTask(options, "bug00xx/Bug41_2.ceylon").call();
        Assert.assertEquals("Compilation worked", Boolean.TRUE, result);
    }

    @Test
    public void testBug111(){
        compareWithJavaSource("bug01xx/Bug111");
    }
    
    @Test
    public void testBug151(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug01xx.bug151", "bug01xx/Bug151.ceylon");
    }
    
    @Test
    public void testBug192(){
        compareWithJavaSource("bug01xx/Bug192");
    }

    @Test
    public void testBug193(){
        compareWithJavaSource("bug01xx/Bug193");
    }

    @Test
    public void testBug202(){
        compareWithJavaSource("bug02xx/Bug202");
    }
    
    @Test
    public void testBug224(){
        compareWithJavaSource("bug02xx/Bug224");
    }
    
    @Test
    public void testBug227(){
        compareWithJavaSource("bug02xx/Bug227");
    }
    
    @Test
    public void testBug233(){
        compile("bug02xx/Bug233_Java.java", "bug02xx/Bug233_Type.java");
        compareWithJavaSource("bug02xx/Bug233");
    }
    
    @Test
    public void testBug241(){
        compareWithJavaSource("bug02xx/Bug241");
    }
    
    @Test
    public void testBug242(){
        compareWithJavaSource("bug02xx/Bug242");
    }
    
    @Test
    public void testBug247(){
        compareWithJavaSource("bug02xx/Bug247");
    }

    @Test
    public void testBug248(){
        compareWithJavaSource("bug02xx/Bug248");
    }

    @Test
    public void testBug249(){
        compareWithJavaSource("bug02xx/Bug249");
    }

    @Test
    public void testBug253(){
        compareWithJavaSource("bug02xx/Bug253");
    }

    @Test
    public void testBug260(){
        compareWithJavaSource("bug02xx/Bug260");
    }

    @Test
    public void testBug261(){
        compareWithJavaSource("bug02xx/Bug261");
    }

    @Test
    public void testBug269(){
        compareWithJavaSource("bug02xx/Bug269");
    }

    @Test
    public void testBug270(){
        compareWithJavaSource("bug02xx/Bug270");
    }
    
    @Test
    public void testBug283() {
        compareWithJavaSource("bug02xx/Bug283");
    }
    
    @Test
    public void testBug291(){
        compareWithJavaSource("bug02xx/Bug291");
    }
    
    @Test
    public void testBug298(){
        compareWithJavaSource("bug02xx/Bug298");
    }
    
    @Test
    public void testBug299(){
        compareWithJavaSource("bug02xx/Bug299");
    }
    
    @Test
    public void testBug311(){
        compareWithJavaSource("bug03xx/assert/Bug311");
    }

    @Test
    public void testBug313(){
        compareWithJavaSource("bug03xx/Bug313");
    }

    @Test
    public void testBug323(){
        compareWithJavaSource("bug03xx/Bug323");
    }

    @Test
    public void testBug324(){
        compareWithJavaSource("bug03xx/Bug324");
    }

    @Test
    public void testBug327(){
        compareWithJavaSource("bug03xx/Bug327");
    }

    @Test
    public void testBug329(){
        compareWithJavaSource("bug03xx/Bug329");
    }

    @Test
    public void testBug330(){
        // compile them both at the same time
        compile("bug03xx/Bug330_1.ceylon", "bug03xx/Bug330_2.ceylon");
        // compile them individually, loading the other half from the .car
        compile("bug03xx/Bug330_1.ceylon");
        compile("bug03xx/Bug330_2.ceylon");
    }

    @Test
    public void testBug353(){
        // compile them both at the same time
        compile("bug03xx/Bug353_1.ceylon", "bug03xx/Bug353_2.ceylon");
    }

    @Test
    public void testBug366(){
        compareWithJavaSource("bug03xx/Bug366");
    }

    @Test
    public void testBug399(){
        compile("bug03xx/Bug399.ceylon");
    }

    @Test
    public void testBug404(){
        compareWithJavaSource("bug04xx/Bug404");
    }

    @Test
    public void testBug406(){
        compareWithJavaSource("bug04xx/Bug406");
    }

    @Test
    public void testBug407(){
        // make sure we don't get an NPE error
        assertErrors("bug04xx/Bug407", new CompilerError(25, "expression is not iterable: Set<Map<String,Integer>.Entry<String,Integer>> is not a subtype of Iterable"));
    }

    @Test
    public void testBug410(){
        compareWithJavaSource("bug04xx/Bug410");
    }
    
    @Test
    public void testBug420(){
        compile("bug04xx/Bug420.ceylon");
    }

    @Test
    public void testBug441(){
        compareWithJavaSource("bug04xx/Bug441");
    }
    
    @Test
    public void testBug445(){
        compareWithJavaSource("bug04xx/Bug445");
    }

    @Test
    public void testBug446(){
        compareWithJavaSource("bug04xx/Bug446");
    }

    @Test
    public void testBug457(){
        compareWithJavaSource("bug04xx/Bug457");
    }

    @Test
    public void testBug458(){
        compile("bug04xx/bug458/a/module.ceylon", "bug04xx/bug458/a/package.ceylon", "bug04xx/bug458/a/a.ceylon");
        compile("bug04xx/bug458/b/module.ceylon", "bug04xx/bug458/b/b.ceylon");
    }

    @Test
    public void testBug475(){
        compareWithJavaSource("bug04xx/Bug475");
    }

    @Test
    public void testBug476(){
        compareWithJavaSource("bug04xx/Bug476");
    }

    @Test
    public void testBug477(){
        compareWithJavaSource("bug04xx/Bug477");
    }

    @Test
    public void testBug479(){
        compareWithJavaSource("bug04xx/Bug479");
    }

    @Test
    public void testBug490(){
        compareWithJavaSource("bug04xx/Bug490");
    }
    
    @Test
    public void testBug493(){
        compareWithJavaSource("bug04xx/bug493/module");
    }

    @Test
    public void testBug494(){
        compareWithJavaSource("bug04xx/Bug494");
    }
}


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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.cmr.JDKUtils.JDK;


public class IssuesTests_2000_2499 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-2000-2499";
    }

    @Test
    public void testBug2001() {
        compareWithJavaSource("bug20xx/Bug2001");
    }

    @Test
    public void testBug2002() {
        compareWithJavaSource("bug20xx/Bug2002");
    }

    @Test
    public void testBug2003() {
        compile("bug20xx/bug2003/Bug2003.java");
    }

    @Test
    public void testBug2005() {
        compareWithJavaSource("bug20xx/Bug2005");
    }
    
    @Test
    public void testBug2007() {
        compareWithJavaSource("bug20xx/Bug2007");
        run("com.redhat.ceylon.compiler.java.test.issues.bug20xx.Bug2007_2");
    }

    @Test
    public void testBug2011() {
        compareWithJavaSource("bug20xx/bug2011/Bug2011");
    }
    
    @Test
    public void testBug2012() {
        compareWithJavaSource("bug20xx/Bug2012");
    }
    
    @Test
    public void testBug2013() {
        compareWithJavaSource("bug20xx/Bug2013");
    }

    @Test
    public void testBug2016() {
        compareWithJavaSource("bug20xx/Bug2016");
    }

    @Test
    public void testBug2021() {
        compareWithJavaSource("bug20xx/Bug2021");
    }
    
    @Test
    public void testBug2022() {
        compareWithJavaSource("bug20xx/Bug2022");
    }
    
    @Test
    public void testBug2024() {
        compile("bug20xx/bug2024a/module.ceylon", "bug20xx/bug2024a/package.ceylon", "bug20xx/bug2024a/Instant.ceylon");
        compile("bug20xx/bug2024b/module.ceylon", "bug20xx/bug2024b/Bug2024.ceylon", "bug20xx/bug2024b/Bug2024.java");
        compile("bug20xx/bug2024b/module.ceylon", "bug20xx/bug2024b/Bug2024.ceylon", "bug20xx/bug2024b/Bug2024B.java");
        
        // special case: a single package module
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath()+"/bug20xx");
        options.addAll(defaultOptions);
        CeyloncTaskImpl task = getCompilerTask(options, 
                null,
                Arrays.asList("bug2024c"));
        Boolean ret = task.call();
        assertTrue(ret);
    }
    
    @Test @Ignore
    public void testBug2026() {
        // In this order everything compiles okay
        compile("bug20xx/bug2026/module.ceylon", "bug20xx/bug2026/Identifier.ceylon",
                "bug20xx/bug2026/ImportElements.ceylon", "bug20xx/bug2026/Node.ceylon",
                "bug20xx/bug2026/ImportElement.ceylon",
                "bug20xx/bug2026/ImportTypeElement.ceylon"
                );
        // In this order it doesn't
        compile("bug20xx/bug2026/module.ceylon", "bug20xx/bug2026/Identifier.ceylon",
                "bug20xx/bug2026/ImportElements.ceylon", "bug20xx/bug2026/Node.ceylon",
                "bug20xx/bug2026/ImportTypeElement.ceylon",
                "bug20xx/bug2026/ImportElement.ceylon"
                );
    }

    @Test
    public void testBug2032() {
        compareWithJavaSource("bug20xx/Bug2032");
        run("com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2032");
    }

    @Test
    public void testBug2033() {
        compareWithJavaSource("bug20xx/Bug2033");
    }

    @Test
    public void testBug2034() {
        compile("bug20xx/Bug2034.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2034");
    }

    @Test
    public void testBug2037() {
        compareWithJavaSource("bug20xx/Bug2037");
    }

    @Test
    public void testBug2038() {
        compareWithJavaSource("bug20xx/Bug2038");
    }
    
    @Test
    public void testBug2039() {
        compareWithJavaSource("bug20xx/Bug2039");
    }
    
    @Test
    public void testBug2048() {
        compareWithJavaSource("bug20xx/Bug2048");
    }
    
    @Test
    public void testBug2056() {
        compareWithJavaSource("bug20xx/Bug2056");
    }
    
    @Test
    public void testBug2058() {
        compareWithJavaSource("bug20xx/Bug2058");
        run("com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2058");
    }

    @Test
    public void testBug2061() {
        compareWithJavaSource("bug20xx/Bug2061");
    }
    
    @Test
    public void testBug2062() {
        compareWithJavaSource("bug20xx/Bug2062");
    }
    
    @Test
    public void testBug2064() {
        compile("bug20xx/Bug2064Java.java");
        compile("bug20xx/Bug2064.ceylon");
    }
    
    @Test
    public void testBug2066() {
        compareWithJavaSource("bug20xx/Bug2066");
    }
    
    @Test
    public void testBug2068() {
        compareWithJavaSource("bug20xx/Bug2068");
    }
    
    @Test
    public void testBug2069() {
        compareWithJavaSource("bug20xx/Bug2069");
    }
    
    @Test @Ignore
    public void testBug2073() {
        compareWithJavaSource("bug20xx/Bug2073");
    }
    
    @Test
    public void testBug2078() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2078", "bug20xx/Bug2078.ceylon");
    }
    
    @Test
    public void testBug2082() {
        compareWithJavaSource("bug20xx/Bug2082");
    }
    
    @Test
    public void testBug2089() {
        compareWithJavaSource("bug20xx/Bug2089");
    }
    
    @Test
    public void testBug2090() {
        assertErrors("bug20xx/Bug2090",
                new CompilerError(6, "type parameter 'Arguments' of declaration 'getMethods' has argument 'Anything' which is not assignable to upper bound 'Anything[]' of 'Arguments'"));
    }
    
    @Test @Ignore
    public void testBug2092() {
        compareWithJavaSource("bug20xx/Bug2092");
    }
    
    @Test
    public void testBug2096() {
        compareWithJavaSource("bug20xx/Bug2096");
    }
    
    @Test
    public void testBug2099() {
        compareWithJavaSource("bug20xx/Bug2099");
    }

    @Test
    public void testBug2109() {
        compile("bug21xx/Bug2109.ceylon", "bug21xx/Bug2109Annotations.java");
        run("com.redhat.ceylon.compiler.java.test.issues.bug21xx.run");
    }

    @Test
    public void testBug2118() {
        compareWithJavaSource("bug21xx/Bug2118");
    }

    @Test
    public void testBug2120() {
        compareWithJavaSource("bug21xx/Bug2120");
    }

    @Test
    public void testBug2124() {
        compareWithJavaSource("bug21xx/Bug2124");
        run("com.redhat.ceylon.compiler.java.test.issues.bug21xx.bug2124");
    }

    @Test
    public void testBug2132() {
        compile("bug21xx/GenericOuter.java", "bug21xx/DateInnerFactory.java");
        compareWithJavaSource("bug21xx/Bug2132");
    }

    @Test
    public void testBug2135() {
        compareWithJavaSource("bug21xx/Bug2135");
        run("com.redhat.ceylon.compiler.java.test.issues.bug21xx.bug2135");
    }

    @Test
    public void testBug2136() {
        if(JDKUtils.jdk == JDK.JDK8){
            assertErrors("bug21xx/Bug2136",
                    new CompilerError(Kind.WARNING, null, 22, "You import JDK7, which is provided by the JDK8 you are running on, but we cannot check that you are not using any JDK8-specific classes or methods. Upgrade your import to JDK8 if you depend on JDK8 classes or methods."),
                    new CompilerError(Kind.WARNING, null, 23, "You import JDK7, which is provided by the JDK8 you are running on, but we cannot check that you are not using any JDK8-specific classes or methods. Upgrade your import to JDK8 if you depend on JDK8 classes or methods."),
                    new CompilerError(Kind.WARNING, null, 24, "You import JDK7, which is provided by the JDK8 you are running on, but we cannot check that you are not using any JDK8-specific classes or methods. Upgrade your import to JDK8 if you depend on JDK8 classes or methods."),
                    new CompilerError(Kind.WARNING, null, 25, "You import JDK7, which is provided by the JDK8 you are running on, but we cannot check that you are not using any JDK8-specific classes or methods. Upgrade your import to JDK8 if you depend on JDK8 classes or methods."),
                    new CompilerError(Kind.WARNING, null, 20, "imported declaration is deprecated: 'StringBufferInputStream'"),
                    new CompilerError(Kind.WARNING, null, 22, "type is deprecated: 'StringBufferInputStream'"));
        }else{
            assertErrors("bug21xx/Bug2136",
                    new CompilerError(Kind.WARNING, null, 20, "imported declaration is deprecated: 'StringBufferInputStream'"),
                    new CompilerError(Kind.WARNING, null, 22, "type is deprecated: 'StringBufferInputStream'"));
        }
    }
    
    @Test
    public void testBug2143() {
        compareWithJavaSource("bug21xx/Bug2143");
    }

    @Test
    public void testBug2150() {
        compareWithJavaSource("bug21xx/Bug2150");
    }

    @Test
    public void testBug2151() {
        compareWithJavaSource("bug21xx/Bug2151");
    }

    @Test
    public void testBug2159() {
        compareWithJavaSource("bug21xx/Bug2159");
    }

    @Test
    public void testBug2164() {
        compareWithJavaSource("bug21xx/Bug2164");
    }

    @Test
    public void testBug2165() {
        compareWithJavaSource("bug21xx/Bug2165");
    }

    @Test
    public void testBug2168() {
        compile("bug21xx/Bug2168.ceylon");
    }

    @Test
    public void testBug2170() {
        compareWithJavaSource("bug21xx/Bug2170");
    }

    @Test
    public void testBug2186() {
        assertErrors("bug21xx/Bug2186",
                new CompilerError(23, "missing invocation expression"));
    }
    
    @Test
    public void testBug2188() {
        compareWithJavaSource("bug21xx/Bug2188");
    }
    
    @Test
    public void testBug2189() {
        compareWithJavaSource("bug21xx/bug2189/Bug2189");
    }

    @Test
    public void testBug2190() {
        compareWithJavaSource("bug21xx/Bug2190");
    }
    
    @Test
    public void testBug2193() {
        compareWithJavaSource("bug21xx/Bug2193");
    }
    
    @Test
    public void testBug2197() {
        compareWithJavaSource("bug21xx/Bug2197");
    }
    
    @Test
    @Ignore
    public void testBug2198() {
        compareWithJavaSource("bug21xx/Bug2198");
    }

    @Test
    public void testBug2206() {
        compareWithJavaSource("bug22xx/Bug2206");
    }
    
    @Test
    public void testBug2208() {
        compareWithJavaSource("bug22xx/Bug2208");
    }

    @Test
    public void testBug2214() {
        compareWithJavaSource("bug22xx/Bug2214");
    }

    @Test
    public void testBug2219() {
        compareWithJavaSource("bug22xx/Bug2219"); 
    }
    
    @Test
    public void testBug2228() {
        compareWithJavaSource("bug22xx/Bug22281");
        run("com.redhat.ceylon.compiler.java.test.issues.bug22xx.bug2228Method");
        compareWithJavaSource("bug22xx/Bug2228");
    }

    @Test
    public void testBug2229() {
        compareWithJavaSource("bug22xx/Bug2229"); 
    }

    @Test
    public void testBug2236() {
        compareWithJavaSource("bug22xx/Bug2236"); 
    }

    @Test
    public void testBug2239() {
        compareWithJavaSource("bug22xx/Bug2239"); 
    }

    @Test
    public void testBug2240() {
        compareWithJavaSource("bug22xx/Bug2240"); 
    }

    @Test
    public void testBug2244() {
        compareWithJavaSource("bug22xx/Bug2244"); 
    }
    
    @Test
    public void testBug2249() {
        compile("bug22xx/bug2249/moda/moda.ceylon", "bug22xx/bug2249/moda/package.ceylon", "bug22xx/bug2249/moda/module.ceylon");
        compile("bug22xx/bug2249/modb/modb.ceylon", "bug22xx/bug2249/modb/module.ceylon");
    }

    @Test
    public void testBug2251() {
        compareWithJavaSource("bug22xx/Bug2251"); 
    }

    @Test
    public void testBug2252() {
        compareWithJavaSource("bug22xx/Bug2252"); 
    }

    @Test
    public void testBug2253() {
        compareWithJavaSource("bug22xx/Bug2253"); 
    }

    @Test
    public void testBug2255() {
        compareWithJavaSource("bug22xx/Bug2255"); 
    }

    @Test
    public void testBug2259() {
        compareWithJavaSource("bug22xx/Bug2259"); 
    }

    @Test
    public void testBug2260() {
        assertErrors("bug22xx/Bug2260",
                new CompilerError(29, "duplicate annotation: there are multiple annotations of type 'Anno'"),
                new CompilerError(31, "duplicate annotation: there are multiple annotations of type 'Transient' for targets: '[GETTER]'"));
    }

    @Test
    public void testBug2262() {
        compareWithJavaSource("bug22xx/Bug2262"); 
    }

    @Test
    public void testBug2264() {
        compareWithJavaSource("bug22xx/Bug2264"); 
    }

    @Test
    public void testBug2265() {
        compareWithJavaSource("bug22xx/Bug2265");
        run("com.redhat.ceylon.compiler.java.test.issues.bug22xx.bug2265");
    }
    
    @Test
    public void testBug2266() {
        compareWithJavaSource("bug22xx/Bug2266");
    }
    
    @Test
    public void testBug2269() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug22xx.bug2269", "bug22xx/Bug2269.ceylon"); 
    }
    
    @Test
    public void testBug2272() {
        compareWithJavaSource("bug22xx/Bug2272"); 
    }

    @Test
    public void testBug2273() {
        compareWithJavaSource("bug22xx/Bug2273"); 
    }
    
    @Test
    public void testBug2274() {
        compareWithJavaSource("bug22xx/Bug2274"); 
    }
    
    @Test
    public void testBug2276() {
        compareWithJavaSource("bug22xx/Bug2276"); 
        run("com.redhat.ceylon.compiler.java.test.issues.bug22xx.bug2276"); 
    }
    
    @Test
    public void testBug2279() {
        compareWithJavaSource("bug22xx/Bug2279"); 
    }
    
    @Test
    public void testBug2280() {
        compareWithJavaSource("bug22xx/Bug2280"); 
    }
    
    @Test
    public void testBug2281() {
        compareWithJavaSource("bug22xx/Bug2281"); 
    }
    
    @Test
    public void testBug2282() {
        compareWithJavaSource("bug22xx/Bug2282"); 
    }
    
    @Test
    public void testBug2284() {
        compareWithJavaSource("bug22xx/Bug2284"); 
    }
    
    @Test
    public void testBug2288() {
        compareWithJavaSource("bug22xx/Bug2288"); 
    }
    
    @Test
    public void testBug2289() {
        compareWithJavaSource("bug22xx/bug2289/Bug2289"); 
    }
    
    @Test
    public void testBug2291() {
        compareWithJavaSource("bug22xx/Bug2291"); 
    }
    
    @Test
    public void testBug2293() {
        compareWithJavaSource("bug22xx/Bug2293"); 
    }
    
    @Test
    public void testBug2302() throws Exception {
        compile("bug23xx/Bug2302A.ceylon"); 
        compile("bug23xx/Bug2302B.ceylon");
    }
    
    @Test
    public void testBug2304() throws Exception {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug23xx.bug2304",
                "bug23xx/Bug2304.ceylon"); 
    }
    
    @Test
    public void testBug2305() throws Exception {
        compareWithJavaSource("bug23xx/Bug2305"); 
    }

    @Test
    public void testBug2306() throws Exception {
        compareWithJavaSource("bug23xx/Bug2306"); 
    }

    @Test
    public void testBug2322() throws Exception {
        compareWithJavaSource("bug23xx/Bug2322"); 
    }
}

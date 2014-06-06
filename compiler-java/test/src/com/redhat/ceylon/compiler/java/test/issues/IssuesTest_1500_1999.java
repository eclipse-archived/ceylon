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

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;


public class IssuesTest_1500_1999 extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-1500-1999";
    }

    @Test
    public void testBug1507() {
        compareWithJavaSource("bug15xx/Bug1507");
    }

    @Test
    public void testBug1508() {
        compareWithJavaSource("bug15xx/Bug1508");
    }

    @Test
    public void testBug1509() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.testBug1509", "bug15xx/Bug1509.ceylon");
    }

    @Test
    public void testBug1510() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1510", "bug15xx/Bug1510.ceylon");
    }

    @Test
    public void testBug1511() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1511", "bug15xx/Bug1511.ceylon");
    }

    @Test
    public void testBug1521() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1521", "bug15xx/Bug1521.ceylon");
    }
    
    @Test
    public void testBug1524() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1524", "bug15xx/Bug1524.ceylon");
    }

    @Test
    public void testBug1525() {
        compareWithJavaSource("bug15xx/Bug1525");
        compareWithJavaSource("bug15xx/Bug1525_2");
    }

    @Test
    public void testBug1528() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1528", "bug15xx/Bug1528.ceylon");
    }

    @Test
    public void testBug1530() {
        compareWithJavaSource("bug15xx/Bug1530");
        run("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1530");
    }

    @Test
    public void testBug1532() {
        compareWithJavaSource("bug15xx/Bug1532");
    }
    
    @Test
    public void testBug1533() {
        compareWithJavaSource("bug15xx/Bug1533");
        run("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1533_callsite");
    }

    @Test
    public void testBug1535() {
        compareWithJavaSource("bug15xx/Bug1535");
    }

    @Test
    public void testBug1536() {
        compareWithJavaSource("bug15xx/Bug1536");
    }
    
    @Test
    public void testBug1538() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.run1538", "bug15xx/Bug1538.ceylon");
    }

    @Test
    public void testBug1543() {
        compareWithJavaSource("bug15xx/Bug1543");
    }

    @Test
    public void testBug1544() {
        compareWithJavaSource("bug15xx/Bug1544");
    }

    @Test
    public void testBug1545() {
        compareWithJavaSource("bug15xx/Bug1545");
    }

    @Test
    public void testBug1548() {
        compareWithJavaSource("bug15xx/Bug1548");
    }

    @Test
    public void testBug1551() {
        compareWithJavaSource("bug15xx/Bug1551");
    }

    @Test
    public void testBug1549() {
        compareWithJavaSource("bug15xx/Bug1549");
    }

    @Test
    public void testBug1550() {
        compareWithJavaSource("bug15xx/Bug1550");
    }

    @Test
    public void testBug1555() {
        compareWithJavaSource("bug15xx/Bug1555");
    }

    @Test
    public void testBug1557() {
        compareWithJavaSource("bug15xx/Bug1557");
    }

    @Test
    public void testBug1559() {
        compareWithJavaSource("bug15xx/Bug1559");
    }

    @Test
    public void testBug1563() {
        compareWithJavaSource("bug15xx/Bug1563");
        run("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1563");
    }

    @Test
    public void testBug1564() {
        compareWithJavaSource("bug15xx/Bug1564");
    }

    @Test
    public void testBug1568() {
        compareWithJavaSource("bug15xx/bug1568/Bug1568.src", "bug15xx/bug1568/Bug1568.ceylon", "bug15xx/bug1568/module.ceylon");
    }

    @Test
    public void testBug1570() {
        compareWithJavaSource("bug15xx/Bug1570");
    }

    @Test
    public void testBug1571() {
        compareWithJavaSource("bug15xx/Bug1571");
    }

    @Test
    public void testBug1572() throws Throwable {
        compile("bug15xx/bug1572/mod/module.ceylon", "bug15xx/bug1572/mod/run.ceylon",
                "bug15xx/bug1572/test/module.ceylon", "bug15xx/bug1572/test/run.ceylon");
        runInJBossModules("test", "com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1572.mod/1");
    }

    @Test
    public void testBug1576() {
        assertErrors("bug15xx/Bug1576",
                new CompilerError(21, "native declaration not found"),
                new CompilerError(26, "native declaration not found"),
                new CompilerError(29, "native declaration not found"),
                new CompilerError(32, "native declaration not found"),
                new CompilerError(36, "native declaration not found"),
                new CompilerError(40, "native declaration not found"),
                new CompilerError(45, "native declaration not found"),
                new CompilerError(50, "native declaration not found"),
                new CompilerError(55, "native declaration not found")
                );
    }

    @Test
    public void testBug1578() {
        compareWithJavaSource("bug15xx/Bug1578");
    }
    
    @Test
    public void testBug1579() {
        compareWithJavaSource("bug15xx/Bug1579");
    }
    
    @Test
    public void testBug1580() {
        assertErrors(
                new String[] { "bug15xx/bug1580/Bug1580.ceylon", "bug15xx/bug1580/module.ceylon" },
                defaultOptions,
                null,
                new CompilerError(24, "could not determine type of method or attribute reference: instance of NioXnioProvider"));
    }

    @Test
    public void testBug1581() {
        assertErrors(
                new String[] { "bug15xx/bug1581/Bug1581Java.java", "bug15xx/bug1581/Bug1581.ceylon", "bug15xx/bug1581/module.ceylon" },
                defaultOptions,
                null,
                new CompilerError(22, "package not found in imported modules: java.util (add module import to module descriptor of com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581)"),
                new CompilerError(25, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(27, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(29, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(32, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(20, "Error while loading the com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581/1 module:\n   Error while resolving type of parameter 'props' of method '<init>' for com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581::Bug1581Java:\n   Failed to find declaration for Properties"),
                new CompilerError(22, "parameter type could not be determined: props of Bug1581Java"),
                new CompilerError(23, "could not determine type of method or attribute reference: props of Bug1581Java"),
                new CompilerError(23, "value type could not be inferred"));
    }
    
    @Test
    public void testBug1582() {
        compareWithJavaSource("bug15xx/Bug1582");
    }
    
    @Test
    public void testBug1583() {
        compareWithJavaSource("bug15xx/Bug1583");
    }
    
    @Test
    public void testBug1589() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1589", "bug15xx/Bug1589.ceylon");
    }
    
    @Test
    public void testBug1593() {
        compareWithJavaSource("bug15xx/Bug1593");
    }
    
    @Test
    public void testBug1594() {
        compareWithJavaSource("bug15xx/Bug1594");
    }

    @Test
    public void testBug1602() {
        compareWithJavaSource("bug16xx/Bug1602.src", "bug16xx/Bug1602Java.java", "bug16xx/Bug1602.ceylon");
    }
    
    @Test
    public void testBug1603() {
        compareWithJavaSource("bug16xx/Bug1603");
    }
    
    @Test
    public void testBug1607() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1607", "bug16xx/Bug1607.ceylon");
    }

    @Test
    public void testBug1611() {
        compareWithJavaSource("bug16xx/Bug1611");
    }

    @Test
    public void testBug1612() {
        compareWithJavaSource("bug16xx/Bug1612");
    }

    @Test
    public void testBug1604() {
        compareWithJavaSource("bug16xx/Bug1604");
    }

    @Test
    public void testBug1608() {
        compareWithJavaSource("bug16xx/Bug1608");
    }

    @Test
    public void testBug1618() throws Throwable {
        compile("bug16xx/bug1618/dep/Foo.ceylon", "bug16xx/bug1618/dep/module.ceylon", "bug16xx/bug1618/dep/package.ceylon",
                "bug16xx/bug1618/launcher/module.ceylon", "bug16xx/bug1618/launcher/run.ceylon",
                "bug16xx/bug1618/main/module.ceylon", "bug16xx/bug1618/main/foo.ceylon");
        runInJBossModules("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1618.launcher");
    }

    @Test
    public void testBug1621() {
        compareWithJavaSource("bug16xx/Bug1621");
    }

    @Test
    public void testBug1625() {
        compareWithJavaSource("bug16xx/Bug1625");
        compareWithJavaSource("bug16xx/Bug1625_2");
    }

    @Test
    public void testBug1629() {
        compareWithJavaSource("bug16xx/Bug1629");
    }
    
    @Test
    public void testBug1638() {
        compareWithJavaSource("bug16xx/Bug1638");
        run("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1638run");
    }
    
    @Test
    public void testBug1639() {
        assertErrors("bug16xx/Bug1639",
                new CompilerError(22, "dynamic is not yet supported on this platform"),
                new CompilerError(23, "dynamic is not yet supported on this platform"));
    }
    
    @Ignore("Only used for profiling")
    @Test
    public void testBug1647() throws IOException {
//        System.err.println("Press enter to continue");
//        System.in.read();
//        System.err.println("Let's go");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1647", "bug16xx/Bug1647.ceylon");
//        System.err.println("Press enter to quit");
//        System.in.read();
//        System.err.println("Done");
    }

    @Test
    public void testBug1648() {
        assertErrors("bug16xx/Bug1648",
                new CompilerError(22, "dynamic is not yet supported on this platform"));
    }

    @Test
    public void testBug1649() {
        compareWithJavaSource("bug16xx/Bug1649");
    }

    @Test
    public void testBug1651() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1651", "bug16xx/Bug1651.ceylon");
    }
    
    @Test
    public void testBug1652() {
        compareWithJavaSource("bug16xx/Bug1652");
    }
    
    @Test
    public void testBug1655() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1655", 
                "bug16xx/Bug1655.ceylon");
    }
    
    @Test
    public void testBug1658() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1658", "bug16xx/Bug1658.ceylon");
    }
    
    @Test
    public void testBug1659() {
        compareWithJavaSource("bug16xx/Bug1659");
    }
    
    @Test
    public void testBug1662() {
        compareWithJavaSource("bug16xx/Bug1662");
    }

    @Test
    public void testBug1664() {
        assertErrors("bug16xx/Bug1664",
                new CompilerError(30, "refined member type parameter NewUnitType of convertTo in Bug1664UnitOfTime with upper bound which member type parameter NewUnitType does not satisfy not yet supported: UnitType"),
                new CompilerError(37, "refined member type parameter NewUnitType of convertTo in Bug1664UnitOfTime with upper bound which member type parameter NewUnitType does not satisfy not yet supported: UnitType"),
                new CompilerError(-1, "com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Milliseconds is not abstract and does not override abstract method <NewUnitType>convertTo(com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor) in com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664UnitOfTime"),
                new CompilerError(44, "method convertTo in class com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664UnitOfTime<UnitType> cannot be applied to given types;\n"
                        +"  required: com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor\n"
                        +"  found: com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor\n"
                        +"  reason: explicit type argument com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Seconds does not conform to declared bound(s) com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Milliseconds"));
    }
}

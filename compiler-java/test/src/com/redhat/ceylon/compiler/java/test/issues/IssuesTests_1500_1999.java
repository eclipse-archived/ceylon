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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.launcher.Main;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState;
import com.redhat.ceylon.compiler.java.launcher.Main.ExitState.CeylonState;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.CeyloncTool;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.sun.tools.javac.util.Position;
import com.sun.tools.javac.util.Position.LineMap;


public class IssuesTests_1500_1999 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
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

    @Test @Ignore
    public void testBug1572() throws Throwable {
        compile("bug15xx/bug1572/mod/module.ceylon", "bug15xx/bug1572/mod/run.ceylon",
                "bug15xx/bug1572/test/module.ceylon", "bug15xx/bug1572/test/run.ceylon");
        runInJBossModules("test", "com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1572.mod/1");
    }

    @Test
    public void testBug1576() {
        assertErrors("bug15xx/Bug1576",
                new CompilerError(21, "no native implementation for backend: native 'bug1576angular' is not implemented for one or more backends"),
                new CompilerError(26, "no native implementation for backend: native 'Bug1576Class' is not implemented for one or more backends"),
                new CompilerError(29, "no native implementation for backend: native 'bug1576Method' is not implemented for one or more backends"),
                new CompilerError(32, "no native implementation for backend: native 'bug1576Attr' is not implemented for one or more backends"),
                new CompilerError(36, "no native implementation for backend: native 'bug1576angular' is not implemented for one or more backends"),
                new CompilerError(36, "no native implementation for backend: native 'module' is not implemented for one or more backends"),
                new CompilerError(40, "no native implementation for backend: native 'Bug1576Class' is not implemented for one or more backends"),
                new CompilerError(45, "no native implementation for backend: native 'Bug1576Class' is not implemented for one or more backends"),
                new CompilerError(50, "no native implementation for backend: native 'bug1576Method' is not implemented for one or more backends"),
                new CompilerError(55, "no native implementation for backend: native 'bug1576Attr' is not implemented for one or more backends")
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
                new CompilerError(22, "Error while loading the org.jboss.xnio.nio/3.1.0.CR7 module:\n   Declaration 'org.xnio.XnioProvider' could not be found in module 'org.jboss.xnio.nio' or its imported modules"),
                new CompilerError(24, "could not determine type of method or attribute reference: 'instance' of 'NioXnioProvider': Error while loading the org.jboss.xnio.nio/3.1.0.CR7 module:\n   Declaration 'org.xnio.Xnio' could not be found in module 'org.jboss.xnio.nio' or its imported modules"));
    }

    @Test
    public void testBug1581() {
        assertErrors(
                new String[] { "bug15xx/bug1581/Bug1581Java.java", "bug15xx/bug1581/Bug1581.ceylon", "bug15xx/bug1581/module.ceylon" },
                defaultOptions,
                null,
                new CompilerError(20, "Error while loading the com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581/1 module:\n"+
                        "   Error while resolving type of parameter 'props' of method '<init>' for com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581::Bug1581Java:\n"+
                        "   Failed to find declaration for Properties"),
                new CompilerError(22, "package not found in imported modules: java.util (add module import to module descriptor of com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581)"),
                new CompilerError(25, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(27, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(29, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(32, "cannot find symbol\n  symbol:   class Properties\n  location: class com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581.Bug1581Java"),
                new CompilerError(22, "parameter type could not be determined: 'props' of 'Bug1581Java': Error while loading the com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581/1 module:\n" + 
                        "   Error while resolving type of parameter 'props' of method '<init>' for com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581::Bug1581Java:\n" + 
                        "   Failed to find declaration for Properties"),
                new CompilerError(23, "could not determine type of method or attribute reference: 'props' of 'Bug1581Java': Error while loading the com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581/1 module:\n"+
                        "   Error while resolving type of getter 'props' for com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1581::Bug1581Java:\n"+
                        "   Failed to find declaration for Properties"));
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

    @Ignore("Used for benchmarking")
    @Test
    public void testBug1631() throws Exception {
//        System.err.println("Press enter to continue");
//        System.in.read();
//        System.err.println("Let's go");
        long totals = 0;
        int runs = 1;
        for(int i=0;i<runs;i++){
            long start = System.nanoTime();
            ErrorCollector c = new ErrorCollector();
            assertCompilesOk(c, getCompilerTask(Arrays.asList(/*"-verbose:benchmark", */"-out", destDir), c, "bug16xx/bug1631/run.ceylon").call2());
//            benchmarkParse("bug16xx/bug1631/run.ceylon");
            long end = System.nanoTime();
            long total = end - start;
            System.err.println("Took "+(total/1_000_000)+"ms");
            totals += total;
        }
        System.err.println("Average "+((totals/1_000_000)/runs)+"ms");
        
//        System.err.println("Press enter to quit");
//        System.in.read();
//        System.err.println("Done");
    }

    private void benchmarkParse(String file) throws Exception{
        String readSource = readFile(new File(getPackagePath(), file));
        String source = readSource.toString();
        char[] chars = source.toCharArray();
        LineMap map = Position.makeLineMap(chars, chars.length, false);
        System.err.println(map.hashCode());
        
        ANTLRStringStream input = new ANTLRStringStream(source);
        CeylonLexer lexer = new CeylonLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CeylonParser parser = new CeylonParser(tokens);
//        CompilationUnit cu = parser.compilationUnit();
//        System.err.println(cu.hashCode());
    }
    
    @Test
    public void testBug1638() {
        compareWithJavaSource("bug16xx/Bug1638");
        run("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1638run");
    }
    
    @Test
    public void testBug1639() {
        assertErrors("bug16xx/Bug1639",
                new CompilerError(22, "dynamic is not supported on the JVM"),
                new CompilerError(23, "dynamic is not supported on the JVM"));
    }
    
    @Test
    @Ignore
    public void testBug1643() {
        compareWithJavaSource("bug16xx/Bug1643");
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
    public void testBug1647Sum() throws IOException {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1647Sum", "bug16xx/Bug1647Sum.ceylon");
    }

    @Test
    public void testBug1648() {
        assertErrors("bug16xx/Bug1648",
                new CompilerError(22, "dynamic is not supported on the JVM"));
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
                new CompilerError(30, "refined member type parameter 'NewUnitType' of 'convertTo' in 'Bug1664UnitOfTime' with upper bound which refining member type parameter 'NewUnitType' does not satisfy not yet supported: 'UnitType' ('NewUnitType' should be upper bounded by 'Bug1664Milliseconds')"),
                new CompilerError(37, "refined member type parameter 'NewUnitType' of 'convertTo' in 'Bug1664UnitOfTime' with upper bound which refining member type parameter 'NewUnitType' does not satisfy not yet supported: 'UnitType' ('NewUnitType' should be upper bounded by 'Bug1664Seconds')"),
                new CompilerError(28, "com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Milliseconds is not abstract and does not override abstract method <NewUnitType>convertTo(com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor) in com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664UnitOfTime"),
                new CompilerError(35, "com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Seconds is not abstract and does not override abstract method <NewUnitType>convertTo(com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor) in com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664UnitOfTime"),
                new CompilerError(44, "method convertTo in class com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664UnitOfTime<UnitType> cannot be applied to given types;\n"
                        +"  required: com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor\n"
                        +"  found: com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor\n"
                        +"  reason: explicit type argument com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Seconds does not conform to declared bound(s) com.redhat.ceylon.compiler.java.test.issues.bug16xx.Bug1664Milliseconds"));
    }

    @Test
    public void testBug1665() {
        compareWithJavaSource("bug16xx/Bug1665");
    }

    @Test
    public void testBug1666() {
        compareWithJavaSource("bug16xx/Bug1666");
    }
    
    @Test
    public void testBug1671() {
        compareWithJavaSource("bug16xx/Bug1671");
    }

    @Test
    public void testBug1676() {
        compareWithJavaSource("bug16xx/Bug1676_2");
    }

    @Test
    public void testBug1682() {
        compareWithJavaSource("bug16xx/Bug1682");
    }

    @Test
    public void testBug1686() {
        compareWithJavaSource("bug16xx/Bug1686");
    }
    
    @Test
    public void testBug1695() {
        compareWithJavaSource("bug16xx/Bug1695");
    }

    @Test
    public void testBug1696() {
        compareWithJavaSource("bug16xx/Bug1696");
    }

    @Test
    public void testBug1701() {
        compareWithJavaSource("bug17xx/Bug1701");
    }
    
    @Test
    public void testBug1712() {
        compareWithJavaSource("bug17xx/bug1712/Bug1712Mixed");
        compareWithJavaSource("bug17xx/bug1712/Bug1712CRLF");
        compareWithJavaSource("bug17xx/bug1712/Bug1712CR");
        compareWithJavaSource("bug17xx/bug1712/Bug1712LF");
    }
    
    @Test
    public void testBug1717() {
        compareWithJavaSource("bug17xx/Bug1717");
    }
    
    @Test
    public void testBug1719() {
        compareWithJavaSource("bug17xx/Bug1719");
    }
    
    @Test
    public void testBug1723() {
        compile("bug17xx/Bug1723_1.ceylon");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug17xx.bug1723", "bug17xx/Bug1723_2.ceylon");
    }
    
    @Test
    public void testBug1725() {
        compareWithJavaSource("bug17xx/Bug1725");
    }
    
    @Test
    public void testBug1726() {
        compareWithJavaSource("bug17xx/Bug1726");
    }
    
    @Test
    public void testBug1728() {
        compareWithJavaSource("bug17xx/Bug1728");
    }
    
    @Test
    public void testBug1731() {
        compareWithJavaSource("bug17xx/Bug1731");
    }
    
    @Test
    public void testBug1734() {
        compareWithJavaSource("bug17xx/Bug1734");
    }

    @Test
    public void testBug1735() {
        compile("bug17xx/Bug1735.java");
        compareWithJavaSource("bug17xx/Bug1735");
    }

    @Test
    public void testBug1737() {
        compareWithJavaSource("bug17xx/Bug1737");
    }
    
    @Test
    public void testBug1739() {
        compile("bug17xx/Bug1739.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug17xx.bug1739");
    }
    
    @Test
    public void testBug1740() {
        compareWithJavaSource("bug17xx/Bug1740");
    }
    
    @Test
    public void testBug1743() {
        assertErrors("bug17xx/Bug1743",
                new CompilerError(22, "nothing to return from"),
                new CompilerError(22, "statement or initializer may not occur directly in interface body"));
    }

    @Test
    public void testBug1744() {
        compareWithJavaSource("bug17xx/Bug1744");
    }
    
    @Test
    public void testBug1746() {
        compareWithJavaSource("bug17xx/Bug1746");
    }
    
    @Test
    public void testBug1748() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug17xx.bug1748", "bug17xx/Bug1748.ceylon");
    }
    
    @Test
    public void testBug1755() {
        compareWithJavaSource("bug17xx/bug1755/mod1/mod1.src", "bug17xx/bug1755/mod1/mod1.ceylon", "bug17xx/bug1755/mod1/module.ceylon", "bug17xx/bug1755/mod1/package.ceylon");
        compareWithJavaSource("bug17xx/bug1755/mod2/mod2");
    }

    @Test
    public void testBug1761A() {
        assertErrors("bug17xx/Bug1761A",
                new CompilerError(2, "shared declaration is not a member of a class, interface, or package: 'Bar' in 'bug1761A'"));
    }
    @Test
    public void testBug1761B() {
        assertErrors("bug17xx/Bug1761B",
                new CompilerError(1, "type of declaration anonymous function is not visible everywhere declaration is visible: 'Bar' involves an unshared type declaration"));
    }
    
    @Test
    public void testBug1762() {
        compareWithJavaSource("bug17xx/Bug1762");
    }

    @Test
    public void testBug1765() {
        compareWithJavaSource("bug17xx/Bug1765");
    }


    @Test
    public void testBug1767() {
        compareWithJavaSource("bug17xx/bug1767/Bug1767.src", "bug17xx/bug1767/Bug1767.ceylon", "bug17xx/bug1767/module.ceylon");
    }

    @Test
    public void testBug1768() {
        assertErrors("bug17xx/Bug1768",
                Arrays.asList("-continue"),
                null,
                new CompilerError(22, "declaration is not a value, and may not be annotated late"),
                new CompilerError(22, "value is not an uninitialized reference, and may not be annotated late"));
    }

    @Test
    public void testBug1773() {
        ErrorCollector collector = new ErrorCollector();
        
        CeyloncTaskImpl task = getCompilerTask(defaultOptions, collector, "bug17xx/Bug1773.ceylon");

        // now compile it all the way
        ExitState exitState = task.call2();
        Assert.assertEquals(ExitState.CeylonState.ERROR, exitState.ceylonState);
        
        // make sure we only got one, do not trust actualErrors.size() for that since it's a Set so
        // two methods with same contents would count as one.
        Assert.assertEquals(1, exitState.errorCount);
    
        TreeSet<CompilerError> actualErrors = collector.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors, new CompilerError(22, "dynamic is not supported on the JVM"));
    }

    @Test
    public void testBug1777() {
        compareWithJavaSource("bug17xx/Bug1777");
    }

    @Test
    public void testBug1778() {
        compile("bug17xx/Bug1778.java");
        compareWithJavaSource("bug17xx/Bug1778");
    }

    @Test
    public void testBug1779() {
        assertErrors("bug17xx/Bug1779",
                Arrays.asList("-continue"),
                null,
                new CompilerError(22, "missing class body or aliased class reference"),
                new CompilerError(23, "incorrect syntax: no viable alternative at token '}'"));
    }

    @Test
    public void testBug1784() {
        compile("bug17xx/Bug1784.java");
        compareWithJavaSource("bug17xx/Bug1784");
    }

    @Test
    public void testBug1787() {
        compareWithJavaSource("bug17xx/Bug1787");
    }

    @Test
    public void testBug1789() {
        assertErrors("bug17xx/Bug1789",
                Arrays.asList("-continue"),
                null,
                new CompilerError(22, "could not determine type of function or value reference: 'd'"),
                new CompilerError(24, "does not definitely return: 'd' has branches which do not end in a return statement"),
                new CompilerError(24, "type declaration does not exist: 'Id'"));

    }
    
    @Test
    public void testBug1796() {
        compareWithJavaSource("bug17xx/Bug1796");
    }

    @Test
    public void testBug1803() {
        compile("bug18xx/bug1803.ceylon");
        compile("bug18xx/bug1803.ceylon");
    }
    
    @Test
    public void testBug1811() {
        compile("bug18xx/Bug1811A.ceylon");
        compile("bug18xx/Bug1811B.ceylon");
    }
    
    @Test
    public void testBug1814() {
        compareWithJavaSource("bug18xx/Bug1814");
    }
    
    @Test
    public void testBug1816() {
        compareWithJavaSource("bug18xx/Bug1816");
    }
    
    @Test
    public void testBug1818() {
        compareWithJavaSource("bug18xx/Bug1818");
    }
    
    @Test
    public void testBug1822() {
        compareWithJavaSource("bug18xx/Bug1822");
    }
    
    @Test
    public void testBug1823() {
        compareWithJavaSource("bug18xx/Bug1823");
    }
    
    @Test
    public void testBug1824() {
        compareWithJavaSource("bug18xx/Bug1824");
    }
    
    @Test
    public void testBug1825() {
        compareWithJavaSource("bug18xx/bug1825/Bug1825");
    }
    
    @Test
    public void testBug1830() {
        ErrorCollector collector = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(defaultOptions, collector, "bug18xx/Bug1830.ceylon");
        ExitState call2 = task.call2();
        Assert.assertEquals(CeylonState.ERROR, call2.ceylonState);
        Assert.assertEquals(Main.EXIT_ERROR, call2.javacExitCode);
    }
    
    @Test
    public void testBug1831() {
        compareWithJavaSource("bug18xx/Bug1831");
    }

    @Test
    public void testBug1834() {
        compareWithJavaSource("bug18xx/Bug1834");
    }

    @Test
    public void testBug1836() {
        ErrorCollector collector = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(defaultOptions, collector, "bug18xx/Bug1836.ceylon");
        ExitState call2 = task.call2();
        Assert.assertEquals(CeylonState.ERROR, call2.ceylonState);
        Assert.assertEquals(Main.EXIT_ERROR, call2.javacExitCode);
    }
    
    @Test
    public void testBug1844() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug18xx.bug1844", "bug18xx/Bug1844.ceylon");
    }
    
    @Test
    public void testBug1845() {
        compareWithJavaSource("bug18xx/Bug1845");
    }

    @Test
    public void testBug1849() {
        compareWithJavaSource("bug18xx/Bug1849");
    }
    
    @Test
    public void testBug1851() {
        compareWithJavaSource("bug18xx/Bug1851");
    }
    
    @Test
    public void testBug1852() {
        assertErrors(new String[]{"bug18xx/Bug1852.ceylon"},
                Arrays.asList("-suppress-warnings", "importsOtherJdk"),
                null,
                new CompilerError(Kind.WARNING, "", 21, "declaration is never used: 's'"));
    }
    
    @Test
    public void testBug1857() {
        assertErrors("bug18xx/Bug1857",
                new CompilerError(Kind.ERROR, "", 22, "function or value does not exist: 'ß'"));
    }

    @Test
    public void testBug1873() {
        compareWithJavaSource("bug18xx/Bug1873");
    }
    
    @Test
    public void testBug1875() {
        compareWithJavaSource("bug18xx/Bug1875");
    }

    @Test
    public void testBug1877() {
        compile("bug18xx/Bug1877Java.java");
        compareWithJavaSource("bug18xx/Bug1877");
    }

    @Test
    public void testBug1882_JDK7() {
        Assume.assumeTrue("Runs on JDK7", JDKUtils.jdk == JDKUtils.JDK.JDK7);
        compilesWithoutWarnings("bug18xx/bug1882/module.ceylon");
    }

    @Test
    public void testBug1882_JDK8() {
        Assume.assumeTrue("Runs on JDK8", JDKUtils.jdk == JDKUtils.JDK.JDK8);
        assertErrors("bug18xx/bug1882/module",
                new CompilerError(Kind.WARNING, "module.ceylon", 3, "You import JDK7, which is provided by the JDK8 you are running on, but we cannot check that you are not using any JDK8-specific classes or methods. Upgrade your import to JDK8 if you depend on JDK8 classes or methods.")
                );
    }

    @Test
    public void testBug1887() {
        compareWithJavaSource("bug18xx/Bug1887");
    }

    @Test
    public void testBug1888() {
        compareWithJavaSource("bug18xx/Bug1888");
    }

    @Test
    public void testBug1892() {
        compile("bug18xx/Bug1892Annotation.java");
        compareWithJavaSource("bug18xx/Bug1892");
    }
    
    @Test
    public void testBug1894() {
        compareWithJavaSource("bug18xx/Bug1894");
    }

    @Test
    public void testBug1895() {
        compareWithJavaSource("bug18xx/Bug1895");
    }

    @Test
    public void testBug1898() {
        compareWithJavaSource("bug18xx/Bug1898");
    }
    
    @Test
    public void testBug1899() {
        compareWithJavaSource("bug18xx/Bug1899");
    }

    @Test
    public void testBug1900() {
        compareWithJavaSource("bug19xx/Bug1900");
    }

    @Test
    public void testBug1901() {
        compareWithJavaSource("bug19xx/Bug1901");
    }

    @Test
    public void testBug1905() {
        compareWithJavaSource("bug19xx/Bug1905");
    }

    @Test
    public void testBug1908() {
        compareWithJavaSource("bug19xx/Bug1908");
        run("com.redhat.ceylon.compiler.java.test.issues.bug19xx.bug1908");
    }

    @Test
    public void testBug1914() {
        compareWithJavaSource("bug19xx/Bug1914");
    }

    @Test
    public void testBug1915() {
        compareWithJavaSource("bug19xx/Bug1915");
    }

    @Test
    public void testBug1916() {
        compareWithJavaSource("bug19xx/Bug1916");
    }

    @Test
    public void testBug1917() {
        compile("bug19xx/Bug1917Java.java");
        compareWithJavaSource("bug19xx/Bug1917");
    }

    @Test
    public void testBug1918() {
        compareWithJavaSource("bug19xx/Bug1918");
        compareWithJavaSource("bug19xx/Bug1918B");
    }

    @Test
    public void testBug1922() {
        compareWithJavaSource("bug19xx/Bug1922");
    }
    
    @Test
    public void testBug1923() {
        compareWithJavaSource("bug19xx/Bug1923");
    }

    @Test
    public void testBug1924() {
        compareWithJavaSource("bug19xx/Bug1924");
    }

    @Test
    public void testBug1925() {
        compareWithJavaSource("bug19xx/Bug1925");
    }
    
    @Test
    public void testBug1927() {
        compareWithJavaSource("bug19xx/Bug1927");
    }

    @Test
    public void testBug1928() {
        compareWithJavaSource("bug19xx/Bug1928");
    }

    @Test
    public void testBug1930() {
        compareWithJavaSource("bug19xx/Bug1930");
    }

    @Test
    public void testBug1932() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug19xx.bug1932", "bug19xx/Bug1932.ceylon");
    }

    @Test
    public void testBug1935() {
        compareWithJavaSource("bug19xx/Bug1935");
    }

    @Test
    public void testBug1937() {
        compareWithJavaSource("bug19xx/Bug1937");
    }

    @Test
    public void testBug1939() {
        compareWithJavaSource("bug19xx/Bug1939");
    }

    @Test
    public void testBug1942() {
        compareWithJavaSource("bug19xx/Bug1942");
    }
    
    @Test
    public void testBug1946() {
        compareWithJavaSource("bug19xx/Bug1946");
    }
    
    @Test
    public void testBug1953() {
        compareWithJavaSource("bug19xx/Bug1953");
    }

    @Test
    public void testBug1955() {
        compareWithJavaSource("bug19xx/Bug1955");
        run("com.redhat.ceylon.compiler.java.test.issues.bug19xx.bug1955");
    }

    @Test
    public void testBug1958() {
        compareWithJavaSource("bug19xx/Bug1958");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testBug1969() {
        java.util.List<File> sourceFiles = new ArrayList<File>(1);
        sourceFiles.add(new File(getPackagePath(), "bug19xx/Bug1969.ceylon"));

        CeyloncTool runCompiler = makeCompiler();
        StringWriter writer = new StringWriter();
        CeyloncFileManager runFileManager = (CeyloncFileManager)runCompiler.getStandardFileManager(writer, null, null, null);

        // make sure the destination repo exists
        new File(destDir).mkdirs();

        List<String> options = new LinkedList<String>();
        options.addAll(defaultOptions);
        if(!options.contains("-src"))
            options.addAll(Arrays.asList("-src", getSourcePath()));
        if(!options.contains("-cacherep"))
            options.addAll(Arrays.asList("-cacherep", getCachePath()));
        if(!options.contains("-cp"))
            options.addAll(Arrays.asList("-cp", getClassPathAsPath()));
        options.add("-verbose");
        Iterable<? extends JavaFileObject> compilationUnits1 =
                runFileManager.getJavaFileObjectsFromFiles(sourceFiles);
        
        CeyloncTaskImpl task = runCompiler.getTask(writer, runFileManager, null, options, null, compilationUnits1);
        
        ErrorCollector collector = new ErrorCollector();
        assertCompilesOk(collector, task.call2());

        Assert.assertTrue(writer.toString().length() > 0);
    }

    @Test
    public void testBug1972() {
        compareWithJavaSource("bug19xx/Bug1972");
    }
    
    @Test
    public void testBug1973() {
        compareWithJavaSource("bug19xx/Bug1973");
    }

    @Test
    public void testBug1975() {
        compareWithJavaSource("bug19xx/Bug1975");
    }

    @Test
    public void testBug1982() {
        compareWithJavaSource("bug19xx/Bug1982");
        run("com.redhat.ceylon.compiler.java.test.issues.bug19xx.bug1982");
    }

    @Test
    public void testBug1983() {
        compareWithJavaSource("bug19xx/Bug1983");
    }
    
    @Test
    public void testBug1984() {
        compareWithJavaSource("bug19xx/Bug1984");
    }
    @Test
    public void testBug1985(){
        compile("bug19xx/Bug1985.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug19xx.bug1985");
    }
    
    
    
    @Test
    public void testBug1989() {
        compareWithJavaSource("bug19xx/Bug1989");
    }

    @Test
    public void testBug1997() {
        assertErrors("bug19xx/Bug1997",
                new CompilerError(21, "missing class body or aliased class reference"));
    }
    
    @Test
    public void testBug1998() {
        //compareWithJavaSource("bug19xx/Bug1998");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug19xx.run", "bug19xx/Bug1998.ceylon");
    }
}

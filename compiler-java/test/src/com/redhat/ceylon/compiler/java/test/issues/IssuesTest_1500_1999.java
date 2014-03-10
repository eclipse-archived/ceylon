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

import org.junit.Test;

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
    public void testBug1510() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1510", "bug15xx/Bug1510.ceylon");
    }

    @Test
    public void testBug1511() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1511", "bug15xx/Bug1511.ceylon");
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
}

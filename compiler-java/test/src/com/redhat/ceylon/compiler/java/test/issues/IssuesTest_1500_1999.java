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
    public void testBug1530() {
        compareWithJavaSource("bug15xx/Bug1530");
        run("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1530");
    }

    @Test
    public void testBug1532() {
        compareWithJavaSource("bug15xx/Bug1532");
    }

    @Test
    public void testBug1536() {
        compareWithJavaSource("bug15xx/Bug1536");
    }
}

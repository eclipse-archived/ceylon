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

import com.redhat.ceylon.compiler.java.test.CompilerTests;


public class IssuesTests_5500_5999 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-5500-5999";
    }

    @Test
    public void testBug5855() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug58xx.bug5855",
                "bug58xx/bug5855.ceylon");
    }

    @Test
    public void testBug5868() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug58xx.bug5868",
                "bug58xx/bug5868.ceylon");
    }

    @Test
    public void testBug5924() {
        compile("bug59xx/bug5924.ceylon");
    }

    @Test
    public void testBug5953() {
        compile("bug59xx/bug5953.ceylon");
    }

}

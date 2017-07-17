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

import java.util.Arrays;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class IssuesTests_7000_7499 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-7000-7499";
    }

    @Test
    public void bug7027(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug70xx.bug7027",
                "bug70xx/bug7027.ceylon");
    }

    @Test
    public void bug7053(){
        compile("bug70xx/bug7053/run.ceylon");
    }

    @Test
    public void bug7073(){
        compile("bug70xx/bug7073.ceylon");
    }

    @Test
    public void bug7083(){
        compile(Arrays.asList("-fully-export-maven-dependencies"), "bug70xx/bug7083/run.ceylon");
    }

    @Test
    public void bug7090(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug70xx.bug7090",
                "bug70xx/bug7090.ceylon");
    }

    @Test
    public void bug7105(){
        compile("bug70xx/bug7105/run.ceylon");
    }
}

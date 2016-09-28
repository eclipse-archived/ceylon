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

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;


public class IssuesTests_6500_6999 extends CompilerTests {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-6500-6999";
    }
    
    @Test
    public void testBug6532() {
        assertErrors("bug65xx/Bug6532",
                new CompilerError(25, "missing named argument to required parameter 'Anything(Integer, String) service' of 'Endpoint'"),
                new CompilerError(27, "parameterized expression not the target of a specification statement: parameter list is not followed by '=>' specifier (add '=>' specifier, or explicitly specify 'function' keyword or return type)")
                );
    }

    @Test
    public void testBug6533() {
        compile("bug65xx/Bug6533A.ceylon", "bug65xx/Bug6533Annotation.ceylon");
        compile("bug65xx/Bug6533B.ceylon", "bug65xx/Bug6533Annotation.ceylon");
    }
}

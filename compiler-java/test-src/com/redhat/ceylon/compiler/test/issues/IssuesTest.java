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
package com.redhat.ceylon.compiler.test.issues;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class IssuesTest extends CompilerTest {
    
    @Test
    public void testBug111(){
        compareWithJavaSource("Bug111");
    }
    
    @Test
    public void testBug151(){
        compileAndRun("com.redhat.ceylon.compiler.test.issues.bug151", "Bug151.ceylon");
    }
    
    @Test
    public void testBug193(){
        compareWithJavaSource("Bug193");
    }

    @Test
    public void testBug224(){
        compareWithJavaSource("Bug224");
    }
    
    @Test
    public void testBug227(){
        compareWithJavaSource("Bug227");
    }
    
    @Test
    public void testBug241(){
        compareWithJavaSource("Bug241");
    }
    
    @Test
    public void testBug242(){
        compareWithJavaSource("Bug242");
    }
    
    @Test
    public void testBug247(){
        compareWithJavaSource("Bug247");
    }
}

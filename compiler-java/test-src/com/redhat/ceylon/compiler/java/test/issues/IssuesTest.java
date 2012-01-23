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

public class IssuesTest extends CompilerTest {
    
    @Test
    public void testBug111(){
        compareWithJavaSource("Bug111");
    }
    
    @Test
    public void testBug151(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug151", "Bug151.ceylon");
    }
    
    @Test
    public void testBug192(){
        compareWithJavaSource("Bug192");
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

    @Test
    public void testBug248(){
        compareWithJavaSource("Bug248");
    }

    @Test
    public void testBug249(){
        compareWithJavaSource("Bug249");
    }

    @Test
    public void testBug253(){
        compareWithJavaSource("Bug253");
    }

    @Test
    public void testBug261(){
        compareWithJavaSource("Bug261");
    }

    @Test
    public void testBug269(){
        compareWithJavaSource("Bug269");
    }

    @Test
    public void testBug270(){
        compareWithJavaSource("Bug270");
    }
    
    @Test
    public void testBug298(){
        compareWithJavaSource("Bug298");
    }
    
    @Test
    public void testBug311(){
        compareWithJavaSource("assert/Bug311");
    }

    @Test
    public void testBug313(){
        compareWithJavaSource("Bug313");
    }

    @Test
    public void testBug324(){
        compareWithJavaSource("Bug324");
    }

    @Test
    public void testBug327(){
        compareWithJavaSource("Bug327");
    }

    @Test
    public void testBug329(){
        compareWithJavaSource("Bug329");
    }

    @Test
    public void testBug330(){
        // compile them both at the same time
        compile("Bug330_1.ceylon", "Bug330_2.ceylon");
        // compile them individually, loading the other half from the .car
        compile("Bug330_1.ceylon");
        compile("Bug330_2.ceylon");
    }
}

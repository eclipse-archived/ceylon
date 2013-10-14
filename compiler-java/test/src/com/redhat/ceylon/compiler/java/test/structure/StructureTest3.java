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
package com.redhat.ceylon.compiler.java.test.structure;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class StructureTest3 extends CompilerTest {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-3";
    }
    
    @Test
    public void testMcrInstantiatorDelegates(){
        compareWithJavaSource("mcr/InstantiatorDelegates");
    }
    
    //
    // Member Class Refinement
    
    // Default Member Class of Class
    @Test
    public void testMcrClassDefaultMemberClass(){
        compareWithJavaSource("mcr/ClassDefaultMemberClass");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithParams");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithDefaultedParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithDefaultedParams");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithSequencedParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithSequencedParams");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassWithTypeParams(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassWithTypeParams");
    }
    
    @Test
    public void testMcrClassMemberClassesWithMemberSubclasses(){
        compareWithJavaSource("mcr/ClassMemberClassesWithMemberSubclasses");
    }
    
    @Test
    public void testMcrClassAliasedDefaultMemberClass(){
        compareWithJavaSource("mcr/ClassAliasedDefaultMemberClass");
    }
    
    // Default Member Class of Interface
    @Test
    public void testMcrInterfaceDefaultMemberClass(){
        compareWithJavaSource("mcr/InterfaceDefaultMemberClass");
    }
    
    @Test
    public void testMcrInterfaceMemberClassesWithMemberSubclasses(){
        compareWithJavaSource("mcr/InterfaceMemberClassesWithMemberSubclasses");
    }
    
    // Formal Member Class of Class
    @Test
    public void testMcrClassFormalMemberClass(){
        compareWithJavaSource("mcr/ClassFormalMemberClass");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassReference(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassReference");
    }
    
    @Test
    public void testMcrClassDefaultMemberClassSpecifier(){
        compareWithJavaSource("mcr/ClassDefaultMemberClassSpecifier");
    }
    
    // Formal Member Class of Interface
    @Test
    public void testMcrInterfaceFormalMemberClass(){
        compareWithJavaSource("mcr/InterfaceFormalMemberClass");
    }
    
    @Test
    public void testMcrInterfaceAliasedFormalMemberClass(){
        compareWithJavaSource("mcr/InterfaceAliasedFormalMemberClass");
    }
    
    @Test
    public void testMcrSubclassedTwice(){
        compareWithJavaSource("mcr/SubclassedTwice");
    }
    
    @Test
    public void testMcrInterfaceFormalMemberClassDefaultedParameter(){
        compareWithJavaSource("mcr/InterfaceFormalMemberClassDefaultedParameter");
    }
}
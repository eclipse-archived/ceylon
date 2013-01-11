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
package com.redhat.ceylon.compiler.java.test.annotations;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class AnnotationsTest extends CompilerTest {
    @Test
    public void testTypeGrouping(){
        compareWithJavaSource("typeGrouping");
    }
    @Test
    public void testUnionTypeInfo(){
        compareWithJavaSource("UnionTypeInfo");
    }
    @Test
    public void testClass(){
        compareWithJavaSource("Klass");
    }
    @Test
    public void testInterface(){
        compareWithJavaSource("Interface");
    }
    @Test
    public void testMethod(){
        compareWithJavaSource("method");
    }
    @Test
    public void testAttribute(){
        compareWithJavaSource("attribute");
    }
    @Test
    public void testMemberClass(){
        compareWithJavaSource("MemberKlass");
    }
    @Test
    public void testMemberObject(){
        compareWithJavaSource("MemberObject");
    }
    @Test
    public void testKlassInMemberObject(){
        compareWithJavaSource("KlassInMemberObject");
    }
    @Test
    public void testLocalClass(){
        compareWithJavaSource("LocalKlass");
    }
    @Test
    public void testLocalMethod(){
        compareWithJavaSource("LocalMethod");
    }
    @Test
    public void testLocalObject(){
        compareWithJavaSource("LocalObject");
    }
    @Test
    public void testModule(){
        compareWithJavaSource("module");
    }
    @Test
    public void testTypeReferences(){
        compile("Interface.ceylon", "Klass.ceylon", "MemberKlass.ceylon", "KlassInMemberObject.ceylon");
        compareWithJavaSource("typereferences");
    }
    @Test
    public void testTypeParameters(){
        compareWithJavaSource("TypeParameters");
    }
 }
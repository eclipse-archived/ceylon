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
package com.redhat.ceylon.compiler.java.test.expression.ref;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class StaticRefTest extends CompilerTest {
    
    @Test
    public void testRefAttributeRef() {
        compareWithJavaSource("AttributeRef");
        //compile("AttributeRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.attributeRef");
    }
    
    @Test
    public void testRefValueParameterRef() {
        compareWithJavaSource("ValueParameterRef");
        //compile("ValueParameterRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.valueParameterRef");
    }
    
    @Test
    public void testRefMethodRef() {
        compareWithJavaSource("MethodRef");
        //compile("MethodRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.methodRef");
    }
    
    @Test
    public void testRefFunctionalParameterRef() {
        compareWithJavaSource("FunctionalParameterRef");
        //compile("FunctionalParameterRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.functionalParameterRef");
    }
    
    @Test
    public void testRefMemberClassRef() {
        compareWithJavaSource("MemberClassRef");
        //compile("MemberClassRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.memberClassRef");
    }
    
    @Test
    public void testRefMemberClassRefInFunction() {
        compareWithJavaSource("MemberClassRefInFunction");
        //compile("MemberClassRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.memberClassRefInFunction");
    }
    
    @Test
    public void testRefFunrefs() {
        compareWithJavaSource("funrefs");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.funrefs");
        
    }

}

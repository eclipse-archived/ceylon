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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class Java8InterfaceTests extends CompilerTests {
    
    List<String> optsForJava8Interfaces() {
        ArrayList<String> opts = new ArrayList<String>(defaultOptions);
        opts.add("-target");
        opts.add("8");
        opts.add("-source");
        opts.add("8");
        return opts;
    }
    
    @Override
    public void  compareWithJavaSource(String source) {
        super.compareWithJavaSource(optsForJava8Interfaces(), source+".src", source+".ceylon");
    }
    
    @Test
    public void ifaceWithConcreteMethods() {
        compareWithJavaSource("iface/InterfaceWithConcreteMethods");
        compareWithJavaSource("iface/InterfaceWithConcreteMethodsSatisfier");
    }
    
    @Test
    public void ifaceWithConcreteAttributes() {
        compareWithJavaSource("iface/InterfaceWithConcreteAttributes");
        compareWithJavaSource("iface/InterfaceWithConcreteAttributesSatisfier");
    }
    
    @Test
    public void genericIface() {
        compareWithJavaSource("iface/GenericInterface");
    }
    
    @Test
    public void defaultSatisfyCompanion() {
        compareWithJavaSource("iface/DefaultSatisfyCompanion");
    }
    
    @Test
    public void companionSatisfyDefault() {
        compareWithJavaSource("iface/CompanionSatisfyDefault");
    }
    
    @Test
    public void interfaceWithTypeMembers() {
        compareWithJavaSource("iface/InterfaceWithTypeMembers");
    }
    
    @Test
    public void mixed() {
        compareWithJavaSource("iface/Mixed");
    }
    
    @Test
    public void invokingObjectMethods() {
        compareWithJavaSource("iface/InvokingObjectMethods");
    }
}

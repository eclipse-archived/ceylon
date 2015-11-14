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
shared interface InterfaceWithAttributes {
 shared formal Integer formalAttr;
 shared formal variable Integer formalAttr2;
 shared default Integer defaultAttr{ return 1; }
 shared Integer concreteAttr{ return 1; }
}

shared interface InterfacewithAttributes2 satisfies InterfaceWithAttributes {
    shared actual formal Integer defaultAttr;
}

shared abstract class KlassWithAttributesSuper2() {
 shared formal Integer formalAttr;
 shared formal Integer formalAttr2;
 shared default Integer defaultAttr = 2;
 shared default Integer defaultGetter {return 2;}
 shared default Integer defaultGetterSetter {return 2;} assign defaultGetterSetter {}
}

shared abstract class KlassWithAttributesSuper1() extends KlassWithAttributesSuper2() {
 // we implement a formal attr
 shared actual Integer formalAttr = 1;
 // we give a default impl to a formal attr
 shared actual default Integer formalAttr2 = 1;
 // we make a default attr formal
 shared actual formal Integer defaultAttr;
 shared actual formal Integer defaultGetter;
 shared variable actual formal Integer defaultGetterSetter;
}


shared class KlassWithAttributes() extends KlassWithAttributesSuper1() {
    Integer n1 = 1;
    shared Integer n2 = 2;
    variable Integer n3 = 3;
    shared variable Integer n4 = 4;
    Integer n5 {
        return 5;
    }
    shared Integer n6 {
        return 6;
    }
    Integer n7 {
        return 7;
    }
    assign n7 {
    }
    shared Integer n8 {
        return 8;
    }
    assign n8 {
    }
    shared variable String s = ""; 
    shared variable Integer i = +1; 
    shared variable Boolean b = true; 
    shared variable Float f = 1.0; 
    
    void capture() {
        value x = n1;
        value y = n3;
        value z = n5;
    }

    shared late String lateString;
    shared late variable String lateVariableString;

    // override all formal attrs
    shared actual Integer formalAttr2 = 3;
    shared actual Integer defaultAttr = 3;
    shared actual Integer defaultGetter {return 3;}
    shared actual Integer defaultGetterSetter {return 3;} assign defaultGetterSetter {}
    
    // special attributes
    shared actual variable String string = "123";
    shared actual variable Integer hash = 123;
    
    shared variable Integer _M_PI = 2;
    shared variable Integer _MPI = 2;
    shared variable Integer mPI = 2;
}

shared class KlassWithAttributesAsParameters(foo, bar, gee) {
    shared Boolean foo;
    Boolean bar;
    variable shared Boolean gee;
}
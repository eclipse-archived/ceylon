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
shared interface InterfacewithMethods {
    shared formal void formalMethod(Integer p1, String p2);
    shared formal void varargs(Integer* args);
    shared default void defaultMethod(){}
    shared void concreteMethod(){}
}

shared interface InterfacewithMethods2 satisfies InterfacewithMethods {
    shared actual formal void defaultMethod();
}

shared abstract class KlassWithMethodsSuper2() {
 shared formal void formalMethod();
 shared formal void formalMethod2();
 shared default void defaultMethod(){}
}

shared abstract class KlassWithMethodsSuper1() extends KlassWithMethodsSuper2() {
 // we implement a formal method
 shared actual void formalMethod(){}
 // we give a default impl to a formal method
 shared actual default void formalMethod2(){}
 // we make a default method formal
 shared actual formal void defaultMethod();
}

shared class KlassWithMethods() extends KlassWithMethodsSuper1() {
 void empty(){}
 shared void emptyPublic(){}
 Integer natural(){return 1;}
 shared Integer naturalPublic(){return 1;}
 Integer param(Integer p){return p;}
 shared Integer paramPublic(Integer p){return p;}
 // override all formal methods
 shared actual void formalMethod2(){}
 shared actual void defaultMethod(){}
 
 // varargs
 shared void varargs(Integer* args){}
}

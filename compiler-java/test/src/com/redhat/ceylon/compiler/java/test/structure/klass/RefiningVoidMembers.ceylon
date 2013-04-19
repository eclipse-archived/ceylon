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

@noanno
abstract class RefiningAnythingMembersClass<T>(T t) {
    shared void notRefinable(){}
    
    //shared default void defaultGettervoid {}
    shared default Anything defaultGetterAnything {
        return 1;
    }
    shared default T defaultGetterT {
        return t;
    }
    shared default void defaultReturnsvoidImplicit(){}
    shared default void defaultReturnsvoidExplicit(){return;}
    shared default Anything defaultReturnsAnythingImplicit(){
        return 1;
    }
    shared default Anything defaultReturnsAnythingExplicit(){
        return 1;
    }
    shared default T defaultReturnsT() {
        return t;    
    }
    
    //shared formal void formalGettervoid;
    shared formal Anything formalGetterAnything;
    shared formal T formalGetterT;
    shared formal void formalReturnsvoid();
    shared formal Anything formalReturnsAnything();
    shared formal T formalReturnsT();
}

@noanno
abstract class RefiningAnythingMembersClass_Boxed() extends RefiningAnythingMembersClass<Anything>(1) {
    /*shared actual Anything defaultGettervoid {
        return 1;
    }*/
    shared actual Anything defaultGetterT {
        return 1;
    }
    shared actual Anything defaultReturnsvoidExplicit(){
        return 1;
    }
    shared actual Anything defaultReturnsT() {
        return 1;    
    }
    
    shared actual Anything formalGetterT {
        return 1;
    }
    shared actual Anything formalReturnsvoid() {
        return 1;
    }
    shared actual Anything formalReturnsT() {
        return 1;
    }
}

@noanno
abstract class RefiningAnythingMembersClass_Unboxed() extends RefiningAnythingMembersClass<Anything>(1) {
    /* shared actual void defaultGetterAnything {
    }*/
    shared actual void defaultReturnsAnythingExplicit(){
        return;
    }
    shared actual void defaultReturnsAnythingImplicit(){
    }
    shared actual void defaultReturnsT() {
    }
    /* shared actual void formalGetterAnything {
    };*/
    /* shared actual void formalGetterT {
    };*/
    shared actual void formalReturnsAnything() {}
    shared actual void formalReturnsT() {}
}


@noanno
interface RefiningAnythingMembersInterface<T> {
    
    shared void notRefinable(){}
    
    //shared default void defaultGettervoid {}
    shared default Anything defaultGetterAnything {
        return 1;
    }
    shared default T defaultGetterT {
        return defaultReturnsT();
    }
    shared default void defaultReturnsvoidImplicit(){}
    shared default void defaultReturnsvoidExplicit(){return;}
    shared default Anything defaultReturnsAnythingImplicit(){
        return 1;
    }
    shared default Anything defaultReturnsAnythingExplicit(){
        return 1;
    }
    shared default T defaultReturnsT() {
        return defaultGetterT;
    }
    
    //shared formal void formalGettervoid;
    shared formal Anything formalGetterAnything;
    shared formal T formalGetterT;
    shared formal void formalReturnsvoid();
    shared formal Anything formalReturnsAnything();
    shared formal T formalReturnsT();
}

@noanno
abstract class RefiningAnythingMembersInterface_Boxed() satisfies RefiningAnythingMembersInterface<Anything> {
    shared actual Anything defaultGetterT {
        return 1;
    }
    shared actual Anything defaultReturnsvoidExplicit(){
        return 1;
    }
    shared actual Anything defaultReturnsT() {
        return 1;    
    }
    
    shared actual Anything formalGetterT {
        return 1;
    }
    shared actual Anything formalReturnsvoid() {
        return 1;
    }
    shared actual Anything formalReturnsT() {
        return 1;
    }   
}

@noanno
abstract class RefiningAnythingMembersInterface_Unboxed() satisfies RefiningAnythingMembersInterface<Anything> {
    /* shared actual void defaultGetterAnything {
    }*/
    shared actual void defaultReturnsAnythingExplicit(){
        return;
    }
    shared actual void defaultReturnsAnythingImplicit(){
    }
    shared actual void defaultReturnsT() {
    }
    /* shared actual void formalGetterAnything {
    };*/
    /* shared actual void formalGetterT {
    };*/
    shared actual void formalReturnsAnything() {}
    shared actual void formalReturnsT() {}
}
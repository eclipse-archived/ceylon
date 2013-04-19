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
interface RawInv<T,U> {
    shared formal T a;
    shared default T aDefault { 
        throw; 
    }
    assign aDefault {
    }
    shared formal T m(T t);
    shared default T mDefault(T t) {
        throw;
    }
    shared formal T mDefaulted(T? t=null);
    
    shared formal U a2;
}
@noanno
class RawInvImplementor<X,Y>() satisfies RawInv<X&Y, Y> {
    shared actual X&Y a {
        throw;
    }
    shared actual Y a2 {
        throw;
    }
    shared actual X&Y m(X&Y t) {
        throw;
    }
    shared actual X&Y mDefaulted(X?&Y? t) {
        throw;
    }
}


@noanno
interface RawIn<in T,U> {
    shared formal void m(T t);
    shared default void mDefault(T t) {
    }
    shared formal void mDefaulted(T? t=null);
    
    shared formal void m2(U u);
}

@noanno
class RawInImplementor<X,Y>() satisfies RawIn<X&Y,Y> {
    shared actual void m2(Y u) {
    }
    shared actual void m(X&Y t) {
    }
    shared actual void mDefaulted(X?&Y? t) {
    }
}


@noanno
interface RawOut<out T,U> {
    shared formal T a;
    shared T aDefault { 
        throw; 
    }
    shared formal T m();
    shared default T mDefault() {
        throw;
    }
    
    shared formal U a2;
}

@noanno
class RawOutImplementor<X,Y>() satisfies RawOut<X&Y,Y> {
    shared actual X&Y a {
        throw;
    }
    shared actual X&Y m() {
        throw;
    }
    shared actual Y a2 {
        throw;
    } 
}
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
class Foo() {
    shared void a() { }
}
@noanno
class Bar() extends Foo() {
    shared void b() { }
}
@noanno
class Baz() extends Foo() {
    shared void c() { }
}

@noanno
class GenInv<T>() {
    shared void a() { }
}
@noanno
class GenCov<out T>() {
    shared void a() { }
}
@noanno
class GenCon<in T>() {
    shared void a() { }
}

// For an ordinary class or interface type T:
// - The Ceylon type Foo<T> appearing in an extends or satisfies clause
//   results in the Java type Foo<T>
@noanno
interface ISats {
    shared formal void z();
}
@noanno
class Sats<T>() given T satisfies ISats {
    shared void a() { }
}
@noanno
class BarSats() extends Bar() satisfies ISats {
    shared actual void z() { }
}

// For the root type Anything:
// - The Ceylon type Foo<Anything> appearing in an extends or satisfies
//   clause results in the Java raw type Foo<Object>
@noanno
class ExtAnything() extends GenInv<Anything>() {
}

// For the nothing type Nothing:
// - The Ceylon type Foo<Nothing> appearing in an extends or satisfies
//   clause results in the Java raw type Foo
@noanno
class ExtNothing() extends GenInv<Nothing>() {
}

@noanno
class Conversions() {
    shared void m() {
        // For an ordinary class or interface type T:
        // - The Ceylon type T results in the Java type T
        Foo c1 = Foo();
        c1.a();
        
        // For an optional type T?:
        //  - The Ceylon type T? results in the Java type T
        Integer? n1 = 1;
        Integer? n2 = null;
        
        // For any other union type U|V (U nor V is Optional):
        // - The Ceylon type U|V results in the Java type Object
        Bar|Baz u1 = Bar();
        u1.a();
        if (is Bar u1) {
            u1.a();
            u1.b();
        }
        
        // For any other intersection type U|V:
        // - The Ceylon type U&V results in the Java type Object
        Bar&ISats i1 = BarSats();
        i1.a();
        i1.b();
        i1.z();
                
        // For an erased type:
        // - Any of the Ceylon types Anything, Object, Nothing, Equality,
        //   Basic, and Nothing result in the Java type Object
        Anything e1;
        Object e2;
        Nothing e3;
        Basic e4;
        Nothing e5;
        
        // For an ordinary class or interface type T:
        // - The Ceylon type Foo<T> appearing anywhere else results in the Java type
        // - Foo<T> if Foo is invariant in T,
        // - Foo<? extends T> if Foo is covariant in T, or
        // - Foo<? super T> if Foo is contravariant in T
        GenInv<String> g1;
        GenCov<String> g2;
        GenCon<String> g3;
        
        // For an optional type T?:
        // - The Ceylon type Foo<T?> results in the Java type Foo<T>.
        GenInv<String?> g4;
        
        // For any other union type U|V:
        // - The Ceylon type Foo<U|V> results in the raw Java type Foo.
        GenInv<String|Integer> g5;
        
        // For any other intersection type U&V:
        // - The Ceylon type Foo<U&V> results in the raw Java type Foo.
        GenInv<String&Integer> g6;
        
        // For the root type Anything:
        // - The Ceylon type Foo<Anything> appearing anywhere else results in the Java type
        // - Foo<Object> if Foo<T> is invariant in T
        // - Foo<?> if Foo<T> is covariant in T, or
        // - Foo<Object> if Foo<T> is contravariant in T
        GenInv<Anything> v1;
        GenCov<Anything> v2;
        GenCon<Anything> v3;
        
        // For the nothing type Nothing:
        // - The Ceylon type Foo<Nothing> appearing anywhere else results in the Java type
        // - Foo<T> if Foo is invariant in T,
        // - Foo<? extends T> if Foo is covariant in T, or
        // - Foo<? super T> if Foo is contravariant in T
        GenInv<Nothing> b1;
        GenCov<Nothing> b2;
        GenCon<Nothing> b3;
    }
}

// Test upper bound erasure

@noanno
interface ISats2 {}

@noanno
class UpperBoundErasure1<T>() given T satisfies ISats & ISats2 & Basic & Identifiable {
    void foo<T>() given T satisfies ISats & ISats2 & Basic & Identifiable {
        
    }
}

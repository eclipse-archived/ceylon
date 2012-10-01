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
@nomodel
class Foo() {
    shared void a() { }
}
@nomodel
class Bar() extends Foo() {
    shared void b() { }
}
@nomodel
class Baz() extends Foo() {
    shared void c() { }
}

@nomodel
class GenInv<T>() {
    shared void a() { }
}
@nomodel
class GenCov<out T>() {
    shared void a() { }
}
@nomodel
class GenCon<in T>() {
    shared void a() { }
}

// For an ordinary class or interface type T:
// - The Ceylon type Foo<T> appearing in an extends or satisfies clause
//   results in the Java type Foo<T>
@nomodel
interface ISats {
    shared formal void z();
}
@nomodel
class Sats<T>() given T satisfies ISats {
    shared void a() { }
}
@nomodel
class BarSats() extends Bar() satisfies ISats {
    shared actual void z() { }
}

// For the root type Void:
// - The Ceylon type Foo<Void> appearing in an extends or satisfies
//   clause results in the Java raw type Foo<Object>
@nomodel
class ExtVoid() extends GenInv<Void>() {
}

// For the bottom type Bottom:
// - The Ceylon type Foo<Bottom> appearing in an extends or satisfies
//   clause results in the Java raw type Foo
@nomodel
class ExtBottom() extends GenInv<Bottom>() {
}

@nomodel
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
        // - Any of the Ceylon types Void, Object, Nothing, Equality,
        //   IdentifiableObject, and Bottom result in the Java type Object
        Void e1;
        Object e2;
        Nothing e3;
        IdentifiableObject e4;
        Bottom e5;
        
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
        
        // For the root type Void:
        // - The Ceylon type Foo<Void> appearing anywhere else results in the Java type
        // - Foo<Object> if Foo<T> is invariant in T
        // - Foo<?> if Foo<T> is covariant in T, or
        // - Foo<Object> if Foo<T> is contravariant in T
        GenInv<Void> v1;
        GenCov<Void> v2;
        GenCon<Void> v3;
        
        // For the bottom type Bottom:
        // - The Ceylon type Foo<Bottom> appearing anywhere else results in the Java type
        // - Foo<T> if Foo is invariant in T,
        // - Foo<? extends T> if Foo is covariant in T, or
        // - Foo<? super T> if Foo is contravariant in T
        GenInv<Bottom> b1;
        GenCov<Bottom> b2;
        GenCon<Bottom> b3;
    }
}

// Test upper bound erasure

@nomodel
interface ISats2 {}

@nomodel
class UpperBoundErasure1<T>() given T satisfies ISats & ISats2 & IdentifiableObject & Identifiable {
    void foo<T>() given T satisfies ISats & ISats2 & IdentifiableObject & Identifiable {
        
    }
}

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
shared interface InterfaceA {} 
shared interface InterfaceB {} 
shared interface InterfaceWithTypeParam<T, U> {}

shared class TypeParameters <T,U> (T t, U u) {

 shared T attr = t;
 
 shared T method(T t, U u){
  return t;
 }

 //
 // methods with type params
 
 shared T methodWithTypeParameters<T, U>(T t, U u){
  return t;
 }
 
 //
 // methods with defaulted type params
 
 shared void methodWithDefaultedTypeParameters<A, T = Number | String, U = Number>(){
 }

 //
 // upper bounds tests
 
 shared F methodWithUpperBounds<F>(F f)
  given F satisfies InterfaceA & InterfaceB {
  return f;
 }

 shared F methodWithParameterizedUpperBounds<F>(F f)
  given F satisfies InterfaceWithTypeParam<InterfaceA, InterfaceB> {
  return f;
 }

 shared F methodWithSelfParameterizedUpperBounds<F>(F f)
  given F satisfies InterfaceWithTypeParam<F, F> {
  return f;
 }

 shared F methodWithErasedUpperBounds<F>(F f)
  given F satisfies Basic {
  return f;
 }
 
 //
 // variance tests
 shared O methodWithVariance<in I, out O>(I i)
  given I satisfies O{
  return i;
 }
}

//
// class with defaulted type params

shared class ClassWithDefaultedTypeParameters<A, T = Number | String, U = Number>(){
}

//
// upper bounds tests

shared class ClassWithUpperBounds<F>(F f)
  given F satisfies InterfaceA & InterfaceB {
}

shared class ClassWithParameterizedUpperBounds<F>(F f)
 given F satisfies InterfaceWithTypeParam<InterfaceA, InterfaceB> {
}

shared class ClassWithSelfParameterizedUpperBounds<F>(F f)
 given F satisfies InterfaceWithTypeParam<F, F> {
}

shared class ClassWithErasedUpperBounds<F>(F f)
 given F satisfies Basic {
}
 
//
// variance tests

shared class ClassWithVariance<in I, out O>(I i)
  given I satisfies O{
}

//
// Toplevel methods

//
// methods with type params

shared T methodWithTypeParameters<T, U>(T t, U u){
 return t;
}

//
// methods with defaulted type params

shared void methodWithDefaultedTypeParameters<A, T = Number | String, U = Number>(){
}

//
// upper bounds tests

shared F methodWithUpperBounds<F>(F f)
 given F satisfies InterfaceA & InterfaceB {
 return f;
}
shared F methodWithParameterizedUpperBounds<F>(F f)
 given F satisfies InterfaceWithTypeParam<InterfaceA, InterfaceB> {
 return f;
}
shared F methodWithSelfParameterizedUpperBounds<F>(F f)
 given F satisfies InterfaceWithTypeParam<F, F> {
 return f;
}
shared F methodWithErasedUpperBounds<F>(F f)
 given F satisfies Basic {
 return f;
}

//
// variance tests
shared O methodWithVariance<in I, out O>(I i)
 given I satisfies O{
 return i;
}

//
// case types

shared class ClassWithCaseTypes<T>() 
 given T of Integer | String {
 shared void methodWithCaseTypes<T>() 
  given T of Integer | String {}
}

shared void methodWithCaseTypes<T>() 
 given T of Integer | String {}

//
// inner members and type parameters

shared interface InterfaceContainer<O1> {
    shared interface InnerInterface<O2> {
        shared interface InnerInterface<O3> {}
        shared class InnerClass<O3>() {}
    }
    shared class InnerClass<O2>() {
        shared interface InnerInterface<O3> {}
        shared class InnerClass<O3>() {}
    }
    shared interface InnerInterface2 {}
}

shared interface ClassContainer<O1> {
    shared interface InnerInterface<O2> {
        shared interface InnerInterface<O3> {}
        shared class InnerClass<O3>() {}
    }
    shared class InnerClass<O2>() {
        shared interface InnerInterface<O3> {}
        shared class InnerClass<O3>() {}
    }
    shared interface InnerInterface2 {}
}

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
interface InterfaceA {} 
interface InterfaceB {} 
interface InterfaceWithTypeParam<T, U> {}

class TypeParameters <T,U> (T t, U u) {

 shared T attr = t;
 
 T method(T t, U u){
  return t;
 }

 //
 // methods with type params
 
 T methodWithTypeParameters<T, U>(T t, U u){
  return t;
 }
 
 //
 // upper bounds tests
 
 F methodWithUpperBounds<F>(F f)
  given F satisfies InterfaceA & InterfaceB {
  return f;
 }

 F methodWithParameterizedUpperBounds<F>(F f)
  given F satisfies InterfaceWithTypeParam<InterfaceA, InterfaceB> {
  return f;
 }

 F methodWithSelfParameterizedUpperBounds<F>(F f)
  given F satisfies InterfaceWithTypeParam<F, F> {
  return f;
 }

 F methodWithErasedUpperBounds<F>(F f)
  given F satisfies IdentifiableObject {
  return f;
 }
 
 //
 // lower bounds tests
 F methodWithLowerBounds<F>(F f)
  given F abstracts InterfaceA {
  return f;
 }
}

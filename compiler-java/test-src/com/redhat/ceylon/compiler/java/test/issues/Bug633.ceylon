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
//interface Cov<out T> {
//    shared formal T m();
//}
//interface Contrav<in T> {
//    shared formal void m2(T t);
//}
//class A() satisfies Cov<Number> & Contrav<Number> {
//    shared actual default Number m(){ return 1; }
//    shared actual default void m2(Number t){ }
//}
//class B() extends A() satisfies Cov<Integer> & Contrav<Integer> {
//    shared actual Integer m(){ return 1; }
//    shared actual default void m2(Integer t){ }
//}

class Bug633() extends Object() satisfies Empty {}

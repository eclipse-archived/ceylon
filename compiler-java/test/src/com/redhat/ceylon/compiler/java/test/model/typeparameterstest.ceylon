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
TypeParameters<Integer,String> t1 = TypeParameters<Integer,String>(1, "foo");

ClassWithDefaultedTypeParameters<String> td = ClassWithDefaultedTypeParameters<String>();

class T2() satisfies InterfaceA & InterfaceB {}
ClassWithUpperBounds<T2> t2 = ClassWithUpperBounds(T2());

class T3() satisfies InterfaceWithTypeParam<InterfaceA, InterfaceB>{}
ClassWithParameterizedUpperBounds<T3> t3 = ClassWithParameterizedUpperBounds(T3());

class T4() satisfies InterfaceWithTypeParam<T4, T4>{}
ClassWithSelfParameterizedUpperBounds<T4> t4 = ClassWithSelfParameterizedUpperBounds(T4());

ClassWithErasedUpperBounds<T2> t5 = ClassWithErasedUpperBounds(T2());

ClassWithVariance<Number, Number> t6 = ClassWithVariance<Number, Number>(1);

ClassWithCaseTypes<Integer> t7 = ClassWithCaseTypes<Integer>();

void testToplevelMethodTypeParameters(){
    methodWithDefaultedTypeParameters<String>();
    methodWithUpperBounds(T2());
    methodWithParameterizedUpperBounds(T3());
    methodWithSelfParameterizedUpperBounds(T4());
    methodWithErasedUpperBounds(T2());
    methodWithVariance(1);
    methodWithCaseTypes<Integer>();
}

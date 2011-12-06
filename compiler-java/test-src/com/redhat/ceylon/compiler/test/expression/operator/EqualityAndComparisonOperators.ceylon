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
interface BasicOperatorsA {}
@nomodel
interface BasicOperatorsB {}

@nomodel
shared abstract class MyNatural()
        extends Object()
        satisfies Castable<MyNatural|Integer|Float> &
                  Integral<MyNatural> &
                  Subtractable<MyNatural,Integer> {}

@nomodel
shared class EqualityAndComparisonOperators() {

// M2 b1 := n1 extends Natural;
// M2 b1 := n1 satisfies Object;

    void equalityAndComparisonUnboxed(IdentifiableObject o1, IdentifiableObject o2) {
        variable Boolean sync := o1 === o2;
        variable Natural n1 := 0;
        variable Natural n2 := 0;
        sync := n1 == n2;
        sync := n1 != n2;
        Comparison c = n1 <=> n2;
        sync := n1 < n2;
        sync := n1 > n2;
        sync := n1 <= n2;
        sync := n1 >= n2;
    }

    void equalityAndComparisonBoxed(IdentifiableObject o1, IdentifiableObject o2,
                                    MyNatural n1, MyNatural n2) {
        variable Boolean? sync := o1 === o2;
        sync := n1 == n2;
        sync := n1 != n2;
        Comparison c = n1 <=> n2;
        sync := n1 < n2;
        sync := n1 > n2;
        sync := n1 <= n2;
        sync := n1 >= n2;
    }

    void testInBoxed(Natural n1, Natural n2){
        Boolean b1 = n1 in n1..n2;
    }

    void testInUnboxed(MyNatural n1, MyNatural n2){
        Boolean? b1 = n1 in n1..n2;
    }

    void testIs() {
        variable Boolean sync := false;
        Object foo = sync; 
        // boxing test
        sync := is Boolean sync;
        Boolean? dest = is Boolean true;
        // normal test
        sync := is Boolean foo;
        // trick
        sync := is Nothing foo;
        sync := is Bottom foo;
        sync := is Void foo;
        // unions
        //FIXME: parser doesn't like that 
        // sync := is BasicOperatorsA | BasicOperatorsB foo;
        // intersections
        //FIXME: parser doesn't like that
        // sync := is BasicOperatorsA & BasicOperatorsB foo;
        // erased types
        sync := is Equality foo;
        // type parameters
        //M2: sync := is Sequence<Boolean> foo;
    }
}
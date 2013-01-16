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
shared abstract class EqualityAndComparisonInteger()
        extends Object()
        satisfies Castable<EqualityAndComparisonInteger|Integer|Float> &
                  Integral<EqualityAndComparisonInteger> &
                  Comparable<EqualityAndComparisonInteger> {}

@nomodel
shared class EqualityAndComparisonOperators() {

// M2 b1 = n1 extends Integer;
// M2 b1 = n1 satisfies Object;

    void equalityAndComparisonUnboxed(Basic o1, Basic o2) {
        variable Boolean sync = o1 === o2;
        variable Integer n1 = 0;
        variable Integer n2 = 0;
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    void equalityAndComparisonBoxed(Basic o1, Basic o2,
                                    EqualityAndComparisonInteger n1, EqualityAndComparisonInteger n2) {
        variable Boolean? sync = o1 === o2;
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    void testInBoxed(Integer n1, Integer n2){
        Boolean b1 = n1 in n1..n2;
    }

    void testInUnboxed(EqualityAndComparisonInteger n1, EqualityAndComparisonInteger n2){
        Boolean? b1 = n1 in n1..n2;
    }

    void testIs() {
        variable Boolean sync = false;
        Object foo = sync; 
        // boxing test
        sync = sync is Empty;
        // we used to test boxing stuff like "true is Empty" but that doesn't typecheck anymore now that
        // all the unboxable types are final (boolean,character,integer,float,string), and the typechecker
        // can assert that a type test is either always true or always wrong 
        //Boolean? dest = true is Boolean;
        
        // normal test
        sync = foo is Boolean;
        // unions
        sync = foo is BasicOperatorsA | BasicOperatorsB;
        // intersections
        sync = foo is BasicOperatorsA & BasicOperatorsB;
        // type aliases
        alias Alias1 => BasicOperatorsA | BasicOperatorsB;
        alias Alias2 => BasicOperatorsA & BasicOperatorsB;
        sync = foo is Alias1;
        sync = foo is Alias2;
        // erased types
        sync = foo is Basic;
        sync = foo is Exception;
        // type parameters
        //M5: sync = is Sequence<Boolean> foo;
    }
}
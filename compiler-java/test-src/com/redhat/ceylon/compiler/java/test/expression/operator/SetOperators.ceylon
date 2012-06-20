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
shared class SetOperators() {

    void testSetOperatorsWithSameTypes(Set<Integer> a, Set<Integer> b) {
        variable Set<Integer> x;
        x := a | b;
        x := a & b;
        x := a ^ b;
        x := a ~ b;
        x |= a;
        x &= a;
        x ^= a;
        x ~= a;
    }

    void testSetOperatorsWithDifferentTypes(Set<Integer> a, Set<Float> b) {
        variable Set<Integer|Float> x1 := a | b;
        variable Set<Integer&Float> x2 := a & b;
        variable Set<Integer|Float> x3 := a ^ b;
        variable Set<Integer> x4 := a ~ b;
        x4 &= b;
        x4 ~= b;
    }
    
    void testSetOperatorsAndInvocationOnResult(Set<Integer> a, Set<Float> b) {
        (a | a).contains(1);
        (a & a).contains(1);
        (a ^ a).contains(1);
        (a ~ a).contains(1);
        
        (a | b).contains(3.14);
        (a & b).contains(3.14);
        (a ^ b).contains(3.14);
        (a ~ b).contains(3.14);
    }
    
    void testSetOperatorsPrecedence(Set<Integer> a, Set<Integer> b, Set<Integer> c) {
        variable Set<Integer> x1 := a | b & c;
        variable Set<Integer> x2 := b & c | a;
    }
    
    void testSetOperatorsWithErasedTypes(Set<String> setOfString, Set<Integer | Float> setOfUnionType, Set<Sized & Empty> setOfIntersectionType, Set<Bottom> setOfBottom) {
        variable Set<Integer | Float | String> x1 := setOfUnionType | setOfString;
        variable Set<Integer | Float & String> x2 := setOfUnionType & setOfString;
        variable Set<Integer | Float | String> x3 := setOfUnionType ^ setOfString;
        variable Set<Integer | Float> x4 := setOfUnionType ~ setOfString;
        
        variable Set<Sized & Empty | String> y1 := setOfIntersectionType | setOfString;
        variable Set<Sized & Empty & String> y2 := setOfIntersectionType & setOfString;
        variable Set<Sized & Empty | String> y3 := setOfIntersectionType ^ setOfString;
        variable Set<Sized & Empty> y4 := setOfIntersectionType ~ setOfString;
        
        variable Set<Bottom | String> z1 := setOfBottom | setOfString;
        variable Set<Bottom & String> z2 := setOfBottom & setOfString;
        variable Set<Bottom | String> z3 := setOfBottom ^ setOfString;
        variable Set<Bottom> z4 := setOfBottom ~ setOfString;
    }

}
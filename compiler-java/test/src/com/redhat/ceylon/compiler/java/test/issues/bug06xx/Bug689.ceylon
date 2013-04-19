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
class Bug689<X,Y>(X x, Y y, X&Y xy) {
    
    Sequence<X|Y> parameterisedMethodWithUnion<X,Y>(X x, Y y) {
        return [x,y];
    }
    Sequence<X&Y> parameterisedMethodWithIntersection<X,Y>(X&Y xy) {
        return [xy];
    }
    Sequence<X|Y> methodWithUnion(X x, Y y) {
        return [x,y];
    }
    Sequence<X&Y> methodWithIntersection(X&Y xy) {
        return [xy];
    }
    Sequence<X|Y> attributeWithUnion = [x,y];
    Sequence<X&Y> attributeWithIntersection = [xy];
    variable Sequence<X|Y> variableWithUnion = [x,y];
    variable Sequence<X&Y> variableWithIntersection = [xy];

    Sequence<Sequence<X|Y>> attributeWithUnionWithinTypeArg = [[x,y]];
    Sequence<X|Y>|Empty attributeWithUnionErasedToIterable = [x,y];
    

    variable Number sync;
    sync = parameterisedMethodWithUnion(1,2).first;
    sync = parameterisedMethodWithIntersection(1).first;
    sync = Bug689(1,2,3).methodWithUnion(1,2).first;
    sync = Bug689(1,2,3).methodWithIntersection(1).first;
    sync = Bug689(1,2,3).attributeWithUnion.first;
    sync = Bug689(1,2,3).attributeWithIntersection.first;
    sync = Bug689(1,2,3).variableWithUnion.first;
    sync = Bug689(1,2,3).variableWithIntersection.first;
    sync = (Bug689(1,2,3).variableWithUnion = [1,2]).first;
    sync = (Bug689(1,2,3).variableWithIntersection = [1]).first;
    
    sync = Bug689(1,2,3).attributeWithUnionWithinTypeArg.first.first;
    Number? o = Bug689(1,2,3).attributeWithUnionErasedToIterable.get(0);

    // model loader tests
    sync = Bug689_ModelLoader(1,2,3).parameterisedMethodWithUnion(1,2).first;
    sync = Bug689_ModelLoader(1,2,3).methodWithUnion(1,2).first;
    sync = Bug689_ModelLoader(1,2,3).attributeWithUnion.first;
    sync = Bug689_ModelLoader(1,2,3).variableWithUnion.first;
    sync = (Bug689_ModelLoader(1,2,3).variableWithUnion = [1,2]).first;
    
    value c = "abc".withLeading('d').first;
}
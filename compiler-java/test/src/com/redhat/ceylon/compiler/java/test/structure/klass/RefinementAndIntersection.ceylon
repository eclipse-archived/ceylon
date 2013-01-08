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
interface RefinementAndIntersection_G {
    shared void g(){}
}
@nomodel 
interface RefinementAndIntersection_H {
    shared void h(){}
}
@nomodel
interface RefinementAndIntersection_Co<out T> {
    formal shared T get();
}
@nomodel
class RefinementAndIntersection_SuperCoGood() satisfies RefinementAndIntersection_Co<RefinementAndIntersection_G> {
    default shared actual RefinementAndIntersection_G get() { return nothing; }
}
@nomodel
class RefinementAndIntersection_SubCoGood() extends RefinementAndIntersection_SuperCoGood() satisfies RefinementAndIntersection_Co<RefinementAndIntersection_H> {
    default shared actual RefinementAndIntersection_H&RefinementAndIntersection_G get() { return nothing; }
}

@nomodel
void refinementAndIntersection_method(){
    RefinementAndIntersection_SubCoGood sub = RefinementAndIntersection_SubCoGood();
    // make sure we can call methods of H
    sub.get().g();
    sub.get().h();
}
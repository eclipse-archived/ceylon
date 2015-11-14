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
class Foo(){
    shared interface I{}
    shared class Bar() satisfies I{}
}

class NonBasicNonIdentifiable() extends Object() {
    equals(Object o) => false;
    hash => 1;
}
object nonBasicNonIdentifiable extends Object() {
    equals(Object o) => false;
    hash => 1;
}
class NonBasicButIdentifiable() extends Object() satisfies Identifiable {
    equals(Object o) => false;
    hash => 1;
}
object nonBasicButIdentifiable extends Object() satisfies Identifiable {
    equals(Object o) => false;
    hash => 1;
}

shared void bug706() {
    Anything x1 = Foo().Bar();
    assert(is Identifiable x1, is Basic x1);
    
    class C() {}
    object obj extends C() {}        
    Anything x = obj;
    assert(is Identifiable x, is Basic x);
    
    Anything x2 = NonBasicButIdentifiable();
    assert(is Identifiable x2, !is Basic x2);

    Anything x2o = nonBasicButIdentifiable;
    assert(is Identifiable x2o, !is Basic x2o);
    
    Anything x3 = NonBasicNonIdentifiable();
    assert(!is Identifiable x3, !is Basic x3);

    Anything x3o = nonBasicNonIdentifiable;
    assert(!is Identifiable x3o, !is Basic x3o);
}

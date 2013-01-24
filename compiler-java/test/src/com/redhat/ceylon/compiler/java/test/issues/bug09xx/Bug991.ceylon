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
interface Bug991Iterable{
    shared default void f() {}
}

@nomodel
interface Bug991List satisfies Bug991Iterable {
    shared default actual void f() {}
}

@nomodel
interface Bug991Sequence satisfies Bug991List {
    shared default actual void f() => Bug991List::f();
}

@nomodel
class Bug991SequenceClass() satisfies Bug991List {
    shared default actual void f() => Bug991List::f();
}
/*
@nomodel
interface Top<out E>{
    shared default E e(){ return nothing; }
}
@nomodel
class Middle() satisfies Top<Iterable<Character>>{}
@nomodel
class Bottom() extends Middle() satisfies Top<Sequence<Character>>{
    shared void f(){
        // BUG:
        Sequence<Character> s = e();
    }
}

@nomodel
void bug991(){
    Bottom().f();
}
*/
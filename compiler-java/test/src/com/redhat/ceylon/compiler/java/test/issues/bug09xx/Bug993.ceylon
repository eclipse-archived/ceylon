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
interface Bug993Top<out E>{
    shared default E e() { return nothing; }
}

@noanno
interface Bug993MiddleInterface satisfies Bug993Top<Iterable<Character>> {
}

@noanno
class Bug993MiddleClass() satisfies Bug993Top<Iterable<Character>> {
}

@noanno
class Bug993BottomFromInterface() satisfies Bug993MiddleInterface & Bug993Top<Sequence<Character>>{
    shared void f(){
        Sequence<Character> s1 = e();
        Sequence<Character> s2 = this.e();
        value s = {for(i in e()) for(ii in e()) e().lastIndex};
    }

    shared class Inner(){
        Sequence<Character> s1 = e();
        Sequence<Character> s2 = outer.e();
    }
}

@noanno
class Bug993BottomFromClass() extends Bug993MiddleClass() satisfies Bug993Top<Sequence<Character>>{
    shared void f(){
        Sequence<Character> s1 = e();
        Sequence<Character> s2 = this.e();
        value s = {for(i in e()) for(ii in e()) e().lastIndex};
    }
    
    shared class Inner(){
        Sequence<Character> s1 = e();
        Sequence<Character> s2 = outer.e();
    }
}

@noanno
interface Bug993BottomInterface satisfies Bug993MiddleInterface & Bug993Top<Sequence<Character>>{
    shared void f(){
        Sequence<Character> s1 = e();
        Sequence<Character> s2 = this.e();
        value s = {for(i in e()) for(ii in e()) e().lastIndex};
    }
    
    shared class Inner(){
        Sequence<Character> s1 = e();
        Sequence<Character> s2 = outer.e();
    }
}

@noanno
void bug993Outside(Bug993BottomFromClass b){
    Sequence<Character> s = b.e();
}

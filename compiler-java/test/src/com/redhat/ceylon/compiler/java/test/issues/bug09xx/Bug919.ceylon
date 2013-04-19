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
/*
shared class Duple<out Element, out First, out Rest>(first, rest)
        extends Object()
        satisfies Sequence<Element>
        given First satisfies Element
        given Rest satisfies Element[] {
        
    shared actual First first;
    shared actual Rest&Element[] rest;
    
    shared actual Duple<Element,First,Rest> clone { return nothing; }
    shared actual Element item(Integer i){ return nothing; }
    shared actual Integer lastIndex { return nothing; }
    shared actual Sequence<Element> reversed { return nothing; }
    shared actual Sequence<Element> segment(Integer a, Integer b){ return nothing; }
    shared actual Sequence<Element> span(Integer a, Integer b){ return nothing; }
    shared actual Sequence<Element> spanFrom(Integer a){ return nothing; }
    shared actual Sequence<Element> spanTo(Integer a){ return nothing; }
}
*/

@noanno
class Bug919() {
    void m(){
        value t = Tuple("a", Tuple({}, {}));
        value t2 = ["a", {}];
        value t3 = {{}, {{"a", "b"}, {}, {"c"}, {}, {"d", "e"}}, {}, {{"a", "b"}, {}, {"c"}, {}, {"d", "e"}}, {}};
        value t4 = Tuple({}, Tuple(Tuple("c", {}), {}));
        value t5 = Tuple<Sequential<String>, Empty, Empty>({}, {});
    }
}

@noanno
shared void bug919_2(){
    bug919_3({{{}},{}});
    bug919_3(Tuple(Tuple({}, {}),Tuple({}, {})));
    bug919_3({{}, {{"a", "b"}, {}, {"c"}, {}, {"d", "e"}}, {}, {{"a", "b"}, {}, {"c"}, {}, {"d", "e"}}, {}});
}
@noanno
shared void bug919_3(Iterable<Iterable<Iterable<String>>> iterables){
}
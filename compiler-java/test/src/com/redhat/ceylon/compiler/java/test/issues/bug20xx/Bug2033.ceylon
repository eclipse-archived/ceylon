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
shared interface Iterable<out Element> {
    shared formal
    Iterable<Element> repeat(Integer times = 2);
    
    shared formal
    Anything repeat2(); 
    
    shared formal
    Iterable<Element> repeatAttr;
    
    shared formal class Member(Integer times = 2){}
}

@noanno
shared interface Iterable2<out Element> {
    shared formal
    Iterable2<Element> repeat(Integer times = 2);
    
    shared formal
    Anything repeat2(); 
    
    shared formal
    Iterable2<Element> repeatAttr;
}

@noanno
shared interface List<out Element> satisfies Iterable<Element> {
    shared actual formal
    List<Element> repeat(Integer times);

    shared actual formal
    Integer repeat2(); 

    shared actual formal
    List<Element> repeatAttr;
    
    shared actual formal
    class Member(Integer times) extends super.Member(times){}
}

@noanno
shared interface List2<out Element> satisfies Iterable2<Element> {
    shared actual formal
    List2<Element> repeat(Integer times);
    
    shared actual formal
    Integer repeat2(); 
    
    shared actual formal
    List2<Element> repeatAttr;
}

@noanno
shared interface ListMutator<in Element>
        satisfies List<Anything> {
}

@noanno
shared interface ListMutator2<in Element>
        satisfies List2<Anything> {
}

@noanno
shared abstract class ListMutatorClass<in Element>()
        satisfies List<Anything> {
}

@noanno
shared interface MutableList<Element> satisfies List<Element> & ListMutator<Element> {
}

@noanno
shared interface MutableList2<Element> satisfies List2<Element> & ListMutator2<Element> {
}

@noanno
shared abstract class MutableListClass<Element>() extends ListMutatorClass<Element>() satisfies List<Element> {
}


@noanno
interface MweList1<T> satisfies MutableList<T> {
}

@noanno
abstract class MweList1Class<T>() extends MutableListClass<T>() {
}

@noanno
abstract class MweList2<T>() satisfies MutableList<T> {
}

@noanno
class MweList3<T>() satisfies MutableList2<T> {
    repeat(Integer times) => nothing;
    repeatAttr => nothing;
    repeat2() => 1;
}

//@noanno
//class C() satisfies L2 {
//    //shared actual void 
//    m(Integer i) => print("");
//}
//
//@noanno
//interface L {
//    shared formal void m(Integer i= 1);
//}
//
//@noanno
//interface L2 satisfies L {
//    shared actual formal void m(Integer i);
//}
//


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
class Bug1686() satisfies Enumerable<Bug1686>{
    
    void f(Range<Integer> x, Range<Bug1686> x2) {
        value y = x.withLeading(0);
        value y2 = x2.withLeading(Bug1686());
        print((1..3).withLeading(0).first);
    }
    
    shared actual Bug1686 neighbour(Integer off) => nothing;
    shared actual Integer offset(Bug1686 other) => nothing;
}
/*interface TupleLike<out Element, out First, out Second>
        given First satisfies Element
        given Second satisfies Element {
} 
@noanno
interface Foo<Element> {
    shared formal TupleLike<Other|Element,Other,Element> withLeading<Other>(
        "The first element of the resulting sequence."
        Other element);
    shared formal [Other,Element*] withLeading2<Other>(
        "The first element of the resulting sequence."
        Other element);
}

@error:"satisfies a sealed interface in a different module: Sequence in ceylon.language"
class Bar<Element>() extends Object() satisfies Sequence<Element> {
    shared actual Element first => nothing;
    shared actual Element last => nothing;
    shared actual Element[] rest => nothing;
    shared actual Integer size => nothing;
    shared actual Element getFromFirst(Integer index)  => nothing;
    shared actual Iterator<Element> iterator() => nothing;
}*/
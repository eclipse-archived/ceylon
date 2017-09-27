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
void bug1154() {
    Bug1154Bar<Boolean> b1 = Bug1154Bar(true);
    Bug1154Bar<String> b2 = Bug1154Bar("hello");
    value first = Bug1154(b1, []);
    // cannot find symbol
    //  symbol:   class Element
    //  location: class misc.run_
    value step = first.withLeading(b2);
    // cannot find symbol
    //  symbol:   class Element
    //  location: class misc.run_
    value t = step.tuple;
}

@noanno
shared class Bug1154Bar<T>(shared T element){}

@noanno
shared class Bug1154<Element,First,Rest>(Bug1154Bar<First> first, Rest rest)
 given First satisfies Element
 given Rest satisfies Element[]{ 

 shared alias TupleType => Tuple<First|Element,First,Rest>;

 shared TupleType tuple = Tuple(first.element, rest);

 shared Bug1154<NewFirst|Element,NewFirst,TupleType> withLeading<NewFirst>(Bug1154Bar<NewFirst> newFirst) => Bug1154(newFirst, tuple);

 shared void juu(void f(TupleType params)) => f(tuple);
}

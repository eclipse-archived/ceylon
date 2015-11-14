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
import java.util {
    JMap=Map {
        JEntry=Entry
    }
}

@noanno
abstract class Bug1924<RootTerminal>() {
    class Inner() {}
    shared void construct() {
        class State() {}
        Object o = State();
        // cannot fast fail, rely on reified generics
        Boolean b = o is State;
        Object o2 = Inner();
        // can fast fail, rely on reified generics
        Boolean b2 = o2 is Inner;
    }
}

@noanno
class JavaEntry<K,V>(K->V entry)
        extends Object()
        satisfies JEntry<K,V>
        given K satisfies Object
        given V satisfies Object {

    key => entry.key;
    \ivalue => entry.item;
    shared actual V setValue(V? v) {
        throw Exception("asd");
    }
    shared actual Boolean equals(Object that) {
        if (is JEntry<out Anything,out Anything> that) {
        }
        if (is JMap<out Anything,out Anything> that) {
        }
        if (is JMap<out Anything,out Integer> that) {
        }
        return false;
    }
    shared actual Integer hash => 31;
}

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
class Bug979Iterable({String*} strings) satisfies {String*} {
    shared actual Iterator<String> iterator() { return strings.iterator(); }
}
@nomodel
class Bug979Set({String*} strings) 
    extends Object() 
    satisfies Set<String> {

    shared actual Iterator<String> iterator() { return strings.iterator(); }

    shared actual Set<String> clone => this;

    shared actual Set<String> complement<Other>(Set<Other> set)
            given Other satisfies Object {
        return nothing;
    }

    shared actual Set<String|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object {
        return nothing;
    }

    shared actual Set<String&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object {
        return nothing;
    }

    shared actual Set<String|Other> union<Other>(Set<Other> set)
            given Other satisfies Object {
        return nothing;
    }

}

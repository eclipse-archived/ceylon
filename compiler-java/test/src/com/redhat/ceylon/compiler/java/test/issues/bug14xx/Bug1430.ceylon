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
class Bug1430() satisfies Sequence<Integer> {
    shared actual Integer lastIndex => 0;
    shared actual Integer first => 1;
    shared actual Integer last => 1;
    shared actual Integer size => 1;
    shared actual Iterator<Integer> iterator() => (super of List<Integer>).iterator();
    shared actual Integer[] rest => [];
    shared actual Integer hash => 1;
    shared actual Boolean equals(Object other) => false;
    shared actual Boolean contains(Object element) => element==1;
    shared actual Bug1430 clone() => Bug1430();
    shared actual Sequence<Integer> reversed => this;
    shared actual Integer? get(Integer index) {
        return index==0 then 1 else null;
    }
    shared actual Integer[] segment(Integer from, Integer length) {
        return from==0 && length>0 then this else [];
    }
    shared actual Integer[] span(Integer from, Integer to) {
        return from==0 then this else [];
    }
    shared actual Integer[] spanFrom(Integer from) {
        return from==0 then this else [];
    }
    shared actual Integer[] spanTo(Integer to) {
        return to==0 then [] else this;
    }
}
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
class Bug837<Element>(first, Integer length) 
        extends Object()
        satisfies Sequence<Element> 
        given Element satisfies Ordinal<Element> {

    shared actual Element first;

    shared actual Element last = first;

    shared actual Element? get(Integer index) {
        throw;
    }

    shared actual Integer lastIndex {
        return length-1;
    }

    shared actual Element[] rest { 
        return Bug837(first.successor, length-1); 
    }

    shared actual Sequence<Element> reversed {
        //TODO!
        throw;
    }

    shared actual Element[] segment(Integer from, Integer length) {
        throw;
    }

    shared actual Element[] span(Integer from, Integer to) {
        throw;
    }
    shared actual Element[] spanFrom(Integer from) {
        throw;
    }
    shared actual Element[] spanTo(Integer to) {
        throw;
    }

    shared actual Sequence<Element> clone { 
        return this; 
    }
    
    shared actual Iterator<Element> iterator() => nothing;
    shared actual Integer size => nothing;

    shared actual Boolean contains(Object o) => nothing;
}
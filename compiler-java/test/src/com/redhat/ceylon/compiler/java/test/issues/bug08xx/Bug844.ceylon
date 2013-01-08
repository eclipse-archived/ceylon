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
// simplest test case
@nomodel
shared class Bug844_Top<out Element>(){
    shared default Element first(){ return nothing; }
    shared default Element firstAttribute = nothing;
}
@nomodel
shared class Bug844_Bottom<out Element, out First>(firstAttribute) extends Bug844_Top<Element>()
    given First satisfies Element {
    shared actual First first(){ return nothing; }
    shared actual First firstAttribute;
}

// full error report
@nomodel
shared abstract class Bug844_Tuple<out Element, out First, out Rest>(first, rest)
        extends Object()
        satisfies Sequence<Element>
        given First satisfies Element
        given Rest of Empty|Sequence<Element> {

    shared actual First first;
    shared actual Rest&Element[] rest;

    shared actual String string { 
        return "(" first?.string else "null" ", " rest.string[1...] "";
    }

    shared actual Element? item(Integer index) {
        switch (index<=>0)
        case (smaller) { return null; }
        case (equal) { return first; }
        case (larger) { return rest.item(index-1); }
    }

    shared actual Integer lastIndex {
        if (exists restLastIndex = rest.lastIndex) {
            return restLastIndex+1;
        }
        else {
            return 0;
        }
    }

    shared actual Sequence<Element> reversed {
        return rest.reversed.withTrailing(first);
    }

    shared actual Element[] segment(Integer from, Integer length) {
        return from<=0 then rest[0:length+from-1].withLeading(first) 
                else rest[from-1:length];
    }

    shared actual Element[] span(Integer from, Integer end) {
        return from<=end then this[from:end-from+1] 
                else this[end:from-end+1].reversed.sequence;
    }

    shared actual Element[] spanFrom(Integer from) { return nothing; }
    shared actual Element[] spanTo(Integer to) { return nothing; }

    shared actual Sequence<Element> clone { 
        return this; 
    }
}

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
class SequenceImpl<out Element>() satisfies Sequence<Element> {

    shared actual Integer lastIndex = bottom;
    
    shared actual Element first = bottom;

    shared actual Element[] rest = bottom;

    shared actual SequenceImpl<Element> clone = bottom;

    shared actual Integer hash = bottom;

    shared actual Boolean equals(Object other) {
        throw;
    }
    
    shared actual Element last {
        return bottom;
    }
    
    shared actual Element[] segment(Integer from, Integer len) {
        return bottom;
    }
    
    shared actual Element[] spanTo(Integer index) {
        return bottom;
    }
    
    shared actual Element[] spanFrom(Integer index) {
        return bottom;
    }
    
    shared actual Element[] span(Integer from, Integer to) {
        return bottom;
    }
    shared actual Sequence<Element> reversed = bottom;
    shared actual Element? item(Integer index) {
        return bottom;
    }
    
}
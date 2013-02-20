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
class ListImpl<out Element>() extends Object() satisfies List<Element> {
    shared actual ListImpl<Element> clone = nothing;
    shared actual ListImpl<Element> segment(Integer element, Integer length) {
        return nothing;
    }
    shared actual ListImpl<Element> span(Integer from, Integer to) {
        return nothing;
    }
    shared actual ListImpl<Element> spanFrom(Integer from) {
        return nothing;
    }
    shared actual ListImpl<Element> spanTo(Integer to) {
        return nothing;
    }
    shared actual Integer? lastIndex {
        return nothing;
    }
    shared actual Element? get(Integer index) {
        return nothing;
    }
    shared actual ListImpl<Element> reversed {
        throw;
    }
    shared actual Iterator<Element> iterator => nothing;
    shared actual List<Element> rest => nothing;
}
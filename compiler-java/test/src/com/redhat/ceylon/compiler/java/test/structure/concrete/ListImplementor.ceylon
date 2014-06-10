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
class ListImplementor<X>() satisfies List<X> {

    shared actual ListImplementor<X> clone() { 
        throw;
    }
    
    shared actual ListImplementor<X> segment(Integer from, Integer length) {
        throw;
    }
    
    shared actual Integer? lastIndex {
        throw;
    } 
    
    shared actual ListImplementor<X> span(Integer from, Integer to) {
        throw;
    }
    
    shared actual ListImplementor<X> spanFrom(Integer from) {
        throw;
    }
    
    shared actual ListImplementor<X> spanTo(Integer to) {
        throw;
    }
    
    shared actual Integer hash {
        throw;
    }
    
    shared actual Boolean equals(Object other) {
        throw;
    }
    
    shared actual X elementAt(Integer index) {
        throw;
    }
    
    shared actual ListImplementor<X> reversed {
        throw;
    }

    shared actual Iterator<X> iterator() {
        throw;
    }
    
    shared actual List<X> rest => nothing;
}
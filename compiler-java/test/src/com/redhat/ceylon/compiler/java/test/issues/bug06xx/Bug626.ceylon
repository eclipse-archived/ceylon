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
class Bug626() satisfies Iterable<Bottom[]> {
    shared actual Boolean equals(Object that) {return bottom;}
    shared actual Integer hash = bottom;
    shared actual Iterator<Bottom[]> iterator = bottom;
}

@nomodel
void bug626f(Iterable<Iterable<Container<Bottom>|Closeable>> f, Iterable<Iterable<Container<Bottom>&Closeable>> f2, Iterable<Iterable<Bottom>> f3){
    Iterable<Iterable<Object>> i = f;
    Iterable<Iterable<Object>> i2 = f2;
    Iterable<Iterable<Object>> i3 = f3;
    Bottom[] temp = join { iterables = Bug626().sequence; };
}

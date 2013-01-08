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
interface Bug476A{}
@nomodel
interface Bug476B satisfies Bug476A{}
@nomodel
interface Bug476C{}
 
@nomodel
abstract class Bug476Top<out T>() {
    shared formal T m();
    shared formal Bug476Top<T> m2();
    shared formal T attr;
    shared formal T attr2;
}

@nomodel
class Bug476Bottom<T,U>() extends Bug476Top<Bug476A>(){
    shared actual Bug476A&T&U m() {
        return bottom;
    }
    shared actual Bug476Top<Bug476B>&Bug476C m2() {
        return bottom;
    }
    shared actual variable Bug476A&T&U attr = bottom;
    shared actual Bug476A&T&U attr2 {
        return bottom;
    }
    assign attr2 {
    }
}

@nomodel
shared abstract class Bug476<out T>() satisfies List<T> {
    shared actual T[] span(Integer from, Integer to) {
        return bottom;
    }
    shared actual T[] spanFrom(Integer from) {
        return bottom;
    }
    shared actual T[] spanTo(Integer to) {
        return bottom;
    }
    shared actual T[] segment(Integer from, Integer length) {
        return bottom;
    }
    shared actual String string = bottom;
    shared actual Integer hash { return 1; }
    shared actual Boolean equals(Object that) {
        return bottom;
    }
}
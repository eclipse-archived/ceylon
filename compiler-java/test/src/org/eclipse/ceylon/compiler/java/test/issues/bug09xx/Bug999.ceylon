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
interface Bug999Interface<in Key, out Value> {
    shared formal Value? get1(Key k);
    shared formal Value? get2(Key k);
    shared formal Value attr1;
    shared formal Value attr2;
    shared formal Value attr3;
    shared formal Value attr4;
    shared formal Boolean attr5;
    shared formal Boolean attr6;
    shared formal Boolean attr7;
    shared formal Boolean attr8;
}
@noanno
class Bug999(Integer i, Boolean f()) 
        satisfies Bug999Interface<Integer, Boolean> {
    get1(Integer j) => i==j then f();
    shared actual Boolean? get2(Integer j) => i==j then f();
    attr1 => true;
    shared actual Boolean attr2 => true;
    attr3 = true;
    shared actual Boolean attr4 = true;
    attr5 => true;
    shared actual Boolean attr6 => true;
    attr7 = true;
    shared actual Boolean attr8 = true;
}

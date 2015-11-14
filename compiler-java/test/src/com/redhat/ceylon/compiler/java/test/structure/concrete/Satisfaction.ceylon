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
interface I1 {
    shared formal Integer i1();
    shared default Integer m1(Integer n = i1()) {
        return n + i1();
    }
}
@noanno
class CI1() satisfies I1 {
    shared actual Integer i1() {
        return 1;
    }
}
@noanno
interface I2 {
    shared formal Integer i2();
    shared default Integer m2(Integer n = i2()) {
        return n + this.i2();
    }
}
@noanno
interface I3 satisfies I1&I2{
    
}
@noanno
interface I4 {
    shared formal Integer i4();
    shared default Integer m4(Integer n = i4()) {
        return n;
    }
}
@noanno
class CI3() satisfies I1&I2 {
    shared actual Integer i1() {
        return 1;
    }
    shared actual Integer i2() {
        return 2;
    }
    shared actual Integer m2(Integer n) {
        return 2;
    }
}
@noanno
abstract class A7() satisfies I3&I4 {
    shared actual Integer i1() {
        return 1;
    }
    shared actual Integer m4(Integer n) {
        return 5;
    }
}
@noanno
class CA7() extends A7() {
    shared actual Integer i2() {
        return 2;
    }
    shared actual Integer i4() {
        return 4;
    }
}
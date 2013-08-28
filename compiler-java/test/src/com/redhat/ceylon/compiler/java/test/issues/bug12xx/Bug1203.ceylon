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
class Bug1203Super() {
    shared default Integer m(Integer s = 1) { return s; }
}

@noanno
class Bug1203Sub() extends Bug1203Super() {
    shared actual Integer m(Integer s) { return super.m(); }
}

@noanno
interface Bug1203Super2 {
    shared formal Integer m(Integer s = 2);
}

@noanno
class Bug1203Sub2a() satisfies Bug1203Super2 {
    shared actual default Integer m(Integer s) { return 20 + s; }
}

@noanno
abstract class Bug1203Sub2b() satisfies Bug1203Super2 {
}

@noanno
class Bug1203SubSub2a() extends Bug1203Sub2a() {
    shared actual Integer m(Integer s) { return super.m(); }
}

@noanno
interface Bug1203Super3 {
    shared default Integer m(Integer s = 3) { return s; }
}

@noanno
class Bug1203Sub3() satisfies Bug1203Super3 {
    shared actual Integer m(Integer s) { return super.m(); }
}

@noanno
abstract class Bug1203Super4() {
    shared formal Integer m(Integer s = 4);
}

@noanno
class Bug1203Sub4() extends Bug1203Super4() {
    shared actual default Integer m(Integer s) { return 40 + s; }
}

@noanno
class Bug1203SubSub4() extends Bug1203Sub4() {
    shared actual Integer m(Integer s) { return super.m(); }
}

@noanno
interface Bug1203Super5 {
    shared default void m(Integer s = 5) { }
}

@noanno
class Bug1203Sub5() satisfies Bug1203Super5 {
    shared actual default Integer m(Integer s) { return s; }
}

@noanno
class Bug1203SubSub5() extends Bug1203Sub5() {
    shared actual Integer m(Integer s) { return 50 + s; }
}

@noanno
void bug1203() {
    assert(Bug1203Sub().m(0) == 1);
    assert(Bug1203SubSub2a().m(0) == 22);
    assert(Bug1203Sub3().m(0) == 3);
    assert(Bug1203SubSub4().m(0) == 44);
    assert(Bug1203Sub5().m() == 5);
    assert(Bug1203SubSub5().m() == 55);
}

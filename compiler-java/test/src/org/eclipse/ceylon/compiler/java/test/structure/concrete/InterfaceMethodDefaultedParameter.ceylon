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
interface I<T> {
    shared formal void m(Integer i = 1);

    shared formal Integer m2(Integer i = 1);

    shared formal Integer m3(Integer i = 1, Integer n = 2*i);

    shared formal Integer m4(Integer i, Integer n = 2*i);

    shared formal Integer m5<U>(Integer i, T? t = null, U? u = null);
}

@noanno
void positional<T>(I<T> i, I<String> i2) {
    i.m();
    i2.m(2);
    i2.m2();
    i.m2(2);
    i.m3();
    i.m3(5);
    i.m3(4, 5);
    i.m4(5);
    i.m4(4, 5);
    i.m5<String>(5);
    i.m5<String>(5, null);
    i.m5<String>(5, null, "");
}

@noanno
void named<T>(I<T> i, I<String> i2) {
    i.m{};
    i2.m{i=2;};
    i2.m2{};
    i.m2(2);
    i.m3{};
    i.m3{i=5;};
    i.m3{i=4; n=5;};
    i.m3{n=5; i=4;};
    i.m3{n=5;};
    i.m4(5);
    i.m4(4, 5);
    i.m5<String>(5);
    i.m5<String>(5, null);
    i.m5<String>(5, null, "");
}
 
@noanno
class C<T>() satisfies I<T> {
    shared actual void m(Integer i) {}

    shared actual Integer m2(Integer i) { return i;}

    shared actual Integer m3(Integer i, Integer n) { return i;}

    shared actual Integer m4(Integer i, Integer n) { return i;}

    shared actual Integer m5<U>(Integer i, T? t, U? u) { return i;}
}
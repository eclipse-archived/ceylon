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
class C<T>() {
    void m(Integer i = 1) {
    }

    shared Integer m2(Integer i = 1) {
        return i;
    }

    Integer m3(Integer i = 1, Integer n = 2*i) {
        return i;
    }

    Integer m4(Integer i, Integer n = 2*i) {
        return i;
    }

    Integer m5<U>(Integer i, T? t = null, U? u = null) {
        return i;
    }

void positional(C<Boolean> c) {
    c.m();
    c.m(2);
    c.m2();
    c.m2(2);
    c.m3();
    c.m3(5);
    c.m3(4, 5);
    c.m4(5);
    c.m4(4, 5);
    c.m5<String>(5);
    c.m5<String>(5, null);
    c.m5<String>(5, null, "");
}

void named(C<Boolean> c) {
    c.m{};
    m{i=2;};
    m2{};
    m2(2);
    m3{};
    m3{i=5;};
    m3{i=4; n=5;};
    m3{n=5; i=4;};
    m3{n=5;};
    m4(5);
    m4(4, 5);
    c.m5<String>(5);
    m5<String>(5, null);
    m5<String>(5, null, "");
}    
 
 }
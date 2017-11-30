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
void f1(Integer i = 1) {}

@noanno
Integer f2(Integer i = 1) { return i; }

@noanno
Integer f3(Integer i = 1, Integer n = 2*i) { return i; }

@noanno
Integer f4(Integer i, Integer n = 2*i) { return i; }

@noanno
Integer f5<U>(Integer i, U? u = null) {
    return i;
}

@noanno
void positional() {
    f1();
    f1(1);
    f2();
    f2(2);
    f3();
    f3(1);
    f3(1, 2);
    f4(1);
    f4(1, 4);
    f5<String>(1);
    f5<String>(1, "");
}

@noanno
void named() {
    f1{};
    f1{i=1;};
    f2{};
    f2{i=2;};
    f3{};
    f3{i=1;};
    f3{i=1; n=2;};
    f3{n=2; i=1;};
    f3{n=2;};
    f4{i=1;};
    f4{i=1; n=4;};
    f4{n=4; i=1;};
    f5<String>{i=1;};
    f5<String>{i=1; u="";};
    f5<String>{u=""; i=1;};
}
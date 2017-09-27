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
class C1(Integer i = 1) { }

@noanno
class C3(Integer i = 1, Integer n = 2*i) { }

@noanno
class C4(Integer i, Integer n = 2*i) { }

@noanno
class C5<U>(Integer i, U? u = null) { }

@noanno
void positional() {
    C1();
    C1(1);
    C3();
    C3(1);
    C3(1, 2);
    C4(1);
    C4(1, 2);
    C5<String>(1);
    C5<String>(1, "");
}

@noanno
void named() {
    C1{};
    C1{i=1;};
    C3{};
    C3{i=10;};
    C3{i=10; n=20;};
    C3{n=20; i=10;};
    C3{n=10;};
    C4{n=20; i=10;};
    C4{i=10; n=20;};
    C5<String>{i=10;};
    C5<String>{i=10; u="";};
    C5<String>{u=""; i=10; };
}

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
class Fookls() {
    shared Integer prop = 42;
    shared void f1(Integer n = 5) {}
    shared void f2(Integer n, String s = "test") {}
    shared void f3(Integer n = 5, Integer m = n) {}
    shared void f4(Integer n = 5, Integer m = n + 1) {}
    shared void f5(Integer n = prop) {}
}
@nomodel
interface Barface {
    shared formal void f1(Integer n = 5);
    shared formal void f2(Integer n, String s = "test");
    shared formal void f3(Integer n = 5, Integer m = n);
    shared formal void f4(Integer n = 5, Integer m = n + 1);
}
@nomodel
class Barkls() satisfies Barface {
    shared actual void f1(Integer n) {}
    shared actual void f2(Integer n, String s) {}
    shared actual void f3(Integer n, Integer m) {}
    shared actual void f4(Integer n, Integer m) {}
}
@nomodel
void methodInvocationWithDefaultedParameters() {
    Fookls f = Fookls();
    f.f1();
    f.f1(6);
    f.f2(1);
    f.f2(1, "foo");
    f.f3();
    f.f3(6);
    f.f3(6, 9);
    f.f4();
    f.f4(6);
    f.f4(6, 9);
    
    f.f1{};
    f.f1{n=6;};
    f.f2{n=1;};
    f.f2{n=1; s="foo";};
    f.f3{};
    f.f3{n=6;};
    f.f3{m=9; n=6;};
    f.f4{};
    f.f4{n=6;};
    f.f4{m=9; n=6;};
    
    Barface b = Barkls();
    b.f1();
    b.f1(6);
    b.f2(1);
    b.f2(1, "foo");
    b.f3();
    b.f3(6);
    b.f3(6, 9);
    b.f4();
    b.f4(6);
    b.f4(6, 9);
}

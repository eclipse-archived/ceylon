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
class Fookls(init) {
    shared Integer prop = 42;
    Integer privProp = 24;
    Integer init;
    shared default void f1(Integer n = 5) {}
    shared default void f2(Integer n, String s = "test") {}
    shared default void f3(Integer n = 5, Integer m = n) {}
    shared default void f4(Integer n = 5, Integer m = n + 1) {}
    shared default void f5(Integer n = prop) {}
    shared default void f6(Integer n = privProp) {}
    shared default void f7(Integer n = this.prop) {}
    shared default void f8(Integer n = init) {}
    shared default void f9(Integer n = prop.successor) {}
    shared default void fa(Integer n = 5, Integer* seq) {}
}
@noanno
class Foosubkls(Integer init) extends Fookls(init) {
    shared actual void f1(Integer n) {}
    shared actual void f2(Integer n, String s) {}
    shared actual void f3(Integer n, Integer m) {}
    shared actual void f4(Integer n, Integer m) {}
    shared actual void f5(Integer n) {}
    shared actual void f6(Integer n) {}
    shared actual void f7(Integer n) {}
    shared actual void f8(Integer n) {}
    shared actual void f9(Integer n) {}
    shared actual void fa(Integer n, Integer* seq) {}
}
@noanno
interface Barface {
    shared formal Integer prop;
    shared formal void f1(Integer n = 5);
    shared formal void f2(Integer n, String s = "test");
    shared formal void f3(Integer n = 5, Integer m = n);
    shared formal void f4(Integer n = 5, Integer m = n + 1);
    shared formal void f5(Integer n = prop);
    shared formal void f7(Integer n = this.prop);
    shared formal void f9(Integer n = prop.successor);
    shared formal void fa(Integer n = 5, Integer* seq);
}
@noanno
class Barkls() satisfies Barface {
    shared actual Integer prop = 42;
    shared actual void f1(Integer n) {}
    shared actual void f2(Integer n, String s) {}
    shared actual void f3(Integer n, Integer m) {}
    shared actual void f4(Integer n, Integer m) {}
    shared actual void f5(Integer n) {}
    shared actual void f7(Integer n) {}
    shared actual void f9(Integer n) {}
    shared actual void fa(Integer n, Integer* seq) {}
}
@noanno
void methodInvocationWithDefaultedParameters() {
    Fookls f = Fookls(88);
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
    f.f5();
    f.f5(6);
    f.f6();
    f.f6(6);
    f.f7();
    f.f7(6);
    f.f8();
    f.f8(6);
    f.f9();
    f.f9(6);
    f.fa();
    f.fa(6);
    f.fa(6);
    f.fa(6, 1, 2, 3);
    f.fa(6, *[1, 2, 3]);
    
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
    f.f5{};
    f.f5{n=6;};
    f.f6{};
    f.f6{n=6;};
    f.f7{};
    f.f7{n=6;};
    f.f8{};
    f.f8{n=6;};
    f.f9{};
    f.f9{n=6;};
    f.fa{};
    f.fa{seq=[1, 2, 3];};
    f.fa{n=6;};
    f.fa{n=6; seq=[1, 2, 3];};
    
    Foosubkls f2 = Foosubkls(88);
    f2.f1();
    f2.f1(6);
    f2.f2(1);
    f2.f2(1, "foo");
    f2.f3();
    f2.f3(6);
    f2.f3(6, 9);
    f2.f4();
    f2.f4(6);
    f2.f4(6, 9);
    f2.f5();
    f2.f5(6);
    f2.f6();
    f2.f6(6);
    f2.f7();
    f2.f7(6);
    f2.f8();
    f2.f8(6);
    f2.f9();
    f2.f9(6);
    f2.fa();
    f2.fa(6);
    f2.fa(6);
    f2.fa(6, 1, 2, 3);
    f2.fa(6, *[1, 2, 3]);
    
    f2.f1{};
    f2.f1{n=6;};
    f2.f2{n=1;};
    f2.f2{n=1; s="foo";};
    f2.f3{};
    f2.f3{n=6;};
    f2.f3{m=9; n=6;};
    f2.f4{};
    f2.f4{n=6;};
    f2.f4{m=9; n=6;};
    f2.f5{};
    f2.f5{n=6;};
    f2.f6{};
    f2.f6{n=6;};
    f2.f7{};
    f2.f7{n=6;};
    f2.f8{};
    f2.f8{n=6;};
    f2.f9{};
    f2.f9{n=6;};
    f2.fa{};
    f2.fa{seq=[1, 2, 3];};
    f2.fa{n=6;};
    f2.fa{n=6; seq=[1, 2, 3];};
    
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
    b.f5();
    b.f5(6);
    b.f7();
    b.f7(6);
    b.f9();
    b.f9(6);
    b.fa(6);
    b.fa(6);
    b.fa(6, 1, 2, 3);
    b.fa(6, *[1, 2, 3]);
    
    b.f1{};
    b.f1{n=6;};
    b.f2{n=1;};
    b.f2{n=1; s="foo";};
    b.f3{};
    b.f3{n=6;};
    b.f3{m=9; n=6;};
    b.f4{};
    b.f4{n=6;};
    b.f4{m=9; n=6;};
    b.f5{};
    b.f5{n=6;};
    b.f7{};
    b.f7{n=6;};
    b.f9{};
    b.f9{n=6;};
    b.fa{};
    b.fa{seq=[1, 2, 3];};
    b.fa{n=6;};
    b.fa{n=6; seq=[1, 2, 3];};
    
    Barkls b2 = Barkls();
    b2.f1();
    b2.f1(6);
    b2.f2(1);
    b2.f2(1, "foo");
    b2.f3();
    b2.f3(6);
    b2.f3(6, 9);
    b2.f4();
    b2.f4(6);
    b2.f4(6, 9);
    b2.f5();
    b2.f5(6);
    b2.f7();
    b2.f7(6);
    b2.f9();
    b2.f9(6);
    b2.fa(6);
    b2.fa(6);
    b2.fa(6, 1, 2, 3);
    b2.fa(6, *[1, 2, 3]);
    
    b2.f1{};
    b2.f1{n=6;};
    b2.f2{n=1;};
    b2.f2{n=1; s="foo";};
    b2.f3{};
    b2.f3{n=6;};
    b2.f3{m=9; n=6;};
    b2.f4{};
    b2.f4{n=6;};
    b2.f4{m=9; n=6;};
    b2.f5{};
    b2.f5{n=6;};
    b2.f7{};
    b2.f7{n=6;};
    b2.f9{};
    b2.f9{n=6;};
    b2.fa{};
    b2.fa{seq=[1, 2, 3];};
    b2.fa{n=6;};
    b2.fa{n=6; seq=[1, 2, 3];};
}

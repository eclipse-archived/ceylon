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

abstract class NativeClassMismatchSuper() {}

interface NativeClassMismatchSuper1 {
    shared formal void test1(Integer i);
}
interface NativeClassMismatchSuper2 {
    shared formal void test2(Integer i);
}


native class NativeClassMismatch1() {}

native("jvm") class NativeClassMismatch1() {}

native("js") shared class NativeClassMismatch1() {}


native class NativeClassMismatch2(Integer i) {}

native("jvm") class NativeClassMismatch2(Integer i, Boolean b) {}

native("js") class NativeClassMismatch2(String s) {}


native class NativeClassMismatch3() extends NativeClassMismatchSuper() {}

native("jvm") class NativeClassMismatch3() extends NativeClassMismatchSuper() {}

native("js") class NativeClassMismatch3() extends NativeClassMismatchSuper() {}


native class NativeClassMismatch4() satisfies NativeClassMismatchSuper1, NativeClassMismatchSuper2 {
    native shared actual void test1(Integer i);
    native shared actual void test2(Integer i);
}

native("jvm") class NativeClassMismatch4() satisfies NativeClassMismatchSuper1 {
    native("jvm") shared actual void test1(Integer i) {}
}

native("js") class NativeClassMismatch4() satisfies NativeClassMismatchSuper1, NativeClassMismatchSuper2 {
    native("js") shared actual void test1(Integer i) {}
    native("js") shared actual void test2(Integer i) {}
}


native class NativeClassMismatch5() satisfies NativeClassMismatchSuper1 {
    native shared actual void test1(Integer i);
}

native("jvm") class NativeClassMismatch5() satisfies NativeClassMismatchSuper1 {
    native("jvm") shared actual void test1(Integer i) {}
}

native("js") class NativeClassMismatch5() satisfies NativeClassMismatchSuper2 {
    native("js") shared actual void test2(Integer i) {}
}


class NativeClassMismatch6() {}

native class NativeClassMismatch6() {}

native("js") class NativeClassMismatch6() {}


native class NativeClassMismatch7() satisfies NativeClassMismatchSuper1 {
    native shared actual void test1(Integer i);
}

native("jvm") class NativeClassMismatch7() satisfies NativeClassMismatchSuper1 {
    native("js") shared actual void test1(Integer i) {}
}

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

shared abstract class NativeClassMismatchSuper() {}

shared interface NativeClassMismatchSuper1 {
    shared formal void test1(Integer i);
}
shared interface NativeClassMismatchSuper2 {
    shared formal void test2(Integer i);
}


native class NativeClassMismatch1() {}

native("jvm") class NativeClassMismatch1() {}

/*@error*/ native("js") shared class NativeClassMismatch1() {}


native class NativeClassMismatch2(Integer i) {}

/*@error*/ native("jvm") class NativeClassMismatch2(Integer i, Boolean b) {}

native("js") class NativeClassMismatch2(/*@error*/ String s) {}


native class NativeClassMismatch3() extends NativeClassMismatchSuper() {}

native("jvm") class NativeClassMismatch3() extends NativeClassMismatchSuper() {}

native("js") class NativeClassMismatch3() extends NativeClassMismatchSuper() {}


native class NativeClassMismatch4() satisfies NativeClassMismatchSuper1 & NativeClassMismatchSuper2 {
    native shared actual void test1(Integer i);
    native shared actual void test2(Integer i);
}

/*@error*/ native("jvm") class NativeClassMismatch4() satisfies NativeClassMismatchSuper1 {
    native("jvm") shared actual void test1(Integer i) {}
}

native("js") class NativeClassMismatch4() satisfies NativeClassMismatchSuper1 & NativeClassMismatchSuper2 {
    native("js") shared actual void test1(Integer i) {}
    native("js") shared actual void test2(Integer i) {}
}


native class NativeClassMismatch5() satisfies NativeClassMismatchSuper1 {
    native shared actual void test1(Integer i);
}

native("jvm") class NativeClassMismatch5() satisfies NativeClassMismatchSuper1 {
    native("jvm") shared actual void test1(Integer i) {}
}

/*@error*/ native("js") class NativeClassMismatch5() satisfies NativeClassMismatchSuper2 {
    native("js") shared actual void test2(Integer i) {}
}


shared class NativeClassMismatch6() {}

/*@error*/ native shared class NativeClassMismatch6() {}

/*@error*/ native("js") shared class NativeClassMismatch6() {}


native class NativeClassMismatch7() satisfies NativeClassMismatchSuper1 {
    native shared actual void test1(Integer i);
}

/*@error*/ native("jvm") class NativeClassMismatch7() satisfies NativeClassMismatchSuper1 {
    /*@error*/ native("js") shared actual void test1(Integer i) {}
}


native shared class NativeClassMismatch8jvm() satisfies NativeClassMismatchSuper1 {
    native shared actual void test1(Integer i);
}

native("jvm") shared class NativeClassMismatch8jvm() satisfies NativeClassMismatchSuper1 {
    native("jvm") shared actual void test1(Integer i) {
        /*@error*/ NativeClassMismatch8js().test2(i);
    }
}

native shared class NativeClassMismatch8js() satisfies NativeClassMismatchSuper2 {
    native shared actual void test2(Integer i);
}

native("js") shared class NativeClassMismatch8js() satisfies NativeClassMismatchSuper2 {
    native("js") shared actual void test2(Integer i) {
        /*@error*/ NativeClassMismatch8jvm().test1(i);
    }
}

native("jvm")
void nativeClassMismatch8jvm() {
    NativeClassMismatch8jvm().test1(0);
}

native("js")
void nativeClassMismatch8js() {
    NativeClassMismatch8js().test2(0);
}

native class NativeClassMismatch9() {
    native shared void test1(Integer i);
    native shared void test2(Integer i);
    native shared void test3(Integer i);
    native shared void test4(Integer i);
    native shared void test5(Integer i);
}

/*@error*/ native("jvm") class NativeClassMismatch9() {
    native("jvm") shared void test1(Integer i) {}
    native("jvm") shared void test2(/*@error*/ String s) {}
    /*@error*/ native("jvm") shared String test3(Integer i) { return ""; }
    /*@error*/ native("jvm") shared void test4() {}
    /*@error*/ native("jvm") shared void testX(Integer i) {}
    /*@error*/ shared void testY(Integer i) {}
}

/*@error*/ native("js") class NativeClassMismatch9() {
    native("js") shared void test1(Integer i) {}
    native("js") shared void test2(/*@error*/ String s) {}
    /*@error*/ native("js") shared String test3(Integer i) { return ""; }
    /*@error*/ native("js") shared void test4() {}
    /*@error*/ native("js") shared void testX(Integer i) {}
    /*@error*/ shared void testY(Integer i) {}
}

native shared class NativeClassMismatch10(Integer x, Integer y) {
    native shared void test() { privmeth(); }
    native shared Integer foo => privattr;
    native shared void test2();
    native void privmeth();
    native Integer privattr;
}

/*@error*/ native("jvm") shared class NativeClassMismatch10(Integer x, Integer y) {
    native("jvm") shared void test2() {
        test();
        value x = foo;
    }
}

/*@error*/ native("js") shared class NativeClassMismatch10(Integer x, Integer y) {
    native("js") shared void test2() {
        test();
        value x = foo;
    }
}


native shared class NativeClassMismatch11<A,B>() {}

/*@error*/ native("jvm") shared class NativeClassMismatch11<A,B,C>() {}

/*@error*/ native("js") shared class NativeClassMismatch11() {}


native shared class NativeClassMismatch12<A>() given A satisfies Usable {}

/*@error*/ native("jvm") shared class NativeClassMismatch12<B>() {}

/*@error*/ native("js") shared class NativeClassMismatch12<A>() given A satisfies Category {}


native class NativeClassMismatch13() {
    shared void test1(Integer i) {}
    native shared void test2(Integer i) {}
    native shared void test3(Integer i);
}

/*@error*/ native("jvm") class NativeClassMismatch13() {
    /*@error*/ shared void test1(Integer i) {}
    /*@error*/ shared void test2(Integer i) {}
    /*@error*/ shared void test3(Integer i) {}
}

/*@error*/ native("js") class NativeClassMismatch13() {
    /*@error*/ shared void test1(Integer i) {}
    /*@error*/ shared void test2(Integer i) {}
    /*@error*/ shared void test3(Integer i) {}
}

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

abstract class NativeClassExtendsSuper<A,B>(shared A a, B b) {
    shared formal A test();
    shared B test2() { return b; }
}

native class NativeClassExtends<A,B,C>(A a, B b, C c) extends NativeClassExtendsSuper<A,B>(a, b) {
    native shared actual A test();
    native shared C test3() { return c; }
    native shared Integer test4() { return 1; }
}

native("jvm") class NativeClassExtends<A,B,C>(A a, B b, C c) extends NativeClassExtendsSuper<A,B>(a, b) {
    native("jvm") shared actual A test() {
        throw Exception("NativeClassExtends-JVM");
    }
    native("jvm") shared C test3() { return c; }
}

native("js") class NativeClassExtends<A,B,C>(A a, B b, C c) extends NativeClassExtendsSuper<A,B>(a, b) {
    native("js") shared actual A test() {
        throw Exception("NativeClassExtends-JS");
    }
}

shared void testNativeClassExtends() {
    value obj = NativeClassExtends<Integer,String,Boolean>(1, "foo", true);
    value a = obj.test();
    value b = obj.test2();
    value c = obj.test3();
    value d = obj.test4();
}
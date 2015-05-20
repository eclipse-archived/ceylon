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

interface NativeClassSatisfiesSuper1 {
    shared formal void test1(Integer i);
}
interface NativeClassSatisfiesSuper2 {
    shared formal void test2(Integer i);
}

native class NativeClassSatisfies() satisfies NativeClassSatisfiesSuper1, NativeClassSatisfiesSuper2 {
    native shared actual void test1(Integer i);
    native shared actual void test2(Integer i);
}

native("java") class NativeClassSatisfies() satisfies NativeClassSatisfiesSuper1, NativeClassSatisfiesSuper2 {
    native("java") shared actual void test1(Integer i) {
        throw Exception("NativeClassSatisfies-JVM");
    }
    native("java") shared actual void test2(Integer i) {
        test1(i);
    }
}

native("js") class NativeClassSatisfies() satisfies NativeClassSatisfiesSuper1, NativeClassSatisfiesSuper2 {
    native("js") shared actual void test1(Integer i) {
        throw Exception("NativeClassSatisfies-JS");
    }
    native("js") shared actual void test2(Integer i) {
        test1(i);
    }
}

shared void testNativeClassSatisfies() {
    value x = NativeClassSatisfies().test2(0);
}
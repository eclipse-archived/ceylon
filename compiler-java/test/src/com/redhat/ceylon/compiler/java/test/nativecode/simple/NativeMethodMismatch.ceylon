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
native void nativeMethodMismatch1();

native("jvm") void nativeMethodMismatch1() {
}

native("js") shared void nativeMethodMismatch1() {
}

native void nativeMethodMismatch2();

native("jvm") String nativeMethodMismatch2() {
    throw Exception("NativeMethodMismatch-JVM");
}

native("js") Integer nativeMethodMismatch2() {
    throw Exception("NativeMethodMismatch-JS");
}

native void nativeMethodMismatch3(Integer i);

native("jvm") void nativeMethodMismatch3(Integer i, Boolean b) {
    throw Exception("NativeMethodMismatch-JVM");
}

native("js") void nativeMethodMismatch3(String s) {
    throw Exception("NativeMethodMismatch-JS");
}

native shared void nativeMethodMismatch4jvm();

native("jvm") shared void nativeMethodMismatch4jvm() {
    nativeMethodMismatch4js();
}

native shared void nativeMethodMismatch4js();

native("js") shared void nativeMethodMismatch4js() {
    nativeMethodMismatch4jvm();
}

native("jvm")
void testjvm() {
    nativeMethodMismatch4jvm();
}

native("js")
void testjs() {
    nativeMethodMismatch4js();
}

native shared void nativeMethodMismatch5<A,B>();

native("jvm") shared void nativeMethodMismatch5<A,B,C>() {
}

native("js") shared void nativeMethodMismatch5() {
}

native shared void nativeMethodMismatch6<T>() given T satisfies Usable;

native("jvm") shared void nativeMethodMismatch6<T>() {
}

native("js") shared void nativeMethodMismatch6<T>() given T satisfies Category {
}

void test() {
}

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

native void nativeMethodMismatch4jvm();

native("jvm") void nativeMethodMismatch4jvm() {
    nativeMethodMismatch4js();
}

native void nativeMethodMismatch4js();

native("js") void nativeMethodMismatch4js() {
    nativeMethodMismatch4jvm();
}

void test() {
    nativeMethodMismatch4jvm();
    nativeMethodMismatch4js();
}
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
native shared class NativeClassShared() {
    native shared Integer test(Integer i);
    native shared Integer foo;
    native shared Integer bar;
    native assign bar;
}

native("jvm") shared class NativeClassShared() {
    native("jvm") shared Integer test(Integer i) {
        throw Exception("NativeClassShared-JVM");
    }
    native("jvm") shared Integer foo => 0;
    native("jvm") shared Integer bar => 0;
    native("jvm") assign bar { test(0); }
}

native("js") shared class NativeClassShared() {
    native("js") shared Integer test(Integer i) {
        throw Exception("NativeClassShared-JS");
    }
    native("js") shared Integer foo => 0;
    native("js") shared Integer bar => 0;
    native("js") assign bar {test(0); }
}

void testNativeClassShared() {
    value x = NativeClassShared().foo;
    value y = NativeClassShared().bar;
    NativeClassShared().bar = x;
}

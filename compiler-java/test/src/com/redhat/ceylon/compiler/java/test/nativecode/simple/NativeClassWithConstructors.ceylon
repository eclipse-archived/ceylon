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
native
shared class NativeClassWithConstructors {
    shared native new (Integer x, Integer y) {}
    shared native new base(Integer x, Integer y) {}
}

native("jvm")
shared class NativeClassWithConstructors {
    native("jvm") shared new (Integer x, Integer y) {}
    native("jvm") shared new base(Integer x, Integer y) {}
}

native("js")
shared class NativeClassWithConstructors {
    native("js") shared new (Integer x, Integer y) {}
    native("js") shared new base(Integer x, Integer y) {}
}


//native
//shared class NativeClassWithConstructors2 {
//    shared native new (Integer x, Integer y);  // SYNTAX ERROR
//    shared native new base(Integer x, Integer y);  // SYNTAX ERROR
//}
//
//native("jvm")
//shared class NativeClassWithConstructors2 {
//    native("jvm") shared new (Integer x, Integer y) {}
//    native("jvm") shared new base(Integer x, Integer y) {}
//}
//
//native("js")
//shared class NativeClassWithConstructors2 {
//    native("js") shared new (Integer x, Integer y) {}
//    native("js") shared new base(Integer x, Integer y) {}
//}


native
shared class NativeClassWithConstructors3 {
    shared native new (Integer x, Integer y) {}
    shared native new base(Integer x, Integer y) {}
}

native("jvm")
shared class NativeClassWithConstructors3 {
}

native("js")
shared class NativeClassWithConstructors3 {
}


void testNativeClassWithConstructors() {
    value klza = NativeClassWithConstructors(1, 2);
    value klzb = NativeClassWithConstructors.base(1, 2);
    value klz3a = NativeClassWithConstructors3(1, 2);
    value klz3b = NativeClassWithConstructors3.base(1, 2);
    throw Exception("NativeClassWithConstructors-JVM");
}

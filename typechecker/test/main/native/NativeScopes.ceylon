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
native shared class NativeScopes() {
    native shared Integer foo() {
        return 1;
    }
    native shared void test();
}

native("jvm") shared class NativeScopes() {
    native("jvm") shared void test() {
        native("jvm") void test2() {
            native("jvm") void test3() {
                value x = foo();
            }
        }
        native("jvm") class Bar() {
            native("jvm") class Bar2() {
                native("jvm") void test() {
                    value x = foo();
                }
            }
        }
        throw Exception("NativeScopes-JVM");
    }
}

native("js") shared class NativeScopes() {
    native("js") shared void test() {
        native("js") void test2() {
            native("js") void test3() {
                value x = foo();
            }
        }
        native("js") class Bar() {
            native("js") class Bar2() {
                native("js") void test() {
                    value x = foo();
                }
            }
        }
        throw Exception("NativeScopes-JS");
    }
}

void testNativeScopes() {
    NativeScopes().test();
}

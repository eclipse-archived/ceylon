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
@nomodel
interface Common {
    shared formal void common();
}

@nomodel 
interface FooInterface {
    shared formal void foo();
}

@nomodel
class Foo() satisfies Common & FooInterface {
    shared actual void common() {}
    shared actual void foo() {}
}
@nomodel
class FooSub() extends Foo() {
    shared void foo2() {}
}

@nomodel
interface BarInterface {
    shared formal void bar();
}
@nomodel
class Bar() satisfies Common & BarInterface {
    shared actual void common() {}
    shared actual void bar() {}
}
@nomodel
class MethodIfIs() {
    shared void m(Object? x) {
        if (is Foo x) {
            x.foo();
            if (is FooSub x) {
                x.foo2();
            }
        }
        if (is Foo|Bar x) {
            x.common();
        }
        if (is Foo&BarInterface x) {
            x.common();
            x.foo();
            x.bar();
        }
        if (is Nothing x) {
        }
        if (is FooSub y = give()) {
            y.foo();
        }
        if (is Nothing y = give()) {
        }
        Void nothing = null;
        if (is Character? nothing) {
        }
        //!is
        if (!is Object x) {
            print("x is null");
        }
        if (!is Nothing x) {
            print(x.string);
        }
        String|Integer aint1 = "s";
        String|Integer aint2 = 2;
        if (!is Integer aint1) {
            print(aint1.reversed);
        }
        if (!is String aint2) {
            print(aint2-2);
        }
        /*if (!is Integer aint1, aint1.size==1) {
            print(aint1.uppercased);
        }
        if (!is String aint2, aint2>0) {
            print(aint2/2);
        }*/
    }
    Foo? give() {
        return FooSub();
    }
}

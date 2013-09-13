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

@noanno
String bug1286_getterSetter {
    String getterSetter {
        Consumer<Object> times1 = nothing;
        Consumer<String> objects1 = times1;
        interface Consumer<in E> {
            shared void add(E e) {}
        }
        return "";
    }
    assign getterSetter {
        Consumer<Object> times1 = nothing;
        Consumer<String> objects1 = times1;
        interface Consumer<in E> {
            shared void add(E e) {}
        }
    }
    return "";
}
@noanno
assign bug1286_getterSetter {
    object objectDefinition extends Object() {
        shared actual Integer hash = 0;
        shared actual Boolean equals(Object other) => false;
        Consumer<Object> times1 = nothing;
        Consumer<String> objects1 = times1;
        interface Consumer<in E> {
            shared void add(E e) {}
        }
    }
}
@noanno
object bug1286_objectDefinition extends Object() {
    shared actual Integer hash = 0;
    shared actual Boolean equals(Object other) => false;
    void m(String setterArgument, Anything methodArgument(), Object objectArgument) {}
    m{
        value setterArgument {
            Consumer<Object> times1 = nothing;
            Consumer<String> objects1 = times1;
            interface Consumer<in E> {
                shared void add(E e) {}
            } 
            return ""; 
        }
        void methodArgument() {
            Consumer<Object> times1 = nothing;
            Consumer<String> objects1 = times1;
            interface Consumer<in E> {
                shared void add(E e) {}
            }
        }
        object objectArgument extends Object() {
            shared actual Integer hash = 0;
            shared actual Boolean equals(Object other) => false;
            Consumer<Object> times1 = nothing;
            Consumer<String> objects1 = times1;
            interface Consumer<in E> {
                shared void add(E e) {}
            }
        }
    };
}
@noanno
Object bug1286_method(Object objectArgument) {
    return bug1286_method{
        objectArgument = bug1286_method{
            object objectArgument extends Object() {
                shared actual Integer hash = 0;
                shared actual Boolean equals(Object other) => false;
                Consumer<Object> times1 = nothing;
                Consumer<String> objects1 = times1;
                interface Consumer<in E> {
                    shared void add(E e) {}
                }
            }
        };
    };
}
@noanno
class Bug1286_initializer(Object objectArgument) {
    bug1286_method{
        objectArgument = bug1286_method{
            object objectArgument extends Object() {
                shared actual Integer hash = 0;
                shared actual Boolean equals(Object other) => false;
                Consumer<Object> times1 = nothing;
                Consumer<String> objects1 = times1;
                interface Consumer<in E> {
                    shared void add(E e) {}
                }
            }
        };
    };
}


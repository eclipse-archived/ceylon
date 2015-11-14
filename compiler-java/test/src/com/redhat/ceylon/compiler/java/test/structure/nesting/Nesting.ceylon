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
void func<T,U>(T? t = null, U? u = null) {
}

class C<T>(T? t = null) {
    void m<U>(T? t = null, U? u = null){}
}

interface I<T> {
    shared formal void m<U>(T? t = null, U? u = null);
}

class OC<T>(T t) {
    T m() {
        return t;
    }
    class IC<U>(T? t = null, U? u = null) {
        void m<V>(T? t = null, U? u = null, V? v = null) {}
        interface III<V> {
            shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
        }
        class IIC<V>(V? v = null) {
            void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
        }
    }
    interface II<U> {
        shared formal void m<V>(T? t = null, U? u = null, V? v = null);
        interface III<V> {
            shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
        }
        class IIC<V>(V? v = null) {
            void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
        }
    }
}

interface OI<T> {
    shared formal T m();
    class IC<U>(T? t = null, U? u = null) {
        void m<V>(T? t = null, U? u = null, V? v = null) {}
        interface III<V> {
            shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
        }
        class IIC<V>(V? v = null) {
            void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
        }
    }
    interface II<U> {
        shared formal void m<V>(T? t = null, U? u = null, V? v = null);
        interface III<V> {
            shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
        }
        class IIC<V>(V? v = null) {
            void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
        }
    }
}

class Local<R>(R? r = null) {
    void local<S>(S? s = null) {
        void func<T,U>(R? r = null, S? s = null, T? t = null, U? u = null) {
        }
        
        class C<T>(R? r = null, S? s = null, T? t = null) {
            void m<U>(R? r = null, S? s = null, T? t = null, U? u = null){}
        }
        
        interface I<T> {
            shared formal void m<U>(R? r = null, S? s = null, T? t = null, U? u = null);
        }
        
        class OC<T>(R? r = null, S? s = null, T? t = null) {
            T? m() {
                return t;
            }
            class IC<U>(R? r = null, S? s = null, T? t = null, U? u = null) {
                void m<V>(R? r = null, S? s = null, T? t = null, U? u = null, V? v = null) {}
                interface III<V> {
                    shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
                }
                class IIC<V>(V? v = null) {
                    void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
                }
            }
            interface II<U> {
                shared formal void m<V>(R? r = null, S? s = null, T? t = null, U? u = null, V? v = null);
                interface III<V> {
                    shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
                }
                class IIC<V>(V? v = null) {
                    void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
                }
            }
        }
        
        interface OI<T> {
            shared formal T m();
            class IC<U>(R? r = null, S? s = null, T? t = null, U? u = null) {
                void m<V>(R? r = null, S? s = null, T? t = null, U? u = null, V? v = null) {}
                interface III<V> {
                    shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
                }
                class IIC<V>(V? v = null) {
                    void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
                }
            }
            interface II<U> {
                shared formal void m<V>(R? r = null, S? s = null, T? t = null, U? u = null, V? v = null);
                interface III<V> {
                    shared formal void m<W>(T? t = null, U? u = null, V? v = null, W? w = null);
                }
                class IIC<V>(V? v = null) {
                    void m<W>(T? t = null, U? u = null, V? v = null, W? w = null){}
                }
            }
        }
    }
}
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
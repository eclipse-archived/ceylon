shared class LocalKlass() {
    void m() {
        class Inner<T>() satisfies Sized {
            shared Integer i1 = 1;
            shared Integer i2 {
                return 1;
            }
            assign i2 {
            }
            shared void m2(Integer arg1, T... t) {
            }
            shared actual Integer size {
                return 0;
            }
            shared actual Boolean empty {
                return true;
            }
        }
    }
}
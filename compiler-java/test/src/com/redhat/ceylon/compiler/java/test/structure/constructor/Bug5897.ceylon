void bug5897() {
    class C() {
        shared class D {
            shared new create() {}
        }
    }
    value c = C();
    value d = c.D.create(); // Incompatible types
    // required: C.D
    // found:    java.lang.Object
    
    class C2() {
        shared class D {
            shared new create() {}
        }
    }
    value c2 = C2();
    value d2 = C2.D.create(c2)();
}
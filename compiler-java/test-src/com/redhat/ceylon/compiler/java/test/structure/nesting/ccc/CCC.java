package com.redhat.ceylon.compiler.java.test.structure.nesting.ccc;

class C {
    private void m1() {}
    class CC {
        private final C $outer() { return C.this; }
        class CCC {
            final CC $outer() { return CC.this; }
            void m2() {
                m1();
            }
        }
    }
}

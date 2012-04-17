package com.redhat.ceylon.compiler.java.test.structure.nesting.cci;

class C {
    private void m1() {}
    class CC {
        private final C $outer() { return C.this; }
        class CCI$impl {
            void m2(CC $outer) {
                $outer.$outer().m1();
            }
        }
    }
}
interface CCI {// implicitly static, so top level
}
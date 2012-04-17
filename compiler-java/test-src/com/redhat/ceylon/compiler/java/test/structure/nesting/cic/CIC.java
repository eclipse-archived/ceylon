package com.redhat.ceylon.compiler.java.test.structure.nesting.cic;

class C {
    private void m1() {}
    class CI$impl {
    }
    class CIC {
        private final CI $outer;
        private final CI $outer() { return $outer; }
        CIC(CI $outer) {
            this.$outer = $outer;
        }
        void m2() {
            this.$outer().$outer().m1();
        }
    }
}
interface CI {
    public C $outer();
}
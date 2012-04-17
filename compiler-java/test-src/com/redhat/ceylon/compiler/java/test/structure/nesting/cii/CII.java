package com.redhat.ceylon.compiler.java.test.structure.nesting.cii;

class C {
    private void m1() {}
    class CI$impl {
        private C $outer;
        private final C $outer() { return this.$outer; }
        CI$impl(C $outer) {
            this.$outer = $outer;
        }
        class CII$impl {
            private final CI $outer;
            private final CI $outer() {return this.$outer;}
            CII$impl(CI $outer) {
                this.$outer = $outer;
            }
            void m2() {
                $outer().$outer().m1();
            }
        }
    }
}
interface CI {
    public C $outer();
}
interface CII {
    public CI $outer();
    public void m2();
}
package com.redhat.ceylon.compiler.java.test.structure.nesting.icc;

interface I {
    public void m1();
}
class I$impl {
    
}
class IC {
    private final I $outer;
    private final I $outer() { return $outer;}
    IC(I $outer) {
        this.$outer = $outer;
    }
    class ICC {
        private final IC $outer() { return IC.this;}
        public void m2() {
            $outer().$outer().m1();
        }
    }
}
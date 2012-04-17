package com.redhat.ceylon.compiler.java.test.structure.nesting.iii;

interface I {
    public void m1();
}
interface II {
    public I $outer();
}
interface III {
    public II $outer();
    public void m2();
}
class I$impl {
    class II$impl {
        private final II $this;
        private final I $outer;
        private final I $outer() { return this.$outer; }
        II$impl(II $this, I $outer) {
            this.$this = $this;
            this.$outer = $outer;
        }
        class III$impl {
            private final III $this;
            private final II $outer;
            private final II $outer() { return this.$outer; }
            III$impl(III $this, II $outer) {
                this.$this = $this;
                this.$outer = $outer;
            }
            void m2() {
                $outer().$outer().m1();
            }
        }
    }
}

// Implementation

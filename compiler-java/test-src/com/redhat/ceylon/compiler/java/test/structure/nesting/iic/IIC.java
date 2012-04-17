package com.redhat.ceylon.compiler.java.test.structure.nesting.iic;

interface I {
    public void m1();
}
class I$impl {
    void m1() {}
}
interface II {// implicitly static, so top level
    public I $outer();
}
class II$impl {// $impl class nested in place of its interface
    private II $this;
    II$impl(II $this) {
        this.$this = $this;
    }
    class IIC {
        private final II $outer;
        private final II $outer() { return $outer; }
        IIC(II $outer) {
            this.$outer = $outer;
        }
        void m2() {
            this.$outer().$outer().m1();
        }
    }
}
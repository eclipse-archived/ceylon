package com.redhat.ceylon.compiler.java.test.structure.nesting.ici;

interface I {
    public void m1();
}
class I$impl {// companion class is sibling
    
}
class IC {// implicitly static, so sibling
    private final I $outer;
    private final I $outer() { return $outer; }
    IC(I $outer) {
        this.$outer = $outer;
    }
    static class ICI$impl {
        private ICI$impl() {
        }
        void m2(IC $outer) {
            $outer.$outer().m1();
        }
    }
}
interface ICI {// implicitly static, so sibling
    public void m2();
}
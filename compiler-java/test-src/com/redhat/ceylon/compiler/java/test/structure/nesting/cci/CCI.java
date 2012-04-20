package com.redhat.ceylon.compiler.java.test.structure.nesting.cci;

interface C$CC$CCI<T extends ceylon.language.String, X extends ceylon.language.Boolean> {
    
    public abstract com.redhat.ceylon.compiler.java.test.structure.nesting.cci.C<? extends T, ? super ceylon.language.Boolean>.CC $outer();
    
    public T m2();
}
class C<T extends ceylon.language.String, X extends ceylon.language.Boolean> {
    
    private final <U>T m1(final X b, final U u) {
        throw new ceylon.language.Exception(null, null);
    }
    
    private final <U>T m1(final X b) {
        return null;
    }
    
    private final <U>U m1$u(final X b) {
        return null;
    }
    
    class CC {
        
        final class C$CC$CCI$impl {
            private final com.redhat.ceylon.compiler.java.test.structure.nesting.cci.C$CC$CCI<? extends T, ? super X> $this;
            
            private final com.redhat.ceylon.compiler.java.test.structure.nesting.cci.C<? extends T, ? super X>.CC $outer() {
                return com.redhat.ceylon.compiler.java.test.structure.nesting.cci.C.CC.this;
            }
            
            public T m2() {
                return m1(null);
            }
            
            C$CC$CCI$impl(com.redhat.ceylon.compiler.java.test.structure.nesting.cci.C$CC$CCI<? extends T, ? super ceylon.language.Boolean> $this) {
                this.$this = $this;
            }
        }
        
        CC() {
        }
    }
    
    C() {
    }
}
/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
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
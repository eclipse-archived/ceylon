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

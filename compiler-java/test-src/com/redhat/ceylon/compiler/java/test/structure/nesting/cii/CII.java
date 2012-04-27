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
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
package com.redhat.ceylon.compiler.java.test.structure.nesting.icc;

interface I {
    public void m1();
}
class I$impl {
    private final I $this;
    I$impl(I $this) {
        this.$this = $this;
    }
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
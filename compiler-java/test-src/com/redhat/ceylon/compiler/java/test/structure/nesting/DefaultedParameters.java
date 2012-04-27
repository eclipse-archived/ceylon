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
package com.redhat.ceylon.compiler.java.test.structure.nesting;

import com.redhat.ceylon.compiler.java.metadata.Ignore;

/* class C(Integer i = 1, String s = "") { */
class C {
    C(Integer i, String s) {}
    @Ignore
    C(Integer i) {this(i, "");}
    @Ignore
    C() { this(1); }
    
    /* void m(Integer i=1, String s="") {} */
    void m(Integer i, String s) {}
    @Ignore
    void m(Integer i) {m(i, "");}
    @Ignore
    void m() { m(1); }
}

/* void f(Integer i=1, String s="") {} */
class f {
    void f(Integer i, String s) {}
    @Ignore
    void f(Integer i) {f(i, "");}
    @Ignore
    void f() {f(1);}
}

interface I {
    /* void m(Integer i=1, String s="") {this.m2();} */
    public void m(Integer i, String s);
    @Ignore
    public void m(Integer i);
    @Ignore
    public void m();
    
    public void m2();
}
class I$impl {
    private I $this;
    I$impl(I $this) {// top level, so no need to generate a $outer parameter
        this.$this = $this;
    }
    public void m(Integer i, String s) {$this.m2();}
    public void m(Integer i) { m(i, m$s(i)); }
    public void m() { m(m$i()); }
    // we'd actually only generate these if the m() was `formal`
    // we can inline them if m() has a concrete implementation
    Integer m$i() { return 1; } 
    String m$s(Integer i) { return ""; }
}

class Implementor implements I {
    private final I$impl $I$impl;
    Implementor() {
        $I$impl = new I$impl(this);
    }

    @Override
    public void m(Integer i, String s) {
        $I$impl.m(i, s);
    }

    @Override
    public void m(Integer i) {
        $I$impl.m(i);
    }

    @Override
    public void m() {
        $I$impl.m();
    }
    
    public void m2() {
        // some impl
    }
}

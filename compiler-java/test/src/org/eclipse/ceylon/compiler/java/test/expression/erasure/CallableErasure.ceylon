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
@noanno
class CE_Foo(){
    Callable<Anything,[]>&Obtainable erasedGetter {
        throw;
    }
    Callable<Anything,[]> nonErasedGetter {
        throw;
    }
    void f() {
        // #1
        erasedGetter();
        nonErasedGetter();
    }
}

@noanno
shared class CE_MethodClass(){
    shared Callable<Integer,[]>&Obtainable erasedMethod = nothing;
    shared Callable<Integer,[]> nonErasedMethod = nothing;
}

@noanno
shared interface CE_MethodInterface {
    shared formal void m1();
    shared formal void m2();
    shared formal void m3();
    shared formal void m4();
}

@noanno
shared class CE_MethodInterfaceImpl(CE_MethodClass c, 
                                    Callable<Integer,[]>&Obtainable erased, 
                                    Callable<Integer,[]> nonErased)
    satisfies CE_MethodInterface {
    // #3
    m1 = c.erasedMethod;
    m2 = c.nonErasedMethod;
    // #4
    m3 = nonErased;
    m4 = erased;
}

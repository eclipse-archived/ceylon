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
interface Bug6404I<T> {
    shared formal void foo();
}

interface Bug6404Gettable<T> {
    shared formal Bug6404I<T> get();
}

class Bug6404C<T>() satisfies Bug6404Gettable<T> {
    //shared actual Bug6404I<T> // works if uncommented
    get() => object satisfies Bug6404I<T> {
        shared actual void foo() {
            assert (this is Bug6404I<T>); // Assertion failed violated this is I<T>
        }
    };
}

shared void bug6404() {
    assert (Bug6404C<String>().get() is Bug6404I<String>); // Assertion failed violated C<String>().get() is I<String>
    Bug6404C<String>().get().foo();
}
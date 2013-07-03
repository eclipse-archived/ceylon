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
interface Bug991Iterable{
    shared default void f() {}
}

@noanno
interface Bug991List satisfies Bug991Iterable {
    shared default actual void f() {}
}

@noanno
interface Bug991Sequence satisfies Bug991List {
    shared default actual void f() => super.f();
}

@noanno
class Bug991SequenceClass() satisfies Bug991List {
    shared default actual void f() => super.f();
    
    void m(){
        // make sure comprehensions are correctly generated too, because they need to use
        // AbstractIterator to avoid generating a supertype accessor method
        value s = [for (i in 1..2) i];
    }
}
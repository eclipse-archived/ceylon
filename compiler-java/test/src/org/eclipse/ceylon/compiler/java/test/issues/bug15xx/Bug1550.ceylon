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
interface Bug1550List {
    shared default [Bug1550List] m() => nothing;
    shared default [Bug1550List] a => nothing;
}

@noanno
class Bug1550MyList() satisfies Bug1550List {
    shared actual [Bug1550MyList] m() => nothing;
    shared actual [Bug1550MyList] a => nothing;
    
    shared void test(){
        m()[0].test();
        a[0].test();
    }
}

@noanno
interface Bug1550List2<out Element> {
    shared default [Bug1550List2<Element>] m1() => nothing;
    shared default [Bug1550List2<Element>] m2() => nothing;
    shared default [Bug1550List2<Element>] a1 => nothing;
    shared default [Bug1550List2<Element>] a2 => nothing;
}

@noanno
class Bug1550L<T>() satisfies Bug1550List2<T> {
    shared actual [Bug1550List2<T>] m1() => super.m1();
    shared actual [Bug1550List2<T>] a1 => super.a1;
    void test(){
        [Bug1550List2<T>] l1 = super.m1();
        [Bug1550List2<T>] l2 = super.a1;
    }
}

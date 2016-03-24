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
shared interface Bug1151_A {
    shared default void methodA( Integer a = 1 ) {
    }
    Anything methodA2( Integer a = 1 ) => null; 
    Anything methodA3( Integer a = 1 ) => null;
}
@noanno
shared class Bug1151_B() {
    shared default void methodB( Integer a = 1 ) {
    }
    Anything methodB2( Integer a = 1 ) => null;
}
@noanno
shared class Bug1151_C() extends Bug1151_B() satisfies Bug1151_A {
    shared actual Integer methodA( Integer a ) {
        return a;
    }
    shared actual Integer methodB( Integer a ) {
        return a;
    }
    Integer methodA2( Integer a ) => a;
    Integer methodB2( Integer a ) => a;
}
void bug1151_callsite() {
    value c = Bug1151_C();
    Integer ia3 = c.methodA();
    assert(1 == ia3);
    
    Integer ib3 = c.methodB();
    assert(1 == ib3);
}

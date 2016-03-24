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
interface Bug1174_I {
    shared default Anything m(Integer a = 1) {
        return null;
    }
    shared default Anything m2(Integer a = 1) => null;
    
    shared formal Anything m3(String? s = null);
    shared formal Anything m4(String? s = null);
    shared formal Anything m5(String? s = null);
}
@noanno
class Bug1174_C() satisfies Bug1174_I {
    shared actual default String m(Integer a) {
        return "foo";
    }
    shared actual default String m2(Integer a) => "foo";
    
    m3(String? s) => null;
    shared actual Anything m4(String? s) => null;
    shared actual Anything m5(String? s) {
        return null;
    }
}
@noanno
void bug1174_callsite() {
    assert("foo" == Bug1174_C().m());
    assert("foo" == Bug1174_C().m2());
}
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
 
shared void stubFunctionWithAssertion1(
    "Lorem ipsum dolor sit amet, consectetur adipisicing elit..." 
    Integer? n, 
    "Donec quis nibh at felis congue commodo..."
    String? s) {
    "parameter n should exists"
    assert(exists n);
    "parameter n should be in range 0..255"
    assert(n >= 0 && n <=255);
    "parameter s should exists"
    assert(exists s);
    "parameter s should not be empty"
    assert(s.size != 0);
}

shared void stubFunctionWithAssertion2(Integer? n, String? s) {
    assert(exists n);
    assert(n >= 0 && n <=255);
    assert(exists s);
    assert(s.size != 0);
    assert(s.size < n);
    
    if( n == 0 ) {
        assert(n == 999); // this assert shouldn't be documented
    }
}

shared class StubClassWithAssertions(shared Integer? n, shared String? s) {

    assert(exists n, 
           exists s,
           0 < n < 123k,
           s.any((Character c) => c.digit));
           
    void f() {}
    
    assert(n == 999); // this assert shouldn't be documented

}
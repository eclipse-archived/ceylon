/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
@nomodel
shared class SequenceOperators() {
    variable Boolean b1 := false;
    variable Boolean b2 := false;
    variable Natural n1 := 0;
    variable Natural n2 := 0;
    variable Integer i1 := +0;
    variable Integer i2 := +0;
    variable Float f1 := 0.0;
    variable Float f2 := 0.0;
    
    void sequence(Correspondence<Natural, String> c1, Correspondence<Natural,String>? c2) {
        variable String? s := c1[n1];
// M2:
//        if (c1 satisfies OpenCorrespondence<Natural, String>) {
//            c1[n1] := s;
//        }
        s :=  c2?[n1];
// M2:        
//        Natural[] indices = {1, 2, 3};
//        variable String[] seq1 := c1[indices];
//        variable Iterable<String> it1 := c1[indices.iterator];
		String[] sequence = {"foo", "bar"};
        String[] subrange = sequence[n1..n2];
/*
        variable String[] upperRange = c1[n1...];
        Natural[] spreadMember = n1[].size;
        variable Iterable<String>[] spreadInvoke = n1[].lines();
        spreadInvoke = n1[].lines{};
*/       
    }
}
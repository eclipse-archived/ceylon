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
@nomodel
shared class SequenceOperators() {
    
    Correspondence<Integer, String> c1 = [""];
    Correspondence<Integer,String>? c2 = [""];
    String[] sequence = {};
    
    T box<T>(T x){ return x; }
    
    void testSequence(Correspondence<Integer, String> c1, Correspondence<Integer,String>? c2, List<String>? c3, 
                      Ranged<String, String> stringRange, Integer&List<String> integerAndList) {
        variable String? s = c1[1];
        s = this.c1[1];
        s = c1[box(1)];
// M5:
//        if (c1 satisfies OpenCorrespondence<Integer, String>) {
//            c1[n1] = s;
//        }
// M?:        
//        Integer[] indices = {1, 2, 3};
//        variable String[] seq1 = c1[indices];
//        variable Iterable<String> it1 = c1[indices.iterator];
        String[] sequence = ["foo", "bar"];
        variable String[] subrange;
        subrange = sequence[1..2];
        subrange = this.sequence[1..2];
        subrange = sequence[box(1)..box(2)];
        subrange = sequence[1:2];
        subrange = this.sequence[1:2];
        subrange = sequence[box(1):box(2)];
        
        // make sure the length is cast to Integer and not String
        String stringRangeRet = stringRange["foo":integerAndList];
        
        variable String[] upperRange;
        upperRange = sequence[1...];
        upperRange = this.sequence[1...];
        upperRange = sequence[box(1)...];
        variable String[] lowerRange;
        lowerRange = sequence[...1];
        lowerRange = this.sequence[...1];
        lowerRange = sequence[...box(1)];
        
        Integer[] spreadMemberWithUnboxedType = sequence*.size;
        Integer[] intSequence = [1];
        Integer[] spreadMemberWithBoxedType = intSequence*.wholePart;
        variable Character?[] spreadInvoke;
        spreadInvoke = sequence*.item(0);
        spreadInvoke = sequence*.item{index = 0;};

        String[] empty = {};
        String[] upperCasedEmpty = empty*.uppercased;
    }
    
    void testString(){
        String string = "";
        Character? c = string[0];
        variable String sync;
        sync = string[0..1];
        sync = string[0:1];
        sync = string[0...];
        sync = string[...1];
    }
}
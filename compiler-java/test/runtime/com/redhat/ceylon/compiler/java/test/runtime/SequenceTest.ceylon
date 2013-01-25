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
shared class SequenceTest () {
  
    @test
    shared void test(){
        String[] stringSequence = ["foo", "bar", "longer"];
        // size is unboxed
        assertEquals({3, 3, 6}, stringSequence*.size);
        
        // wholePart is boxed
        Integer[] intSequence = [3, 3, 6];
        assertEquals({3, 3, 6}, intSequence*.wholePart);
        
        // invoke
        assertEquals({`f`, `b`, `l`}, stringSequence*.get(0));
        assertEquals({`f`, `b`, `l`}, stringSequence*.get{index = 0;});
    }
}

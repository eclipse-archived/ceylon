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
class ArraySequenceIterationStatic() {
    void arrayOfIntegers(ArraySequence<Integer> seq) {
        for (element in seq) {
            print(element + 5);
        }
    }
    
    void arrayOfIntegersWithBreak(ArraySequence<Integer> array) {
        for (element in array) {
            function x() {
                return element + 10;
            }
            if (element == 10) {
                break;
            }
        } else {
            print("foo");
        }
    }
    
    void keyValue(ArraySequence<Integer->Float> array) {
        for (i->f in array) {
            print(i+f);
        }
    }
    
}
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
class RangeIterationStatic<Element>() 
        given Element satisfies Enumerable<Element> {
    
    void rangeOfIntegers(Range<Integer> seq) {
        for (item in seq) {
            print(item + 5);
        }
    }
    
    void rangeOfIntegersWithBreak(Range<Integer> array) {
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
    
    void rangeOfIntegerWithStep(Range<Integer> seq, Integer step) {
        // check this uses the iterator version, which is faster
        for (item in seq.by(step)) {
            print(item);
        }
    }
    void rangeOfElementWithStep(Range<Element> seq, Integer step) {
        for (item in seq.by(step)) {
            print(item);
        }
        for (item in seq.by(step)) {
            print(item);
            break;
        }
    }
    void rangeOfElementWithStepAndDefReturn(Range<Element> seq, Integer step) {
        for (item in seq.by(step)) {
            if (true) {
                return;
            }
        }
    }
    void rangeOfElementWithStepAndObfuscDefReturn(Range<Element> seq, Integer step) {
        for (item in seq.by(step)) {
            print(item);
            if (false) {
                
            } else {
                return;
            }
        }
    }
    void rangeOfElementWithStepAndDefExit(Range<Element> seq, Integer step) {
        for (item in seq.by(step)) {
            if (true) {
                break;
            }
        }
    }
    void rangeOfElementWithStepAndObfuscDefExit(Range<Element> seq, Integer step) {
        for (item in seq.by(step)) {
            print(item);
            if (false) {
                
            } else {
                break;
            }
        }
    }
}
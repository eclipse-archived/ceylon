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
class RangeOp<T>() {
    void int(Integer i1, Integer i2) {   
        Range<Integer> range = i1..i2;
    }
    void t<T>(T t1, T t2) 
            given T satisfies Ordinal<T>&Comparable<T> {
        Range<T> range = t1..t2;
    }
    
}
// #1027
@noanno
abstract class RangeOpEnum(Integer i) of rangeOpE|rangeOpF 
        satisfies Comparable<RangeOpEnum> & Ordinal<RangeOpEnum> {
    shared actual Comparison compare(RangeOpEnum other) 
            => i<=>other.i;
}
@noanno
object rangeOpF extends RangeOpEnum(1) {
    shared actual RangeOpEnum predecessor => rangeOpE;
    shared actual RangeOpEnum successor => rangeOpE;
}
@noanno
object rangeOpE extends RangeOpEnum(2) {
    shared actual RangeOpEnum predecessor => rangeOpF;
    shared actual RangeOpEnum successor => rangeOpF;
}
@noanno
void bug() {
    Range<RangeOpEnum> range = rangeOpE..rangeOpF;
}
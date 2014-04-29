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
class OptimizedForWithSegment(start, length) {
    Integer start;
    Integer length;
    
    void literals() {
        variable Integer sum = 0;
        for (i in 0:10) {
            sum += i;
        }
        for (c in 'a':26) {
            sum += c.integer;
        }
    }
    
    void expressions() {
        variable Integer sum = 0;
        for (i in start:length) {
            sum += i;
        }
        for (i in start+10:length+10) {
            sum += i;
        }
    }
    
    void by() {
        variable Integer sum = 0;
        // positional argument
        for (i in (1:10).by(3)) {
            sum += i;
        }
        // named argument, specifier
        for (i in (1:10).by{step=3;}) {
            sum += i;
        }
        // named argument, getter
        //for (i in (1..10).by{Integer step{ return 3;}}) {
        //    sum += i;
        //}
    }
    
    void disabled() {
        variable Integer sum = 0;
        @disableOptimization
        for (i in 1:10) {
            sum += i;
        }
        @disableOptimization:"SegmentOpIteration"
        for (i in 1:10) {
            sum += i;
        }
    }
    
    void flow() {
        variable Integer sum = 0;
        for (withElse in 1:10) {
            sum += withElse;
        } else {
            sum = 0;
        }
        for (breaks in 1:10) {
            sum += breaks;
            break;
        }
        for (breaksWithElse in 1:10) {
            sum += breaksWithElse;
            break;
        } else {
            sum = 0;
        }
        for (breaksWithElse in 1:10) {
            sum += breaksWithElse;
            if (breaksWithElse == 5) {
                break;
            }
        } else {
            sum = 0;
        }
        
        for (returns in 1:10) {
            sum += returns;
            break;
        }
        for (returnsWithElse in 1:10) {
            sum += returnsWithElse;
            break;
        } else {
            sum = 0;
        }
        for (returnsWithElse in 1:10) {
            sum += returnsWithElse;
            if (returnsWithElse == 5) {
                break;
            }
        } else {
            sum = 0;
        }
        
        for (throws in 1:10) {
            sum += throws;
            break;
        }
        for (throwsWithElse in 1:10) {
            sum += throwsWithElse;
            break;
        } else {
            sum = 0;
        }
        for (throwsWithElse in 1:10) {
            sum += throwsWithElse;
            if (throwsWithElse == 5) {
                break;
            }
        } else {
            sum = 0;
        }
    }
    
    void captured() {
        for (i in 0:10) {
            function x() {
                return i + 10;
            }
        }
    }
    
}
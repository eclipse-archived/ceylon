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
class OptimizedForWithRange(start, end) {
    Integer start;
    Integer end;
    
    void literals() {
        variable Integer sum = 0;
        for (i in 1..10) {
            sum += i;
        }
        for (c in 'a'..'z') {
            sum += c.integer;
        }
    }
    
    void expressions() {
        variable Integer sum = 0;
        for (i in start..end) {
            sum += i;
        }
        for (i in start+10..end+10) {
            sum += i;
        }
    }
    
    void by() {
        variable Integer sum = 0;
        // positional argument
        for (i in (1..10).by(3)) {
            sum += i;
        }
        // named argument, specifier
        for (i in (1..10).by{step=3;}) {
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
        for (i in 1..10) {
            sum += i;
        }
        @disableOptimization:"RangeIteration"
        for (i in 1..10) {
            sum += i;
        }
    }
    
    void flow() {
        variable Integer sum = 0;
        for (withElse in 1..10) {
            sum += withElse;
        } else {
            sum = 0;
        }
        for (breaks in 10..1) {
            sum += breaks;
            break;
        }
        for (breaksWithElse in 10..1) {
            sum += breaksWithElse;
            break;
        } else {
            sum = 0;
        }
        for (breaksWithElse in 10..1) {
            sum += breaksWithElse;
            if (breaksWithElse == 5) {
                break;
            }
        } else {
            sum = 0;
        }
        
        for (returns in 10..1) {
            sum += returns;
            break;
        }
        for (returnsWithElse in 10..1) {
            sum += returnsWithElse;
            break;
        } else {
            sum = 0;
        }
        for (returnsWithElse in 10..1) {
            sum += returnsWithElse;
            if (returnsWithElse == 5) {
                break;
            }
        } else {
            sum = 0;
        }
        
        for (throws in 10..1) {
            sum += throws;
            break;
        }
        for (throwsWithElse in 10..1) {
            sum += throwsWithElse;
            break;
        } else {
            sum = 0;
        }
        for (throwsWithElse in 10..1) {
            sum += throwsWithElse;
            if (throwsWithElse == 5) {
                break;
            }
        } else {
            sum = 0;
        }
    }
    
    void captured() {
        for (i in 0..10) {
            function x() {
                return i + 10;
            }
        }
    }
    
}
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
void rangeOpIterationOptimizationCorrect() {

    void check(Boolean assertion, String message="") {
        if (!assertion) {
            throw Exception ("**** ASSERTION FAILED \"`` message ``\" ****");
        }
    }
    
    void fail(String message) {
        check(false, message);
    }
    
    void checkEq<Equatable>(Equatable expect, Equatable got, String message="") 
        given Equatable satisfies Object {
        if (expect != got) {
            fail("`` expect ``!=`` got ``: `` message ``");
        }
    } 

    value maxInteger = 9223372036854775807;
    value minInteger = -9223372036854775808;

    // First check that Range.by() (whose implementation is also optimized for 
    // Integer) behaves as we expect 
    function unoptimizedRangeWithBy(Integer start, Integer end, Integer step) {
        value unoptimized = ArrayBuilder<Integer>();
        @disableOptimization
        for (i in (start..end).by(step)) {
            //print("unoptimized " i "");
            unoptimized.append(i);
        }
        return unoptimized.sequence();
    }
    
    checkEq([0, 3, 6, 9], unoptimizedRangeWithBy(0, 10, 3), "(0, 10, 3)");
    checkEq([1, 4, 7, 10], unoptimizedRangeWithBy(1, 10, 3), "(1, 10, 3)");
    checkEq([2, 5, 8], unoptimizedRangeWithBy(2, 10, 3), "(2, 10, 3)");
    checkEq([3, 6, 9], unoptimizedRangeWithBy(3, 10, 3), "(3, 10, 3)");
    checkEq([4, 7, 10], unoptimizedRangeWithBy(4, 10, 3), "(4, 10, 3)");
    checkEq([5, 8], unoptimizedRangeWithBy(5, 10, 3), "(5, 10, 3)");
    checkEq([6, 9], unoptimizedRangeWithBy(6, 10, 3), "(6, 10, 3)");
    checkEq([7, 10], unoptimizedRangeWithBy(7, 10, 3), "(7, 10, 3)");
    checkEq([8], unoptimizedRangeWithBy(8, 10, 3), "(8, 10, 3)");
    checkEq([9], unoptimizedRangeWithBy(9, 10, 3), "(9, 10, 3)");
    checkEq([10], unoptimizedRangeWithBy(10, 10, 3), "(10, 10, 3)");
    
    checkEq([10, 7, 4, 1], unoptimizedRangeWithBy(10, 0, 3), "(10, 0, 3)");
    checkEq([10, 7, 4, 1], unoptimizedRangeWithBy(10, 1, 3), "(10, 1, 3)");
    checkEq([10, 7, 4], unoptimizedRangeWithBy(10, 2, 3), "(10, 2, 3)");
    checkEq([10, 7, 4], unoptimizedRangeWithBy(10, 3, 3), "(10, 3, 3)");
    checkEq([10, 7, 4], unoptimizedRangeWithBy(10, 4, 3), "(10, 4, 3)");
    checkEq([10, 7], unoptimizedRangeWithBy(10, 5, 3), "(10, 5, 3)");
    checkEq([10, 7], unoptimizedRangeWithBy(10, 6, 3), "(10, 6, 3)");
    checkEq([10, 7], unoptimizedRangeWithBy(10, 7, 3), "(10, 7, 3)");
    checkEq([10], unoptimizedRangeWithBy(10, 8, 3), "(10, 8, 3)");
    checkEq([10], unoptimizedRangeWithBy(10, 9, 3), "(10, 9, 3)");
    checkEq([10], unoptimizedRangeWithBy(10, 10, 3), "(10, 10, 3)");
    
    checkEq([-10], unoptimizedRangeWithBy(-10, -10, 2), "(-10, -10, 2)");
    checkEq([-10], unoptimizedRangeWithBy(-10, -9, 2), "(-10, -9, 2)");
    checkEq([-10, -8], unoptimizedRangeWithBy(-10, -8, 2), "(-10, -8, 2)");
    checkEq([-10, -8], unoptimizedRangeWithBy(-10, -7, 2), "(-10, -7, 2)");
    checkEq([-10, -8, -6], unoptimizedRangeWithBy(-10, -6, 2), "(-10, -6, 2)");
    checkEq([-10, -8, -6], unoptimizedRangeWithBy(-10, -5, 2), "(-10, -5, 2)");
    checkEq([-10, -8, -6, -4], unoptimizedRangeWithBy(-10, -4, 2), "(-10, -4, 2)");
    checkEq([-10, -8, -6, -4], unoptimizedRangeWithBy(-10, -3, 2), "(-10, -3, 2)");
    checkEq([-10, -8, -6, -4, -2], unoptimizedRangeWithBy(-10, -2, 2), "(-10, -2, 2)");
    checkEq([-10, -8, -6, -4, -2], unoptimizedRangeWithBy(-10, -1, 2), "(-10, -1, 2)");
    checkEq([-10, -8, -6, -4, -2, 0], unoptimizedRangeWithBy(-10, -0, 2), "(-10, -0, 2)");
    
    // check possible overflow cases
    
    checkEq([maxInteger-3, maxInteger-1], unoptimizedRangeWithBy(maxInteger-3, maxInteger, 2), 
        "(maxInteger-3, maxInteger, 2)");
    checkEq([minInteger+3, minInteger+1], unoptimizedRangeWithBy(minInteger+3, minInteger, 2), 
        "(minInteger+3, minInteger, 2)");
    checkEq([minInteger+3, minInteger+1], unoptimizedRangeWithBy(minInteger+3, minInteger, 2), 
        "(minInteger+3, minInteger, 2)");
    
    // Check that range iteration optimization of a 'for (i in lhs..rhs) { ' 
    // loop produces the same results as an unoptimized range iteration...
    void optimizedMatches(Integer start, Integer end) {
        value unoptimized = ArrayBuilder<Integer>();
        @disableOptimization
        for (i in start..end) {
            //print("unoptimized " i "");
            unoptimized.append(i);
        }
        
        value optimized = ArrayBuilder<Integer>();
        @requireOptimization:"RangeOpIteration"
        for (i in start..end) {
            //print("optimized " i "");
            optimized.append(i);
        } 
        
        check(unoptimized.sequence() == optimized.sequence(), 
            "Incorrect optimization of `for (i in `` start ``..`` end ``) { ... }`");
    }
    
    optimizedMatches(0,0);
    optimizedMatches(1,1);
    optimizedMatches(-1,-1);
    optimizedMatches(maxInteger,maxInteger);
    optimizedMatches(minInteger,minInteger);
    optimizedMatches(0,1);
    optimizedMatches(1,0);
    optimizedMatches(0,10);
    optimizedMatches(10,0);
    optimizedMatches(-1,1);
    optimizedMatches(1,-1);
    optimizedMatches(maxInteger-5,maxInteger);
    optimizedMatches(maxInteger,maxInteger-5);
    optimizedMatches(minInteger+5,minInteger);
    optimizedMatches(minInteger,minInteger+5);
    
    // Check that range iteration optimization of a 'for (i in (lhs..rhs).by(step)) { ' 
    // loop produces the same results as an unoptimized range iteration...
    void optimizedWithByMatches(Integer start, Integer end, Integer step) {
        value unoptimized = ArrayBuilder<Integer>();
        @disableOptimization
        for (i in (start..end).by(step)) {
            //print("unoptimized " i "");
            unoptimized.append(i);
        }
        
        value optimized = ArrayBuilder<Integer>();
        @requireOptimization:"RangeOpIteration"
        for (i in (start..end).by(step)) {
            //print("optimized " i "");
            optimized.append(i);
        }
        
        check(unoptimized.sequence() == optimized.sequence(), 
            "Incorrect optimization of `for (i in (`` start ``..`` end ``).by(`` step ``)) { ... }`");
    }
        
    optimizedWithByMatches(0,0,1);
    optimizedWithByMatches(1,1,1);
    optimizedWithByMatches(-1,-1,1);
    optimizedWithByMatches(0,0,3);
    optimizedWithByMatches(1,1,3);
    optimizedWithByMatches(-1,-1,3);
    optimizedWithByMatches(maxInteger,maxInteger, 1);
    optimizedWithByMatches(minInteger,minInteger, 1);
    optimizedWithByMatches(maxInteger,maxInteger, 3);
    optimizedWithByMatches(minInteger,minInteger, 3);
    // TODO optimizedWithByMatches(minInteger,maxInteger, maxInteger);
    // TODO optimizedWithByMatches(minInteger,maxInteger, maxInteger/2);
    optimizedWithByMatches(0,1,1);
    optimizedWithByMatches(1,0,1);
    optimizedWithByMatches(0,10,1);
    optimizedWithByMatches(10,0,1);
    optimizedWithByMatches(-1,1,1);
    optimizedWithByMatches(1,-1,1);
    optimizedWithByMatches(0,1,3);
    optimizedWithByMatches(1,0,3);
    optimizedWithByMatches(0,10,3);
    optimizedWithByMatches(10,0,3);
    optimizedWithByMatches(-1,1,3);
    optimizedWithByMatches(1,-1,3);
    optimizedWithByMatches(maxInteger-5,maxInteger,1);
    optimizedWithByMatches(maxInteger,maxInteger-5,1);
    optimizedWithByMatches(minInteger+5,minInteger,1);
    optimizedWithByMatches(minInteger,minInteger+5,1);
    optimizedWithByMatches(maxInteger-5,maxInteger,3);
    optimizedWithByMatches(maxInteger,maxInteger-5,3);
    optimizedWithByMatches(minInteger+5,minInteger,3);
    optimizedWithByMatches(minInteger,minInteger+5,3);
    
    // Check that the exception thrown for a negative step in a
    // 'for (i in (0..10).by(step)) {' loop is the same for optimized and 
    // unoptimized versions 
    for (step in {0, -3}) {
        try {
            @requireOptimization:"RangeOpIteration"
            for (i in (0..10).by(step)) {
                fail("Range.by(`` step `` didn't throw");
            }
        } catch (AssertionError e) {
            try {
                @disableOptimization
                for (i in (0..10).by(step)) {
                    fail("Range.by(`` step `` didn't throw");
                }
            } catch (AssertionError e2) {
                check(e.message == e2.message, "Exception messages with step= `` step `` differ: Optimized is '``
                    e.message ``' unoptimized is '`` e2.message ``'");
                check(e.string == e2.string, "Exception .string values with step= `` step `` differ: Optimized is '``
                    e.string ``' unoptimized is '`` e2.string ``'");
            }
        }
    }  
    
    // With else block
    @disableOptimization:"RangeOpIteration"
    for (i in 0..10) {
        break;    
    } else {
        fail("else with with unconditional break");
    }
    @requireOptimization:"RangeOpIteration"
    for (i in 0..10) {
        break;    
    } else {
        fail("else with with unconditional break");
    }
    
    @disableOptimization:"RangeOpIteration"
    for (i in 0..10) {
        if (i == 1) {
            break;
        }
    } else {
        fail("else with conditional break");
    }
    @requireOptimization:"RangeOpIteration"
    for (i in 0..10) {
        if (i == 1) {
            break;
        }
    } else {
        fail("else with conditional break");
    }
    
    @disableOptimization:"RangeOpIteration"
    for (i in 0..10) {
        if (i == 11) {
            fail("else with conditional break (2)");
            break;
        }
    } else {
        
    }
    @requireOptimization:"RangeOpIteration"
    for (i in 0..10) {
        if (i == 11) {
            fail("else with conditional break (2)");
            break;
        }
    } else {
        
    }
}
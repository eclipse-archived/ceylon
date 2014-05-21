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
void segmentOpIterationOptimizationCorrect() {

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
    function unoptimizedSegmentWithBy(Integer start, Integer length, Integer step) {
        value unoptimized = ArrayBuilder<Integer>();
        @disableOptimization
        for (i in (start:length).by(step)) {
            //print("unoptimized " i "");
            unoptimized.append(i);
        }
        return unoptimized.sequence;
    }
    
    checkEq([0, 3, 6, 9], unoptimizedSegmentWithBy(0, 10, 3), "(0, 10, 3)");
    checkEq([1, 4, 7], unoptimizedSegmentWithBy(1, 9, 3), "(1, 9, 3)");
    checkEq([2, 5, 8], unoptimizedSegmentWithBy(2, 8, 3), "(2, 8, 3)");
    checkEq([3, 6, 9], unoptimizedSegmentWithBy(3, 7, 3), "(3, 7, 3)");
    checkEq([4, 7], unoptimizedSegmentWithBy(4, 6, 3), "(4, 6, 3)");
    checkEq([5, 8], unoptimizedSegmentWithBy(5, 5, 3), "(5, 5, 3)");
    checkEq([6, 9], unoptimizedSegmentWithBy(6, 4, 3), "(6, 4, 3)");
    checkEq([7], unoptimizedSegmentWithBy(7, 3, 3), "(7, 3, 3)");
    checkEq([8], unoptimizedSegmentWithBy(8, 2, 3), "(8, 2, 3)");
    checkEq([9], unoptimizedSegmentWithBy(9, 1, 3), "(9, 1, 3)");
    checkEq([], unoptimizedSegmentWithBy(10, 0, 3), "(10, 0, 3)");
    
    checkEq([], unoptimizedSegmentWithBy(-10, -10, 2), "(-10, -10, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -9, 2), "(-10, -9, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -8, 2), "(-10, -8, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -7, 2), "(-10, -7, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -6, 2), "(-10, -6, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -5, 2), "(-10, -5, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -4, 2), "(-10, -4, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -3, 2), "(-10, -3, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -2, 2), "(-10, -2, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -1, 2), "(-10, -1, 2)");
    checkEq([], unoptimizedSegmentWithBy(-10, -0, 2), "(-10, -0, 2)");
    
    // check possible overflow cases
    /* TODO
    checkEq([maxInteger-3, maxInteger-1], unoptimizedSegmentWithBy(maxInteger-3, maxInteger, 2), 
        "(maxInteger-3, maxInteger, 2)");
    checkEq([minInteger+3, minInteger+1], unoptimizedSegmentWithBy(minInteger+3, minInteger, 2), 
        "(minInteger+3, minInteger, 2)");
    checkEq([minInteger+3, minInteger+1], unoptimizedSegmentWithBy(minInteger+3, minInteger, 2), 
        "(minInteger+3, minInteger, 2)");
    */
    // Check that range iteration optimization of a 'for (i in lhs..rhs) { ' 
    // loop produces the same results as an unoptimized range iteration...
    void optimizedMatches(Integer start, Integer length) {
        value unoptimized = ArrayBuilder<Integer>();
        @disableOptimization
        for (i in start:length) {
            //print("unoptimized " i "");
            unoptimized.append(i);
        }
        
        value optimized = ArrayBuilder<Integer>();
        @requireOptimization:"SegmentOpIteration"
        for (i in start:length) {
            //print("optimized " i "");
            optimized.append(i);
        } 
        
        check(unoptimized.sequence == optimized.sequence, 
            "Incorrect optimization of `for (i in `` start ``:`` length ``) { ... }`");
    }
    
    optimizedMatches(0,0);
    optimizedMatches(1,1);
    optimizedMatches(-1,-1);
    // TODO optimizedMatches(maxInteger,maxInteger);
    optimizedMatches(minInteger,minInteger);
    optimizedMatches(0,1);
    optimizedMatches(1,0);
    optimizedMatches(0,10);
    optimizedMatches(10,0);
    optimizedMatches(-1,1);
    optimizedMatches(1,-1);
    // TODO optimizedMatches(maxInteger-5,maxInteger);
    // TODO optimizedMatches(maxInteger,maxInteger-5);
    optimizedMatches(minInteger+5,minInteger);
    optimizedMatches(minInteger,minInteger+5);
    
    // Check that range iteration optimization of a 'for (i in (lhs..rhs).by(step)) { ' 
    // loop produces the same results as an unoptimized range iteration...
    void optimizedWithByMatches(Integer start, Integer length, Integer step) {
        value unoptimized = ArrayBuilder<Integer>();
        @disableOptimization
        for (i in (start:length).by(step)) {
            //print("unoptimized " i "");
            unoptimized.append(i);
        }
        
        value optimized = ArrayBuilder<Integer>();
        @requireOptimization:"RangeOpIteration"
        for (i in (start:length).by(step)) {
            //print("optimized " i "");
            optimized.append(i);
        }
        
        check(unoptimized.sequence == optimized.sequence, 
            "Incorrect optimization of `for (i in (`` start ``:`` length``).by(`` by ``)) { ... }`");
    }
        
    optimizedWithByMatches(0,0,1);
    optimizedWithByMatches(1,1,1);
    optimizedWithByMatches(-1,-1,1);
    optimizedWithByMatches(0,0,3);
    optimizedWithByMatches(1,1,3);
    optimizedWithByMatches(-1,-1,3);
    // TODO optimizedWithByMatches(maxInteger,maxInteger, 1);
    optimizedWithByMatches(minInteger,minInteger, 1);
    // TODO optimizedWithByMatches(maxInteger,maxInteger, 3);
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
    // TODO optimizedWithByMatches(maxInteger-5,maxInteger,1);
    // TODO optimizedWithByMatches(maxInteger,maxInteger-5,1);
    optimizedWithByMatches(minInteger+5,minInteger,1);
    optimizedWithByMatches(minInteger,minInteger+5,1);
    // TODO optimizedWithByMatches(maxInteger-5,maxInteger,3);
    // TODO optimizedWithByMatches(maxInteger,maxInteger-5,3);
    optimizedWithByMatches(minInteger+5,minInteger,3);
    optimizedWithByMatches(minInteger,minInteger+5,3);
    
    // Check that the exception thrown for a negative step in a
    // 'for (i in (0..10).by(step)) {' loop is the same for optimized and 
    // unoptimized versions 
    for (step in {0, -3}) {
        try {
            @requireOptimization:"SegmentOpIteration"
            for (i in (0:10).by(step)) {
                fail("Segment.by(`` step `` didn't throw");
            }
        } catch (AssertionError e) {
            try {
                @disableOptimization
                for (i in (0:10).by(step)) {
                    fail("Segment.by(`` step `` didn't throw");
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
    @disableOptimization:"SegmentOpIteration"
    for (i in 0:10) {
        break;    
    } else {
        fail("else with with unconditional break");
    }
    @requireOptimization:"SegmentOpIteration"
    for (i in 0:10) {
        break;    
    } else {
        fail("else with with unconditional break");
    }
    
    @disableOptimization:"SegmentOpIteration"
    for (i in 0:10) {
        if (i == 1) {
            break;
        }
    } else {
        fail("else with conditional break");
    }
    @requireOptimization:"SegmentOpIteration"
    for (i in 0:10) {
        if (i == 1) {
            break;
        }
    } else {
        fail("else with conditional break");
    }
    
    @disableOptimization:"SegmentOpIteration"
    for (i in 0:10) {
        if (i == 10) {
            fail("else with conditional break (2)");
            break;
        }
    } else {
        
    }
    @requireOptimization:"SegmentOpIteration"
    for (i in 0:10) {
        if (i == 10) {
            fail("else with conditional break (2)");
            break;
        }
    } else {
        
    }
    
    
    for (i in 0:0) {
        fail("zero length");
    }
    for (i in 0:-1) {
        fail("negative length");
    }
    for (i in 0:minInteger) {
        fail("``minInteger`` length");
    }
}
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
import ceylon.language.meta{type}

void compareSpanInteger2130(Integer start, Integer end, Integer[]? expect = null, Integer? breakAfter=null) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in start..end) {
            ss += i^2;
            if (exists expect) {
                if(exists expected = expect[index]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index>=breakAfter) {
                break; 
            }
            index++;
        }
    } catch (Throwable t) {
        t1 = t;
    }
    
    variable Integer index2 = 0;
    variable Integer ss2 = 0;
    variable Throwable? t2 = null;
    try {
        for (i in start..end) {
            ss2 += i^2;
            if (exists expect) {
                if(exists expected = expect[index2]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index2``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index2`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index2>=breakAfter) {
                break; 
            }
            index2++;
        }
    } catch (Throwable t) {
        t2 = t;
    }
    
    if (index != index2) {
        problems = ["different number of iterations: expected ``index `` but was ``index2``", *problems];
    }
    if (ss != ss2) {
        problems = ["different sums: expected ``ss`` but was ``ss2``", *problems];
    }
    if (exists got1=t1) {
        if (exists got2= t2) {
            if (type(got1) != type(got2)) {
                problems = ["different exception types: expected ``type(got1)`` but got ``type(got2)``", *problems];
            }
            if (got1.message != got2.message) {
                // XXX #2320 problems = ["different exception messages: expected ``got1.message`` but got ``got2.message``", *problems];
            }
        } else {
            problems = ["unoptimized threw ``got1``, but optimized ran normally", *problems];
        }
    } else if (exists got2=t2) {
        problems = ["optimized threw ``got2``, but unoptimized ran normally", *problems];
    }
    
    if (problems nonempty) {
        throw Exception("(``start``..``end``): ``problems``");
    }
}

void compareSpanIntegerBy2130(Integer start, Integer end, Integer by, Integer[]? expect = null, Integer? breakAfter=null, Boolean debug=false) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in (start..end).by(by)) {
            if (debug) {
                print("unoptimized (``start``..``end``).by(``by``)[``index``]: ``i``");
            }
            ss += i^2;
            if (exists expect) {
                if(exists expected = expect[index]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index>=breakAfter) {
                break; 
            }
            index++;
        }
    } catch (Throwable t) {
        t1 = t;
    }
    
    variable Integer index2 = 0;
    variable Integer ss2 = 0;
    variable Throwable? t2 = null;
    try {
        for (i in (start..end).by(by)) {
            if (debug) {
                print("optimized (``start``..``end``).by(``by``)[``index``]: ``i``");
            }
            ss2 += i^2;
            if (exists expect) {
                if(exists expected = expect[index2]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index2``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index2`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index2>=breakAfter) {
                break; 
            }
            index2++;
        }
    } catch (Throwable t) {
        t2 = t;
    }
    
    if (index != index2) {
        problems = ["different number of iterations: expected ``index `` but was ``index2``", *problems];
    }
    if (ss != ss2) {
        problems = ["different sums: expected ``ss`` but was ``ss2``", *problems];
    }
    if (exists got1=t1) {
        if (exists got2= t2) {
            if (type(got1) != type(got2)) {
                problems = ["different exception types: expected ``type(got1)`` but got ``type(got2)``", *problems];
            }
            if (got1.message != got2.message) {
                // XXX #2320 problems = ["different exception messages: expected ``got1.message`` but got ``got2.message``", *problems];
            }
        } else {
            problems = ["unoptimized threw ``got1``, but optimized ran normally", *problems];
        }
    } else if (exists got2=t2) {
        problems = ["optimized threw ``got2``, but unoptimized ran normally", *problems];
    }
    
    if (problems nonempty) {
        throw Exception("(``start``..``end``).by(``by``): ``problems``");
    }
}

void compareSpanCharacter2130(Character start, Character end, Character[]? expect = null, Integer? breakAfter=null) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in start..end) {
            ss += i.integer^2;
            if (exists expect) {
                if(exists expected = expect[index]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index>=breakAfter) {
                break; 
            }
            index++;
        }
    } catch (Throwable t) {
        t1 = t;
    }
    
    variable Integer index2 = 0;
    variable Integer ss2 = 0;
    variable Throwable? t2 = null;
    try {
        for (i in start..end) {
            ss2 += i.integer^2;
            if (exists expect) {
                if(exists expected = expect[index2]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index2``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index2`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index2>=breakAfter) {
                break; 
            }
            index2++;
        }
    } catch (Throwable t) {
        t2 = t;
    }
    
    if (index != index2) {
        problems = ["different number of iterations: expected ``index `` but was ``index2``", *problems];
    }
    if (ss != ss2) {
        problems = ["different sums: expected ``ss`` but was ``ss2``", *problems];
    }
    if (exists got1=t1) {
        if (exists got2= t2) {
            if (type(got1) != type(got2)) {
                problems = ["different exception types: expected ``type(got1)`` but got ``type(got2)``", *problems];
            }
            if (got1.message != got2.message) {
                problems = ["different exception messages: expected ``got1.message`` but got ``got2.message``", *problems];
            }
        } else {
            problems = ["unoptimized threw ``got1``, but optimized ran normally", *problems];
        }
    } else if (exists got2=t2) {
        problems = ["optimized threw ``got2``, but unoptimized ran normally", *problems];
    }
    
    if (problems nonempty) {
        throw Exception("(``start.integer``..``end.integer``): ``problems``");
    }
}

void compareSpanCharacterBy2130(Character start, Character end, Integer by, Character[]? expect = null, Integer? breakAfter=null) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in (start..end).by(by)) {
            ss += i.integer^2;
            if (exists expect) {
                if(exists expected = expect[index]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index>=breakAfter) {
                break; 
            }
            index++;
        }
    } catch (Throwable t) {
        t1 = t;
    }
    
    variable Integer index2 = 0;
    variable Integer ss2 = 0;
    variable Throwable? t2 = null;
    try {
        for (i in (start..end).by(by)) {
            ss2 += i.integer^2;
            if (exists expect) {
                if(exists expected = expect[index2]) {
                    if (i!=expected) {
                        problems = ["iterated over unexpected element on index ``index2``: got ``i`` expected ``expected``", *problems];
                    }
                } else {
                    problems = ["more iterations than expected: expected ``expect.size`` iterations, but index ``index2`` had value ``i``", *problems];
                }
            }
            if (exists breakAfter,
                index2>=breakAfter) {
                break; 
            }
            index2++;
        }
    } catch (Throwable t) {
        t2 = t;
    }
    
    if (index != index2) {
        problems = ["different number of iterations: expected ``index `` but was ``index2``", *problems];
    }
    if (ss != ss2) {
        problems = ["different sums: expected ``ss`` but was ``ss2``", *problems];
    }
    if (exists got1=t1) {
        if (exists got2= t2) {
            if (type(got1) != type(got2)) {
                problems = ["different exception types: expected ``type(got1)`` but got ``type(got2)``", *problems];
            }
            if (got1.message != got2.message) {
                problems = ["different exception messages: expected ``got1.message`` but got ``got2.message``", *problems];
            }
        } else {
            problems = ["unoptimized threw ``got1``, but optimized ran normally", *problems];
        }
    } else if (exists got2=t2) {
        problems = ["optimized threw ``got2``, but unoptimized ran normally", *problems];
    }
    
    if (problems nonempty) {
        throw Exception("(``start``..``end``).by(``by``): ``problems``");
    }
}

void bug2130Span() {
    // various normal cases
    compareSpanInteger2130 { start = 0; end = 0; };
    compareSpanInteger2130 { start = 0; end = 1; };
    compareSpanInteger2130 { start = 1; end = 0; };
    compareSpanInteger2130 { start = 1; end = 1; };
    compareSpanInteger2130 { start = -2; end = 2; };
    compareSpanInteger2130 { start = 2; end = -2; };
    compareSpanInteger2130 { start = runtime.maxIntegerValue; end = runtime.maxIntegerValue; };
    compareSpanInteger2130 { start = runtime.minIntegerValue; end = runtime.minIntegerValue; };
    
    // overflowing span (immediate throw, loop not entered)
    compareSpanInteger2130 { start = -2; end = runtime.maxIntegerValue; };
    // reversed overflowing span
    compareSpanInteger2130 { start = runtime.maxIntegerValue; end = -2; };
    
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareSpanIntegerBy2130 { start = 0; end = 0; by = step; };
        compareSpanIntegerBy2130 { start = 0; end = 1; by = step; };
        compareSpanIntegerBy2130 { start = 1; end = 0; by = step; };
        compareSpanIntegerBy2130 { start = 1; end = 1; by = step; };
        compareSpanIntegerBy2130 { start = 1; end = 10; by = step; };
        compareSpanIntegerBy2130 { start = -2; end = 2; by = step; };
        compareSpanIntegerBy2130 { start = 2; end = -2; by = step; };
        compareSpanIntegerBy2130 { start = runtime.maxIntegerValue; end = runtime.maxIntegerValue; by = step; };
        compareSpanIntegerBy2130 { start = runtime.minIntegerValue; end = runtime.minIntegerValue; by = step; };
        
        
        compareSpanIntegerBy2130{
            start=-2; 
            end=runtime.maxIntegerValue;
            by=step;
            breakAfter=if (step == 1 || step == 2) then 3 else null;
            debug=false;
        };
        compareSpanIntegerBy2130{
            start=runtime.maxIntegerValue; 
            end=-2;
            by=step;
            breakAfter=if (step == 1 || step == 2) then 3 else null;
            debug=false;
        };
        
    }
    compareSpanCharacter2130 { start = '\0'; end = '\0'; };
    compareSpanCharacter2130('\0', '\0'.neighbour { offset = 1; });
    compareSpanCharacter2130 { start = 'z'; end = 'a'; };// pb with Spans whose last is zero
    compareSpanCharacter2130 { start = '\0'.neighbour(1); end = '\0'; };// pb with Spans whose last is zero
    compareSpanCharacter2130('\0'.neighbour(1), '\0'.neighbour { offset = 1; });
    compareSpanCharacter2130('\{#10FFFF}', '\{#10FFFF}');
    compareSpanCharacter2130('\0', '\{#10FFFF}');
    compareSpanCharacter2130 { start = 'a'; end = 'z'; };
    compareSpanCharacter2130 { start = 'z'; end = 'a'; };
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareSpanCharacterBy2130 { start = 'a'; end = 'z'; by = step; };
        compareSpanCharacterBy2130 { start = 'z'; end = 'a'; by = step; };
    }
    
}


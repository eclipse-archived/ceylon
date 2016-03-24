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

void compareMeasureInteger2130(Integer start, Integer length, 
    Integer[]? expect = null, Integer? breakAfter=null, Boolean debug=false) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in start:length) {
            if (debug) {
                print("unoptimized (``start``:``length``)[``index``]: ``i``");
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
        for (i in start:length) {
            if (debug) {
                print("optimized (``start``:``length``)[``index2``]: ``i``");
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
                problems = ["different exception messages: expected ``got1.message`` but got ``got2.message``", *problems];
            }
        } else {
            problems = ["unoptimized threw ``got1``, but optimized ran normally", *problems];
        }
    } else if (exists got2=t2) {
        problems = ["optimized threw ``got2``, but unoptimized ran normally", *problems];
    }
    
    if (problems nonempty) {
        throw Exception("(``start``:``length``): ``problems``");
    }
}

void compareMeasureIntegerBy2130(Integer start, Integer length, Integer by, Integer[]? expect = null, Integer? breakAfter=null) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in (start:length).by(by)) {
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
        for (i in (start:length).by(by)) {
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
        throw Exception("(``start``:``length``).by(``by``): ``problems``");
    }
}

void compareMeasureCharacter2130(Character start, Integer length, Character[]? expect = null, Integer? breakAfter=null) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in start:length) {
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
        for (i in start:length) {
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
        throw Exception("(``start``:``length``): ``problems``");
    }
}

void compareMeasureCharacterBy2130(Character start, Integer length, Integer by, Character[]? expect = null, Integer? breakAfter=null) {
    variable String[] problems = empty;
    
    variable Integer index = 0;
    variable Integer ss = 0;
    variable Throwable? t1 = null;
    try {
        @disableOptimization
        for (i in (start:length).by(by)) {
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
        for (i in (start:length).by(by)) {
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
        throw Exception("(``start``:``length``).by(``by``): ``problems``");
    }
}
 
void bug2130Measure() {
    compareMeasureInteger2130 { start = 0; length = 0; };
    compareMeasureInteger2130 { start = 0; length = -1; };
    compareMeasureInteger2130 { start = 0; length = 1; };
    compareMeasureInteger2130 { start = 0; length = 10; };
    compareMeasureInteger2130 { start = 0; length = 10; };
    compareMeasureInteger2130{
        start=0; 
        length=runtime.maxIntegerValue; 
        breakAfter=10;
    };
    compareMeasureInteger2130{
        start=-2; 
        length=runtime.maxIntegerValue; 
        breakAfter=10;
    };
    compareMeasureInteger2130{
        start=1; 
        length=runtime.maxIntegerValue; 
        breakAfter=10;
    };
    compareMeasureInteger2130 { start = runtime.maxIntegerValue; length = 10; };
    compareMeasureInteger2130 { start = runtime.minIntegerValue; length = 10; };
    compareMeasureInteger2130 { start = runtime.maxIntegerValue-5; length = 10; };
    compareMeasureInteger2130 { start = runtime.minIntegerValue-5; length = 10; };
    
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareMeasureIntegerBy2130 { start = 0; length = 0; by = step; };
        compareMeasureIntegerBy2130 { start = 0; length = -1; by = step; };
        compareMeasureIntegerBy2130 { start = 0; length = 1; by = step; };
        compareMeasureIntegerBy2130 { start = 0; length = 10; by = step; };
        compareMeasureIntegerBy2130(runtime.minIntegerValue, 10, step);
        compareMeasureIntegerBy2130(runtime.maxIntegerValue, 10, step);
    }
    compareMeasureIntegerBy2130 { start = runtime.minIntegerValue-5; length = 10; by=3; };
    compareMeasureIntegerBy2130 { start = runtime.maxIntegerValue-5; length = 10; by=3; };
    
    // Characters
    compareMeasureCharacter2130 { start = '\0'; length = 0; };
    compareMeasureCharacter2130 { start = '\0'; length = -1; };
    compareMeasureCharacter2130 { start = '\0'; length = 1; };
    compareMeasureCharacter2130 { start = '\0'; length = 10; };
    compareMeasureCharacter2130 { start = '\0'; length = 10; };
    compareMeasureCharacter2130{
        start='\0'; 
        length=100; 
        breakAfter=10;
    };
    value maxCharacterValue = #10FFFF.character;
    value minCharacterValue = 0.character;
    compareMeasureCharacter2130{
        start=maxCharacterValue.neighbour(-2); 
        length=100; 
        breakAfter=10;
    };
    compareMeasureCharacter2130{
        start='\0'.neighbour(1); 
        length=100; 
        breakAfter=10;
    };
    
    compareMeasureCharacter2130 { start = maxCharacterValue; length = 10; };
    compareMeasureCharacter2130 { start = minCharacterValue; length = 10; };
    compareMeasureCharacter2130 { start = maxCharacterValue.neighbour(-5); length = 10; };
    //compareMeasureCharacter2130 { start = minCharacterValue.neighbour(-5); length = 10; };
    
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareMeasureCharacterBy2130 { start = '\0'; length = 0; by = step; };
        compareMeasureCharacterBy2130 { start = '\0'; length = -1; by = step; };
        compareMeasureCharacterBy2130 { start = '\0'; length = 1; by = step; };
        compareMeasureCharacterBy2130 { start = '\0'; length = 10; by = step; };
        compareMeasureCharacterBy2130(minCharacterValue, 10, step);
        compareMeasureCharacterBy2130(maxCharacterValue, 10, step);
    }
    //compareMeasureCharacterBy2130 { start = minCharacterValue-5; length = 10; by=3; };
    compareMeasureCharacterBy2130 { start = maxCharacterValue.neighbour(-5); length = 10; by=3; };
    
}

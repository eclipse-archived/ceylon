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
    compareSpanInteger2130(0, 0);
    compareSpanInteger2130(0, 1);
    compareSpanInteger2130(1, 0);
    compareSpanInteger2130(1, 1);
    compareSpanInteger2130(-2, 2);
    compareSpanInteger2130(2, -2);
    compareSpanInteger2130(runtime.maxIntegerValue, runtime.maxIntegerValue);
    compareSpanInteger2130(runtime.minIntegerValue, runtime.minIntegerValue);
    
    // overflowing span (immediate throw, loop not entered)
    compareSpanInteger2130(-2, runtime.maxIntegerValue);
    // reversed overflowing span
    compareSpanInteger2130(runtime.maxIntegerValue, -2);
    
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareSpanIntegerBy2130(0, 0, step);
        compareSpanIntegerBy2130(0, 1, step);
        compareSpanIntegerBy2130(1, 0, step);
        compareSpanIntegerBy2130(1, 1, step);
        compareSpanIntegerBy2130(1, 10, step);
        compareSpanIntegerBy2130(-2, 2, step);
        compareSpanIntegerBy2130(2, -2, step);
        compareSpanIntegerBy2130(runtime.maxIntegerValue, runtime.maxIntegerValue, step);
        compareSpanIntegerBy2130(runtime.minIntegerValue, runtime.minIntegerValue, step);
        
        if (step == 1 && step == 2) {// those take too damn long!
            /*compareSpanIntegerBy2130{
                start=-2; 
                end=runtime.maxIntegerValue;
                by=step;
                breakAfter=3;
                debug=true;
            };
            compareSpanIntegerBy2130{
                start=runtime.maxIntegerValue; 
                end=-2;
                by=step;
                breakAfter=3;
                debug=true;
            }; //TODO takes forever*/
        } else {
            //compareSpanIntegerBy2130(-2, runtime.maxIntegerValue, step);
            //compareSpanIntegerBy2130(runtime.maxIntegerValue, -2, step);
        }
    }
    
    compareSpanCharacter2130('\0', '\0');
    compareSpanCharacter2130('\0', '\0'.neighbour(1));
    // TODO compareSpanCharacter2130('\0'.neighbour(1), '\0');
    compareSpanCharacter2130('\0'.neighbour(1), '\0'.neighbour(1));
    // TODO compareSpanCharacter2130('\{#10FFFF}', '\{#10FFFF}');
    compareSpanCharacter2130('a', 'z');
    compareSpanCharacter2130('z', 'a');
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareSpanCharacterBy2130('a', 'z', step);
        compareSpanCharacterBy2130('z', 'a', step);
    }
    
}


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
    compareMeasureInteger2130(0, 0);
    compareMeasureInteger2130(0, -1);
    compareMeasureInteger2130(0, 1);
    compareMeasureInteger2130(0, 10);
    compareMeasureInteger2130(0, 10);
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
    compareMeasureInteger2130(runtime.maxIntegerValue, 10);
    compareMeasureInteger2130(runtime.minIntegerValue, 10);
    compareMeasureInteger2130(runtime.maxIntegerValue-5, 10);
    compareMeasureInteger2130(runtime.minIntegerValue-5, 10);
    
    for (step in [-1, 0, 1, 2, runtime.maxIntegerValue]) {
        compareMeasureIntegerBy2130(0, 0, step);
        compareMeasureIntegerBy2130(0, -1, step);
        compareMeasureIntegerBy2130(0, 1, step);
        compareMeasureIntegerBy2130(0, 10, step);
        // TODO compareMeasureIntegerBy2130(runtime.maxIntegerValue, 10, step);
        // TODO compareMeasureIntegerBy2130(runtime.minIntegerValue, 10, step);
    }
    
    
    variable value x=0;
    variable value min = 1;
    variable value length = runtime.maxIntegerValue;
    // overflow of start+length
    variable Integer[] expect = [1, 2, 3, 4];
    for (i in min:length) {
        //print(i);
        assert(exists expected = expect[x],
            expected == i);
        if (x++ > 2) {
            x=0;
            break;
        }
    }
    
    x=0;
    @disableOptimization
    for (i in min:length) {
        //print(i);
        assert(exists expected = expect[x],
            expected == i);
        if (x++ > 2) {
            x=0;
            break;
        }
    }
    
    // overflow during iteration
    min = runtime.maxIntegerValue-2;
    length = 4;
    // overflow of start+length
    expect = [runtime.maxIntegerValue-2, runtime.maxIntegerValue-1, runtime.maxIntegerValue, runtime.minIntegerValue];
    for (i in min:length) {
        //print(i);
        assert(exists expected = expect[x++],
            expected == i);
    }
    
    x=0;
    @disableOptimization
    for (i in min:length) {
        //print(i);
        assert(exists expected = expect[x++],
            expected == i);
    }
    
    // length <= 0 (should get empty iteration)
    min = 1;
    length = 0;
    expect = [];
    for (i in min:length) {
        //print(i);
        assert(exists expected = expect[x],
            expected == i);
        
    }
    
    x=0;
    @disableOptimization
    for (i in min:length) {
        //print(i);
        assert(exists expected = expect[x],
            expected == i);
    }
    
    // step <= 0 (should get identical exception thrown)
    min = 0;
    length = 10;
    variable value step = 0;
    try {
        @disableOptimization
        for (i in (min:length).by(step)) {
        }
        "expected an exception for unoptimized case"
        assert(false);
    } catch (Throwable e) {
        try {
            for (i in (min:length).by(step)) {
            }
            "expected an exception for optimized case"
            assert(false);
        } catch (Throwable e2) {
            if (e.message != e2.message) {
                throw Exception("``e.message`` != ``e2.message``");
            }
        }
    }
    
    // overflow during iteration (expect identical initial iteration(s) followed by idential exception)
    min = runtime.maxIntegerValue-5;
    step = 3;
    length = 10;
    expect = [runtime.maxIntegerValue-5, runtime.maxIntegerValue-2];
    //print(min.neighbour(step));
    try {
        @disableOptimization
        for (i in (min:length).by(step)) {
            //print(i);
            assert(exists expected = expect[x++],
                expected == i);
        }
        "expected an exception for unoptimized case"
        assert(false);
    } catch (Throwable e) {
        x = 0;
        try {
            for (i in (min:length).by(step)) {
                //print(i);
                assert(exists expected = expect[x++],
                    expected == i);
            }
            "expected an exception for optimized case"
            assert(false);
        } catch (Throwable e2) {
            if (e.message != e2.message) {
                throw Exception("``e.message`` != ``e2.message``");
            }
        }
    }
    
    /*
    for (c in 'a':26) {
        print(c);
    }
    for (c in ('a':26).by(2)) {
        print(c);
    }*/
}

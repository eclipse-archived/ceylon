void bug2130() {
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
}
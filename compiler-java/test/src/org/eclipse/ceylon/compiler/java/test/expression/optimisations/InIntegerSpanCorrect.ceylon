shared void inIntegerSpanCorrect() {
    void test(Integer x, Integer y, Integer z) {
        variable Throwable? t1 = null;
        variable Boolean|String expect = "broken";
        try {
            Integer[] it = y..z;
            expect = x in it;
        }
        catch (Throwable t) {
            t1 = t;
        }
        variable Throwable? t2 = null;
        variable Boolean|String got = "also broken";
        try {
            got = x in y..z;
        } 
        catch (Throwable t) {
            t2=t;
        }
        
        if(exists e1=t1) {
            if (exists e2=t2) {
                if (e2.message != e1.message) {
                    throw Exception("``x`` in ``y``..``z``:` exception messages differ");
                } else {
                    print("``x`` in ``y``..``z`` throws ``e2``");
                }
            } else {
                e1.printStackTrace();
                throw Exception("``x`` in ``y``..``z``:` unoptimized threw but optimized operation did not");
            }
        } else {
            if (exists e2=t2) {
                throw Exception("``x`` in ``y``..``z``:` optimized threw but unoptimized operation did not");
            } else {
                if (expect != got) {
                    throw Exception("``x`` in ``y``..``z``: got optimized result ``got`` but expected ``expect``");
                } else {
                    print("``x`` in ``y``..``z`` == ``got``");
                }
            }
        }
        
    }
    test(-1, 0,3);
    test(0, 0, 3);
    test(1, 0, 3);
    test(2, 0, 3);
    test(3, 0, 3);
    test(4, 0, 3);
    
    test(-1, 3,0);
    test(0, 3, 0);
    test(1, 3, 0);
    test(2, 3, 0);
    test(3, 3, 0);
    test(4, 3, 0);
    
    test(0, 0, 0);
    test(1, 1, 1);
    
    test(runtime.maxIntegerValue, runtime.maxIntegerValue, runtime.maxIntegerValue);
    test(runtime.minIntegerValue, runtime.minIntegerValue, runtime.minIntegerValue);
    test(runtime.maxIntegerValue, runtime.minIntegerValue, runtime.maxIntegerValue);
    test(runtime.minIntegerValue, runtime.minIntegerValue, runtime.maxIntegerValue);
    test(runtime.maxIntegerValue, runtime.maxIntegerValue, runtime.minIntegerValue);
    test(runtime.minIntegerValue, runtime.maxIntegerValue, runtime.minIntegerValue);
    
    test(runtime.maxIntegerValue, runtime.maxIntegerValue, 1);
    test(runtime.maxIntegerValue, runtime.maxIntegerValue, 2);
    test(runtime.minIntegerValue, runtime.maxIntegerValue, 2);
    test(runtime.maxIntegerValue, 1, runtime.maxIntegerValue);
    test(runtime.maxIntegerValue, 2, runtime.maxIntegerValue);
    test(runtime.minIntegerValue, 3, runtime.maxIntegerValue);
    test(runtime.minIntegerValue, runtime.maxIntegerValue, runtime.maxIntegerValue);
}

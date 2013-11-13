import java.lang{IntArray, LongArray, arrays}
Integer arrayIterationStaticN = 1_000_000;
LongArray arrayIterationStaticInts = arrays.toLongArray(0..100);
"The optimized version"
Integer arrayIterationStaticBench() {
    // avoid getter overhead by declaring a local var
    value array = arrayIterationStaticInts;
    variable value i = arrayIterationStaticN;
    variable value sum = 0;
    value t0 = system.nanoseconds;
    while (i > 0) {
        for (x in array.array) {
            sum += x;
        }
        i--;
    }
    value t1 = system.nanoseconds;
    print("Optimized result ``sum``");
    return t1-t0;
}
"The unoptimized version"
Integer arrayIterationStaticBenchDis() {
    // avoid getter overhead by declaring a local var
    value array = arrayIterationStaticInts;
    variable value i = arrayIterationStaticN;
    variable value sum = 0;
    value t0 = system.nanoseconds;
    while (i > 0) {
        @disableOptimization
        for (x in array.array) {
            sum += x;
        }
        i--;
    }
    value t1 = system.nanoseconds;
    print("Unoptimized result ``sum``");
    return t1-t0;
}
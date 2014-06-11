import ceylon.language.meta{type}
import java.lang{IntArray, LongArray}
Integer tupleIterationStaticN = 1_000_000;
[Integer, Integer+] tupleIterationStaticInts {
    value s = [0, 1, 2, *[for (i in 3..100) i]];
    assert(is [Integer, Integer, Integer, Integer, Integer+] s);
    return s;
}
"The optimized version"
Integer tupleIterationStaticBench() {
    // avoid getter overhead by declaring a local var
    value seq = tupleIterationStaticInts;
    variable value i = tupleIterationStaticN;
    variable value sum = 0;
    value t0 = system.nanoseconds;
    while (i > 0) {
        sum = 0;
        for (x in seq) {
            sum += x;
        }
        i--;
    }
    value t1 = system.nanoseconds;
    print("Optimized result ``sum``");
    return t1-t0;
}
"The unoptimized version"
Integer tupleIterationStaticBenchDis() {
    // avoid getter overhead by declaring a local var
    value seq = tupleIterationStaticInts;
    variable value i = tupleIterationStaticN;
    variable value sum = 0;
    value t0 = system.nanoseconds;
    while (i > 0) {
        sum = 0;
        @disableOptimization
        for (x in seq) {
            sum += x;
        }
        i--;
    }
    value t1 = system.nanoseconds;
    print("Optimized result ``sum``");
    return t1-t0;
}
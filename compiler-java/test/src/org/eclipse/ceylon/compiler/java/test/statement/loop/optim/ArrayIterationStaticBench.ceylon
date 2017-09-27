import java.lang{LongArray}
import ceylon.interop.java { createJavaLongArray }

Integer arrayIterationStaticN = 1_000_000;
LongArray arrayIterationStaticLongs = createJavaLongArray( 0:200 );
"The optimized version"
Integer arrayIterationStaticBench() {
    // avoid getter overhead by declaring a local var
    value array = arrayIterationStaticLongs.integerArray;
    variable value i = arrayIterationStaticN;
    variable value sum = 0;
    value t0 = system.nanoseconds;
    while (i > 0) {
        sum = 0;
        for (x in array) {
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
    value array = arrayIterationStaticLongs.integerArray;
    variable value i = arrayIterationStaticN;
    variable value sum = 0;
    value t0 = system.nanoseconds;
    while (i > 0) {
        sum = 0;
        @disableOptimization
        for (x in array) {
            sum += x;
        }
        i--;
    }
    value t1 = system.nanoseconds;
    print("Unoptimized result ``sum``");
    return t1-t0;
}
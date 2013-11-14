import java.lang{IntArray, LongArray, arrays}
Integer arraySequenceIterationStaticN = 1_000_000;
ArraySequence<Integer> arraySequenceIterationStaticInts {
    value s = {for (i in 0..100) i}.sequence;
    assert(is ArraySequence<Integer> s);
    return s;
}
"The optimized version"
Integer arraySequenceIterationStaticBench() {
    // avoid getter overhead by declaring a local var
    value seq = arraySequenceIterationStaticInts;
    variable value i = arraySequenceIterationStaticN;
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
Integer arraySequenceIterationStaticBenchDis() {
    // avoid getter overhead by declaring a local var
    value seq = arraySequenceIterationStaticInts;
    variable value i = arraySequenceIterationStaticN;
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
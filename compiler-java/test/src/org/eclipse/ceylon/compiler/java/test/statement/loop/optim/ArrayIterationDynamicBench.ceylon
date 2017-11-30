import java.lang{IntArray, LongArray, arrays}
Integer arrayIterationDynamicN = 1_000_000;
Integer arrayIterationDynamicK = 100;
//Iterable<Integer> arrayIterationDynamicInts = Array(0..100);
"The optimized version"
Integer arrayIterationDynamicBenchOptimized(String argTypeName, Iterable<Integer> arrayIterationDynamicInts) {
    // avoid getter overhead by declaring a local var
    value array = arrayIterationDynamicInts;
    variable value i = arrayIterationDynamicN;
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
    print("Optimized loop with ``argTypeName``-typed argument result ``sum``");
    return t1-t0;
}

"The unoptimized version"
Integer arrayIterationDynamicBenchUnoptimized(String argTypeName, Iterable<Integer> arrayIterationDynamicInts) {
    // avoid getter overhead by declaring a local var
    value array = arrayIterationDynamicInts;
    variable value i = arrayIterationDynamicN;
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
    print("Unptimized loop with ``argTypeName``-typed argument result ``sum``");
    return t1-t0;
}

Integer arrayIterationDynamicBenchOptimizedArray() {
    Array<Integer> arg=Array(0..arrayIterationDynamicK);
    return arrayIterationDynamicBenchOptimized("Array", arg);
}
Integer arrayIterationDynamicBenchOptimizedArraySequence() {
    assert (is ArraySequence<Integer> arg = {for (i in 0..arrayIterationDynamicK) i }.sequence);
    return arrayIterationDynamicBenchOptimized("ArraySequence", arg);
}
Integer arrayIterationDynamicBenchOptimizedRange() {
    Range<Integer> arg = 0..arrayIterationDynamicK;
    return arrayIterationDynamicBenchOptimized("Range", arg);
}
Integer arrayIterationDynamicBenchOptimizedComprehension() {
    Iterable<Integer> arg = {for (i in 0..arrayIterationDynamicK) i};
    return arrayIterationDynamicBenchOptimized("Comprehension", arg);
}

Integer arrayIterationDynamicBenchUnoptimizedArray() {
    Array<Integer> arg=Array(0..arrayIterationDynamicK);
    return arrayIterationDynamicBenchUnoptimized("Array", arg);
}
Integer arrayIterationDynamicBenchUnoptimizedArraySequence() {
    assert (is ArraySequence<Integer> arg = {for (i in 0..arrayIterationDynamicK) i }.sequence);
    return arrayIterationDynamicBenchUnoptimized("ArraySequence", arg);
}
Integer arrayIterationDynamicBenchUnoptimizedRange() {
    Range<Integer> arg = 0..arrayIterationDynamicK;
    return arrayIterationDynamicBenchUnoptimized("Range", arg);
}
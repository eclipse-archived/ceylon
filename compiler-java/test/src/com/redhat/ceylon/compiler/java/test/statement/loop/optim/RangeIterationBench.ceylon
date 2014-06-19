void rangeIterationBench<Element>(Element one, Element n, Integer iterations) 
        given Element satisfies Integral<Element>{
    value range = one..n;
    variable value product = one;
    variable value j = iterations;
    variable value t0 = system.nanoseconds;
    while (j-- > 0) {
        for (i in range) {
            product *= i;
        }
    }
    print("with opt:\t``product``\t``(system.nanoseconds-t0)/1_000_000.0``ms");
    
    product = one;
    j = iterations;
    t0 = system.nanoseconds;
    while (j-- > 0) {
        @disableOptimization
        for (i in range) {
            product *= i;
        }
    }
    print("without opt:\t``product``\t``(system.nanoseconds-t0)/1_000_000.0``ms");
    
    product = one;
    j = iterations;
    t0 = system.nanoseconds;
    while (j-- > 0) {
        for (i in range.by(2)) {
            product *= i;
        }
    }
    print("by with opt:\t``product``\t``(system.nanoseconds-t0)/1_000_000.0``ms");
    
    product = one;
    j = iterations;
    t0 = system.nanoseconds;
    while (j-- > 0) {
        @disableOptimization
        for (i in range.by(2)) {
            product *= i;
        }
    }
    print("by without opt:\t``product``\t``(system.nanoseconds-t0)/1_000_000.0``ms");
}
void rangeIterationBench_run() {
    value r = 0..0;
    for (i in r) {
        print(i);
    }
    
    print("Warmup
           ------");
    rangeIterationBench(1, 1000, 1_000_000);
    
    print("Timed
           ------");
    rangeIterationBench(1, 1000, 1_000_000);
}
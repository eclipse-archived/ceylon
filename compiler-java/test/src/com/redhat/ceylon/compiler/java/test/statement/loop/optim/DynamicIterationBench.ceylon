import ceylon.language.meta{type}

Integer dynamicIterationBench({Integer*} iterable) {
    variable value sum = 0;
    for (int in iterable) {
        sum += int;
    }
    return sum;
}

Integer dynamicIterationBenchNoArrayOpt({Integer*} iterable) {
    variable value sum = 0;
    @disableOptimization:"ArrayIterationDynamic"
    for (int in iterable) {
        sum += int;
    }
    return sum;
}

Integer dynamicIterationBenchNoTupleOpt({Integer*} iterable) {
    variable value sum = 0;
    @disableOptimization:"TupleIterationDynamic"
    for (int in iterable) {
        sum += int;
    }
    return sum;
}

Integer dynamicIterationBenchNoOpts({Integer*} iterable) {
    variable value sum = 0;
    @disableOptimization
    for (int in iterable) {
        sum += int;
    }
    return sum;
}

void dynamicIterationBench_run(Integer n, {Integer*} list, String t) {
    print("``t``, list size=``list.size``");
    variable value result = 0;
    variable value t0 = system.nanoseconds;
    for (i in 0..n) {
        result = dynamicIterationBench(list);
    }
    variable value time = (system.nanoseconds-t0)/1_000_000.0;
    print("dynamicIterationBench:           n=``n``,\ttime=``time``ms,\tresult=``result``");
    
    t0 = system.nanoseconds;
    for (i in 0..n) {
        result = dynamicIterationBenchNoArrayOpt(list);
    }
    time = (system.nanoseconds-t0)/1_000_000.0;
    print("dynamicIterationBenchNoArrayOpt: n=``n``,\ttime=``time``ms,\tresult=``result``");
    
    t0 = system.nanoseconds;
    for (i in 0..n) {
        result = dynamicIterationBenchNoTupleOpt(list);
    }
    time = (system.nanoseconds-t0)/1_000_000.0;
    print("dynamicIterationBenchNoTupleOpt: n=``n``,\ttime=``time``ms,\tresult=``result``");
    
    t0 = system.nanoseconds;
    for (i in 0..n) {
        result = dynamicIterationBenchNoOpts(list);
    }
    time = (system.nanoseconds-t0)/1_000_000.0;
    print("dynamicIterationBenchNoOpts:     n=``n``,\ttime=``time``ms,\tresult=``result``");
    print("");
}

void dynamicIterationBench_main() {
    
    print("warmup
           ------");
    variable value end = 5;
    variable value iterations = 100000;
    
    variable Integer[] taillessTuple = [];
    for (i in 0..end) {
        taillessTuple = [i, *taillessTuple];
    }
    
    dynamicIterationBench_run(iterations, [0, *(1..end)], "[Integer, Range]");
    dynamicIterationBench_run(iterations, Array<Integer>(0..end), "Array");
    dynamicIterationBench_run(iterations, [0, *(Array<Integer>(1..end).sequence())], "[Integer, Array]");
    dynamicIterationBench_run(iterations, taillessTuple, "[Integer, Integer, ...]");
    dynamicIterationBench_run(iterations, [*(0..end)], "Range");
    
    print("
           timed
           -----");
    end = 500;
    iterations = 1000;
    
    taillessTuple = [];
    for (i in 0..end) {
        taillessTuple = [i, *taillessTuple];
    }
    dynamicIterationBench_run(iterations, [0, *(1..end)], "[Integer, Range]");
    dynamicIterationBench_run(iterations, Array<Integer>(0..end), "Array");
    dynamicIterationBench_run(iterations, [0, *(Array<Integer>(1..end).sequence())], "[Integer, Array]");
    dynamicIterationBench_run(iterations, taillessTuple, "[Integer, Integer, ....]");
    dynamicIterationBench_run(iterations, [*(0..end)], "Range");
}
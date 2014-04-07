/*{Integer*} bug1524_fib
        => {0,1,*mapPairs(plus<Integer>,bug1524_fib.rest,bug1524_fib)};

shared void bug1524() {
    print(bug1524_fib.taking(10));
}
*/

shared void bug1524() {
    variable Integer i = 0;
    value it = { i++, i++, i++ };
    assert(i == 0);
    assert(it.sequence == [ 0, 1, 2 ]);
    assert(it.sequence == [ 3, 4, 5 ]);
}
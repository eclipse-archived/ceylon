doc "Add the given `Summable` values.
see (times, sum)
shared Value plus<Value>(Value x, Value y)
        given Value satisfies Summable<Value>
        => x+y;
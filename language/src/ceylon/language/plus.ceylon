"Add the given [[Summable]] values."
see (`function times`, `function sum`)
shared Value plus<Value>(Value x, Value y)
        given Value satisfies Summable<Value>
        => x+y;
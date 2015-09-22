"Add the given [[Summable]] values.
 
     (1..100).by(2).fold(0)(plus<Integer>)"
see (`function times`, `function sum`)
tagged("Numbers")
shared Value plus<Value>(Value x, Value y)
        given Value satisfies Summable<Value>
        => x+y;
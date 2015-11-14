"Multiply the given [[Numeric]] values.
 
     (1..100).by(2).fold(1)(times<Integer>)"
see (`function plus`, `function product`)
tagged("Numbers")
shared Value times<Value>(Value x, Value y)
        given Value satisfies Numeric<Value>
        => x*y;
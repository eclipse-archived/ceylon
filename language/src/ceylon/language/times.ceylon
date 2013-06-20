"Multiply the given `Numeric` values."
//see (`plus`, `product`)
shared Value times<Value>(Value x, Value y)
        given Value satisfies Numeric<Value>
        => x*y;
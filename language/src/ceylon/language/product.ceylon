"Given a nonempty stream of `Numeric` values, return 
 the product of the values."
see (`function sum`)
shared Value product<Value>({Value+} values) 
        given Value satisfies Numeric<Value> {
    variable value product = values.first;
    for (val in values.rest) {
        product*=val;
    }
    return product;
}

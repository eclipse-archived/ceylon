doc "Given a nonempty sequence of `Summable` values, 
     return the sum of the values."
see (Summable)
shared Value sum<Value>(Sequence<Value> values) 
        given Value satisfies Summable<Value> {
    variable value sum = values.first;
    for (val in values.rest) {
        sum+=val;
    }
    return sum;
}

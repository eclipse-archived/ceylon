doc "Given a sequence of `Comparable` values, return the 
     largest value in the sequence."
see (Comparable, min, largest)
shared Value max<Value>(Sequence<Value> values) 
        given Value satisfies Comparable<Value> {
    variable value max := values.first;
    for (val in values.rest) {
        if (val>max) {
            max:=val;
        }
    }
    return max;
}

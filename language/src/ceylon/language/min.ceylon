doc "Given a sequence of `Comparable` values, return the 
     smallest value in the sequence.`
see (Comparable, max, smallest)
shared Value min<Value>(Sequence<Value> values) 
        given Value satisfies Comparable<Value> {
    variable value min := values.first;
    for (val in values.rest) {
        if (val<min) {
            min:=val;
        }
    }
    return min;
}

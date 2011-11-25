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

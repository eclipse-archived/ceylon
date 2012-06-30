doc "Given a nonempty sequence of `Comparable` values, 
     return the smallest value in the sequence."
see (Comparable, max, smallest)
shared Result min<Value,Result>(Iterable<Value>&ContainerWithFirstElement<Result> values) 
        given Value satisfies Comparable<Value> & Result {
    ContainerWithFirstElement<Result> cwfe = values;
    value first = cwfe.first;
    if (is Value first) {
        variable value min:=first;
        for (val in values.rest) {
            if (val<min) {
                min:=val;
            }
        }
        return min;
    }
    else {
        return first;
    }
}

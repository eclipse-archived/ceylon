doc "Given a nonempty sequence of `Comparable` values, 
     return the largest value in the sequence."
see (Comparable, min, largest)
shared Result max<Value,Result>(Iterable<Value>&ContainerWithFirstElement<Result> values) 
        given Value satisfies Comparable<Value> & Result {
    ContainerWithFirstElement<Result> cwfe = values;
    value first = cwfe.first;
    if (is Value first) {
        variable value max:=first;
        for (val in values.rest) {
            if (val>max) {
                max:=val;
            }
        }
        return max;
    }
    else {
        return first;
    }
}

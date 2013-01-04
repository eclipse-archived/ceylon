doc "Given a nonempty sequence of `Comparable` values, 
     return the largest value in the sequence."
see (Comparable, min, largest)
shared Null|Value max<Value,Null>({Value...}&ContainerWithFirstElement<Value,Null> values) 
        given Value satisfies Comparable<Value>
        given Null satisfies Nothing {
    ContainerWithFirstElement<Value,Null> cwfe = values;
    value first = cwfe.first;
    if (exists first) {
        variable value max=first;
        for (val in values.rest) {
            if (val>max) {
                max=val;
            }
        }
        return max;
    }
    else {
        return first;
    }
}

doc "Given a nonempty sequence of `Comparable` values, 
     return the smallest value in the sequence."
see (Comparable, max, smallest)
shared Null|Value min<Value,Null>({Value...}&ContainerWithFirstElement<Value,Null> values) 
        given Value satisfies Comparable<Value>
        given Null satisfies Nothing {
    ContainerWithFirstElement<Value,Null> cwfe = values;
    value first = cwfe.first;
    if (exists first) {
        variable value min=first;
        for (val in values.rest) {
            if (val<min) {
                min=val;
            }
        }
        return min;
    }
    else {
        return first;
    }
}

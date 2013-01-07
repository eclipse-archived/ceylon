doc "Given a nonempty sequence of `Comparable` values, 
     return the largest value in the sequence."
see (Comparable, min, largest)
shared Absent|Value max<Value,Absent>({Value...}&ContainerWithFirstElement<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    ContainerWithFirstElement<Value,Absent> cwfe = values;
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

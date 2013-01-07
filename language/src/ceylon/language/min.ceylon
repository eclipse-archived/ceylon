doc "Given a nonempty sequence of `Comparable` values, 
     return the smallest value in the sequence."
see (Comparable, max, smallest)
shared Absent|Value min<Value,Absent>({Value...}&ContainerWithFirstElement<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    ContainerWithFirstElement<Value,Absent> cwfe = values;
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

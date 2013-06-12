"Given a nonempty stream of `Comparable` values, 
 return the largest value in the stream."
see (Comparable, min, largest)
shared Absent|Value max<Value,Absent>(Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value first=values.first;
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

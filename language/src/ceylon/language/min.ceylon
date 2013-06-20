"Given a nonempty stream of `Comparable` values, 
 return the smallest value in the stream."
see (`Comparable`, max, smallest)
shared Absent|Value min<Value,Absent>(Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value first=values.first;
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

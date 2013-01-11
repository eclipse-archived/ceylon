doc "Given a nonempty sequence of `Comparable` values, 
     return the smallest value in the sequence."
see (Comparable, max, smallest)
shared Absent|Value min<Value,Absent>({Value...}&Container<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    Container<Value,Absent> c = values;
    value first = c.first;
    //value first = (values of Container<Value,Absent>).first;
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

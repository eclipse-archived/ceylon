"Given a nonempty stream of `Comparable` values, 
 return the smallest value in the stream."
see (`interface Comparable`, 
     `function max`, 
     `function smallest`)
shared Absent|Value min<Value,Absent>(Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value it = values.iterator();
    if (!is Finished first = it.next()) {
        variable value min=first;
        while (!is Finished val = it.next()) {
            if (val<min) {
                min=val;
            }
        }
        return min;
    }
    else {
        "iterable must be empty"
        assert (is Absent null);
        return null;
    }
}

"Given a nonempty stream of `Comparable` values, 
 return the largest value in the stream."
see (`interface Comparable`, 
     `function min`, 
     `function largest`)
shared Absent|Value max<Value,Absent>(Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value it = values.iterator();
    if (is Value first = it.next()) {
        variable value max=first;
        while (is Value val = it.next()) {
            if (val>max) {
                max=val;
            }
        }
        return max;
    }
    else {
        "iterable must be empty"
        assert (is Absent null);
        return null;
    }
}

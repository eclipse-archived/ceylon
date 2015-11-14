"Given a stream of [[Comparable]] values, return the 
 smallest value in the stream, or `null` if the stream is
 empty.
 
 For any nonempty stream `it`, `min(it)` evaluates to the 
 first element of `it` such that for every element `e` of 
 `it`, `min(it) <= e`."
see (`interface Comparable`, 
     `function max`,
     `function smallest`)
tagged("Comparisons", "Streams")
shared native Absent|Value min<Value,Absent>
        (Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null;

shared native("js") Absent|Value min<Value,Absent>
        (Iterable<Value,Absent> values) 
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    value it = values.iterator();
    if (!is Finished first = it.next()) {
        variable value min = first;
        while (!is Finished val = it.next()) {
            if (val<min) {
                min = val;
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

shared native("jvm") Absent|Value min<Value,Absent>
        (Iterable<Value,Absent> values)
        given Value satisfies Comparable<Value>
        given Absent satisfies Null {
    
    value it = values.iterator();
    switch (first = it.next())
    case (is Finished) {
        "iterable must be empty"
        assert (is Absent null);
        return null;
    }
    case (is Integer) {
        variable Integer min = first;
        while (is Integer val = it.next()) {
            if ((val of Integer) < min) {
                min = val;
            }
        }
        assert (is Value result = min);
        return result;
    }
    case (is Float) {
        variable Float min = first;
        while (is Float val = it.next()) {
            if ((val of Float) < min) {
                min = val;
            }
        }
        assert (is Value result = min);
        return result;
    }
    else {
        variable value min = first;
        while (!is Finished val = it.next()) {
            if (val<min) {
                min = val;
            }
        }
        return min;
    }
}
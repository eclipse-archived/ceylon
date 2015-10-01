"Given a nonempty stream of [[Summable]] values, return the 
 sum of the values.
 
     {Float+} values = ... ;
     Float total = sum(values);
 
 For the case of a possibly-empty stream, form a nonempty 
 stream starting with the zero element (the [[additive 
 identity|Summable]]).
 
     {Float*} values = ... ;
     Float total = sum { 0.0, *values };"
see (`function product`)
tagged("Streams", "Numbers")
shared native Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value>;

shared native("js") Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value> {
    value it = values.iterator();
    assert (!is Finished first = it.next());
    variable value sum = first;
    while (!is Finished val = it.next()) {
        sum += val;
    }
    return sum;
}

shared native("jvm") Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value> {
    value it = values.iterator();
    switch (first = it.next())
    case (is Integer) {
        // unbox; don't infer type Value&Integer
        variable Integer sum = first;
        while (is Integer val = it.next()) {
            Integer unboxed = val;
            sum += unboxed;
        }
        assert (is Value result = sum);
        return result;
    }
    case (is Float) {
        // unbox; don't infer type Value&Float
        variable Float sum = first;
        while (is Float val = it.next()) {
            Float unboxed = val;
            sum += unboxed;
        }
        assert (is Value result = sum);
        return result;
    }
    case (is Finished) {
        assert (false);
    }
    else {
        variable value sum = first;
        while (!is Finished val = it.next()) {
            sum += val;
        }
        return sum;
    }
}
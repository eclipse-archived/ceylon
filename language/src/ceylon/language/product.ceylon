"Given a nonempty stream of [[Numeric]] values, return the 
 product of the values.
 
     {Float+} values = ... ;
     Float result = product(values);
 
 For the case of a possibly-empty stream, form a nonempty 
 stream starting with the unit element (the [[multiplicative
 identity|Numeric]]).
 
     {Float*} values = ... ;
     Float result = product { 1.0, *values };"
see (`function sum`)
tagged("Streams", "Numbers")
shared native Value product<Value>({Value+} values) 
        given Value satisfies Numeric<Value>;

shared native("js") Value product<Value>({Value+} values) 
        given Value satisfies Numeric<Value> {
    value it = values.iterator();
    assert (!is Finished first = it.next());
    variable value product = first;
    while (!is Finished val = it.next()) {
        product *= val;
    }
    return product;
}

shared native("jvm") Value product<Value>({Value+} values) 
        given Value satisfies Numeric<Value> {
    value it = values.iterator();
    switch (first = it.next())
    case (is Integer) {
        // unbox; don't infer type Value&Integer
        variable Integer product = first;
        while (is Integer val = it.next()) {
            Integer unboxed = val;
            product *= unboxed;
        }
        assert (is Value result = product);
        return result;
    }
    case (is Float) {
        // unbox; don't infer type Value&Float
        variable Float product = first;
        while (is Float val = it.next()) {
            Float unboxed = val;
            product *= unboxed;
        }
        assert (is Value result = product);
        return result;
    }
    case (is Finished) {
        assert (false);
    }
    else {
        variable value product = first;
        while (!is Finished val = it.next()) {
            product *= val;
        }
        return product;
    }
}
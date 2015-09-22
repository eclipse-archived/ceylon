"Given a nonempty stream of [[Numeric]] values, return the 
 product of the values.
 
     {Float+} values = ... ;
     Float result = sum(values);
 
 For the case of a possibly-empty stream, form a nonempty 
 stream starting with the unit element (the [[multiplicative
 identity|Numeric]]).
 
     {Float*} values = ... ;
     Float result = product { 1.0, *values };"
see (`function sum`)
tagged("Streams", "Numbers")
shared Value product<Value>({Value+} values) 
        given Value satisfies Numeric<Value> {
    variable value product = values.first;
    for (val in values.rest) {
        product *= val;
    }
    return product;
}

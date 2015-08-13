"Given a nonempty stream of [[Summable]] values, return the 
 sum of the values.
 
     {Float+} values = ... ;
     Float total = sum(values);
 
 For the case of a possibly-empty stream, form a nonempty 
 stream starting with the zero element.
 
     {Float*} values = ... ;
     Float total = sum { 0, values };"
see (`function product`)
shared Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value> {
    value it = values.iterator();
    assert (!is Finished first = it.next());
    variable value sum = first;
    while (!is Finished val = it.next()) {
        sum += val;
    }
    return sum;
}

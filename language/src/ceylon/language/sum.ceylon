"Given a nonempty stream of [[Summable]] values, return the 
 sum of the values."
see (`function product`)
shared Value sum<Value>({Value+} values) 
        given Value satisfies Summable<Value> {
    value it = values.iterator();
    assert (is Value first = it.next());
    variable value sum = first;
    while (is Value val = it.next()) {
        sum+=val;
    }
    return sum;
}

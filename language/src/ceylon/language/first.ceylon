"The first of the given values (usually a comprehension),
 if any.
 
     Float firstPositive = first { for (x in xs) if (x>0.0) x };"
shared Absent|Value first<Value,Absent>(Iterable<Value,Absent> values)
        given Absent satisfies Null {
    Value? first;
    if (!is Finished next = values.iterator().next()) {
        first = next;
    }
    else {
        first = null;
    }
    assert (is Absent|Value first);
    return first;
}

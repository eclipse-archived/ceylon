shared native Absent|Value internalFirst<Value,Absent>(Iterable<Value,Absent> values)
        given Absent satisfies Null {
    Value? first;
    if (!is Finished next = values.iterator.next()) {
        first = next;
    }
    else {
        first = null;
    }
    assert (is Absent|Value first);
    return first;
}

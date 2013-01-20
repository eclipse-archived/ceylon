doc "The first of the given elements (usually a comprehension),
     if any."
shared Absent|Value first<Value,Absent>(Iterable<Value,Absent> values)
        given Absent satisfies Null 
        => internalFirst(values);

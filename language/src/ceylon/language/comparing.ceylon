"A comparator which delegates to each of the given
 [[comparators]] in turn, returning the first case of
 [[smaller]] or [[larger]] if either are found,
 otherwise returning [[equal]].
 
 Consider the following type:
 
     class Person(shared Integer age, shared String name) {}
 
 Instances of `Person` can be compared by `age`, breaking
 ties by `name`, with this:
 
     comparing(byIncreasing(Person.age), byIncreasing(Person.name))
 
 If no `comparators` are given, the resulting comparator
 always returns `equal`."
see (`function byDecreasing`, `function byIncreasing`)
shared Comparison comparing<in Value>(Comparison(Value,Value)* comparators)
            (Value x, Value y) {
    for (compare in comparators) {
        value comparison = compare(x, y);
        if (comparison != equal) {
            return comparison;
        }
    }
    else {
        return equal;
    }
}

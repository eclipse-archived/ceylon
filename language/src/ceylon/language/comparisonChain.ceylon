"A comparator which delegates to each of the given
 [[comparators]] in turn, returning the first case of
 [[smaller]] or [[larger]] if either are found,
 otherwise returning [[equal]].
 
 Consider the following type:
 
     class Person(shared Integer age, shared String name) {}
 
 Instances of `Person` can be compared by `age`, breaking
 ties by `name`, with this:
 
     comparisonChain{byIncreasing(Person.age), byIncreasing(Person.name)}"
see (`function byDecreasing`, `function byIncreasing`)
shared Comparison comparisonChain<Value>({Comparison(Value,Value)*} comparators)
            (Value x, Value y)
    => {for (f in comparators) f(x, y)}.find(not(equal.equals)) else equal;

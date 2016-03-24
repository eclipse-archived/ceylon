"A single comparator function which delegates to each of the 
 given [[comparator functions|comparators]] in turn, 
 returning the first result of [[smaller]] or [[larger]] if 
 any, or returning [[equal]] otherwise.
 
 Consider the following type:
 
     class Person(shared Integer age, shared String name) {}
 
 A stream of `Person`s may be sorted by `age`, breaking ties 
 by `name`, like this:
 
     people.sort(comparing(byDecreasing(Person.age), byIncreasing(Person.name)))
 
 If no `comparators` are given, the resulting comparator
 always returns `equal`.
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (`function byDecreasing`,
     `function byIncreasing`,
     `function Iterable.max`,
     `function Iterable.sort`)
tagged("Comparisons")
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

/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
see (function byDecreasing,
     function byIncreasing,
     function Iterable.max,
     function Iterable.sort)
tagged("Comparisons")
since("1.1.0")
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

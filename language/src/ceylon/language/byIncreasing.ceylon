"A comparator which orders elements in increasing order 
 according to the `Comparable` returned by the given 
 `comparable()` function."
see (`function byDecreasing`)
shared Comparison byIncreasing<Element,Value>(Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value> 
                => comparable(x)<=>comparable(y);
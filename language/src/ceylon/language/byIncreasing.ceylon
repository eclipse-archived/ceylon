doc "A comparator which orders elements in increasing order 
     according to the `Comparable` returned by the given 
     `comparable()` function."
see(byDecreasing)
shared Comparison? byIncreasing<Element,Value>(Value? comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value> {
    if (exists cx = comparable(x),
        exists cy = comparable(y)) {
        return cx<=>cy;
    }
    else {
        return null;
    }
}
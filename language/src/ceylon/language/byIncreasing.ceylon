shared Comparison? byIncreasing<Element,Value>(Value? comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value> {
    if (exists cx = comparable(x)) {
        if (exists cy = comparable(y)) {
            return cx<=>cy;
        }
    }
    return null;
}
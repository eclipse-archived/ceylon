"A comparator which orders elements in decreasing order 
 according to the `Comparable` value returned by the given 
 [[comparable]] function.
 
      \"Hello World!\".sort(byDecreasing(Character.lowercased))"
see (`function byIncreasing`)
shared Comparison byDecreasing<Element,Value>(Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value>
                => comparable(y)<=>comparable(x);
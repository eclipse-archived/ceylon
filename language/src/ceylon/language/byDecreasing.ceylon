"A comparator which orders elements in decreasing order 
 according to the `Comparable` value returned by the given 
 [[comparable]] function.
 
      \"Hello World!\".sort(byDecreasing(Character.lowercased))
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (`function byIncreasing`,
     `function Iterable.max`,
     `function Iterable.sort`)
shared Comparison byDecreasing<Element,Value>
            (Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value>
                => comparable(y)<=>comparable(x);
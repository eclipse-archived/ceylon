"Produces a comparator function which orders elements in 
 decreasing order according to the [[Comparable]] value 
 returned by the given [[comparable]] function.
 
      \"Hello World!\".sort(byDecreasing(Character.lowercased))
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (`function byIncreasing`,
     `function decreasing`,
     `function Iterable.max`,
     `function Iterable.sort`)
shared Comparison byDecreasing<Element,Value>
            (Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value>
                => comparable(y)<=>comparable(x);

"A comparator function which orders elements in decreasing 
 [[natural order|Comparable]].
 
        \"Hello World!\".sort(decreasing)
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (`function increasing`,
     `function byDecreasing`,
     `function Iterable.max`,
     `function Iterable.sort`)
shared Comparison decreasing<Element>(Element x, Element y)
        given Element satisfies Comparable<Element> 
        => y<=>x;

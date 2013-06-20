"Given two `Comparable` values, return largest of the
 two."
//see (`Comparable`, `smallest`, `max`)
shared Element largest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> =>
                x>y then x else y;
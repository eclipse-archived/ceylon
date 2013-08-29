"Given two `Comparable` values, return largest of the
 two."
see (`interface Comparable`, 
     `function smallest`, 
     `function max`)
shared Element largest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> =>
                x>y then x else y;
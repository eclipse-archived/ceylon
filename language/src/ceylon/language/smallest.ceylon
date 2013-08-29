"Given two `Comparable` values, return smallest of the
 two."
see (`interface Comparable`, 
     `function largest`, 
     `function min`)
shared Element smallest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> =>
                x<y then x else y;
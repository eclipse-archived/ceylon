"Given two `Comparable` values, return smallest of the
 two."
see (`Comparable`, `largest`, `min`)
shared Element smallest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> =>
                x<y then x else y;
"Given two [[Comparable]] values, return smallest of the two.
 
 If exactly one of the given values is an 
 [[undefined `Float`|Float.undefined]], return the other 
 value."
see (`interface Comparable`, 
     `function largest`, 
     `function min`)
tagged("Comparisons")
shared Element smallest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> 
        => if (is Float x, x.undefined) then y
      else if (x<y) then x else y;

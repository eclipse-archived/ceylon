"Given two [[Comparable]] values, return largest of the two.
 
 If exactly one of the given values is an 
 [[undefined `Float`|Float.undefined]], return the other 
 value."
see (`interface Comparable`, 
     `function smallest`, 
     `function max`)
tagged("Comparisons")
shared Element largest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> 
        => if (is Float x, x.undefined) then y
      else if (is Float y, y.undefined) then x
      else if (x>y) then x else y;

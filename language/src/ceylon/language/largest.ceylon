"Given two [[Comparable]] values, return largest of the two.
 
 If exactly one of the given values violates the reflexivity 
 requirement of [[Object.equals]] such that `x!=x`, then the 
 other value is returned. In particular, if exactly one is 
 an [[undefined `Float`|Float.undefined]], it is not 
 returned."
see (`interface Comparable`, 
     `function smallest`, 
     `function max`)
tagged("Comparisons")
shared Element largest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> 
        => if (x!=x || y>x) then y else x;

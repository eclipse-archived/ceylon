"Given two [[Comparable]] values, return smallest of the two.
 
 If exactly one of the given values violates the reflexivity 
 requirement of [[Object.equals]] such that `x!=x`, then the 
 other value is returned. In particular, if exactly one is 
 an [[undefined `Float`|Float.undefined]], it is not 
 returned.
 
 _On the JVM platform, for arguments of type `Integer` or 
 `Float`, prefer [[Integer.smallest]] or [[Float.smallest]]
 in performance-sensitive code._"
see (`interface Comparable`, 
     `function largest`, 
     `function min`, 
     `function Integer.smallest`,
     `function Float.smallest`)
tagged("Comparisons")
shared Element smallest<Element>(Element x, Element y) 
        given Element satisfies Comparable<Element> 
        => if (x!=x || y<x) then y else x;

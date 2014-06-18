"Abstraction of _ordinal types_, that is, types where each
 values has a [[successor]] and [[predecessor]], such as:
  
 - types which represent or are isomorphic to the 
   mathematical integers, for example, [[Integer]] and other 
   [[Integral]] numeric types, and even [[Character]], along 
   with
 - enumerated types which are isomorphic to the mathematical
   integers under modular arithmetic, for example, the days
   of the week.
 
 The _increment_ operator `++` and _decrement_ operator `--`
 are defined for all types which satisfy `Ordinal`.
 
     function increment() {
         count++;
     }
 
 Most ordinal types have a [[total order|Comparable]]. If 
 an ordinal type has a total order, then it should satisfy:
 
 - `x.successor > x`, and
 - `x.predecessor < x`."
see (`class Character`, 
     `class Integer`, 
     `interface Integral`, 
     `interface Comparable`,
     `interface Enumerable`)
by ("Gavin")
shared interface Ordinal<out Other> of Other
        given Other satisfies Ordinal<Other> {
    
    "The successor of this value."
    shared formal Other successor;
    
    "The predecessor of this value."
    shared formal Other predecessor;
    
}

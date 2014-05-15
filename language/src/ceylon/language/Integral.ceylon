"""Abstraction of integral numeric types. That is, types 
   with no fractional part, including [[Integer]].
   
   The division operation for integral numeric types results 
   in a remainder. Therefore, integral numeric types have 
   [[an operation|remainder]], denoted by the _remainder_
   operator `%`, to determine the remainder of any division 
   operation.
   
       if (n%2==0) {
           print("Even!");
       }
   
   Division and the remainder operation should satisfy: 
   
   - `x == (x/y)*y + x%y`
   
   for any instance `y` other than `0`.
   
   `Integral` numeric types are also [[Ordinal]], so ranges 
   of integral values may be produced using the 
   [[segment and span operators|Ordinal]].
       
       // Iterate from 0 to 100 inclusive
       for (i in 0..100) {
           print("The square of ``i`` is ``i^2``");
       }
       
       // Iterate all indices of the array, 
       // from 0 to array.size-1
       for (i in 0:array.size) {
           print(array[i]);
       }"""
see (`class Integer`)
by ("Gavin")
shared interface Integral<Other> of Other
        satisfies Number<Other> & 
                  Enumerable<Other>
        given Other satisfies Integral<Other> {
    
    "The remainder, after dividing this number by the given 
     number."
    see (`function Numeric.divided`)
    shared formal Other remainder(Other other);
    
    "Determine if the number is the additive identity."
    shared formal Boolean zero;
    
    "Determine if the number is the multiplicative identity."
    shared formal Boolean unit;
    
    "Determine if this number is a factor of the given 
     number."
    shared Boolean divides(Other other) 
            => (other % (this of Other)).zero;
    
}
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
   
   All `Integral` numeric types are also [[Enumerable]], so 
   ranges of integral values may be produced using the 
   [[measure]] and [[span]] operators.
       
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
tagged("Numbers")
shared interface Integral<Other> of Other
        satisfies Number<Other> & 
                  Enumerable<Other>
        given Other satisfies Integral<Other> {
    
    "The remainder, after dividing this number by the given 
     number."
    see (`function Numeric.divided`, `function modulo`)
    shared formal Other remainder(Other other);

    "The modulo, after dividing this number by the given 
     number. This differs from [[remainder]] in that the
     result is always positive."
    see (`function Numeric.divided`, `function remainder`)
    throws (`class AssertionError`, "If the modulus is not strictly positive")
    shared default Other modulo(Other modulus){
        if (!modulus.positive) {
            throw AssertionError("modulus must be positive: ``modulus``");
        }
        value result = remainder(modulus);
        if (result.negative){
            return result + modulus;
        }
        return result;
    }
    
    "Determine if the number is the additive identity."
    shared formal Boolean zero;
    
    "Determine if the number is the multiplicative identity."
    shared formal Boolean unit;
    
    "Determine if this number is a factor of the given 
     number."
    shared default Boolean divides(Other other) 
            => (other % (this of Other)).zero;
    
}

"Abstraction of [[numeric|Numeric]] types with a natural 
 [[total order|Comparable]], including the built-in numeric 
 types [[Integer]] and [[Float]].
 
 A `Number` has a well-defined [[magnitude]] together with a 
 [[sign]] of type [[Integer]], which should satisfy:
 
 - `x.magnitude >= 0`
 - `x.magnitude == 0` iff `x==0`
 - `x.sign==0` iff `x==0`
 - `x.sign==1` iff `x>0`
 - `x.sign==-1` iff `x<0`
 
 where `0` is the additive identity of the numeric type.
 
 Not every value commonly considered to be a \"number\" is
 a `Number`. For example, complex numbers aren't `Number`s
 since they don't have a total order."
see (`class Integer`, `class Float`)
by ("Gavin")
shared interface Number<Other> of Other
        satisfies Numeric<Other> & 
                  Comparable<Other>
        given Other satisfies Number<Other> {
    
    "The magnitude of this number. Must satisfy: 
     
     - `magnitude>=0` 
     - `magnitude==0` iff `this==0`
     
     where `0` is the additive identity."
    shared formal Other magnitude;
    
    "The sign of this number: 
     
     - `1` if the number is positive, 
     - `-1` if it is negative, or 
     - `0` if it is zero."
    shared formal Integer sign;
    
    "Determine if the number is strictly positive, that is, 
     if `this>0`, where `0` is the additive identity."
    shared formal Boolean positive;
    
    "Determine if the number is strictly negative, that is, 
     if `this<0`, where `0` is the additive identity."
    shared formal Boolean negative;
    
    "The fractional part of this number, after truncation of 
     the integral part. For [[Integral]] numeric types, the 
     fractional part is always zero."
    shared formal Other fractionalPart;
    
    "The integral value of the number after truncation of 
     the fractional part. For [[Integral]] numeric types, 
     the integral value of a number is the number itself."
    shared formal Other wholePart;
    
    "The result of multiplying this number by the given 
     [[Integer]]."
    shared formal Other timesInteger(Integer integer);
    
    "The result of adding this number to the given 
     [[Integer]]."
    shared formal Other plusInteger(Integer integer);
    
    "The result of raising this number to the given 
     [[Integer]] power."
    shared formal Other powerOfInteger(Integer integer);
    
}


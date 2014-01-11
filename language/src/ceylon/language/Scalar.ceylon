"Abstraction of [[numeric|Numeric]] types representing 
 scalar values with a natural [[total order|Comparable]], 
 including the built-in numeric types [[Integer]] and 
 [[Float]].
 
 `Scalar`s have a well-defined [[magnitude]], which should
 satisfy:
 
 - `x.magnitude >= 0`
 - `x.magnitude == 0` iff `x==0`
 
 where `0` is the additive identity of the numeric type."
see (`class Integer`, `class Float`)
by ("Gavin")
shared interface Scalar<Other> of Other
        satisfies Numeric<Other> & 
                  Comparable<Other> &
                  Number
        given Other satisfies Scalar<Other> {
    
    "The magnitude of this number. Must satisfy: 
     
     - `magnitude>=0` 
     - `magnitude==0` iff `this==0`
     
     where `0` is the additive identity."
    shared actual formal Other magnitude;
    
    "Determine if the number is strictly positive, that is, 
     if `this>0`, where `0` is the additive identity."
    shared actual formal Boolean positive;
    
    "Determine if the number is strictly negative, that is, 
     if `this<0`, where `0` is the additive identity."
    shared actual formal Boolean negative;
    
    shared actual formal Other fractionalPart;
    
    shared actual formal Other wholePart;
    
}


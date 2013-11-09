"Abstraction of numeric types representing scalar
 values, including `Integer` and `Float`."
see (`class Integer`, `class Float`)
by ("Gavin")
shared interface Scalar<Other> of Other
        satisfies Numeric<Other> & Comparable<Other> &
                  Number

        given Other satisfies Scalar<Other> {
    
    "The magnitude of this number."
    shared actual formal Other magnitude;
        
    "The fractional part of the number, after truncation 
     of the integral part. For integral numeric types,
     the fractional part is always zero."
    shared actual formal Other fractionalPart;
    
    "The integral value of the number after truncation 
     of the fractional part. For integral numeric types,
     the integral value of a number is the number 
     itself."
    shared actual formal Other wholePart;

}


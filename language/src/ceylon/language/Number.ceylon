doc "Abstraction of numbers. Numeric operations are provided
     by the subtype `Numeric`. This type defines operations
     which can be expressed without reference to the self
     type `Other` of `Numeric`."
see (Numeric)
by "Gavin"
shared interface Number 
        satisfies Equality & Format {
    
    doc "Determine if the number is positive."
    shared formal Boolean positive;
    
    doc "Determine if the number is negative."    
    shared formal Boolean negative;

    doc "The number, represented as a `Float`."
    throws (OverflowException,
           "if the number is too large to be represented 
            as a `Float`")
    shared formal Float float;
        
    doc "The number, represented as an `Integer`, after 
         truncation of any fractional part."
    throws (OverflowException,
           "if the number is too large to be represented 
            as an `Integer`")
    shared formal Integer integer;
    
    doc "The magnitude of the number."
    shared formal Number magnitude;
    
    doc "The sign of this number. Returns 1 if the number is 
         positive, -1 if it is negative, or 0 if it is 
         zero."
    shared formal Integer sign;
    
    doc "The fractional part of the number, after truncation 
         of the integral part."
    shared formal Number fractionalPart;
    
    doc "The integral value of the number after truncation 
         of the fractional part."
    shared formal Number wholePart;

}

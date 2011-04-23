shared interface Number satisfies Equality {
    
    doc "Determine if the number represents
         an integer value"
    shared formal Boolean integral;
    
    doc "Determine if the number is positive"
    shared formal Boolean positive;
    
    doc "Determine if the number is negative"    
    shared formal Boolean negative;

    doc "Determine if the number is zero"
    shared formal Boolean zero;
    
    doc "Determine if the number is one"
    shared formal Boolean unit;
    
    doc "The number, represented as a |Float|"
    throws (FloatOverflowException
            -> "if the number is too large to be
                represented as a |Float|")
    shared formal Float float;
        
    doc "The number, represented as an |Integer|,
         after truncation of any fractional 
         part"
    throws (IntegerOverflowException
            -> "if the number is too large to be
                represented as an |Integer|")
    shared formal Integer integer;
    
    doc "The number, represented as a |Natural|,
         after truncation of any fractional 
         part"
    throws (NegativeNumberException
            -> "if the number is negative")
    shared formal Natural natural;
    
    doc "The magnitude of the number"
    shared formal subtype magnitude;
    
    doc "1 if the number is positive, -1 if it
         is negative, or 0 if it is zero."
    shared formal subtype sign;
    
    doc "The fractional part of the number,
         after truncation of the integral
         part"
    shared formal subtype fractionalPart;
    
    doc "The integral value of the number 
         after truncation of the fractional
         part"
    shared formal subtype wholePart;

}

public interface Number {
    
    doc "Determine if the number represents
         an integer value"
    public Boolean integral;
    
    doc "Determine if the number is positive"
    public Boolean positive;
    
    doc "Determine if the number is negative"    
    public Boolean negative;

    doc "Determine if the number is zero"
    public Boolean zero;
    
    doc "Determine if the number is one"
    public Boolean unit;
    
    doc "The number, represented as a |Decimal|"
    public Decimal decimal;

    doc "The number, represented as a |Float|"
    throws (FloatOverflowException
            -> "if the number is too large to be
                represented as a |Float|")
    public Float float;
    
    doc "The number, represented as an |Whole|,
         after truncation of any fractional 
         part"
    public Whole whole;
    
    doc "The number, represented as an |Integer|,
         after truncation of any fractional 
         part"
    throws (IntegerOverflowException
            -> "if the number is too large to be
                represented as an |Integer|")
    public Integer integer;
    
    doc "The number, represented as a |Natural|,
         after truncation of any fractional 
         part"
    throws (NegativeNumberException ->
            "if the number is negative")
    public Natural natural;
    
    doc "The magnitude of the number"
    public subtype magnitude;
    
    doc "1 if the number is positive, -1 if it
         is negative, or 0 if it is zero."
    public subtype sign;
    
    doc "The fractional part of the number,
         after truncation of the integral
         part"
    public subtype fractionalPart;
    
    doc "The integral value of the number 
         after truncation of the fractional
         part"
    public subtype wholePart;

}
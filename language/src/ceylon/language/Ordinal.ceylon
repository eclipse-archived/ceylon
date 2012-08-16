doc "Abstraction of ordinal types, that is, types with 
     successor and predecessor operations, including
     `Integer` and other `Integral` numeric types.
     `Character` is also considered an ordinal type. 
     `Ordinal` types may be used to generate a `Range`."
see (Character, Integer, Integral, Range)
by "Gavin"
shared interface Ordinal<Other> of Other
        given Other satisfies Ordinal<Other> {

    doc "The successor of this value."
    throws (OverflowException,
           "if this is the maximum value")
    shared formal Other successor;
    
    doc "The predecessor of this value."
    throws (OverflowException,
           "if this is the minimum value")
    shared formal Other predecessor;

    doc "Returns the distance between the receiver and
         another Ordinal of the same type."
    shared formal Integer distanceFrom(Other other);

}
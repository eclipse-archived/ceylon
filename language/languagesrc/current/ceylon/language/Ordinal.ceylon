shared interface Ordinal<T>
        satisfies Equality {

    doc "The unary |++| operator. The successor of this instance."
    throws (OutOfRangeException
            -> "if this is the maximum value")
    shared formal T successor;
    
    doc "The unary |--| operator. The predecessor of this instance."
    throws (OutOfRangeException
            -> "if this is the minimum value")
    shared formal T predecessor;
    
}
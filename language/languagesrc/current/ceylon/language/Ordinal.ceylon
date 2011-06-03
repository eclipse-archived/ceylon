shared interface Ordinal<out Other>
        satisfies Equality 
        given Other satisfies Ordinal<Other> {

    doc "The unary |++| operator. The successor of this instance."
    throws (OutOfRangeException
            -> "if this is the maximum value")
    shared formal Other successor;
    
    doc "The unary |--| operator. The predecessor of this instance."
    throws (OutOfRangeException
            -> "if this is the minimum value")
    shared formal Other predecessor;
    
}
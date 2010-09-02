public interface Ordinal {

    doc "The unary |++| operator. The successor of this instance."
    throws (OutOfRangeException
            -> "if this is the maximum value")
    public subtype successor;
    
    doc "The unary |--| operator. The predecessor of this instance."
    throws (OutOfRangeException
            -> "if this is the minimum value")
    public subtype predecessor;
    
}
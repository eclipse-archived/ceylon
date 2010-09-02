public extension class Sequences<out X>(Sequence<X> sequence) {
    
    doc "The first element of the sequence, or 
         |null| if the sequence has no elements."
    public X? first {
        return sequence[0]
    }
    
    doc "The last element of the sequence, or 
         |null| if the sequence has no elements."
    public X? last {
        if (exists Natural index = sequence.lastIndex) {
            return sequence[index]
        }
        else {
            return null
        }
    }

}
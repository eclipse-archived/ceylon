public extension class Sequences<out X>(Sequence<X> sequence) {
        
    doc "The binary join operator x + y. 
         The returned sequence does not reflect changes
         to the original sequences."
    public static S join<S,T>(S... sequences) 
            where S>=Sequence<T> & S(T... elements) {

        class JoinedIterable<T>
            implements Iterable<T> {  }

        return S( JoinedIterable() );
    }
        
    doc "The ternary range operator x[from..to], along
         with the binary upper range x[from...] operator. 
         The returned sequence does not reflect changes 
         to the original sequence."
    public static S range<S,T>(S sequence, Natural from, Natural to=sequence.lastIndex) 
             where S>=Sequence<T> & S(T... elements) {

        class SubrangeIterable<T>
            implements Iterable<T> {  }

        return S( SubrangeIterable() );
    }


    doc "The first element of the sequence."
    throws #EmptyException
           "if the list is empty"
    public X first {
        if (sequence.empty) {
            throw EmptyException();
        }
        else {
            return sequence[0];
        }
    }
    
    doc "The last element of the sequence."
    throws #EmptyException
           "if the list is empty"
    public X last {
        return sequence[sequence.lastIndex];
    }
    
    doc "The first element of the sequence, or null if
         the sequence is empty."
    public optional X firstOrNull {
        if (sequence.empty) {
            return first;
        }
        else {
            return null;
        }
    }

    doc "The last element of the sequence, or null if
         the sequence is empty."
    public optional X lastOrNull {
        if (sequence.empty) {
            return last;
        }
        else {
            return null;
        }
    }

}
doc "The ternary range operator x[from..to], along
     with the binary upper range x[from...] operator. 
     The returned sequence does not reflect changes 
     to the original sequence."
public S subrange<S,T>(S sequence, Natural from, Natural to=sequence.lastIndex) 
         where S(T... elements) satisfies Sequence<T> {

    class SubrangeIterable<T>
        implements Iterable<T> {  }

    return S( SubrangeIterable() )
}

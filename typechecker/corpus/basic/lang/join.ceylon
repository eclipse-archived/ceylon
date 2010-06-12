doc "The binary join operator x + y. 
     The returned sequence does not reflect changes
     to the original sequences."
public S join<S,T>(S... sequences) 
        where S(T... elements) satisfies Sequence<T> {

    class JoinedIterable<T>
        implements Iterable<T> {  }

    return S( JoinedIterable() )
}

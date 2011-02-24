doc "Construct a |Map| by evaluating the block for
     each given object and collecting the resulting
     |Entry|s."
shared Map<U,V> map<X,U,V>(iterated Iterable<X> elements,
                           Entry<U,V> containing(coordinated X element)) 
           given U satisfies Equality<U> 
           given V satisfies Equality<V> {
    OpenMap<U,V> map = HashMap<U,V>();
    for (X x in elements) {
        map.add( containing(x) );
    }
}
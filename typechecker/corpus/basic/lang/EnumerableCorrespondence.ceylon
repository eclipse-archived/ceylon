shared interface EnumerableCorrespondence<U,V>
        satisfies Callable<Correspondence<U,V>,Entry<U,V>...>
        given U satisfies Equality<U> 
        given V satisfies Equality<V> {
    shared actual default Correspondence<U,V> call(Entry<U,V>... entries) {
        object correspondence 
                satisfies Correspondence<U,V> {
            //TODO...
        }
        return correspondence
    }
}
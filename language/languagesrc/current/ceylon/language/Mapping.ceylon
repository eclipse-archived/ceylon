shared interface Mapping<in U, out V> 
        //is EnumerableMapping<U,V>
        satisfies Correspondence<U, V> & Iterable<Entry<U,V>> & Sized & Cloneable<Mapping<U,V>> 
        given U satisfies Equality {}
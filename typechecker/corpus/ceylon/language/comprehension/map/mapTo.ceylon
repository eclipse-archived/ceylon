doc "Construct a |Map| by evaluating the block for
     each given value. Each |Entry| is constructed
     from the value and the key result of the
     evaluation."
shared Map<U,V> mapTo<U,V>(iterated Iterable<V> values,
                           U from(coordinated V value)) {
    Entry<U,V> of(V value) { return from(value)->value; }
    return map(values, of);
}
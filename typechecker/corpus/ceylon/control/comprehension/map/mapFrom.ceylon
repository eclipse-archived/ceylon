doc "Construct a |Map| by evaluating the block for
     each given key. Each |Entry| is constructed
     from the key and the value result of the
     evaluation."
shared Map<U,V> mapFrom<U,V>(iterated Iterable<U> keys,
                             V to(coordinated U key)) {
    Entry<U,V> containing(U key) { return key->to(key); }
    return map(keys, containing);
}
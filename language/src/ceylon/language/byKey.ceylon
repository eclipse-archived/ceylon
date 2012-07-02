shared Comparison? byKey<Key>(Comparison? comparing(Key x, Key y))
            (Key->Object x, Key->Object y) 
        given Key satisfies Object {
    return comparing(x.key, y.key);
}
shared Entry<Key,Value>[] zip<Key,Value>(Key[] keys, Value[] values)
        given Key satisfies Equality
        given Value satisfies Equality {
    throw;
}
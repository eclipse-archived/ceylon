shared Entry<Key,Item>[] zip<Key,Item>(Key[] keys, Item[] items)
        given Key satisfies Equality
        given Item satisfies Equality {
    throw;
}
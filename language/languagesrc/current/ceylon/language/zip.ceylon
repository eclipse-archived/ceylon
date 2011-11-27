shared Entry<Key,Item>[] zip<Key,Item>(Key[] keys, Item[] items)
        given Key satisfies Equality
        given Item satisfies Equality {
    
    function entry(Key? key, Item? item) {
        if (exists key) {
            if (exists item) {
                return key->item;
            }
        }
        return null;
    }
    
    value builder = SequenceBuilder<Entry<Key,Item>>();
    for (i in 0..min({keys.size, items.size})-1) {
        if (exists e = entry(keys[i], items[i])) {
            builder.append(e);
        }
    }
    return builder.sequence;
    
}
shared Result forKey<Key,Result>(Result resulting(Key key))
            (Key->Object entry) 
        given Key satisfies Object {
    return resulting(entry.key);
}
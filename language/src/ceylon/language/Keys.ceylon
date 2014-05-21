class Keys<in Key, out Item>(Correspondence<Key,Item> correspondence)
        satisfies Category<Key>
        given Key satisfies Object {
    shared actual Boolean contains(Key key) {
        return correspondence.defines(key);
    }
}

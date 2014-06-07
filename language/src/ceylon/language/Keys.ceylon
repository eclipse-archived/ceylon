class Keys<in Key, out Item>(Correspondence<Key,Item> correspondence)
        satisfies Category<Key>
        given Key satisfies Object {
    contains(Key key) => correspondence.defines(key);
}

class Keys<in Key, out Item>(Correspondence<Key,Item> correspondence) 
        satisfies Category 
        given Key satisfies Object {
    shared actual Boolean contains(Object key) {
        if (is Key key) {
            return correspondence.defines(key);
        }
        else {
            return false;
        }
    }
}

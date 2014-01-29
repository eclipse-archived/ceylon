class Keys<in Key, out Item>(Correspondence<Key,Item> correspondence)
        // needs explicit type arg as workaround for https://github.com/ceylon/ceylon-spec/issues/918
        satisfies Category<Object> 
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

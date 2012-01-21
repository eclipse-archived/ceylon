shared interface Map<Key,out Item> 
        satisfies Collection<Key->Item> &
                  Correspondence<Key, Item> & 
                  Cloneable<Map<Key,Item>>
        given Key satisfies Equality 
        given Item satisfies Equality {}
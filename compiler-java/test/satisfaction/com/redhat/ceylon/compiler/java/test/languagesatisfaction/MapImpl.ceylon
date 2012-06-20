class MapImpl<out Key,out Item>() extends Object()
        satisfies Map<Key,Item>
        given Key satisfies Object
        given Item satisfies Object {
        
    shared actual MapImpl<Key,Item> clone {
        return bottom;
    }
    
    shared actual Iterator<Entry<Key,Item>> iterator = bottom;
    
    shared actual Integer size = 0;
    
    shared actual Item? item(Object key) {
        return bottom;
    }
}
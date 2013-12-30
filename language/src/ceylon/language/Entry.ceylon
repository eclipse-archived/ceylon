"A pair containing a _key_ and an associated value called 
 the _item_. Used primarily to represent the elements of a 
 [[Map]]. The type `Entry<Key,Item>` may be abbreviated 
 `Key->Item`. An instance of `Entry` may be constructed 
 using the `->` operator:
 
     String->Person entry = person.name->person;"
by ("Gavin")
shared final class Entry<out Key, out Item>(key, item)
        extends Object()
        given Key satisfies Object
        given Item satisfies Object {
    
    "The key used to access the entry."
    shared Key key;
    
    "The value associated with the key."
    shared Item item;
    
    "A pair (2 element tuple) with the key and
     item of this entry."
    shared [Key,Item] pair => [key,item];
    
    "Determines if this entry is equal to the given entry. 
     Two entries are equal if they have the same key and 
     the same value."
    shared actual Boolean equals(Object that) {
        if (is Entry<Object,Object> that) {
            return this.key==that.key &&
                    this.item==that.item;
        }
        else {
            return false;
        }
    }
    
    hash => (31 + key.hash) * 31 + item.hash;
    
    "Returns a description of the entry in the form 
     `key->item`."
    shared actual String string => "``key``->``item``";
    
}

"A pair containing a _key_ and an associated value called 
 the _item_. Used primarily to represent the elements of a 
 [[Map]]. The type `Entry<Key,Item>` may be abbreviated 
 `Key->Item`. An instance of `Entry` may be constructed 
 using the `->` operator:
 
     String->Person entry = person.name->person;"
by ("Gavin")
shared final class Entry<out Key, out Item>(key, item)
        extends Object()
        given Key satisfies Object {
    
    "The key used to access the entry."
    shared Key key;
    
    "The item associated with the key."
    shared Item item;
    
    "A pair (2 element tuple) with the key and item of this 
     entry. For any `entry`:
     
         entry.pair == [entry.key,entry.item]"
    shared [Key,Item] pair => [key,item];
    
    "Determines if this entry is equal to the given entry. 
     Two entries are equal if they have the same key and 
     the same item. 
     
     - The keys are considered the same if they are equal,
       in the sense of [[value equality|Object.equals]].
     - Two items are considered the same if they are both 
       null or if neither is null and they are equal."
    shared actual Boolean equals(Object that) {
        if (is Entry<Object,Anything> that) {
            if (this.key!=that.key) {
                return false;
            }
            if (exists thisItem=this.item,
                exists thatItem=that.item) {
                return thisItem==thatItem;
            }
            else {
                return !this.item exists &&
                        !that.item exists;
            }
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash {
        value keyHash = (31 + key.hash) * 31;
        if (exists item) {
            return keyHash + item.hash;
        }
        else {
            return keyHash;
        }
    }
    
    "A description of the entry in the form `key->item`. If 
     [[item]] is `null`, its string representation is the 
     string `\"<null>\"`."
    shared actual String string 
            => "``key``->``stringify(item)``";
    
}

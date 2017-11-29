/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A pair containing a _key_ and an associated value called 
 the _item_. Used primarily to represent the elements of a 
 [[Map]]. The type `Entry<Key,Item>` may be abbreviated 
 `Key->Item`. An instance of `Entry` may be constructed 
 using the `->` operator:
 
     String->Person entry = person.name->person;"
by ("Gavin")
tagged("Collections")
shared final serializable
class Entry<out Key,out Item>(key, item)
        extends Object()
        given Key satisfies Object {
    
    "The key used to access the entry."
    shared Key key;
    
    "The item associated with the key."
    shared Item item;
    
    "A pair (2 element tuple) with the key and item of this 
     entry. For any `entry`:
     
         entry.pair == [entry.key,entry.item]"
    shared [Key, Item] pair => [key, item];
    
    "An `Entry` with the key and item of this entry if this 
     entry's item is non-null, or `null` otherwise."
    since("1.2.0")
    shared <Key->Item&Object>? coalesced
            => if (exists item) then key->item else null;
    
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
            if (exists thisItem = this.item,
                exists thatItem = that.item) {
                return thisItem==thatItem;
            }
            else {
                return !this.item exists 
                    && !that.item exists;
            }
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash 
            => (31 + key.hash) * 31 + (item?.hash else 0);
    
    "A description of the entry in the form `key->item`. If 
     [[item]] is `null`, its string representation is the 
     string `\"<null>\"`."
    shared actual String string 
            => "``key``->``stringify(item)``";
}

"A function that returns the result of applying the given 
 [[function|resulting]] function to the [[item|Entry.item]] 
 of a given [[Entry]], discarding its `key`.
     
     Map<String,List<Item>> map = ... ;
     {Item?*} topItems = map.map(forItem(List<Item>.first));"
see (`function forKey`)
shared Result forItem<Item,Result>(Result resulting(Item item))
            (Object->Item entry) 
        given Item satisfies Object =>
                resulting(entry.item);
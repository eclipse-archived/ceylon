"A function that returns the result of applying the given 
 [[function|resulting]] function to the [[key|Entry.key]] of
 a given [[Entry]], discarding its `item`.
     
     Map<String,List<Item>> map = ... ;
     {String*} uppercaseKeys = map.map(forKey(String.uppercased));"
see (`function forItem`)
shared Result forKey<Key,Result>(Result resulting(Key key))
            (Key->Object entry) 
        given Key satisfies Object =>
                resulting(entry.key);
"A comparator for [[Entry]]s which compares their keys 
 according to the given [[comparing]] function.
 
     value sortedEntries = map.sort(byKey(byIncreasing(String.lowercased)));"
see (`function byItem`)
shared Comparison byKey<Key>(Comparison comparing(Key x, Key y))
            (Key->Object x, Key->Object y) 
        given Key satisfies Object =>
                comparing(x.key, y.key);
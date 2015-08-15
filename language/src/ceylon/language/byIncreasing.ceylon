"Produces a comparator function which orders elements in 
 increasing order according to the [[Comparable]] value 
 returned by the given [[comparable]] function.
 
      \"Hello World!\".sort(byIncreasing(Character.lowercased))
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (`function byDecreasing`,
     `function increasing`,
     `function Iterable.max`,
     `function Iterable.sort`)
shared Comparison byIncreasing<Element,Value>
            (Value comparable(Element e))
            (Element x, Element y)
        given Value satisfies Comparable<Value> 
                => comparable(x)<=>comparable(y);

"A comparator function which orders elements in increasing 
 [[natural order|Comparable]].
 
        \"Hello World!\".sort(increasing)
 
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
see (`function decreasing`,
     `function byIncreasing`,
     `function Iterable.max`,
     `function Iterable.sort`)
shared Comparison increasing<Element>(Element x, Element y)
        given Element satisfies Comparable<Element> 
        => x<=>y;

"A comparator function which orders [[entries|Entry]] by 
 increasing [[natural order|Comparable]] of their 
 [[keys|Entry.key]].
        
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
shared Comparison increasingKey<Key>
            (Key->Anything x, Key->Anything y)
        given Key satisfies Comparable<Key>
        => x.key <=> y.key;

"A comparator function which orders [[entries|Entry]] by 
 increasing [[natural order|Comparable]] of their 
 [[items|Entry.item]].
        
 This function is intended for use with [[Iterable.sort]]
 and [[Iterable.max]]."
shared Comparison increasingItem<Item>
        (Object->Item x, Object->Item y)
        given Item satisfies Comparable<Item>
        => x.item <=> y.item;


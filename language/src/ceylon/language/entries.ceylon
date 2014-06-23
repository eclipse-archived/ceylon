"Given a stream of items, produce a stream of entries. For 
 each non-null item produced by the given stream of items, 
 the resulting stream contains an [[Entry]] of form 
 `key->item` where `key` is the index at which the item 
 occurs in the stream of items."
see (`value Iterable.indexed`)
shared Iterable<<Integer->Element&Object>,Element&Null|Absent> entries<Element,Absent>
            (Iterable<Element,Absent> elements)
        given Absent satisfies Null
                    => elements.indexed;

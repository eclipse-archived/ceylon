"Given a stream of items, produce a stream of entries. 
 For each non-null item produced by the given stream 
 of items, the resulting stream contains an `Entry` 
 of form `key->item` where `key` is the index at which
 the item occurs in the stream of items."
see (`function Iterable.indexed`)
shared {<Integer->Element&Object>*} entries<Element>
            ({Element*} elements) 
                    => elements.indexed;

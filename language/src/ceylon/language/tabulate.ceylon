"Produces a [[Map]] mapping elements to items where each 
 [[entry|Entry]] maps a distinct element of the given 
 [[stream|keys]] to the item produced by the given 
 [[function|collecting]]. Elements are considered distinct 
 if they are not [[equal|Object.equals]].
 
 This is an eager operation, and the resulting map does
 not reflect changes to the given stream."
shared Map<Key,Item> tabulate<Key,Item>(
        "The stream producing the keys of the resulting map."
        {Key*} keys,
        "A function that produces an item for the given
         [[key]]."
        Item collecting(Key key))
        given Key satisfies Object
        => keys.summarize(identity, 
            (Item? item, key)
                    => if (exists item) 
                    then item else collecting(key));
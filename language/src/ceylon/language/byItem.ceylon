doc "A comparator for `Entry`s which compares their items 
     according to the given `comparing()` function."
see(byKey)
shared Comparison? byItem<Item>(Comparison? comparing(Item x, Item y))
            (Object->Item x, Object->Item y) 
        given Item satisfies Object => 
                comparing(x.item, y.item);
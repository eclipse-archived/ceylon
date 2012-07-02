shared Comparison? byItem<Item>(Comparison? comparing(Item x, Item y))
            (Object->Item x, Object->Item y) 
        given Item satisfies Object {
    return comparing(x.item, y.item);
}
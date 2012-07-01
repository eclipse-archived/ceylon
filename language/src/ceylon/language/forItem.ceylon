shared Result forItem<Item,Result>(Result resulting(Item item))
            (Object->Item entry) 
        given Item satisfies Object {
    return resulting(entry.item);
}
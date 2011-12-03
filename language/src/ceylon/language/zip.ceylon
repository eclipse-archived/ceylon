shared Entry<Key,Item>[] zip<Key,Item>(Key[] keys, Item[] items)
        given Key satisfies Equality
        given Item satisfies Equality {
    value builder = SequenceBuilder<Key->Item>();
    variable value ki := keys.iterator;
    variable value ii := items.iterator;
    while (exists eki=ki) {
        if (exists eii = ii) {
            builder.append(eki.head->eii.head);
            ki:=eki.tail;
            ii:=eii.tail;
        }
        else {
            break;
        }
    }
    return builder.sequence;
    
}
doc "Given two sequences, form a new sequence consisting of
     all entries where, for any given index in the resulting
     sequence, the key of the entry is the element occurring 
     at the same index in the first sequence, and the item 
     is the element ocurring at the same index in the second 
     sequence. The length of the resulting sequence is the 
     length of the shorter of the two given sequences. 
     
     Thus:
     
         zip(xs,ys)[i]==xs[i]->ys[i]
     
     for every `0<=i<min({xs.size,ys.zize})`."
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
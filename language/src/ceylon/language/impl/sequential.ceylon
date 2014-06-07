"Obtains a Sequential containing the same elements as a given iterable.
 The effect is broadly equivalent to
   
     iterable.empty then empty else ArraySequence(iterable)

 Note in particular that `iterable.sequence()` is not used, since 
 this is used for evaluating `[*iterable]`.
"
shared Element[] sequential<Element>({Element*} iterable) {
    if (is Element[] iterable) {
        return iterable;
    } 
    if (iterable.empty) {
        return [];
    }
    else {
        object notempty satisfies {Element+} {
            shared actual Iterator<Element> iterator() {
                value it = iterable.iterator();
                object iterator satisfies Iterator<Element> {
                    variable value first = true;
                    shared actual Element|Finished next() {
                        value next = it.next();
                        if (first) {
                            first = false;
                            assert (!next is Finished);
                        }
                        return next;
                    }
                }
                return iterator;
            }
        }
        return ArraySequence(notempty);
    }
}
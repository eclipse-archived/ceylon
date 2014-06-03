"Obtains a Sequential containing the same elements as a given iterable.
 The effect is broadly equivalent to
   
     iterable.empty then empty else ArraySequence(iterable)

 Note in particular that `iterable.sequence` is not used, since 
 this is used for evaluating `[*iterable]`.
"
shared Element[] sequential<Element>({Element*} iterable) {
    if (is Element[] iterable) {
        return iterable;
    }
    value it = iterable.iterator();
    if (!is Finished firstElement = it.next()) {
        object notempty satisfies {Element+} {
            shared actual Iterator<Element> iterator() {
                object iterator satisfies Iterator<Element> {
                    variable value first = true;
                    shared actual Element|Finished next() {
                        if (first) {
                            first = false;
                            return firstElement;
                        }
                        return it.next();
                    }
                }
                return iterator;
            }
        }
        return ArraySequence(notempty);
    } else {
        return [];
    }
}
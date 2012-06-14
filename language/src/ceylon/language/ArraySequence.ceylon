doc "A `Sequence` implemented internally using an array."
class ArraySequence<Element>(Array<Element> elements) 
        extends Object()
        satisfies Sequence<Element> {
    
    Array<Element>&Some<Element> elementArray;
    if (nonempty a = array(elements...)) {
        elementArray = a;
    }
    else {
        throw;
    }
    
    shared actual Element first {
        return elementArray.first;
    }

    shared actual Element? item(Integer index) {
        return elementArray[index];
    }

    shared actual Integer lastIndex {
        return elementArray.lastIndex 
                else 0; //actually cannot occur
    }

    shared actual Element[] rest {
        return { elementArray.rest... };
    }

    shared actual Element[] segment(Integer from, Integer length) {
        return { elementArray.segment(from, length)... };
    }

    shared actual Element[] span(Integer from, Integer? to) {
        return { elementArray[from..to?lastIndex]... };
    }
    
    shared actual Sequence<Element> clone {
        return this;
    }
    
}
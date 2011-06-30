shared class Singleton<Element>(Element element)
        extends Object()
        satisfies Sequence<Element> {
    shared actual Natural lastIndex {
        return 0;
    }
    shared actual Natural size {
        return 1;
    }
    shared actual Element first {
        return element;
    }
    shared actual Element last {
        return element;
    }
    shared actual Element[] rest {
        return {};
    }
    shared actual Element? item(Natural index) {
        if (index==0) {
            return element;
        }
        else {
            return null;
        }
    }
    shared actual Sequence<Element> clone {
        return this;
    }
    shared actual default object iterator 
            extends Object()
            satisfies Iterator<Element> {
        shared actual Element head { 
            return first;
        }
        shared actual Iterator<Element> tail {
            return emptyIterator;
        }
    }
}

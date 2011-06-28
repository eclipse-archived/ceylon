shared class Singleton<Element>(Element uniqueElement)
        extends Object()
        satisfies Sequence<Element> {
    shared actual Natural lastIndex {
        return 0;
    }
    shared actual Natural size {
        return 1;
    }
    shared actual Element first {
        return uniqueElement;
    }
    shared actual Element last {
        return uniqueElement;
    }
    shared actual Element[] rest {
        return {};
    }
    shared actual Element? element(Natural index) {
        if (index==0) {
            return uniqueElement;
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

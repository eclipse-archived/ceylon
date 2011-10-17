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
    shared actual Iterator<Element> iterator {
        object singletonIterator 
                extends Object()
                satisfies Iterator<Element> {
            shared actual Element head { 
                return first;
            }
            shared actual Iterator<Element> tail {
                return emptyIterator;
            }
        }
        return singletonIterator;
    }
    shared actual String string {
        if (is Object first) {
            return "{ " first.string " }";
        }
        else if (is Nothing first) {
            return "{ null }";
        }
        else {
            throw;
        }
    }
    
    shared actual Element[] segment(Natural from, Natural length) {
        if (from>0 || length==0) {
            return {};
        }
        else {
            return this;
        }
    }
    
    shared actual Element[] span(Natural from, Natural to) {
        //this implementation assumes we decide on 
        //supporting reverse spans
        if (from>0 && to>0) {
            return {};
        }
        else {
            return this;
        }
    }
}

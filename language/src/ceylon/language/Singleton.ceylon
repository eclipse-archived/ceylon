doc "A sequence with exactly one element."
shared class Singleton<Element>(Element element)
        extends Object()
        satisfies Sequence<Element> {
    shared actual Integer lastIndex {
        return 0;
    }
    shared actual Integer size {
        return 1;
    }
    shared actual Element first {
        return element;
    }
    shared actual Element last {
        return element;
    }
    shared actual Empty rest {
        return {};
    }
    shared actual Element? item(Integer index) {
        if (index==0) {
            return element;
        }
        else {
            return null;
        }
    }
    shared actual Singleton<Element> clone {
        return this;
    }
    shared actual object iterator extends Object()
            satisfies Iterator<Element> {
        shared actual Element head { 
            return first;
        }
        shared actual Iterator<Element>? tail {
            return null;
        }
        shared actual String string {
            return "Iterator for " outer.string "";
        }
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
    
    shared actual Element[] segment(Integer from, Integer length) {
        return from>0 || length==0 then {} else this;
    }
    
    shared actual Element[] span(Integer from, Integer? to) {
        return from>0 then {} else this;
    }
}

@nomodel
class Bug837<Element>(first, Integer length) 
        extends Object()
        satisfies Sequence<Element> 
        given Element satisfies Ordinal<Element> {

    shared actual Element first;

    shared actual Boolean empty {
        throw;
    }

    shared actual Element? item(Integer index) {
        throw;
    }

    shared actual Integer lastIndex {
        return length-1;
    }

    shared actual Element[] rest { 
        return Bug837(first.successor, length-1); 
    }

    shared actual Sequence<Element> reversed {
        //TODO!
        throw;
    }

    shared actual Element[] segment(Integer from, Integer length) {
        throw;
    }

    shared actual Element[] span(Integer from, Integer to) {
        throw;
    }
    shared actual Element[] spanFrom(Integer from) {
        throw;
    }
    shared actual Element[] spanTo(Integer to) {
        throw;
    }

    shared actual Sequence<Element> clone { 
        return this; 
    }

}
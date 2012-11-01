shared class Tuple<out Element, out First, out Rest>(first, rest)
        extends Object()
        satisfies Sequence<Element>
        given Rest of Empty|Sequence<Element> {
        
    shared actual First&Element first;
    shared actual Rest&Element[] rest;
    
    shared actual Element? item(Integer index) {
        switch (index<=>0)
        case (smaller) { return null; }
        case (equal) { return first; }
        case (larger) { return rest.item(index-1); }
    }
    
    shared actual Integer lastIndex {
        if (exists restLastIndex = rest.lastIndex) {
            return restLastIndex+1;
        }
        else {
            return 0;
        }
    }
    
    shared actual Sequence<Element> reversed {
        return rest.reversed.withTrailing(first);
    }
    
    shared actual Element[] segment(Integer from, Integer length) {
        return from<=0 then rest[0:length+from-1].withLeading(first) 
                else rest[from-1:length];
    }
    
    shared actual Element[] span(Integer from, Integer? to) {
        value end = to else size;
        return from<=end then this[from:end-from+1] 
                else this[end:from-end+1].reversed.sequence;
    }
    
    shared actual Sequence<Element> clone { 
        return this; 
    }
}
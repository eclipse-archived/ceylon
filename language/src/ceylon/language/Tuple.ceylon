doc "Don't forget to document me"
by "gavin"
shared class Tuple<out Element, out First, out Rest>(first, rest)
        extends Object()
        satisfies Sequence<Element>
        given First satisfies Element
        given Rest satisfies Sequential<Element> {
        
    shared actual First first;
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
    
    shared actual transient Sequence<Element> reversed =
            rest.reversed.withTrailing(first);
    
    shared actual Element[] segment(Integer from, Integer length) {
        if (from<=0) {
            return length==1 then {first}
                else rest[0:length+from-1].withLeading(first);
        }
        return rest[from-1:length];
    }
    
    shared actual Element[] span(Integer from, Integer? to) {
        value end = to else size;
        return from<=end then this[from:end-from+1] 
                else this[end:from-end+1].reversed.sequence;
    }
    
    shared actual transient Sequence<Element> clone = this;
    
}
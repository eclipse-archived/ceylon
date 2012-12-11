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
    
    shared actual Sequence<Element> reversed =>
            rest.reversed.withTrailing(first);
    
    shared actual Element[] segment(Integer from, Integer length) {
        if(length < 0)
            return {};
        Integer realFrom = max(0, from);
        if (realFrom==0) {
            return length==1 then {first}
                else rest[0:length+realFrom-1].withLeading(first);
        }
        return rest[realFrom-1:length];
    }
    
    shared actual Element[] span(Integer from, Integer end) {
        Integer realFrom = max(0, from);
        return realFrom<=end then this[from:end-realFrom+1] 
                else this[end:realFrom-end+1].reversed.sequence;
    }
    shared actual Element[] spanTo(Integer to) =>
        to<0 then {} else span(0, to);
    shared actual Element[] spanFrom(Integer from) =>
        span(from, size);
    
    shared actual Sequence<Element> clone => this;
    
}

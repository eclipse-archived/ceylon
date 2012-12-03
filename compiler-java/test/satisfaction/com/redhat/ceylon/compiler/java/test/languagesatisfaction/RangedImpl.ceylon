class RangedImpl<in Index, out Span>() satisfies Ranged<Index, Span> 
        given Index satisfies Comparable<Index> {

    shared actual Span span(Index from, Index to) {
        return bottom;
    }
    
    shared actual Span spanFrom(Index from) {
        return bottom;
    }
    
    shared actual Span spanTo(Index to) {
        return bottom;
    }
    
    shared actual Span segment(Index from, Integer length) {
        return bottom;
    }

}
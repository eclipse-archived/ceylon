shared class Range<Element>(Element start, Element end) 
        extends Object() 
        satisfies Sequence<Element> & Equality & Category
        given Element satisfies Ordinal<Element> & Comparable<Element> { 
    
    doc "The start of the range."
    shared Element start = start;
    
    doc "The end of the range."
    shared Element end = end;
    
    shared Boolean decreasing { return end<start; }
    
    doc "Return a |Sequence| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size,
         until a value outside the range is
         reached."
    shared Element[] by(Natural stepSize) { 
        throw;
    }
    
    shared Boolean includes(Element x) {
        if (decreasing) {
            return x<=start && x>=end;
        }
        else {
            return x>=start && x<=end;
        }
    }
    
    Boolean pastEnd(Element x) {
        if (decreasing) {
            return x<end;
        }
        else {
            return x>end;
        }
    }
    
    Element next(Element x) {
        if (decreasing) {
            return x.successor;
        }
        else {
            return x.predecessor;
        }
    }

    /*shared Natural? index(Element x) {
        if (!includes(x)) {
            return null;
        }
        else {
            //optimize this for numbers!
            variable Natural index:=0;
            variable Element value:=start;
            while (value<x) {
                ++index;
                ++value;
            }
            return index;
        }
    }*/
    
    shared actual Iterator<Element> iterator {
        class RangeIterator(Element x) 
                satisfies Iterator<Element> {
            shared actual Element? head { 
                if (pastEnd(x)) { 
                    return null;
                } 
                else { 
                    return x;
                }
            }
            shared actual Iterator<Element> tail {
                return RangeIterator(next(x));
            }
        }
        return RangeIterator(start);
    }
    
    shared actual Boolean contains(Object obj) {
        if (is Element obj) {
            return includes(obj);
        }
        else {
            return false;
        }
    }
    
    variable Natural index:=0;
    variable Element x:=start;
    while (!pastEnd(x)) {
        ++index;
        x:=next(x);
    }
    
    shared actual Natural size = index;
    shared actual Natural lastIndex { return size-1; }
    
    shared actual Element? value(Natural n) {
        //optimize this for numbers!
        variable Natural index:=0;
        variable Element x:=start;
        while (index<n && !pastEnd(x)) {
            ++index;
            x:=next(x);
        }
        if (pastEnd(x)) {
            return null;
        }
        else {
            return x;
        }
    }
    
    shared actual Boolean equals(Equality that) {
        if (is Range<Element> that) {
            return that.start==start && that.end==end;
        }
        else {
            return false;
        }
    }
    
    //TODO
    
}
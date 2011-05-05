shared class Range<X>(X start, X end) 
        extends Object() 
        satisfies Sequence<X> & Equality & Category
        given X satisfies Ordinal<X> & Comparable<X> { 
    
    doc "The start of the range."
    shared X start = start;
    
    doc "The end of the range."
    shared X end = end;
    
    shared Boolean decreasing { return end<start; }
    
    doc "Return a |Sequence| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size,
         until a value outside the range is
         reached."
    shared X[] by(Natural stepSize) { 
        throw;
    }
    
    shared Boolean includes(X x) {
        if (decreasing) {
            return x<=start && x>=end;
        }
        else {
            return x>=start && x<=end;
        }
    }
    
    Boolean pastEnd(X x) {
        if (decreasing) {
            return x<end;
        }
        else {
            return x>end;
        }
    }
    
    X next (X x) {
        if (decreasing) {
            return x.successor;
        }
        else {
            return x.predecessor;
        }
    }

    /*shared Natural? index(X x) {
        if (!includes(x)) {
            return null;
        }
        else {
            //optimize this for numbers!
            variable Natural index:=0;
            variable X value:=start;
            while (value<x) {
                ++index;
                ++value;
            }
            return index;
        }
    }*/
    
    shared actual Iterator<X> iterator() {
        class RangeIterator(X x) 
                satisfies Iterator<X> {
            shared actual X? head { 
                if (pastEnd(x)) { 
                    return null;
                } 
                else { 
                    return x;
                }
            }
            shared actual Iterator<X> tail {
                return RangeIterator(next(x));
            }
        }
        return RangeIterator(start);
    }
    
    shared actual Boolean contains(Object obj) {
        if (is X obj) {
            return includes(obj);
        }
        else {
            return false;
        }
    }
    
    variable Natural index:=0;
    variable X x:=start;
    while (!pastEnd(x)) {
        ++index;
        x:=next(x);
    }
    
    shared actual Natural size = index;
    shared actual Natural lastIndex { return size-1; }
    
    shared actual X? value(Natural n) {
        //optimize this for numbers!
        variable Natural index:=0;
        variable X x:=start;
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
        if (is Range<X> that) {
            return that.start==start && that.end==end;
        }
        else {
            return false;
        }
    }
    
    //TODO
    
}
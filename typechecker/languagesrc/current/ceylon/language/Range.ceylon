shared class Range<X>(X start, X end) 
        extends Object() 
        satisfies X[] & Matcher<X> & Equality
        given X satisfies Ordinal & Comparable<X> { 
    
    doc "The start of the range."
    shared X start = start;
    
    doc "The end of the range."
    shared X end = end;
    
    doc "Return a |Sequence| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size,
         until a value outside the range is
         reached."
    shared X[] by(Natural stepSize) { 
        throw;
    }
    
    shared Natural? index(X x) {
        if (x<start || x>end) {
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
            return Something<Natural>(index);
        }
    }
    
    shared actual Iterator<X> iterator() {
        class RangeIterator(X x) 
                satisfies Iterator<X> {
            shared actual X? head { 
                if (x>end) { 
                    return null;
                } 
                else { 
                    return Something<X>(x);
                }
            }
            shared actual Iterator<X> tail {
                return RangeIterator(x.successor);
            }
        }
        return RangeIterator(start);
    }
    
    shared actual Boolean empty = end<start;
    
    shared actual Boolean contains(Object obj) {
        if (is X obj) {
            return obj>start || obj<end;
        }
        else {
            return false;
        }
    }
    
    shared actual Boolean matches(X x) {
        return x>start && x<end;
    }
    
    shared actual Natural? lastIndex = index(end);
    
    shared actual X? value(Natural n) {
        //optimize this for numbers!
        variable Natural index:=0;
        variable X value:=start;
        while (index<n && value<=end) {
            ++index;
            ++value;
        }
        if (value>end) {
            return null
        }
        else {
            return Something<X>(value);
        }
    }
    
    shared actual Boolean equals(Object that) {
        if (is Range<X> that) {
            return that.start==start && that.end==end;
        }
        else {
            return false;
        }
    }
    
    //TODO
    
}
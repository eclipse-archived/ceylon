shared class Range<X>(X first, X last) 
        extends Object() 
        satisfies X[] & Matcher<X> & Equality<Range<X>>
        given X satisfies Ordinal & Comparable<X> & Equality<X> { 
    
    doc "The first value in the range."
    shared X first = first;
    
    doc "The last value in the range."
    shared X last = last;
    
    doc "Return a |Sequence| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size,
         until a value outside the range is
         reached."
    shared X[] by(Natural stepSize) { 
        /*return from (Natural index->X value in this) 
                    where (index%stepSize == 0)
                    select (value)*/
    	return from (Entry<Natural,X> e in this) 
                    where (e.key%stepSize == 0)
                    select (e.value);
    }
    
    shared Natural? index(X x) {
        if (x<first || x>last) {
            return null;
        }
        else {
            //optimize this for numbers!
            variable Natural index:=0;
            variable X value:=first;
            while (value<x) {
                ++index;
                ++value;
            }
            return index;
        }
    }
    
    shared actual Iterator<X> iterator() {
        class RangeIterator(X x) 
                satisfies Iterator<X> {
            shared actual X? head { 
                if (x>last) { 
                    return null;
                } 
                else { 
                    return x;
                }
            }
            shared actual Iterator<X> tail {
                return RangeIterator(x.successor);
            }
        }
        return RangeIterator(first);
    }
    
    shared actual Boolean empty = last<first;
    
    shared actual Boolean contains(Object... objects) {
        for (Object x in objects) {
            if (is X x) {
                if ( x<first || x>last ) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        fail {
            return true;
        } 
    }
    
    shared actual Boolean matches(X x) {
        return x>first && x<last;
    }
    
    shared actual Natural? lastIndex = index(last);
    
    shared actual Gettable<X?> value(Natural n) {
        //optimize this for numbers!
        variable Natural index:=0;
        variable X? value:=first;
        while (index<n) {
            ++index;
            ++value;
            if (value>last) {
                value := null;
                break
            }
        }
        return value;
    }
    
    shared actual Boolean equals(Range<X> that) {
        return that.first==first && that.last==last;
    }
    
    //TODO
    
}
shared class Range<Element>(Element first, Element last) 
        extends Object() 
        satisfies Sequence<Element> & Equality & Category
        given Element satisfies Ordinal<Element> & Comparable<Element> { 
    
    doc "The start of the range."
    shared actual Element first = first;
    
    doc "The end of the range."
    shared actual Element last = last;

    shared actual String string {
        return "[" first.string ".." last.string "]";
    }
    
    shared Boolean decreasing { 
        return last<first; 
    }
    
    Boolean pastEnd(Element x) {
        if (decreasing) {
            return x<last;
        }
        else {
            return x>last;
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

    variable Natural index:=0;
    variable Element x:=first;
    while (!pastEnd(x)) {
        ++index;
        x:=next(x);
    }
    
    shared actual Natural size = index;
    
    shared actual Natural lastIndex { 
        return size-1; 
    }
    
    shared actual Element[] rest {
        return Range<Element>(next(first),last);
    }
    
    shared actual Element? item(Natural n) {
        //optimize this for numbers!
        variable Natural index:=0;
        variable Element x:=first;
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
        return RangeIterator(first);
    }
    
    shared actual Boolean contains(Object element) {
        if (is Element element) {
            return includes(element);
        }
        else {
            return false;
        }
    }
    
    shared Boolean includes(Element x) {
        if (decreasing) {
            return x<=first && x>=last;
        }
        else {
            return x>=first && x<=last;
        }
    }

    doc "Return a |Sequence| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size, until 
         a value outside the range is reached."
    shared Element[] by(Natural stepSize) { 
        throw;
    }

    /*shared Natural? index(Element x) {
    if (!includes(x)) {
        return null;
    }
    else {
        //optimize this for numbers!
        variable Natural index:=0;
        variable Element value:=first;
        while (value<x) {
            ++index;
            ++value;
        }
        return index;
    }
    }*/

    shared actual Boolean equals(Equality that) {
        if (is Range<Element> that) {
            return that.first==first && that.last==last;
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash {
        return first.hash/2 + last.hash/2; //TODO: really should be xor
    }
    
    shared actual Sequence<Element> clone {
        return this;
    }
    
}
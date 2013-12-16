"A sequence with exactly one element, which may be null."
shared class Singleton<out Element>(Element element)
        extends Object()
        satisfies [Element+] {
    
    "Returns `0`."
    shared actual Integer lastIndex => 0;
    
    "Returns `1`."
    shared actual Integer size => 1;
    
    "Returns the element contained in this `Singleton`."
    shared actual Element first => element;
    
    "Returns the element contained in this `Singleton`."
    shared actual Element last => element;
    
    "Returns `Empty`."
    shared actual Empty rest => {};
    
    "Returns the contained element, if the specified 
     index is `0`."
    shared actual Element? get(Integer index) {
        if (index==0) {
            return element;
        }
        else {
            return null;
        }
    }
    
    "Returns a `Singleton` with the same element."
    shared actual Singleton<Element> clone => this;
    
    shared actual default Iterator<Element> iterator() {
        class SingletonIterator()
                satisfies Iterator<Element> {
            variable Boolean done = false;
            shared actual Element|Finished next() {
                if (done) {
                    return finished;
                }
                else {
                    done=true;
                    return element;
                }
            }
            shared actual String string {
                return "SingletonIterator";
            }
        }
        return SingletonIterator();
    }
    
    shared actual String string => "[`` element?.string else "null" ``]";
    
    "Returns a `Singleton` if the given starting index 
     is `0` and the given `length` is greater than `0`.
     Otherwise, returns an instance of `Empty`."
    shared actual Empty|Singleton<Element> segment
            (Integer from, Integer length) =>
                    from<=0 && from+length>0 then this else [];
    
    "Returns a `Singleton` if the given starting index 
     is `0`. Otherwise, returns an instance of `Empty`."
    shared actual Empty|Singleton<Element> span
            (Integer from, Integer to) =>
                    (((from <= 0) && (to >= 0)) || 
                    ((from >= 0) && (to <= 0))) 
                        then this else [];
    
    shared actual Empty|Singleton<Element> spanTo
            (Integer to) => to<0 then [] else this;

    shared actual Empty|Singleton<Element> spanFrom
            (Integer from) => from>0 then [] else this;

    "Return this singleton."
    shared actual Singleton<Element> reversed => this;
    
    "A `Singleton` can be equal to another `List` if 
     that `List` has only one element which is equal to 
     this `Singleton`\'s element."
    shared actual Boolean equals(Object that) {
        if (exists element) {
            if (is List<Anything> that) {
                if (that.size==1) {
                    if (exists elem = that.first) {
                        return elem==element;
                    }
                }
            }
            return false;
        } 
        return false;
    }
    
    shared actual Integer hash => 31 + (element?.hash else 0);
    
    "Returns `true` if the specified element is this 
     `Singleton`\'s element."
    shared actual Boolean contains(Object element) {
        if (exists e=this.element) {
            return e==element;
        }
        return false;
    }
    
    "Returns `1` if this `Singleton`\'s element
     satisfies the predicate, or `0` otherwise."
    shared actual Integer count
            (Boolean selecting(Element element)) =>
                    selecting(element) then 1 else 0;
    
    shared actual Singleton<Result> map<Result>
            (Result selecting(Element e)) =>
                    Singleton(selecting(element));
    
    shared actual Singleton<Element>|Empty filter
            (Boolean selecting(Element e)) => 
                    selecting(element) then this else {};
    
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Element e)) =>
                    accumulating(initial, element);
    
    shared actual Element? find
            (Boolean selecting(Element e)) {
        if (selecting(element)) {
            return element;
        }
        return null;
    }
    
    shared actual default Element? findLast
            (Boolean selecting(Element elem)) => 
                    find(selecting);
    
    shared actual Singleton<Element> sort
            (Comparison comparing(Element a, Element b)) => this;
    
    shared actual Boolean any
            (Boolean selecting(Element e)) =>
                    selecting(element);
    
    shared actual Boolean every
            (Boolean selecting(Element e)) =>
                    selecting(element);
    
    shared actual Singleton<Element>|Empty skipping(Integer skip) => 
            skip<1 then this else {};
    
    shared actual Singleton<Element>|Empty taking(Integer take) =>
            take>0 then this else {};
    
    shared actual Singleton<Element&Object>|Empty coalesced {
        if (exists element) {
            return Singleton(element);
        }
        else {
            return [];
        }
    }
    
}

"A [[Sequence]] backed by an [[Array]]. 
 
 Since [[Array]]s are mutable, this class is private to the
 language module, where we can be sure the `Array` is not
 modified after the `ArraySequence` has been initialized."
by("Tom")
shared sealed final
serializable
tagged("Collections", "Sequences")
class ArraySequence<out Element>(Array<Element> array)
        extends Object()
        satisfies [Element+] {
    
    assert(!array.empty);
    
    getFromFirst = array.getFromFirst;
    
    contains = array.contains;
    
    size => array.size;
    
    iterator = array.iterator;
    
    shared actual Element first {
        assert(exists first = array.first);
        return first;
    }
    
    shared actual Element last {
        assert(exists last = array.last);
        return last;
    }
    
    clone() => this;
    
    tuple() => arrayToTuple(array);
    
    each = array.each;
    
    count = array.count;
    
    every = array.every;
    
    any = array.any;
    
    find = array.find;
    
    findLast = array.findLast;
    
    shared actual 
    Result|Element reduce<Result>(accumulating) {
        Result accumulating(Result|Element partial, Element element);
        assert(is Element result = array.reduce(accumulating));
        return result;
    }

    shared actual [Result+] collect<Result>(collecting) {
        Result collecting(Element element);
        assert(nonempty sequence = array.collect(collecting));
        return sequence;
    }
    
    shared actual [Element+] sort(comparing) {
        Comparison comparing(Element x, Element y);
        assert(nonempty sequence = array.sort(comparing));
        return sequence;
    }
    
    shared actual
    ArraySequence<Element>|[] measure(Integer from, Integer length) {
        if(from > lastIndex || length <= 0 || from + length <= 0) {
            return [];
        }
        else {
            return ArraySequence(array[from:length]);
        }
    }
    
    shared actual
    ArraySequence<Element>|[] span(Integer from, Integer to) {
        if(from <= to) {
            if(to < 0 || from > lastIndex) {
                return [];
            }
            else {
                return ArraySequence(array[from..to]);
            }
        }
        else {
            if(from < 0 || to > lastIndex) {
                return [];
            }
            else {
                return ArraySequence(array[from..to]);
            }
        }
    }
    
    shared actual ArraySequence<Element>|[] spanFrom(Integer from) {
        if(from <= 0) {
            return this;
        }
        else if(from < size) {
            return ArraySequence(array[from...]);
        }
        else {
            return [];
        }
    }
    
    shared actual ArraySequence<Element>|[] spanTo(Integer to) {
        if(to >= lastIndex) {
            return this;
        }
        else if(to >= 0) {
            return ArraySequence(array[...to]);
        }
        else {
            return [];
        }
    }
}

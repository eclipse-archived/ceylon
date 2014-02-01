"A collection of unique elements.
 
 A `Set` is a [[Collection]] of its elements.
 
 Sets may be the subject of the binary union, intersection, 
 and complement operators `|`, `&`, and `~`.
 
     value kids = girls|boys; 
 
 Elements are compared for equality using [[Object.equals]] 
 or [[Comparable.compare]]. An element may occur at most 
 once in a set."
shared interface Set<out Element>
        satisfies Collection<Element>
        given Element satisfies Object {
    
    "The fundamental operation for `Set`s. Determines if the
     given value belongs to this set."
    shared actual default Boolean contains(Object element)
            => super.contains(element);
    
    "Determines if this set is a superset of the given 
     `Set`, that is, if this set contains all of the 
     elements in the given set."
    shared default Boolean superset(Set<Object> set) {
        for (element in set) {
            if (!contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    "Determines if this set is a subset of the given `Set`, 
     that is, if the given set contains all of the elements 
     in this set."
    shared default Boolean subset(Set<Object> set) {
        for (element in this) {
            if (!element in set) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    "Two `Set`s are considered equal if they have the same 
     size and if every element of the first set is also an 
     element of the second set, as determined by 
     [[contains]]. Equivalently, a set is equal to a second 
     set if it is both a subset and a superset of the second
     set."
    shared actual default Boolean equals(Object that) {
        if (is Set<Object> that,
                that.size==size) {
            for (element in this) {
                if (!element in that) {
                    return false;
                }
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    shared actual default Integer hash {
        variable Integer hashCode = 0;
        for (elem in this){
            hashCode += elem.hash;
        }
        return hashCode;
    }
    
    "Returns a new `Set` containing all the elements of this 
     set and all the elements of the given `Set`."
    shared formal Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object;
    
    "Returns a new `Set` containing only the elements that 
     are present in both this set and the given `Set`."
    shared formal Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object;
    
    "Returns a new `Set` containing only the elements 
     contained in either this set or the given `Set`, but no 
     element contained in both sets."
    shared formal Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object;
    
    "Returns a new `Set` containing all the elements in this 
     set that are not contained in the given `Set`."
    shared formal Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object;
    
    shared actual formal Set<Element> clone();
    
}

"An immutable [[Set]] with no elements."
shared object emptySet 
        extends Object() 
        satisfies Set<Nothing> {
    
    shared actual Set<Other> union<Other>(Set<Other> set)
            given Other satisfies Object
            => set;
    shared actual Set<Nothing> intersection<Other>(Set<Other> set)
            given Other satisfies Object
            => emptySet;
    shared actual Set<Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object
            => set;
    shared actual Set<Nothing> complement<Other>(Set<Other> set)
            given Other satisfies Object
            => emptySet;
    
    subset(Set<Object> set) => true;
    superset(Set<Object> set) => set.empty;
    
    clone() => emptySet;
    iterator() => emptyIterator;
    size => 0;
    empty => true;
    
    contains(Object element) => false;
    containsAny({Object*} elements) => false;
    containsEvery({Object*} elements) => false;
    
    shared actual Integer count
            (Boolean selecting(Nothing element)) 
            => 0;
    
    shared actual Set<Nothing> map<Result>
            (Result collecting(Nothing element)) 
            => emptySet;
    
    shared actual Set<Nothing> filter
            (Boolean selecting(Nothing element)) 
            => emptySet;
    
    shared actual Result fold<Result>(Result initial,
            Result accumulating(Result partial, Nothing element)) 
            => initial;
    
    shared actual Null find
            (Boolean selecting(Nothing element)) 
            => null;
    
    shared actual [] collect<Result>
            (Result collecting(Nothing element)) 
            => [];
    
    shared actual [] select
            (Boolean selecting(Nothing element)) 
            => [];
    
    shared actual Boolean any
            (Boolean selecting(Nothing element)) 
            => false;
    
    shared actual Boolean every
            (Boolean selecting(Nothing element)) 
            => false;
    
    skipping(Integer skip) => emptySet;
    taking(Integer take) => emptySet;
    by(Integer step) => emptySet;
    
}
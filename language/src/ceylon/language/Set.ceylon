doc "A collection of unique elements.

     A `Set` is a `Collection` of its elements.
     
     Sets may be the subject of the binary union, 
     intersection, exclusive union, and complement operators 
     `|`, `&`, `^`, and `~`."
shared interface Set<out Element>
        satisfies Collection<Element> &
                  Cloneable<Set<Element>>
        given Element satisfies Object {
    
    doc "Determines if this `Set` is a superset of the 
         specified Set, that is, if this `Set` contains all 
         of the elements in the specified `Set`."
    shared default Boolean superset(Set<Object> set) {
        for (element in set) {
            if (!element in this) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    doc "Determines if this `Set` is a subset of the given 
         `Set`, that is, if the given set contains all of 
         the elements in this set."
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
    
    doc "Two `Set`s are considered equal if they have the 
         same size and if every element of the first set is
         also an element of the second set, as determined
         by `contains()`."
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
        variable Integer hashCode = 1;
        for (Element elem in this){
            hashCode *= 31;
            hashCode += elem.hash;
        }
        return hashCode;
    }
    
    doc "Returns a new `Set` containing all the elements of 
         this `Set` and all the elements of the given `Set`."
    shared formal Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object;
    
    doc "Returns a new `Set` containing only the elements 
         that are present in both this `Set` and the given 
         `Set`."
    shared formal Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object;
    
    doc "Returns a new `Set` containing only the elements 
         contained in either this `Set` or the given `Set`, 
         but no element contained in both sets."
    shared formal Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object;
    
    doc "Returns a new `Set` containing all the elements in 
         this `Set` that are not contained in the given
         `Set`."
    shared formal Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object;
    
}

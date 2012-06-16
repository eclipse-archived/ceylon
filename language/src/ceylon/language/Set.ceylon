doc "A collection of unique elements.

     A |Set| is a |Collection| of its elements."
shared interface Set<out Element>
        satisfies Collection<Element> &
                  Cloneable<Set<Element>>
        given Element satisfies Object {

    doc "Returns `1` if the element is part of this `Set`, 
         or `0` otherwise."
    shared actual default Integer count(Object element) {
        return contains(element) then 1 else 0;
    }

    doc "Determines if this `Set` is a superset of the 
         specified Set, that is, if this `Set` contains all 
         of the elements in the specified `Set`."
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

    doc "Determines if this `Set` is a subset of the given 
         `Set`, that is, if the given set contains all of 
         the elements in this set."
    shared default Boolean subset(Set<Object> set) {
        for (element in this) {
            if (!set.contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }

    doc "Two `Set`s are considered equal if they have the 
         same size and share all the same elements."
    shared actual default Boolean equals(Object that) {
        if (is Set<Object> that) {
            if (that.size==size) {
                for (element in this) {
                    if (!element in that) {
                        return false;
                    }
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

    shared actual default Integer hash {
        variable Integer hashCode := 1;
        for(Element elem in this){
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
         but not both."
    shared formal Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object;

    doc "Returns a new `Set` containing all the elements in 
         the given `Set` that are not contained in this 
         `Set`."
    shared formal Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object;

}

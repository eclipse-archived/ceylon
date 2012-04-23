doc "A collection of unique elements.

     A |Set| is a |Collection| of its elements."
shared interface Set<out Element>
        satisfies Collection<Element> &
                  Cloneable<Set<Element>>
        given Element satisfies Object {

    doc "Returns 1 if the element is part of this Set, 0 otherwise."
    shared actual default Integer count(Object element) {
        return contains(element) then 1 else 0;
    }

    doc "Determines if this Set is a superset of the specified Set,
         that is, if this Set contains all of the elements in the
         specified Set."
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

    doc "Determines if this Set is a subset of the specified Set,
         that is, if the specified Set contains all of the
         elements in this Set."
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

    doc "Two Sets are considered equal if they have the same size
         and all its elements are equal."
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

    doc "Returns a new Set containing all the elements of this Set
         and all the elements of the specified Set."
    shared formal Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object;

    doc "Returns a new Set containing only the elements contained
         both in this Set and the specified Set."
    shared formal Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object;

    doc "Returns a new Set containing only the elements contained in
         this Set or the specified Set, but not both."
    shared formal Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object;

    doc "Returns a new set containing all the elements in the specified Set
         that are not contained in this Set."
    shared formal Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object;

}

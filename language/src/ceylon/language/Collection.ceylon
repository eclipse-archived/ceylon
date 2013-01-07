doc "Represents an iterable collection of elements of finite 
     size. `Collection` is the abstract supertype of `List`,
     `Map`, and `Set`.
     
     A `Collection` forms a `Category` of its elements.
     
     All `Collection`s are `Cloneable`. If a collection is
     immutable, it is acceptable that `clone` produce a
     reference to the collection itself. If a collection is
     mutable, `clone` should produce an immutable collection
     containing references to the same elements, with the
     same structure as the original collection&mdash;that 
     is, it should produce an immutable shallow copy of the
     collection."
see (List, Map, Set)
shared interface Collection<out Element>
        satisfies {Element...} &
                  Category &
                  Cloneable<Collection<Element>> {
    
    doc "Determine if the collection is empty, that is, if 
         it has no elements."
    shared actual default Boolean empty => size==0;
    
    doc "Return `true` if the given object is an element of
         this collection. In this default implementation,
         and in most refining implementations, return `false`
         otherwise. An acceptable refining implementation
         may return `true` for objects which are not 
         elements of the collection, but this is not 
         recommended. (For example, the `contains()` method 
         of `String` returns `true` for any substring of the
         string.)"
    shared actual default Boolean contains(Object element) {
        for (elem in this) {
            if (exists elem, elem==element) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    value elementsString =>
            ", ".join ( for (elem in this) 
                            elem?.string else "null" );
    
    doc "A string of form `\"{ x, y, z }\"` where `x`, `y`, 
         and `z` are the `string` representations of the 
         elements of this collection, as produced by the
         iterator of the collection, or the string `\"{}\"` 
         if this collection is empty. If the collection 
         iterator produces the value `null`, the string
         representation contains the string `\"null\"`."
    shared default actual String string =>
            empty then "{}" else "{ " elementsString " }";
    
}

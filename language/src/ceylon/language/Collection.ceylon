doc "Represents an iterable collection of elements of finite 
     size.
     
     A `Collection` forms a `Category` of its elements."
shared interface Collection<out Element>
        satisfies Iterable<Element> &
                  Sized & Category &
                  Cloneable<Collection<Element>> {

    doc "Determine if the collection is empty, that is, if 
         it has no elements."
    shared actual default Boolean empty {
        return size==0;
    }

    doc "Returns true if the specified element is part of 
         this collection."
    shared actual default Boolean contains(Object element) {
        for (elem in this) {
            if (is Object elem) {
                if (elem==element) {
                    return true;
                }
            }
        }
        else {
            return false;
        }
    }

}
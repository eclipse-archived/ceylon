doc "A fixed-sized, non-empty collection."
shared interface Some<out Element>
        satisfies FixedSized<Element> &
                  ContainerWithFirstElement<Element,Bottom> {

    doc "Returns the first element, which always exists."
    shared actual default Element first {
        if (is Element first = iterator.next()) {
            return first;
        }
        else {
            throw;
        }
    }

    doc "Returns `false`, since every `Some` contains at
         least one element."
    shared actual Boolean empty {
        return false;
    }

    doc "Returns a `List` containing all but the first 
         element of this collection."
    shared actual formal List<Element> rest;

}
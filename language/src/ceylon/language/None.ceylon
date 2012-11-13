doc "An iterator which always returns `exhausted`."
object emptyIterator satisfies Iterator<Bottom> {
    shared actual Finished next() = exhausted;
}

doc "A fixed-size collection with no elements."
shared interface None<out Element>
        satisfies FixedSized<Element> &
                  ContainerWithFirstElement<Bottom,Nothing> {

    doc "Returns `null`."
    shared actual default transient Nothing first = null;
    
    doc "Returns `null`."
    shared actual default transient Nothing last = null;
    
    doc "Returns `emptyIterator`."
    shared actual default transient Iterator<Element> iterator = 
            emptyIterator;
    
    doc "Returns 0."
    shared actual default transient Integer size = 0;
    
    doc "Returns `true`."
    shared actual transient Boolean empty = true;
    
}

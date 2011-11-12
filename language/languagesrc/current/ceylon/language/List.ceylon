import ceylon.language { sequenceEntries=entries }

doc "A nonempty, finite list of elements which satisfy 
     `Equality`. A `List` may contain the same element more than
     once, at different indexes."
by "Gavin"
see (Range)
shared interface List<Element>
        satisfies Sequence<Element> & Collection<Element>
        given Element satisfies Equality {
    
    doc "A map of index to element, for each element of this
         list. Equivalent to `entries(list...)`."
    see (sequenceEntries)
    shared default Map<Natural, Element> entries {
        return sequenceEntries(this...);
    }
    
}
import ceylon.language { sequenceEntries=entries }

doc "A finite list of elements which satisfy Equality. A 
     List may contain the same element more than once, at 
     different indexes."
by "Gavin"
see (Range)
shared interface List<out Element>
        satisfies Correspondence<Natural, Element> &  
                  Ordered<Element> & Collection<Element> &
                  Ranged<List<Element>>
        given Element satisfies Equality {
    
    doc "The sequence of elements."
    shared formal Element[] sequence;
    
    doc "A map of index to element, for each element of this
         list. Equivalent to entries(list...)."
    see (sequenceEntries)
    shared default Map<Natural, Element> entries {
        return sequenceEntries(sequence...);
    }
        
}
shared interface Collection<out Element> 
        satisfies Iterable<Element> & 
                  Sized & Category & 
                  Equality {
    shared actual default Boolean empty {
        return size!=0;
    }
}
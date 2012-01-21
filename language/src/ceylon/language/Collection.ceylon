shared interface Collection<out Element> 
        satisfies Iterable<Element> & Sized & 
                  Category & Equality & 
                  Cloneable<Collection<Element>> {
    
    shared actual default Boolean empty {
        return size==0;
    }
    
    shared actual default Boolean contains(Equality element) {
        for (elem in this) {
            if (is Equality elem) {
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
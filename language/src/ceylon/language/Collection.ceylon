shared interface Collection<out Element> 
        satisfies Iterable<Element> & 
                  Sized & Category & 
                  Equality {
    
    shared actual default Boolean empty {
        return size!=0;
    }
    
    shared actual Boolean contains(Equality element) {
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
shared interface Collection<out Element> 
        satisfies Iterable<Element> & 
                  Sized & Category & 
                  Cloneable<Collection<Element>> {
    
    shared actual default Boolean empty {
        return size==0;
    }
    
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
    
    shared default Integer count(Object element) {
        variable value count:=0;
        for (elem in this) {
            if (is Object elem) {
                if (elem==element) {
                    count++;
                }
            }
        }
        return count;
    }
    
}
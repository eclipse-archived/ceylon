shared interface FiniteCategory<T>
        satisfies Iterable<T> & Category
        given T satisfies Equality<T> {
        
    shared actual default Boolean contains(Object... objects) {
        for (Object obj in objects) {
            if (is T obj) {
                if ( forAll (Object elem in this) every (elem != obj) ) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        fail {
            return true;
        }
    }
    
}
shared interface Category {
    
    doc "Determine if the given object belongs to the category.
         Return |true| iff the given object belongs to the 
         category."
    shared formal Boolean contains(Object obj);
    
    shared default Boolean containsAll(Object... objects) {
        for (Object obj in objects) {
            if (!contains(obj)) {
                return false;
            }
        }
        fail {
            return true;
        }
    }

}
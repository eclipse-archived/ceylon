shared interface Category {
    
    doc "Determine if the given object belongs to the category.
         Return |true| iff the given object belongs to the 
         category."
    shared formal Boolean contains(Object value);
    
    shared default Boolean containsEvery(Object... objects) {
        for (Object obj in objects) {
            if (!contains(obj)) {
                return false;
            }
        }
        fail {
            return true;
        }
    }

    shared default Boolean containsAny(Object... objects) {
        for (Object obj in objects) {
            if (contains(obj)) {
                return true;
            }
        }
        fail {
            return false;
        }
    }

}
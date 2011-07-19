shared interface Category {
    
    doc "Determine if the given object belongs to the category.
         Return |true| iff the given object belongs to the 
         category."
    shared formal Boolean contains(Object element);
    
    shared default Boolean containsEvery(Object... elements) {
        for (Object element in elements) {
            if (!contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }

    shared default Boolean containsAny(Object... elements) {
        for (Object element in elements) {
            if (contains(element)) {
                return true;
            }
        }
        else {
            return false;
        }
    }

}
doc "Abstract supertype of objects that contain other 
     values, called elements, where it is possible to 
     efficiently determine if a given value is an element. 
     Category does not satisfy Container, because it is 
     conceptually possible to have a Category whose 
     emptiness cannot be computed."
see (Container)
shared interface Category {
    
    doc "Determines if the given value belongs to this
         Category, that is, if it is an element of this
         Category."
    see (containsEvery, containsAny)
    shared formal Boolean contains(Object element);
    
    doc "Determines if every one of the given values belongs
         to this Category"
    see (contains)
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

    doc "Determines if every one of the given values belongs
         to this Category"
    see (contains)
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
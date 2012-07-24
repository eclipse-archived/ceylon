doc "Abstract supertype of objects that contain other 
     values, called *elements*, where it is possible to 
     efficiently determine if a given value is an element. 
     `Category` does not satisfy `Container`, because it is 
     conceptually possible to have a `Category` whose 
     emptiness cannot be computed.
     
     The `in` operator may be used to determine if a value
     belongs to a `Category`:
     
         if (\"hello\" in \"hello world\") { ... }
         if (69 in 0..100) { ... }
         if (key->value in { for (n in 0..100) n.string->n**2 }) { ... }
     "
see (Container)
by "Gavin"
shared interface Category {
    
    doc "Determines if the given value belongs to this
         `Category`, that is, if it is an element of this
         `Category`."
    see (containsEvery, containsAny)
    shared formal Boolean contains(Object element);
    
    doc "Determines if every one of the given values belongs
         to this `Category`"
    see (contains)
    shared default Boolean containsEvery(Object... elements) {
        for (element in elements) {
            if (!contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }

    doc "Determines if any of the given values belongs
         to this `Category`"
    see (contains)
    shared default Boolean containsAny(Object... elements) {
        for (element in elements) {
            if (contains(element)) {
                return true;
            }
        }
        else {
            return false;
        }
    }

}

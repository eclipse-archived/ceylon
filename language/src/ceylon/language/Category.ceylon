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
     
     Ordinarily, `x==y` implies that `x in cat == y in cat`.
     But this contract is not required since it is possible
     to form a meaningful `Category` using a different
     equivalence relation. For example, an `IdentitySet` is
     a meaningful `Category`."
see (Container)
by "Gavin"
shared interface Category {
    
    doc "Determines if the given value belongs to this
         `Category`, that is, if it is an element of this
         `Category`.
         
         For most `Category`s, if `x==y`, then 
         `category.contains(x)` evaluates to the same
         value as `category.contains(y)`. However, it is
         possible to form a `Category` consistent with some 
         other equivalence relation, for example `===`. 
         Therefore implementations of `contains()` which do 
         not satisfy this relationship are tolerated."
    see (containsEvery, containsAny)
    shared formal Boolean contains(Object element);
    
    doc "Determines if every one of the given values belongs
         to this `Category`."
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

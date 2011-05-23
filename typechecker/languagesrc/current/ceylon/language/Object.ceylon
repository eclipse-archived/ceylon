shared abstract class Object() 
        extends Value() {
        
    /*shared actual Boolean null {
        return false;
    }*/
    
    doc "A developer-friendly string representing the instance."
    shared formal String string;
    
    doc "Determine if this object belongs to the given |Category|.
         The binary |in| operator."
    see (Category, Iterable)
    shared Boolean element(Category|Iterable<Equality> category) {
        if (is Category category) {
            return category.contains(this);
        }
        else if (is Iterable<Equality> category) {
            if (is Equality self = this) {
                for (Equality x in category) {
                    if (x==self) {
                        return true;
                    }
                }
                fail {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            throw; //cannot occur!
        }
    }
    
}
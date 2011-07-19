shared abstract class Object() 
        extends Void() {
        
    doc "A developer-friendly string representing the instance."
    shared formal String string;
    
    doc "Determine if this object belongs to the given |Category|.
         The binary |in| operator."
    shared Boolean contained(Category category) {
        return category.contains(this);
    }
    
    /*shared Boolean contained(Category|Iterable<Equality> category) {
        switch (category)
        case (is Category) {
            return category.contains(this);
        }
        case (is Iterable<Equality>) {
            if (is Equality self = this) {
                for (Equality x in category) {
                    if (x==self) {
                        return true;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
    }*/
    
}
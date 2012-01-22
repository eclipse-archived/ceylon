shared interface Set<out Element> 
        satisfies Collection<Element> &
                  Slots<Set<Equality>> &
                  Cloneable<Set<Element>>
        given Element satisfies Equality {
    
    shared actual default Integer count(Equality element) {
        return contains(element) then 1 else 0;
    }
    
    shared default Boolean superset(Set<Equality> set) {
        for (element in set) {
            if (!contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    shared default Boolean subset(Set<Equality> set) {
        for (element in this) {
            if (!set.contains(element)) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
}
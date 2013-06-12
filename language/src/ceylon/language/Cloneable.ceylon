"Abstract supertype of objects whose value can be 
 cloned."
shared interface Cloneable<out Clone> of Clone
        given Clone satisfies Cloneable<Clone> {
    
    "Obtain a clone of this object. For a mutable 
     object, this should return a copy of the object. 
     For an immutable object, it is acceptable to return
     the object itself."
    shared formal Clone clone;
    
}
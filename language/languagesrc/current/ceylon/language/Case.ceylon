doc "The abstract supertype of all objects which are values
     of an enumerated type. Examples of enumerated types 
     include `Boolean` and `Comparison`."
see (Boolean, Comparison)
by "Gavin"
shared abstract class Case(String caseName) {
    
    doc "The name of the enumerated value"
    shared actual default String string = caseName;
    
    doc "Equality for values of enumerated types is always
         identity equality. This implementation simply
         delegates to `IdentifiableObject`, ensuring the 
         definition there cannot be refined."
    shared actual Boolean equals(Equality that) {
        return super.equals(that);
    }
    
    shared actual Integer hash {
        return super.hash;
    }
    
}
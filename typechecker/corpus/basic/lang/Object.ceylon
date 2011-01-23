shared abstract class Object() 
        extends Void() {
    
    doc "The |Type| of the instance."
    shared Type<subtype> type {
        throw
    }
    
    doc "A developer-friendly string representing the instance."
    shared formal String string;
        
}
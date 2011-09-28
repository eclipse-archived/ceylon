shared class Exception(String? description=null, Exception? cause=null) 
        extends IdentifiableObject() {
    
    shared String? description = description;
    
    shared Exception? cause = cause;
    
    //shared StackTrace stackTrace { throw; }
    
    shared default String message {
        return description ? cause?.message ? "";
    }
    
    shared actual String string {
        return super.string + " \"" message "\"";
    }
    
}
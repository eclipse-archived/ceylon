shared class Exception(Exception? cause=null, String? description=null) 
        extends IdentifiableObject() {
    
    shared String? description = description ? cause?.description;
    
    shared Exception? cause = cause;
    
    //shared StackTrace stackTrace { throw; }
    
    shared actual String string {
        if (exists description) {
            return super.string + ": " + description;
        }
        else {
            return super.string;
        }
    }
    
}
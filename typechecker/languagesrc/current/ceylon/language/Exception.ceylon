shared class Exception(Exception? cause=null, String? message=null) 
        extends IdentifiableObject() {
    
    shared String message = message ? cause?.message ? "";
    
    shared Exception cause = cause ? this;
    
    //shared StackTrace stackTrace { throw; }
    
    shared actual String string {
        if (nonempty message) {
            return super.string + ": " + message;
        }
        else {
            return super.string;
        }
    }
    
}
"The supertype of all exceptions. A subclass represents
 a more specific kind of problem, and may define 
 additional attributes which propagate information about
 problems of that kind."
by ("Gavin", "Tom")
shared native class Exception(description=null, cause=null) {
    
    "The underlying cause of this exception."
    shared Exception? cause;
    
    "A description of the problem."
    String? description;
    
    //shared native StackTrace stackTrace;
    
    "A message describing the problem. This default 
     implementation returns the description, if any, or 
     otherwise the message of the cause, if any."
    //see (`description`, cause)
    shared default String message =>
            description else cause?.message else "";
    
    shared actual default String string =>
            className(this) + " \"``message``\"";
    
    "Print the stack trace to the standard error of
     the virtual machine process."
    shared native void printStackTrace();
    
}

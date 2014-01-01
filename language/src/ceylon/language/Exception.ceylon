import ceylon.language { printTrace=printStackTrace }

"The supertype of all exceptions. A subclass of `Exception`
 represents a more specific kind of problem, and may define 
 additional attributes which propagate information about 
 problems of that kind.
 
 - Programming errors are indicated using 
   [[AssertionException]]. (For example, an out of bounds
   index is considered a programming error.)
 - Other concrete subclasses of `Exception` generally 
   represent transient or _unexpected failures_ that are 
   usually unrecoverable from the point of view of the 
   immediate caller of an operation. (For example, 
   transaction rollback, or loss of connectivity.)
 
 The use of exceptions to indicate _expected failures_, that 
 is, failures that are usually handled by the immediate 
 caller of an operation, is discouraged. (For example,
 nonexistence of a file should not result in an exception.) 
 Instead, the failure should be respresented as a return 
 value of the operation being called."

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
    see (`value cause`)
    shared default String message 
            => description else cause?.message else "";
    
    shared actual default String string 
            => className(this) + " \"``message``\"";
    
    "Print the stack trace to the standard error of the 
     virtual machine process."
    see (`function printTrace`)
    shared void printStackTrace() => printTrace(this);
    
}

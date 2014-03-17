import ceylon.language { printTrace=printStackTrace }

"The supertype of instances that can be thrown using the `throw` statement
 and caught using the `catch` clause of the `try` statement. 
 Such instances are used to represent 
 problems, typically _unexpected failures_, with the running program 
 and can be classified as either
 [[errors|Error]] which are non-transient and generally unrecoverable 
 or [[exceptions|Exception]] which are transient problems which 
 may be recovered from.
 
 The use of throwables to indicate _expected failures_, that 
 is, failures that are usually handled by the immediate 
 caller of an operation, is discouraged. (For example,
 nonexistence of a file should not result in an exception.) 
 Instead, the failure should be respresented as a return 
 value of the operation being called."
by ("Gavin", "Tom")
shared native abstract class Throwable(description=null, cause=null) 
        of Exception | Error {
    "The underlying cause of this exception."
    shared Throwable? cause;
    
    "A description of the problem."
    String? description;
    
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

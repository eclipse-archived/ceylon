"Abstract supertype of resources which are created at the 
 beginning of a `try` statement and destroyed when the 
 statement completes. Unlike an [[Obtainable]] resource, a 
 single instance of `Destroyable` may not be reused between 
 multiple `try` statements or multiple executions of the 
 same `try` statement. 
 
     try (tx = Transaction()) {
         ...
     }
 
 - The resource is instantiated before the body of the `try` 
   statement is executed, and
 - [[destroy]] is called when execution of the body of the 
   `try` statement ends, even if an exception propagates out 
   of the body of the `try`."
see (`interface Obtainable`)
shared interface Destroyable {
    
    "Destroy this resource. Called when execution of the 
     body of the `try` statement ends, even if an exception 
     propagates out of the body of the `try`."
    shared formal void destroy(
        "The exception propagating out of the body of the 
         `try` statement, or `null` if no exception was
         propagated."
        Throwable? error);
    
}
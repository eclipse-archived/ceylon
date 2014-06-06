"Abstract supertype of resources which may be obtained at
 the beginning of a `try` statement and then released when 
 the statement completes. Unlike a [[Destroyable]] resource, 
 a single instance of `Obtainable` may be reused between 
 multiple `try` statements or multiple executions of the 
 same `try` statement.
 
     Lock lock = ... ;
     try (lock) {
         ...
     }
 
 - [[obtain]] is called before the body of the `try` 
   statement is executed, and
 - [[release]] is called when execution of the body of the 
   `try` statement ends, even if an exception propagates out 
   of the body of the `try`.
 
 A class which satisfies `Obtainable` may impose constraints 
 on the ordering and nesting of invocations of `obtain()` 
 and `release()`. For example, it may be not be possible to
 obtain a resource that has already been obtained. Those 
 methods should produce an [[AssertionError]] when any such 
 constraint is violated."
see (`interface Destroyable`)
shared interface Obtainable {
    
    "Obtain this resource. Called before the body of a `try` 
     statement is executed.
     
     If an exception is thown by `obtain()`, then `release()` 
     will not be called."
    throws (`class AssertionError`, 
            "if an illegal state is detected")
    shared formal void obtain();
    
    "Release this resource. Called when execution of the 
     body of a `try` statement ends, even if an exception 
     propagates out of the body of the `try`."
    throws (`class AssertionError`, 
            "if an illegal state is detected")
    shared formal void release(
        "The exception propagating out of the body of the 
         `try` statement, or `null` if no exception was
         propagated."
        Throwable? error);
    
}
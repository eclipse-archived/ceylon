"Abstract supertype of instances which are used as resources
 in a `try` statement and [[released|release]]
 whether or not an exception propagates out of the `try` block.
 Unlike [[Destroyable]] a single `Obtainable` instance can be 
 reused between `try` statements.
 
 Although it is possible to use `Obtainable` instances outside of a `try` 
 statement this is not recommended.
 
 Classes which satisfy `Obtainable` may impose specific constraints on 
 the ordering and nesting of invocations of `obtain()` and `release()`. 
 For example it may be not be allowed to invoke `obtain()` on a 
 resource that has already been `obtain()`ed. 
 Those methods should throw an [[AssertionError]] when such 
 constraints are broken.
 "
shared interface Obtainable {
    
    "Prepares the instance for use.
     If an exception propagates from `obtain()` then `release()` 
     will not be called."
    throws(`class AssertionError`, "If an illegal state is detected")
    shared formal void obtain();
    
    "Releases the instance after use."
    throws(`class AssertionError`, "If an illegal state is detected")
    shared formal void release(
        "The error propagating out of the `try` block, or null"
        Throwable? error);
}
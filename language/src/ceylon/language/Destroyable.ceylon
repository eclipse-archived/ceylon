"Abstract supertype of classes which are instantiated 
 as resources in a `try` statement and [[destroyed|destroy]]
 whether or not an exception propagates out of the `try` block.
 Unlike [[Obtainable]] a single `Destroyable` instance cannot be 
 reused between `try` statements: Its scope is the `try` block.
 "
shared interface Destroyable {
    
    "Called after completion of a `try` block."
    shared formal void destroy(
        "The error propagating out of the `try` block, or null"
        Throwable? error);
    
}
"Abstract supertype of types which may appear
 as the expression type of a resource expression
 in a `try` statement." 
shared interface Closeable {

    "Called before entry to a `try` block."
    shared formal void open();

    "Called after completion of a `try` block."
    shared formal void close(Exception? exception);
    
}
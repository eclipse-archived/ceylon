"Abstract supertype of classes which may appear as the 
 resource expression in a `try` statement."
shared interface Closeable {

    "Called before entry to a `try` block. If `open()` is 
     called as a result of execution of a `try` statement, 
     then `close()` is also guaranteed to be called, even if 
     `open()` throws an exception."
    shared formal void open();

    "Called after completion of a `try` block. If `close()`
     is called as a result of execution of a `try` 
     statement, then it is guaranteed that `open()` was 
     previously called."
    shared formal void close(Throwable? exception);
    
}
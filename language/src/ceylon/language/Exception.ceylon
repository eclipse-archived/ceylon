

"The supertype of all unexpected transient failures. 
 
 A subclass of `Exception`
 represents a more specific kind of problem, and may define 
 additional attributes which propagate information about 
 problems of that kind. Exceptions are usually 
 usually unrecoverable from the point of view of the 
 immediate caller of an operation. (For example, 
 transaction rollback, or loss of connectivity.)"
by ("Gavin", "Tom")
shared native class Exception(String? description=null, Throwable? cause=null) 
        extends Throwable(description, cause) {
    
}
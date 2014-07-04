"The supertype of all unrecoverable failures. Since `Error`s
 are unrecoverable, it is not usual to `catch` them. There 
 are two broad categories of `Error`:
 
 - [[assertion failures|AssertionError]], which indicate
   bugs in the program logic, and
 - problems that occur at the level of the virtual machine,
   including problems with dynamic linking or binary 
   compatibility, or failure to allocate memory."
by ("Gavin", "Tom")
shared native class Error(String? description=null, Throwable? cause=null) 
        extends Throwable(description, cause) {}
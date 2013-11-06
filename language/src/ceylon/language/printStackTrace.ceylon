"Print the stack trace of the given exception using 
 the given function, or to standard error if no
 function is specified."
shared native void printStackTrace(Exception exception, 
        "A function that prints the given string.
         Defaults to `process.writeErrorLine()`."
        void write(String string) => process.writeError(string));

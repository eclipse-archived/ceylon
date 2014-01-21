"Print the stack trace of the given [[Exception]] using the 
 given [[function|write]], or to 
 [[standard error|process.writeError]] if no function is 
 specified."
shared native void printStackTrace(Exception exception, 
        "A function that prints the given string.
         Defaults to [[process.writeError]]."
        void write(String string) => process.writeError(string));

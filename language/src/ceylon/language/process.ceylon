doc "Represents the current process (instance of the virtual
     machine)."
by "Gavin"
   "Tako"
shared native object process {
    
    doc "The command line arguments to the virtual machine."
    shared native String[] arguments;
    
    doc "Determine if an argument of form `-name` or `--name` 
         was specified among the command line arguments to 
         the virtual machine."
    shared native Boolean namedArgumentPresent(String name);

    doc "The value of the first argument of form `-name=value`, 
         `--name=value`, or `-name value` specified among the 
         command line arguments to the virtual machine, if
         any."
    shared native String? namedArgumentValue(String name);

    doc "The value of the given system property of the virtual
         machine, if any."
    shared native String? propertyValue(String name);
    
    doc "The line ending character sequence on this platform."
    shared native String newline;

    doc "Print a string to the standard output of the 
         virtual machine process."
    shared native void write(String string);
    
    doc "Print a line to the standard output of the 
         virtual machine process."
    see (print)
    shared void writeLine(String line="") { 
        write(line);
        write(newline); 
    }
    
    doc "Print a string to the standard error of the 
         virtual machine process."
    shared native void writeError(String string);
    
    doc "Print a line to the standard error of the 
         virtual machine process."
    shared void writeErrorLine(String line="") { 
        writeError(line);
        writeError(newline);
    }
    
    doc "Read a line of input text from the standard input 
         of the virtual machine process."
    shared native String readLine();
    
    doc "The elapsed time in milliseconds since midnight, 
         1 January 1970."
    shared native Integer milliseconds;
    
    doc "The elapsed time in nanoseconds since an arbitrary
         starting point."
    shared native Integer nanoseconds;
    
    shared native void exit(Integer code);
    
    doc "Returns the name of the virtual machine this 
         process is running on."
    shared native String vm;
    
    doc "Returns the version of the virtual machine this 
         process is running on."
    shared native String vmVersion;
    
    doc "Returns the name of the operating system this 
         process is running on."
    shared native String os;
    
    doc "Returns the version of the operating system this 
         process is running on."
    shared native String osVersion;
    
    shared actual String string => "process";
    
}

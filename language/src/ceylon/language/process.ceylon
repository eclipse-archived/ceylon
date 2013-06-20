"Represents the current process (instance of the virtual
 machine)."
by ("Gavin", "Tako")
shared native object process {
    
    "The command line arguments to the virtual machine."
    shared native String[] arguments;
    
    "Determine if an argument of form `-name` or `--name` 
     was specified among the command line arguments to 
     the virtual machine."
    shared native Boolean namedArgumentPresent(String name);

    "The value of the first argument of form `-name=value`, 
     `--name=value`, or `-name value` specified among the 
     command line arguments to the virtual machine, if
     any."
    shared native String? namedArgumentValue(String name);

    "The value of the given system property of the virtual
     machine, if any."
    shared native String? propertyValue(String name);
    
    "The line ending character sequence on this platform."
    shared native String newline;

    "Print a string to the standard output of the 
     virtual machine process."
    shared native void write(String string);
    
    "Print a line to the standard output of the 
     virtual machine process."
    see (`print`)
    shared void writeLine(String line="") { 
        write(line);
        write(newline); 
    }
    
    "Print a string to the standard error of the 
     virtual machine process."
    shared native void writeError(String string);
    
    "Print a line to the standard error of the 
     virtual machine process."
    shared void writeErrorLine(String line="") { 
        writeError(line);
        writeError(newline);
    }
    
    "Read a line of input text from the standard input 
     of the virtual machine process."
    shared native String readLine();
    
    "The elapsed time in milliseconds since midnight, 
     1 January 1970."
    shared native Integer milliseconds;
    
    "The elapsed time in nanoseconds since an arbitrary
     starting point."
    shared native Integer nanoseconds;
    
    shared native void exit(Integer code);
    
    "Returns the name of the virtual machine this 
     process is running on."
    shared native String vm;
    
    "Returns the version of the virtual machine this 
     process is running on."
    shared native String vmVersion;
    
    "Returns the name of the operating system this 
     process is running on."
    shared native String os;
    
    "Returns the version of the operating system this 
     process is running on."
    shared native String osVersion;
    
    shared actual String string => "process";
    
}

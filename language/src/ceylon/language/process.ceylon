
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

    "Print a string to the standard output of the 
     virtual machine process."
    shared native void write(String string);
    
    "Print a line to the standard output of the 
     virtual machine process."
    see (`function print`)
    shared void writeLine(String line="") { 
        write(line);
        write(operatingSystem.newline); 
    }
    
    "Flush the standard output of the 
     virtual machine process."
    shared native void flush();
    
    "Print a string to the standard error of the 
     virtual machine process."
    shared native void writeError(String string);
    
    "Print a line to the standard error of the 
     virtual machine process."
    shared void writeErrorLine(String line="") { 
        writeError(line);
        writeError(operatingSystem.newline);
    }
    
    "Flush the standard error of the 
     virtual machine process."
    shared native void flushError();
    
    "Read a line of input text from the standard input 
     of the virtual machine process. Returns a line of
     text after removal of line-termination characters,
     or `null` to indicate the EOT character."
    shared native String? readLine();
    
    "Force the virtual machine to terminate with 
     the given exit code."
    shared native void exit(Integer code);
    
    string => "process";
    
}

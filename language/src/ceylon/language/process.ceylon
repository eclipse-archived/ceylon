doc "Represents the current process (instance of the virtual
     machine)."
by "Gavin"
   "Tako"
shared object process {
    
    doc "The command line arguments to the virtual machine."
    shared String[] arguments { throw; }
    
    doc "Determine if an argument of form `-name` or `--name` 
         was specified among the command line arguments to 
         the virtual machine."
    shared Boolean namedArgumentPresent(String name) { throw; }

    doc "The value of an argument of form `-name=value`, 
         `--name=value`, or `-name value` specified among the 
         command line arguments to the virtual machine, if
         any."
    shared String? namedArgumentValue(String name) { throw; }

    doc "The value of the given system property of the virtual
         machine, if any."
    shared String? propertyValue(String name) { throw; }
    
    doc "The line ending character sequence on this platform."
    shared String newline { throw; }

    doc "Print a string to the standard output of the 
         virtual machine process."
    shared void write(String string) { throw; }
    
    doc "Print a line to the standard output of the 
         virtual machine process."
    see (print)
    shared void writeLine(String line) { 
        write(line);
        write(newline); 
    }
    
    doc "Print a string to the standard output of the 
         virtual machine process."
    shared void writeError(String string) { throw; }
    
    doc "Print a line to the standard output of the 
         virtual machine process."
    see (print)
    shared void writeErrorLine(String line) { 
        writeError(line);
        writeError(newline);
    }
    
    doc "Read a line of input text from the standard input 
         of the virtual machine process."
    shared String readLine() { throw; }
    
    doc "The elapsed time in milliseconds since midnight, 
         1 January 1970."
    shared Integer milliseconds { throw; }
    
    shared void exit(Integer code) { throw; }
    
    shared actual String string {
        return "process";
    }

}

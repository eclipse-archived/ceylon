doc "Represents the current process (instance of the virtual
     machine)."
by "Gavin"
   "Tako"
shared object process extends Object() {
    
    doc "The command line arguments to the virtual machine."
    shared String[] arguments { throw; }
    
    /*doc "The unix switch-style command line arguments to the 
         virtual machine."
    shared Correspondence<String,String> switches { throw; }

    doc "The current system properties."
    shared Correspondence<String,String> properties { throw; }*/

    doc "Print a string to the standard output of the 
         virtual machine process."
    shared void write(String string) { throw; }
    
    doc "Print a line to the standard output of the 
         virtual machine process."
    see (print)
    shared void writeLine(String line) { 
        write(line); write("\n"); 
    }
    
    doc "Print a string to the standard output of the 
         virtual machine process."
    shared void writeError(String string) { throw; }
    
    doc "Print a line to the standard output of the 
         virtual machine process."
    see (print)
    shared void writeErrorLine(String line) { 
        writeError(line); writeError("\n"); 
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

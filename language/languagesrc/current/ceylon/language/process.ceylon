doc "Represents the current process (instance of the virtual
     machine)."
by "Gavin"
   "Tako"
shared object process extends Object() {
    
    doc "The command line arguments to the virtual machine."
    shared String[] arguments { throw; }
    
    doc "The unix switch-style command line arguments to the 
         virtual machine."
    shared Entries<String,String> switches { throw; }

    doc "The current system properties."
    shared Entries<String,String> properties { throw; }

    doc "Print a string to the console."
    shared void write(String string) { throw; }
    
    doc "Print a line to the console."
    see (print)
    shared void writeLine(String line) { 
        write(line); write("\n"); 
    }
    
    doc "Read a line of input text from the console."
    shared String readLine() { throw; }
    
    shared actual String string { throw; }

}
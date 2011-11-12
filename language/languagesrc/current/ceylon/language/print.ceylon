doc "Print a line to the standard output of the 
     virtual machine process. A shortcut for 
     process.writeLine(line)."
see (process.writeLine)
by "Gavin"
shared void print(String line="") {
    process.writeLine(line);
}

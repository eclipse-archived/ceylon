doc "Print a line to the standard output of the 
     virtual machine process. A shortcut for 
     `process.writeLine(line.string)`."
see (process.writeLine)
by "Gavin"
shared void print(Object line) {
    process.writeLine(line.string);
}

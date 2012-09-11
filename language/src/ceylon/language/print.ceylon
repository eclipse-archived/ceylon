doc "Print a line to the standard output of the virtual 
     machine process, printing the given value's `string`, 
     or `«null»` if the value is `null`.
     
     This method is a shortcut for:
     
         process.writeLine(line?.string else \"«null»\")
     
     and is intended mainly for debugging purposes."
see (process.writeLine)
by "Gavin"
shared void print(Void line) {
    process.writeLine(line?.string else "«null»");
}

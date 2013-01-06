doc "Print a line to the standard output of the virtual 
     machine process, printing the given value's `string`, 
     or `\{#ab}null\{#bb}` if the value is `null`.
     
     This method is a shortcut for:
     
         process.writeLine(line?.string else \"\{#ab}null\{#bb}\")
     
     and is intended mainly for debugging purposes."
see (process.writeLine)
by "Gavin"
shared void print(Void line) { 
    process.writeLine(line?.string else "\{#ab}null\{#bb}");
}

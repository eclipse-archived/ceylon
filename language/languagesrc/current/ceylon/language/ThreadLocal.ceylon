doc "A value that depends upon the current thread."
shared class ThreadLocal<Value>(Value initialValue) {
    
    doc "The value associated with the current thread."
    shared variable Value currentValue:=initialValue;
    
    doc "Clear the value associated with the current thread, 
         reverting to the initial value."
    shared void clear() { currentValue:=initialValue; }
    
}
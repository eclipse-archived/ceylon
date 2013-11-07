"Machine (Virtual or not) on which the current process is running.
 
 Holds information about runtime name, version and about inherent limitations
 like minimum/maximum values that can be represented by the runtime."
shared native object runtime  {
    
    "Returns the name of the runtime / virtual machine this process is running on."
    shared native String name;
    
    "Returns the version of the runtime / virtual machine this process is running on."
    shared native String version;
    
    "The number of bits used to represent the value of an `Integer`."
    shared native Integer integerSize;
    
    "The minimum `Integer` value that can be represented by the runtime.
     
     It is the minimum `Integer` that can be distinguished from its successor
     using below formula:
     
     `Integer(n-1) = Integer(n) - 1` with `Integer(0) = 0`"
    shared native Integer minIntegerValue;

    "The maximum `Integer` value that can be represented by the runtime.
     
     It is the maximum `Integer` that can be distinguished from its predecessor
     using below formula:
     
     `Integer(n+1) = Integer(n) + 1` with `Integer(0) = 0`"
    shared native Integer maxIntegerValue;
    
    string => "runtime [``name`` / ``version``]";
}

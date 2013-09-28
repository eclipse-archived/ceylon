"Machine (Virtual or not) on which the current process is running.
 
 Holds information about machine name, version and also about inherent machine
 limitations like minimum/maximum values that can be represented on it."
shared native object machine  {
    
    "Returns the name of the virtual machine this process is running on."
    shared native String name;
    
    "Returns the version of the virtual machine this process is running on."
    shared native String version;
    
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
    
    shared actual String string => "machine [``name`` / ``version``]";
}

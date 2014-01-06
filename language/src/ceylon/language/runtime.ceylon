"Machine (Virtual or not) on which the current process is running.
 
 Holds information about runtime name, version and about inherent limitations
 like minimum/maximum values that can be represented by the runtime."
shared native object runtime  {
    
    "The name of the runtime / virtual machine this process is running on."
    shared native String name;
    
    "The version of the runtime / virtual machine this process is running on."
    shared native String version;
    
    "The number of bits used to represent the value of an [[Integer]]."
    see (`class Integer`)
    shared native Integer integerSize;
    
    "The minimum [[Integer]] value that can be represented by the runtime.
     
     It is the minimum `Integer` that can be distinguished from its successor
     using below formula:
     
     `Integer(n-1) = Integer(n) - 1` with `Integer(0) = 0`"
    see (`class Integer`)
    shared native Integer minIntegerValue;

    "The maximum [[Integer]] value that can be represented by the runtime.
     
     It is the maximum `Integer` that can be distinguished from its predecessor
     using below formula:
     
     `Integer(n+1) = Integer(n) + 1` with `Integer(0) = 0`"
    see (`class Integer`)
    shared native Integer maxIntegerValue;
    
    "The maximum size of an [[Array]] that is possible for this runtime.
     Note that this is a theoretical limit only. In practice it is usually
     impossible to allocate an array of this size, due to memory constraints."
    see (`class Array`)
    shared native Integer maxArraySize;
    
    string => "runtime [``name`` / ``version``]";
}

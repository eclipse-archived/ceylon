"Represents the system on which the current process is running.
 
 Holds information about system time and locale."
shared native object system {
    
    "The elapsed time in milliseconds since midnight, 1 January 1970."
    shared native Integer milliseconds;
    
    "The elapsed time in nanoseconds since an arbitrary starting point."
    shared native Integer nanoseconds;
    
    "Returns the offset from UTC, in milliseconds, of the default timezone for
     this virtual machine."
    shared native Integer timezoneOffset;
    
    "Returns the IETF language tag representing the default locale for this
     virtual machine."
    shared native String locale;
    
    shared actual String string => "system";
}

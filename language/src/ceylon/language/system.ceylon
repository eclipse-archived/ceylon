"Represents the system on which the current process is 
 executing.
 
 Holds information about system time and locale."
see (`value process`, `value runtime`, `value language`,
     `value operatingSystem`)
shared native object system {
    
    "The elapsed time in milliseconds since midnight, 
     1 January 1970."
    shared native Integer milliseconds;
    
    "The elapsed time in nanoseconds since an arbitrary 
     starting point."
    shared native Integer nanoseconds;
    
    "Returns the offset from UTC, in milliseconds, of the 
     default timezone for this system."
    shared native Integer timezoneOffset;
    
    "Returns the IETF language tag representing the default 
     locale for this system."
    shared native String locale;
    
    "Returns the IANA character set name representing the default 
     character encoding for this system."
    shared native String characterEncoding;
    
    string => "system";
}

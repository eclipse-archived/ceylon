"Operating System on which the current process is running."
shared native object operatingSystem {
    
    "Returns the name of the operating system this process is running on."
    shared native String name;
    
    "Returns the version of the operating system this process is running on."
    shared native String version;
    
    "The line ending character sequence on this platform."
    shared native String newline;
    
    "The path separator character sequence on this platform."
    shared native String pathSeparator;
    
    shared actual String string => "operating system [``name`` / ``version``]";
}

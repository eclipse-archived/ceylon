"Operating System on which the current process is running."
shared native object operatingSystem {
    
    "Returns the name of the operating system this process is running on."
    shared native String name;
    
    "Returns the version of the operating system this process is running on."
    shared native String version;
    
    "The line ending character sequence on this platform."
    shared native String newline;
    
    "The character used on this platform to separate the folder/file elements of a path."
    shared native String fileSeparator;
    
    "The character used on this platform to use as a separator between several paths in a list of paths."
    shared native String pathSeparator;
    
    string => "operating system [``name`` / ``version``]";
}


"Contains information about the Ceylon language version."
see (`value process`, `value runtime`, `value system`,
     `value operatingSystem`)
tagged("Environment")
shared native object language {
    
    "The Ceylon language version."
    shared String version => "1.3.0";
    
    "The Ceylon language major version."
    shared Integer majorVersion => 1;
    
    "The Ceylon language minor version."
    shared Integer minorVersion => 3;
    
    "The Ceylon language release version."
    shared Integer releaseVersion => 0;
    
    "The Ceylon language version qualifier."
    since("1.2.3")
    shared String versionQualifier => "";
    
    "The Ceylon language release name."
    shared String versionName => "Total Internal Reflection";
    
    "The major version of the code generated for the 
     underlying runtime."
    shared native Integer majorVersionBinary;
    
    "The minor version of the code generated for the 
     underlying runtime."
    shared native Integer minorVersionBinary;
    
    shared actual String string => "language";
    
}

shared native("jvm") object language {
    
    shared native("jvm") Integer majorVersionBinary => 8;
    
    shared native("jvm") Integer minorVersionBinary => 0;
    
}

shared native("js") object language {
    
    shared native("js") Integer majorVersionBinary => 8;
    
    shared native("js") Integer minorVersionBinary => 0;
    
}

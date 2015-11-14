
"Contains information about the Ceylon language version."
see (`value process`, `value runtime`, `value system`,
     `value operatingSystem`)
tagged("Environment")
shared native object language {
    
    "The Ceylon language version."
    shared String version => "1.2.1";
    
    "The Ceylon language major version."
    shared Integer majorVersion => 1;
    
    "The Ceylon language minor version."
    shared Integer minorVersion => 2;
    
    "The Ceylon language release version."
    shared Integer releaseVersion => 1;
    
    "The Ceylon language release name."
    shared String versionName => "A Series Of Unlikely Explanations";
    
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

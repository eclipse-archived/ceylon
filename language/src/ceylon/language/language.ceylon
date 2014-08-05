"Contains information about the Ceylon language version."
see (`value process`, `value runtime`, `value system`,
     `value operatingSystem`)
shared native object language {
    
    "The Ceylon language version."
    shared native String version;
    
    "The Ceylon language major version."
    shared native Integer majorVersion;
    
    "The Ceylon language minor version."
    shared native Integer minorVersion;
    
    "The Ceylon language release version."
    shared native Integer releaseVersion;
    
    "The Ceylon language release name."
    shared native String versionName;
    
    "The major version of the code generated for the 
     underlying runtime."
    shared native Integer majorVersionBinary;
    
    "The minor version of the code generated for the 
     underlying runtime."
    shared native Integer minorVersionBinary;
    
    shared native actual String string;
    
}

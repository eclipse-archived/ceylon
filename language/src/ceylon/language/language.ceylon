
"Contains information about the Ceylon language version."
see (`value process`, `value runtime`, `value system`,
     `value operatingSystem`)
tagged("Environment")
shared native object language {
    
    "The Ceylon language version."
    shared String version => "1.3.3-SNAPSHOT"/*@CEYLON_VERSION@*/;
    
    "The Ceylon language major version."
    shared Integer majorVersion => 1/*@CEYLON_VERSION_MAJOR@*/;
    
    "The Ceylon language minor version."
    shared Integer minorVersion => 3/*@CEYLON_VERSION_MINOR@*/;
    
    "The Ceylon language release version."
    shared Integer releaseVersion => 3/*@CEYLON_VERSION_RELEASE@*/;
    
    "The Ceylon language version qualifier."
    since("1.3.0")
    shared String versionQualifier => "SNAPSHOT"/*@CEYLON_VERSION_QUALIFIER@*/;
    
    "The Ceylon language release name."
    shared String versionName => "Contents May Differ REL"/*@CEYLON_VERSION_NAME@*/;
    
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
    
    shared native("jvm") Integer minorVersionBinary => 1;
    
}

shared native("js") object language {
    
    shared native("js") Integer majorVersionBinary => 9;
    
    shared native("js") Integer minorVersionBinary => 1;
    
}

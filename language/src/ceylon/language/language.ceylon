doc "Contains information about the language"
by "The Ceylon Team"
shared object language {
    
    doc "The Ceylon language version."
    shared String version { throw; }
    
    doc "The Ceylon language major version."
    shared Integer majorVersion { throw; }
    
    doc "The Ceylon language minor version."
    shared Integer minorVersion { throw; }
    
    doc "The Ceylon language release version."
    shared Integer releaseVersion { throw; }
    
    doc "The Ceylon language release name."
    shared String versionName { throw; }
    
    doc "The major version of the code generated for the underlying runtime."
    shared Integer majorVersionBinary { throw; }
    
    doc "The minor version of the code generated for the underlying runtime."
    shared Integer minorVersionBinary { throw; }
    
}

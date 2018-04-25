/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Represents the operating system on which the current 
 process is running."
see (value process, value runtime, value language,
    value system)
tagged("Environment")
shared native object operatingSystem {
    
    "Returns the name of the operating system this process 
     is running on: `linux`, `mac`, `unix`, `windows`, or 
     `other`."
    shared native String name {
        if (exists os 
                = process.propertyValue("os.name")
                        ?.lowercased) {
            if ("win" in os) {
                return "windows";
            }
            else if ("mac" in os) {
                return "mac";
            }
            else if ("linux" in os) {
                return "linux";
            }
            else if ("nix" in os
              || "freebsd" in os
              || "openbsd" in os
               || "netbsd" in os
                || "sunos" in os) {
                return "unix";
            }
        }
        return "other";
    }
    
    "Returns the version of the operating system this 
     process is running on or `Unknown` if it was not
     possible to retrieve that information. The result
     is completely dependent on the underlying system."
    shared native String version => "Unknown";
    
    "The line ending character sequence on this platform."
    shared native String newline;
    
    "The character used on this platform to separate the 
     folder/file elements of a path."
    shared native String fileSeparator {
        assert (exists result 
            = process.propertyValue("file.separator"));
        return result;
    }
    
    "The character used on this platform to use as a 
     separator between several paths in a list of paths."
    shared native String pathSeparator {
        assert (exists result 
            = process.propertyValue("path.separator"));
        return result;
    }
    
    shared actual String string 
            => "operating system [``name`` / ``version``]";
}

shared native("jvm") object operatingSystem {
    
    import java.lang {
        System
     }
    
    shared native("jvm") String version 
            => process.propertyValue("os.version") 
                else "Unknown";
    
    shared native("jvm") String newline
            => System.lineSeparator();
    
}

shared native("js") object operatingSystem {
    
    shared native("js") String newline {
        assert (exists result 
            = process.propertyValue("line.separator"));
        return result;
    }
    
}

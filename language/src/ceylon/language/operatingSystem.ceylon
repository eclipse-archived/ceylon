import java.lang { System { lineSeparator } }

"Represents the operating system on which the current 
 process is running."
see (`value process`, `value runtime`, `value language`,
    `value system`)
shared native object operatingSystem {
    
    "Returns the name of the operating system this process is
     running on: `linux`, `mac`, `unix`, `windows` or `other`."
    shared native String name {
        String? os = process.propertyValue("os.name")?.lowercased;
        if (exists os) {
            if (os.contains("win")) {
                return "windows";
            } else if (os.contains("mac")) {
                return "mac";
            } else if (os.contains("linux")) {
                return "linux";
            } else if (os.contains("nix")
                    || os.contains("freebsd")
                    || os.contains("openbsd")
                    || os.contains("netbsd")
                    || os.contains("sunos")) {
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
        assert (exists result = process.propertyValue("file.separator"));
        return result;
    }
    
    "The character used on this platform to use as a 
     separator between several paths in a list of paths."
    shared native String pathSeparator {
        assert (exists result = process.propertyValue("path.separator"));
        return result;
    }
    
    string => "operating system [``name`` / ``version``]";
}

shared native("jvm") object operatingSystem {
    
    shared native("jvm") String version =>
            process.propertyValue("os.version") else "Unknown";
    
    shared native("jvm") String newline =>
            lineSeparator();
    
}

shared native("js") object operatingSystem {
    
    shared native("js") String newline {
        assert (exists result = process.propertyValue("line.separator"));
        return result;
    }
    
}

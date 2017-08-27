"Represents the current process (instance of the virtual
 machine)."
by ("Gavin", "Tako")
see (value language, value runtime, value system,
     value operatingSystem)
tagged("Environment")
shared native object process {
    
    "The command line arguments to the virtual machine."
    shared native String[] arguments;
    
    "Determine if an argument of form `-name` or `--name` 
     was specified among the command line arguments to 
     the virtual machine."
    shared native Boolean namedArgumentPresent(String name);

    "The value of the first argument of form `-name=value`, 
     `--name=value`, or `-name value` specified among the 
     command line arguments to the virtual machine, if
     any."
    shared native String? namedArgumentValue(String name);

    "The value of the given system property of the virtual
     machine, if any."
    shared native String? propertyValue(String name);

    "The value of the given environment variable defined 
     for the current virtual machine process."
    since("1.2.0")
    shared native String? environmentVariableValue(String name);
    
    "Print a string to the standard output of the virtual
     machine process."
    shared native void write(String string);
    
    "Print a line to the standard output of the virtual 
     machine process."
    see (function print)
    shared void writeLine(String line="") { 
        write(line);
        write(operatingSystem.newline); 
    }
    
    "Flush the standard output of the virtual machine 
     process."
    shared native void flush();
    
    "Print a string to the standard error of the virtual 
     machine process."
    shared native void writeError(String string);
    
    "Print a line to the standard error of the virtual 
     machine process."
    shared void writeErrorLine(String line="") { 
        writeError(line);
        writeError(operatingSystem.newline);
    }
    
    "Flush the standard error of the 
     virtual machine process."
    shared native void flushError();
    
    "Read a line of input text from the standard input of 
     the virtual machine process. Returns a line of text 
     after removal of line-termination characters, or `null`
     to indicate the standard input has been closed."
    shared native String? readLine();
    
    "Force the virtual machine to terminate with the given
     exit code."
    shared native Nothing exit(Integer code);
    
    shared actual String string => "process";
    
}

shared native("jvm") object process {
    import java.lang {
        System {
            output = \iout,
            input = \iin,
            error = err
        }
    }
    import java.io {
        BufferedReader,
        InputStreamReader,
        IOException
    }
    import com.redhat.ceylon.compiler.java {
        Util
    }
    
    shared native("jvm") String[] arguments 
            => [for (arg in Util.args) arg.string];
    
    shared native("jvm") void write(String string) 
            => output.print(string);
    
    shared native("jvm") void writeError(String string) 
            => error.print(string);
    
    shared native("jvm") void flush() => output.flush();
    
    shared native("jvm") void flushError() => error.flush();
    
    late value stdinReader 
            = BufferedReader(InputStreamReader(input));
    
    shared native("jvm") String? readLine() {
        try {
            return stdinReader.readLine();
        } 
        catch (IOException e) {
            throw Exception("could not read line from standard input", e);
        }
    }
    
    shared native("jvm") Nothing exit(Integer code) {
        System.exit(code);
        return nothing;
    }
    
    shared native("jvm") String? namedArgumentValue(String name) {
        if (name.empty) {
            return null;
        }
        value args = Util.args;
        for (i in 0:args.size) {
            value arg = args.get(i);
            if (arg.startsWith("-``name``=") || 
                arg.startsWith("--``name``=")) {
                return arg.substring(arg.indexOf("=")+1).string;
            }
            if (arg.string == "-" + name || 
                arg.string == "--" + name) {
                if (i+1 < args.size) {
                    value next = args.get(i+1);
                    if (!next.startsWith("-")) {
                        return next.string;
                    }
                }
                return null;
            }
        }
        else {
            return null;
        }
    }
    
    shared native("jvm") Boolean namedArgumentPresent(String name) {
        if (name.empty) {
            return false;
        }
        for (arg in Util.args) {
            if (arg.startsWith("-``name``=") || 
                arg.startsWith("--``name``=") || 
                    arg.string == "-" + name || 
                    arg.string == "--" + name) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    shared native("jvm") String? propertyValue(String name) 
            => if (name.empty) then null 
            else System.getProperty(name);

    shared native("jvm") String? environmentVariableValue(String name) 
            => if (name.empty) then null 
            else System.getenv(name);
    
}

shared native("js") object process {
    
    String type;
    shared native("js") String[] arguments;
    
    dynamic {
        type = vmType;
        switch (type)
        case ("node") {
            // parse command line arguments
            dynamic argv = nodeProcess.argv;
            if (argv exists && argv.length >= 2) {
                // Ignore the first two arguments 
                // see https://github.com/ceylon/ceylon.language/issues/503
                arguments 
                    = (0:argv.length)
                        .skip(2)
                        .collect((i) 
                            => let (String arg = argv[i]) 
                                    arg);
            }
            else {
                arguments = [];
            }
        }
        case ("browser") {
            // parse URL parameters
            String search 
                    = window.location.search;
            value bits 
                    = search.rest
                        .replace("+"," ")
                        .split('&'.equals)
                        .sequence();
            if (bits.longerThan(1)
                || !bits[0].empty) {
                arguments = bits;
            }
            else {
                arguments = [];
            }
        }
        else {
            arguments = [];
        }
    }
        
    shared native("js") String? namedArgumentValue(String name) {
        if (name.empty) {
            return null;
        }
        for (i in 0:arguments.size) {
            assert (exists arg = arguments[i]);
            switch (type)
            case ("node") {
                if (arg.startsWith("-``name``=") || 
                    arg.startsWith("--``name``=")) {
                    return arg[arg.indexOf("=")+1...];
                }
                if (arg == "-" + name || 
                    arg == "--" + name) {
                    if (exists next = arguments[i+1], 
                        !next.startsWith("-")) {
                        return next;
                    }
                    return null;
                }
            }
            case ("browser") {
                if (arg.startsWith(name + "=")) {
                    value rest = arg[name.size+1...];
                    dynamic {
                        return decodeURIComponent(rest).string;
                    }
                } else if (arg == name) {
                    return null;
                }
            }
            else {}
        }
        else {
            return null;
        }
    }
    
    shared native("js") Boolean namedArgumentPresent(String name) {
        if (name.empty) {
            return false;
        }
        for (arg in arguments) {
            switch (type)
            case ("node") {
                if (arg.startsWith("-``name``=") || 
                    arg.startsWith("--``name``=") || 
                        arg == "-" + name || 
                        arg == "--" + name) {
                    return true;
                }
            }
            case ("browser") {
                if (arg.startsWith(name + "=") ||
                    arg == name) {
                    return true;
                }
            }
            else {}
        }
        else {
            return false;
        }
    }
    
    shared native("js") String? propertyValue(String name) {
        dynamic {
            switch (type)
            case ("node") {
                switch (name)
                case ("os.name") {
                    if (exists platform = nodeProcess.platform) {
                        return platform;
                    }
                }
                case ("os.arch") {
                    if (exists arch = nodeProcess.arch) {
                        return arch;
                    }
                }
                case ("node.version") {
                    if (exists versions = nodeProcess.versions,
                        exists version = versions.node) {
                        return version;
                    }
                }
                else {}
            }
            case ("browser") {
                switch (name)
                case ("os.name") {
                    if (exists platform = navigator.platform) {
                        return platform;
                    }
                }
                case ("browser.version") {
                    if (exists version = navigator.appVersion) {
                        return version;
                    }
                }
                case ("user.language") {
                    if (exists lang = navigator.language) {
                        return lang;
                    }
                }
                case ("user.locale") {
                    if (exists locale = browser.locale) {
                        return locale;
                    }
                }
                else {}
            }
            else {}
            
            value windows
                    => if (exists os = propertyValue("os.name"))
                    then os.lowercased.contains("win")
                      && !os.lowercased.contains("darwin")
                    else false;
            
            switch (name)
            case ("file.encoding") {
                if (exists document, 
                    exists charset = document.defaultCharset) {
                    return charset;
                }
            }
            case ("line.separator") {
                return windows then "\r\n" else "\n";
            }
            case ("file.separator") {
                return windows then "\\" else "/";
            }
            case ("path.separator") {
                return windows then ";" else ":";
            }
            else {}
            return null;
        }
    }
    
    shared native("js") String? environmentVariableValue(String name) {
        dynamic {
            if (type=="node", 
                exists env = nodeProcess.env,
                exists val = env[name]) {
                return val;
            }
            else {
                return null;
            }
        }
    }
    
    shared native("js") void write(String string); 
    dynamic {
        if (type=="node"
            && nodeProcess.stdout exists) {
            write = (dynamic s) {
                nodeProcess.stdout.write(s.valueOf());
            };
        }
        else if (console.log exists) {
            value buffer = StringBuilder();
            write = (String str) {
                buffer.append(str);
                if (str.endsWith(operatingSystem.newline)) {
                    console.log(buffer.string);
                    buffer.clear();
                }
            };
        }
        else {
            write = (String s) {};
        }
    }
    
    shared native("js") void writeError(String string);
    dynamic {
        if (type=="node"
            && nodeProcess.stderr exists) {
            writeError = (dynamic s) {
                nodeProcess.stderr.write(s.valueOf());
            };
        }
        else if (console.error exists) {
            value buffer = StringBuilder();
            writeError = (String str) {
                buffer.append(str);
                if (str.endsWith(operatingSystem.newline)) {
                    console.error(buffer.string);
                    buffer.clear();
                }
            };
        }
        else {
            writeError = (String s) {};
        }
    }
    
    shared native("js") String? readLine() => null;
    
    shared native("js") Nothing exit(Integer code) {
        dynamic {
            if (type=="node" 
                && nodeProcess.exit exists) {
                nodeProcess.exit(code);
            }
        }
        return nothing;
    }
    
    shared native("js") void flush() {}
    
    shared native("js") void flushError() {}
    
}

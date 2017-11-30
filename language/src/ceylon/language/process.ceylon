/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
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
    
    String? namedArgumentValueInternal(String name) {
        if (name.empty) {
            return null;
        }
        value args = arguments;
        for (i in 0:args.size) {
            assert (exists arg = args[i]);
            if (arg.startsWith("-``name``=")) {
                return arg.removeInitial("-``name``=");
            }
            if (arg.startsWith("--``name``=")) {
                return arg.removeInitial("--``name``=");
            }
            if (arg == "-" + name || 
                arg == "--" + name) {
                return 
                if (exists next = args[i+1],
                    !next.startsWith("-"))
                then next
                else null;
            }
        }
        else {
            return null;
        }
    }
    
    Boolean namedArgumentPresentInternal(String name) {
        if (name.empty) {
            return false;
        }
        for (arg in arguments) {
            if (arg.startsWith("-``name``=") || 
                arg.startsWith("--``name``=") || 
                    arg == "-" + name || 
                    arg == "--" + name) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
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
    
    shared native("jvm") String[] arguments 
            => vmArguments.sequence();
    
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
    
    shared native("jvm") String? namedArgumentValue(String name)
            => namedArgumentValueInternal(name);
    
    shared native("jvm") Boolean namedArgumentPresent(String name)
            => namedArgumentPresentInternal(name);
    
    shared native("jvm") String? propertyValue(String name) 
            => if (name.empty) then null 
            else System.getProperty(name);

    shared native("jvm") String? environmentVariableValue(String name) 
            => if (name.empty) then null 
            else System.getenv(name);
    
}

shared native("js") object process {
    
    String type;
    dynamic {
        type = vmType;
    }
    
    shared native("js") String[] arguments;
    
    switch (type)
    case ("node") {
        // parse command line arguments
        dynamic {
            dynamic argv = nodeProcess.argv;
            if (argv exists) {
                arguments 
                    = (0:argv.length)
                        // ignore the first two arguments 
                        .skip(2)
                        .collect((i) 
                            => let (String arg = argv[i]) 
                                    arg);
            }
            else {
                arguments = [];
            }
        }
    }
    case ("browser") {
        // parse URL parameters
        String search;
        dynamic {
            search = window.location.search;
        }
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
        
    shared native("js") String? namedArgumentValue(String name);
    switch (type)
    case ("node") {
        namedArgumentValue 
                = namedArgumentValueInternal;
    }
    case ("browser") {
        namedArgumentValue = (String name) {
            if (name.empty) {
                return null;
            }
            for (i in 0:arguments.size) {
                assert (exists arg = arguments[i]);
                if (arg.startsWith(name + "=")) {
                    value rest = arg[name.size+1...];
                    dynamic {
                        return decodeURIComponent(rest).string;
                    }
                }
                if (arg == name) {
                    return null;
                }
            }
            else {
                return null;
            }
        };
    }
    else {
        namedArgumentValue(String name) => null;
    }
    
    shared native("js") Boolean namedArgumentPresent(String name);
    switch (type)
    case ("node") {
        namedArgumentPresent 
                = namedArgumentPresentInternal;
    }
    case ("browser") {
        namedArgumentPresent = (String name) {
            if (name.empty) {
                return false;
            }
            for (arg in arguments) {
                if (arg.startsWith(name + "=") ||
                    arg == name) {
                    return true;
                }
            }
            else {
                return false;
            }
        };
    }
    else {
        namedArgumentPresent(String name) => false;
    }
    
    String? platformPropertyValue(String name);
    dynamic {
        switch (type)
        case ("node") {
            platformPropertyValue = (String name) {
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
                return null;
            };
        }
        case ("browser") {
            platformPropertyValue = (String name) {
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
                case ("file.encoding") {
                    if (exists charset = document.characterSet) {
                        return charset;
                    }
                }
                else {}
                return null;
            };
        }
        else {
            platformPropertyValue(String name)
                    => null;
        }
    }
    
    value windows
            => if (exists os = platformPropertyValue("os.name"))
            then os.lowercased.contains("win")
             && !os.lowercased.contains("darwin")
            else false;
    
    shared native("js") String? propertyValue(String name)
            => switch (name)
            case ("line.separator") (windows then "\r\n" else "\n")
            case ("file.separator") (windows then "\\" else "/")
            case ("path.separator") (windows then ";" else ":")
            else platformPropertyValue(name);
    
    shared native("js") String? environmentVariableValue(String name);
    if (type=="node") {
        environmentVariableValue = (String name) {
            dynamic {
                return if (exists env = nodeProcess.env)
                    then env[name] else null;
            }
        };
    }
    else {
        environmentVariableValue(String name) 
                => null;
    }
    
    shared native("js") void write(String string); 
    if (type=="node") {
        write = (dynamic s) {
            dynamic {
                if (exists stdout = nodeProcess.stdout) {
                    stdout.write(s.valueOf());
                }
            }
        };
    }
    else {
        value buffer = StringBuilder();
        write = (String str) {
            buffer.append(str);
            if (str.endsWith(operatingSystem.newline)) {
                dynamic {
                    if (exists log = console.log) {
                        log(buffer.string);
                    }
                }
                buffer.clear();
            }
        };
    }
    
    shared native("js") void writeError(String string);
    if (type=="node") {
        writeError = (dynamic s) {
            dynamic {
                if (exists stderr = nodeProcess.stderr) {
                    stderr.write(s.valueOf());
                }
            }
        };
    }
    else {
        value buffer = StringBuilder();
        writeError = (String str) {
            buffer.append(str);
            if (str.endsWith(operatingSystem.newline)) {
                dynamic {
                    if (exists error = console.error) {
                        error(buffer.string);
                    }
                }
                buffer.clear();
            }
        };
    }
    
    shared native("js") Nothing exit(Integer code);
    if (type=="node") { 
        exit = (Integer code) {
            dynamic {
                if (exists exit = nodeProcess.exit) {
                    nodeProcess.exit(code);
                }
            }
            return nothing;
        };
    }
    else {
        exit(Integer code) => nothing;
    }
    
    shared native("js") String? readLine() => null;
    
    shared native("js") void flush() {}
    
    shared native("js") void flushError() {}
    
}

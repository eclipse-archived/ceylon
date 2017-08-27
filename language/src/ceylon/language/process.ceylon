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
        return null;
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
    
    shared native("js") String[] arguments {
        dynamic {
            return (0:_argv.length).collect(Object.string);
        }
    }
    
    shared native("js") String? namedArgumentValue(String name) {
        dynamic {
            return if (name.empty) then null 
                else _namedArgs[name]?.string;
        }
    }
    
    shared native("js") Boolean namedArgumentPresent(String name) {
        dynamic {
            return if (name.empty) then false 
                else _namedArgs[name] exists;
        }
    }
    
    shared native("js") String? propertyValue(String name) {
        dynamic {
            return if (name.empty) then null 
                else _properties[name]?.string;
        }
    }
    
    value node = propertyValue("node.version") exists;
    
    shared native("js") String? environmentVariableValue(String name) {
        dynamic {
            return if (node, _process.env exists)
                then _process.env[name]?.string else null;
        }
    }
    
    shared native("js") void write(String string); 
    dynamic {
        if (node && _process.stdout exists) {
            write = (dynamic s) {
                _process.stdout.write(s.valueOf());
            };
        }
        else if (console exists && console.log exists) {
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
        if (node && _process.stderr exists) {
            writeError = (dynamic s) {
                _process.stderr.write(s.valueOf());
            };
        }
        else if (console exists && console.error exists) {
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
            writeError = write;
        }
    }
    
    shared native("js") String? readLine() => null;
    
    shared native("js") Nothing exit(Integer code) {
        dynamic {
            if (node && _process.exit exists) {
                _process.exit(code);
            }
        }
        return nothing;
    }
    
    shared native("js") void flush() {}
    
    shared native("js") void flushError() {}
    
}

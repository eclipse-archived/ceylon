
"Represents the current process (instance of the virtual
 machine)."
by ("Gavin", "Tako")
see (`value language`, `value runtime`, `value system`,
    `value operatingSystem`)
tagged("Environment")
shared native object process {
    
    "The command line arguments to the virtual machine."
    shared native String[] arguments;
    
    "Determine if an argument of form `-name` or `--name` 
     was specified among the command line arguments to 
     the virtual machine."
    shared native Boolean namedArgumentPresent(String name)
            => arguments.any((element) => element.startsWith("-" + name + "=") ||
            element.startsWith("--" + name + "=") ||
            element.equals("-" + name) || 
            element.equals("--" + name));

    "The value of the first argument of form `-name=value`, 
     `--name=value`, or `-name value` specified among the 
     command line arguments to the virtual machine, if
     any."
    shared native String? namedArgumentValue(String name) {
        if (name.empty) {
            return null;
        }
        value idx = arguments.firstIndexWhere((String arg) =>
            arg.startsWith("-" + name + "=") ||
                arg.startsWith("--" + name + "=") ||
                arg == ("-" + name) ||
                arg == ("--" + name));
        if (exists idx) {
            value arg = arguments[idx] else "";
            if (arg.startsWith("-" + name + "=") || 
                arg.startsWith("--" + name + "=")) {
                return arg[(arg.firstOccurrence('=') else -1) +1...];
            }
            if ((idx + 1) < arguments.size &&
                (arg == ("-" + name) || 
                arg == ("--" + name))) {
                value val = arguments[idx + 1] else "";
                return if (val.startsWith("-")) then null else val;
            }
        }
        return null;
    }

    "The value of the given system property of the virtual
     machine, if any."
    shared native String? propertyValue(String name);

    "The map of all the property values"
    since("1.3.2")
    shared native [<String->String>*] properties;
    
    "The value of the given environment variable defined 
     for the current virtual machine process."
    since("1.2.0")
    shared native String? environmentVariableValue(String name);
    
    "The map of all the property values"
    since("1.3.2")
    shared native [<String->String>*] environmentVariables;
    
    "Print a string to the standard output of the virtual
     machine process."
    shared native void write(String string);
    
    "Print a line to the standard output of the virtual 
     machine process."
    see (`function print`)
    shared void writeLine(String line="") {
        write(line);
        write(operatingSystem.newline);
    }
    
    "Flush the standard output of the virtual machine 
     process. Attention: in some environments like
     browsers this might mean that an extra newline
     will be output!"
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
    
    "Flush the standard error of the  virtual machine
     process. Attention: in some environments like
     browsers this might mean that an extra newline
     will be output!"
    shared native void flushError();
    
    "Read a line of input text from the standard input of 
     the virtual machine process. Returns a line of text 
     after removal of line-termination characters, or `null`
     to indicate the standard input has been closed."
    shared native String? readLine();
    
    "Force the virtual machine to terminate with the given
     exit code."
    shared native Nothing exit(Integer code);
    
    string => "process";
    
}

shared native("jvm") object process {
    import java.io { BufferedReader, InputStreamReader, IOException }
    import java.lang { JString=String, System { sysExit=exit, sysIn=\iin, sysOut=\iout, sysErr=err } }
    import com.redhat.ceylon.compiler.java { Util { sequentialWrapperBoxed } }
    import com.redhat.ceylon.compiler.java.language { ObjectArray }
    
    variable BufferedReader? stdinReader = null;
    variable String[] programArguments = [];
    
    shared void setupArguments(ObjectArray<JString> arguments) {
        programArguments = sequentialWrapperBoxed(arguments);
    }
    
    shared native("jvm") String[] arguments => programArguments;
    
    shared native("jvm") String? propertyValue(String name) {
        return System.getProperty(name);
    }
    
    shared native("jvm") [<String->String>*] properties {
        variable [<String->String>*] result = [];
        value iter = System.properties.stringPropertyNames().iterator();
        while (iter.hasNext()) {
            String name = iter.next().string;
            String val = System.getProperty(name).string;
            result = result.append([name->val]);
        }
        return result;
    }
    
    shared native("jvm") String? environmentVariableValue(String name) {
        return System.getenv(name);
    }
    
    shared native("jvm") [<String->String>*] environmentVariables {
        variable [<String->String>*] result = [];
        value iter = System.getenv().entrySet().iterator();
        while (iter.hasNext()) {
            value entry = iter.next();
            String name = entry.key.string;
            String val = entry.\ivalue.string;
            result = result.append([name->val]);
        }
        return result;
    }
    
    shared native("jvm") void write(String string) {
        sysOut.print(string);
    }
    
    shared native("jvm") void flush() {
        sysOut.flush();
    }
    
    shared native("jvm") void writeError(String string) {
        sysErr.print(string);
    }
    
    shared native("jvm") void flushError() {
        sysErr.flush();
    }
    
    shared native("jvm") String? readLine() {
        try {
            if (stdinReader exists) {
                stdinReader = BufferedReader(InputStreamReader(sysIn));
            }
            return stdinReader?.readLine();
        } 
        catch (IOException e) {
            throw Exception("could not read line from standard input", e);
        }
    }
    
    suppressWarnings("expressionTypeNothing")
    shared native("jvm") Nothing exit(Integer code) {
        sysExit(code);
        return nothing;
    }
    
}

shared native("js") object process {
    variable String outbuf = "";
    
    shared native("js") String[] arguments {
        dynamic {
            return processargv_;
        }
    }
    
    shared native("js") String? propertyValue(String name) {
        dynamic {
            return properties_[name] else null;
        }
    }
    
    shared native("js") [<String->String>*] properties {
        dynamic {
            variable [<String->String>*] result = [];
            dynamic names = \iObject.getOwnPropertyNames(properties_);
            for (String name in names) {
                String val = properties_[name];
                result = result.append([name->val]);
            }
            return result;
        }
    }
    
    shared native("js") String? environmentVariableValue(String name) {
        dynamic {
            if (exists nodeprocess_) {
                return nodeprocess_.env[name] else null;
            } else {
                return null;
            }
        }
    }
    
    shared native("js") [<String->String>*] environmentVariables {
        dynamic {
            variable [<String->String>*] result = [];
            if (exists nodeprocess_) {
                dynamic names = \iObject.getOwnPropertyNames(nodeprocess_.env);
                for (String name in names) {
                    String val = nodeprocess_.env[name];
                    result = result.append([name->val]);
                }
            }
            return result;
        }
    }
    
    shared native("js") void write(String string) {
        dynamic {
            if (exists nodeprocess_) {
                nodeprocess_.stdout.write(string);
            } else if (exists console) {
                value p = string.lastOccurrence('\n');
                if (exists p) {
                    value toprint = string[0:p];
                    value tobuffer = string[p+1...];
                    console.log(toprint);
                    outbuf += tobuffer;
                } else {
                    outbuf += string;
                }
            }
        }
    }
    
    shared native("js") void flush() {
        if (!outbuf.empty) {
            write(outbuf);
            outbuf = "";
        }
    }
    
    shared native("js") void writeError(String string) {
        dynamic {
            if (exists nodeprocess_) {
                nodeprocess_.stderr.write(string);
            } else if (exists console) {
                write(string);
            }
        }
    }
    
    shared native("js") void flushError() {
        flush();
    }
    
    shared native("js") String? readLine() {
        dynamic {
            if (exists window, exists p=window.prompt) {
                return p("Input");
            } else {
                throw Exception("process.readLine not supported on this platform");
            }
        }
    }
    
    suppressWarnings("expressionTypeNothing")
    shared native("js") Nothing exit(Integer code) {
        dynamic {
            if (exists nodeprocess_) {
                nodeprocess_.exit(code);
            }
        }
        return nothing;
    }
    
}



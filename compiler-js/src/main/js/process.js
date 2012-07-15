// implementation of object "process" in ceylon.language

function IdentifiableObject(x){}//IGNORE
function inheritProto(x,y){}//IGNORE
function String$(x){}//IGNORE
function Integer$(x){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports,$empty;//IGNORE

function processClass() {
    var proc = new processClass.$$;
    IdentifiableObject(proc);
    return proc;
}
initTypeProto(processClass, "ceylon.language.process", IdentifiableObject);
var process$proto = processClass.$$.prototype;

var argv = $empty;
var namedArgs = {};
if ((typeof process !== "undefined") && (process.argv !== undefined)) {
    // parse command line arguments
    if (process.argv.length > 2) {
        var args = process.argv.slice(2);
        var argStrings = new Array(args.length);
        for (var i in args) { argStrings[i] = String$(args[i]); }
        argv = ArraySequence(argStrings);
        
        for (var i=0; i<args.length; ++i) {
            var arg = args[i];
            if (arg.charAt(0) == '-') {
                var pos = 1;
                if (arg.charAt(1) == '-') { pos = 2; }
                arg = arg.substr(pos);
                pos = arg.indexOf('=');
                if (pos >= 0) {
                    namedArgs[arg.substr(0, pos)] = String$(arg.substr(pos+1));
                } else {
                    var value = args[i+1];
                    if ((value !== undefined) && (value.charAt(0) != '-')) {
                        namedArgs[arg] = String$(value);
                        ++i;
                    } else {
                        namedArgs[arg] = null;
                    }
                }
            }
        }
    }
} else if (typeof window !== "undefined") {
    // parse URL parameters
    var parts = window.location.search.substr(1).replace('+', ' ').split('&');
    if (parts.length > 0) {
        var argStrings = new Array(parts.length);
        for (i in parts) { argStrings[i] = String$(parts[i]); }
        argv = ArraySequence(argStrings);
        
        for (i in parts) {
            var part = parts[i];
            var pos = part.indexOf('=');
            if (pos >= 0) {
                var value = decodeURIComponent(part.substr(pos+1));
                namedArgs[part.substr(0, pos)] = String$(value);
            } else {
                namedArgs[part] = null;
            }
        }
    }
}
process$proto.getArguments = function() { return argv; }
process$proto.namedArgumentPresent = function(name) {
    return (name.value in namedArgs);
}
process$proto.namedArgumentValue = function(name) {
    var value = namedArgs[name.value];
    return (value !== undefined) ? value : null;
}

var properties = {};
if (typeof navigator !== "undefined") {
    if (navigator.language !== undefined) {
        properties["user.language"] = String$(navigator.language);
    }
    if (navigator.platform !== undefined) {
        properties["os.name"] = String$(navigator.platform);
    }
}
if (typeof process !== "undefined") {
    if (process.platform !== undefined) {
        properties["os.name"] = String$(process.platform);
    }
    if (process.arch !== undefined) {
        properties["os.arch"] = String$(process.arch);
    }
}
if (typeof document !== "undefined") {
    if (document.defaultCharset !== undefined) {
        properties["file.encoding"] = String$(document.defaultCharset);
    }
}

var linesep = String$('\n', 1);
var filesep = String$('/', 1);
var pathsep = String$(':', 1);
var osname = properties["os.name"];
if ((osname !== undefined) && (osname.value.search(/windows/i) >= 0)) {
    linesep = String$("\r\n", 2);
    filesep = String$('\\', 1);
    pathsep = String$(';', 1);
}
properties["line.separator"] = linesep;
properties["file.separator"] = filesep;
properties["path.separator"] = pathsep;

process$proto.propertyValue = function(name) {
    var value = properties[name.value];
    return (value !== undefined) ? value : null;
}

process$proto.getNewline = function() { return linesep; }

if ((typeof process !== "undefined") && (process.stdout !== undefined)) {
    process$proto.write = function(string) {
        process.stdout.write(string.value);
    }
    process$proto.writeLine = function(line) {
        this.write(line);
        this.write(linesep);
    }
} else if ((typeof console !== "undefined") && (console.log !== undefined)) {
    process$proto.writeLine = function(line) {
        console.log(line.value);
    }
    process$proto.write = process$proto.writeLine;
} else {
    process$proto.write = function() {};
    process$proto.writeLine = function() {};    
}

if ((typeof process !== "undefined") && (process.stderr !== undefined)) {
    process$proto.writeError = function(string) {
        process.stderr.write(string.value);
    }
    process$proto.writeErrorLine = function(line) {
        this.writeError(line);
        this.writeError(linesep);
    }
} else if ((typeof console !== "undefined") && (console.error !== undefined)) {
    process$proto.writeErrorLine = function(line) {
        console.error(line.value);
    }
    process$proto.writeError = process$proto.writeErrorLine;
} else {
    process$proto.writeError = process$proto.write;
    process$proto.writeErrorLine = process$proto.writeLine;    
}

process$proto.readLine = function() {
    return String$("", 0);//TODO
}

process$proto.getMilliseconds = function() {
    return Integer(Date.now());
}
process$proto.getNanoseconds = function() {
    return Integer(Date.now()*1000000);
}

if ((typeof process !== "undefined") && (process.exit !== undefined)) {
    process$proto.exit = function(code) {
        process.exit(code.value);
    }
} else {
    process$proto.exit = function() {}
}

var processString = String$("process", 7);
process$proto.getString = function() {
    return processString;
}

var process$ = processClass();
function getProcess() { return process$; }
exports.getProcess=getProcess;

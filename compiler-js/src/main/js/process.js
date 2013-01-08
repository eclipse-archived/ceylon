// implementation of object "process" in ceylon.language

function Basic(x){}//IGNORE
function inheritProto(x,y){}//IGNORE
function String$(x){}//IGNORE
function ArraySequence(x){}//IGNORE
var exports,empty;//IGNORE

function languageClass() {
    var lang = new languageClass.$$;
    Basic(lang);
    return lang;
}
initTypeProto(languageClass, "ceylon.language::language", Basic);
var lang$proto=languageClass.$$.prototype;
lang$proto.getVersion=function() {
    return String$("0.5",3);
}
lang$proto.getMajorVersion=function() { return 0; }
lang$proto.getMinorVersion=function() { return 5; }
lang$proto.getReleaseVersion=function() { return 0; }
lang$proto.getVersionName=function() { return String$("Analytical Engine",11); }
lang$proto.getMajorVersionBinary=function() { return 3; }
lang$proto.getMinorVersionBinary=function() { return 0; }
var languageString = String$("language", 7);
lang$proto.getString = function() {
    return languageString;
}

var language$ = languageClass();
function getLanguage() { return language$; }
exports.getLanguage=getLanguage;

function processClass() {
    var proc = new processClass.$$;
    Basic(proc);
    return proc;
}
initTypeProto(processClass, "ceylon.language::process", Basic);
var process$proto = processClass.$$.prototype;

var argv = empty;
var namedArgs = {};
if ((typeof process !== "undefined") && (process.argv !== undefined)) {
    // parse command line arguments
    if (process.argv.length > 1) {
        var args = process.argv.slice(1);
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
            args[i] = String$(args[i]);
        }
        argv = ArraySequence(args);
    }
} else if (typeof window !== "undefined") {
    // parse URL parameters
    var parts = window.location.search.substr(1).replace('+', ' ').split('&');
    if ((parts.length > 1) || ((parts.length > 0) && (parts[0].length > 0))) {
        var argStrings = new Array(parts.length);
        //can't do "for (i in parts)" anymore because of the added stuff to arrays
        var i;
        for (i=0; i<parts.length; i++) { argStrings[i] = String$(parts[i]); }
        argv = ArraySequence(argStrings);
        
        for (i=0; i < parts.length; i++) {
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
    return (name in namedArgs);
}
process$proto.namedArgumentValue = function(name) {
    var value = namedArgs[name];
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
if ((osname !== undefined) && (osname.search(/windows/i) >= 0)) {
    linesep = String$("\r\n", 2);
    filesep = String$('\\', 1);
    pathsep = String$(';', 1);
}
properties["line.separator"] = linesep;
properties["file.separator"] = filesep;
properties["path.separator"] = pathsep;

process$proto.propertyValue = function(name) {
    var value = properties[name];
    return (value !== undefined) ? value : null;
}

process$proto.getNewline = function() { return linesep; }

if ((typeof process !== "undefined") && (process.stdout !== undefined)) {
    process$proto.write = function(string) {
        process.stdout.write(string.valueOf());
    }
    process$proto.writeLine = function(line) {
        this.write(line.valueOf());
        this.write(linesep.valueOf());
    }
} else if ((typeof console !== "undefined") && (console.log !== undefined)) {
    process$proto.writeLine = function(line) {
        console.log(line.valueOf());
    }
    process$proto.write = process$proto.writeLine;
} else {
    process$proto.write = function() {};
    process$proto.writeLine = function() {};    
}

if ((typeof process !== "undefined") && (process.stderr !== undefined)) {
    process$proto.writeError = function(string) {
        process.stderr.write(string.valueOf());
    }
    process$proto.writeErrorLine = function(line) {
        this.writeError(line.valueOf());
        this.writeError(linesep.valueOf());
    }
} else if ((typeof console !== "undefined") && (console.error !== undefined)) {
    process$proto.writeErrorLine = function(line) {
        console.error(line.valueOf());
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
    return Date.now();
}
process$proto.getNanoseconds = function() {
    return Date.now()*1000000;
}

if ((typeof process !== "undefined") && (process.exit !== undefined)) {
    process$proto.exit = function(code) {
        process.exit(code);
    }
} else {
    process$proto.exit = function() {}
}

var processString = String$("process", 7);
process$proto.getString = function() {
    return processString;
}
process$proto.getVm = function() {
    if (typeof process !== "undefined" && process.execPath && process.execPath.match(/node(\.exe)?$/)) {
        return String$("node.js", 7);
    } else if (typeof window === 'object') {
        return String$("Browser", 7);
    }
    return String$("Unknown JavaScript environment", 30);
}
process$proto.getVmVersion = function() {
    if (typeof process !== "undefined" && typeof process.version === 'string') {
        return String$(process.version);
    }
    return String$("Unknown");
}
process$proto.getOs = function() {
    if (typeof process !== "undefined" && typeof process.platform === 'string') {
        return String$(process.platform);
    }
    return String$("Unknown");
}
process$proto.getOsVersion = function() {
    return String$("Unknown");
}

var process$ = processClass();
function getProcess() { return process$; }
exports.getProcess=getProcess;

// implementation of object "process" in ceylon.language
function languageClass() {
    var lang = new languageClass.$$;
    Basic(lang);
    return lang;
}
languageClass.$$metamodel$$={$nm:'languageClass',$mt:'cls',$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','language']};
initTypeProto(languageClass, "ceylon.language::language", $init$Basic());
var lang$proto=languageClass.$$.prototype;
defineAttr(lang$proto, 'version', function() {
    return String$("0.6.1",3);
},undefined,{$t:{t:String$}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','version']});
defineAttr(lang$proto, 'majorVersion', function(){ return 0; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','majorVersion']});
defineAttr(lang$proto, 'minorVersion', function(){ return 6; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','minorVersion']});
defineAttr(lang$proto, 'releaseVersion', function(){ return 0; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','releaseVersion']});
defineAttr(lang$proto, 'versionName', function(){ return String$("Transmogrifier",14); },undefined,
  {$t:{t:String$}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','versionName']});
defineAttr(lang$proto, 'majorVersionBinary', function(){ return 5; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','majorVersionBinary']});
defineAttr(lang$proto, 'minorVersionBinary', function(){ return 0; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','language','$at','minorVersionBinary']});
var languageString = String$("language", 8);
defineAttr(lang$proto, 'string', function() {
    return languageString;
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});

var language$ = languageClass();
function getLanguage() { return language$; }
exports.getLanguage=getLanguage;

function processClass() {
    var proc = new processClass.$$;
    Basic(proc);
    return proc;
}
processClass.$$metamodel$$={$nm:'processClass',$mt:'cls',$ps:[],$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','process']};
initTypeProto(processClass, "ceylon.language::process", $init$Basic());
var process$proto = processClass.$$.prototype;

var argv = getEmpty();
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
        argv = ArraySequence(args, {Element:{t:String$}});
    }
} else if (typeof window !== "undefined") {
    // parse URL parameters
    var parts = window.location.search.substr(1).replace('+', ' ').split('&');
    if ((parts.length > 1) || ((parts.length > 0) && (parts[0].length > 0))) {
        var argStrings = new Array(parts.length);
        //can't do "for (i in parts)" anymore because of the added stuff to arrays
        var i;
        for (i=0; i<parts.length; i++) { argStrings[i] = String$(parts[i]); }
        argv = ArraySequence(argStrings, {Element:{t:String$}});
        
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
defineAttr(process$proto, 'arguments', function(){ return argv; },undefined,
  {$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','arguments']});
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
    if (navigator.userLanguage||navigator.browserLanguage||navigator.language) {
        properties["user.locale"]=String$(navigator.userLanguage||navigator.browserLanguage||navigator.language);
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

defineAttr(process$proto, 'newline', function(){ return linesep; },undefined,
  {$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','newline']});

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

defineAttr(process$proto, 'milliseconds', function() {
    return Date.now();
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','milliseconds']});
defineAttr(process$proto, 'nanoseconds', function() {
    return Date.now()*1000000;
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','nanoseconds']});

if ((typeof process !== "undefined") && (process.exit !== undefined)) {
    process$proto.exit = function(code) {
        process.exit(code);
    }
} else {
    process$proto.exit = function() {}
}

var processString = String$("process", 7);
defineAttr(process$proto, 'string', function() {
    return processString;
},undefined,{$t:{t:String$},$cont:process$proto,$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});
defineAttr(process$proto, 'vm', function() {
    if (typeof process !== "undefined" && process.execPath && process.execPath.match(/node(\.exe)?$/)) {
        return String$("node.js", 7);
    } else if (typeof window === 'object') {
        return String$("Browser", 7);
    }
    return String$("Unknown JavaScript environment", 30);
},undefined,{$t:{t:String$},$cont:process$proto,$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','vm']});
defineAttr(process$proto, 'vmVersion', function() {
    if (typeof process !== "undefined" && typeof process.version === 'string') {
        return String$(process.version);
    }
    return String$("Unknown");
},undefined,{$t:{t:String$},$cont:process$proto,$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','vmVersion']});
defineAttr(process$proto, 'os',function() {
    if (typeof process !== "undefined" && typeof process.platform === 'string') {
        return String$(process.platform);
    }
    return String$("Unknown");
},undefined,{$t:{t:String$},$cont:process$proto,$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','os']});
defineAttr(process$proto, 'osVersion', function() {
    return String$("Unknown");
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','osVersion']});
defineAttr(process$proto, 'timezoneOffset', function(){
  return new Date().getTimezoneOffset()*60000;
},undefined,{$t:{t:Integer}, $cont:process$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','timezoneOffset']});
defineAttr(process$proto, 'locale', function(){
  return properties["user.locale"] || String$("Unknown",7);
},undefined,{$t:{t:String$}, $cont:process$proto, $an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','process','$at','locale']});

var process$ = processClass();
function getProcess() { return process$; }
exports.getProcess=getProcess;

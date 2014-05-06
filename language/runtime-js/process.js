// implementation of object "process" in ceylon.language
function languageClass() {
    var lang = new languageClass.$$;
    Basic(lang);
    return lang;
}
languageClass.$crtmm$={$nm:'languageClass',$mt:'c',$ps:[],$an:function(){return[shared()];},mod:$CCMM$,d:['$','language']};
initTypeProto(languageClass, "ceylon.language::language", $init$Basic());
var lang$proto=languageClass.$$.prototype;
atr$(lang$proto, 'version', function() {
    return String$("1.1.0",3);
},undefined,{$t:{t:String$}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','version']});
atr$(lang$proto, 'majorVersion', function(){ return 1; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','majorVersion']});
atr$(lang$proto, 'minorVersion', function(){ return 1; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','minorVersion']});
atr$(lang$proto, 'releaseVersion', function(){ return 0; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','releaseVersion']});
atr$(lang$proto, 'versionName', function(){ return String$("Unflatzburged",13); },undefined,
  {$t:{t:String$}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','versionName']});
atr$(lang$proto, 'majorVersionBinary', function(){ return 7; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','majorVersionBinary']});
atr$(lang$proto, 'minorVersionBinary', function(){ return 0; },undefined,
  {$t:{t:Integer}, $cont:lang$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','language','$at','minorVersionBinary']});
var languageString = String$("language", 8);
atr$(lang$proto, 'string', function() {
    return languageString;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});

var language$ = languageClass();
function getLanguage() { return language$; }
ex$.getLanguage=getLanguage;
ex$.$prop$getLanguage={get:getLanguage,$crtmm$:function(){
  return {mod:$CCMM$,d:['$','language'],$t:{t:languageClass}};
}};
function processClass() {
    var proc = new processClass.$$;
    Basic(proc);
    return proc;
}
processClass.$crtmm$={$nm:'processClass',$mt:'c',$ps:[],$an:function(){return[shared()];},mod:$CCMM$,d:['$','process']};
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
        argv = ArraySequence(args, {Element$Iterable:{t:String$}});
    }
} else if (typeof window !== "undefined") {
    // parse URL parameters
    var parts = window.location.search.substr(1).replace('+', ' ').split('&');
    if ((parts.length > 1) || ((parts.length > 0) && (parts[0].length > 0))) {
        var argStrings = new Array(parts.length);
        //can't do "for (i in parts)" anymore because of the added stuff to arrays
        var i;
        for (i=0; i<parts.length; i++) { argStrings[i] = String$(parts[i]); }
        argv = ArraySequence(argStrings, {Element$Iterable:{t:String$}});
        
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
atr$(process$proto, 'arguments', function(){ return argv; },undefined,
  {$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','process','$at','arguments']});
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

if ((typeof process !== "undefined") && (process.stdout !== undefined)) {
    process$proto.write = function(string) {
        if(string)process.stdout.write(string.valueOf());
    }
    process$proto.writeLine = function(line) {
        if(line)process$proto.write(line.valueOf());
        process$proto.write(linesep.valueOf());
    }
} else if ((typeof console !== "undefined") && (console.log !== undefined)) {
    process$proto.writeLine = function(line) {
        console.log(line?line.valueOf():'');
    }
    process$proto.write = process$proto.writeLine;
} else {
    process$proto.write = function() {};
    process$proto.writeLine = function() {};    
}

if ((typeof process !== "undefined") && (process.stderr !== undefined)) {
    process$proto.writeError = function(string) {
        if(string)process.stderr.write(string.valueOf());
    }
    process$proto.writeErrorLine = function(line) {
        if(line)process$proto.writeError(line.valueOf());
        process$proto.writeError(linesep.valueOf());
    }
} else if ((typeof console !== "undefined") && (console.error !== undefined)) {
    process$proto.writeErrorLine = function(line) {
        console.error(line?line.valueOf():'');
    }
    process$proto.writeError = process$proto.writeErrorLine;
} else {
    process$proto.writeError = process$proto.write;
    process$proto.writeErrorLine = process$proto.writeLine;    
}

process$proto.flush = function(){}
process$proto.flushError=function(){}
process$proto.readLine = function() {
  this.writeLine("process.readLine not supported on this platform.");
  return "";
}

if ((typeof process !== "undefined") && (process.exit !== undefined)) {
    process$proto.exit = function(code) {
        process.exit(code);
    }
} else {
    process$proto.exit = function() {}
}

var processString = String$("process", 7);
atr$(process$proto, 'string', function() {
    return processString;
},undefined,{$t:{t:String$},$cont:process$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});


var process$ = processClass();
function getProcess() { return process$; }
getProcess.$crtmm$=function(){return{
  mod:$CCMM$,$t:{t:processClass},$an:function(){return[shared(),$_native()];},d:['$','process']};
}
ex$.getProcess=getProcess;
ex$.$prop$getProcess={get:getProcess,$crtmm$:function(){return{mod:$CCMM$,d:['$','process'],$t:{t:processClass}
}}};
getProcess.$crtmm$=ex$.$prop$getProcess.$crtmm$;
// system

function systemClass() {
    var proc = new systemClass.$$;
    Basic(proc);
    return proc;
}
systemClass.$crtmm$={$nm:'systemClass',$mt:'c',$ps:[],$an:function(){return[shared()];},mod:$CCMM$,d:['$','system']};
initTypeProto(systemClass, "ceylon.language::system", $init$Basic());
var system$proto = systemClass.$$.prototype;

atr$(system$proto, 'milliseconds', function() {
    return Date.now();
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','system','$at','milliseconds']});
atr$(system$proto, 'nanoseconds', function() {
    return Date.now()*1000000;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','system','$at','nanoseconds']});
atr$(system$proto, 'timezoneOffset', function(){
  return new Date().getTimezoneOffset()*60000;
},undefined,{$t:{t:Integer}, $cont:system$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','system','$at','timezoneOffset']});
atr$(system$proto, 'locale', function(){
  return properties["user.locale"] || String$("Unknown",7);
},undefined,{$t:{t:String$}, $cont:system$proto, $an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','system','$at','locale']});
var systemString = String$("system", 7);
atr$(system$proto, 'string', function() {
    return systemString;
},undefined,{$t:{t:String$},$cont:system$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});

var system$ = systemClass();
function getSystem() { return system$; }
ex$.getSystem=getSystem;
ex$.$prop$getSystem={get:getSystem,$crtmm$:function(){return{
  mod:$CCMM$,d:['$','system'],$t:{t:systemClass}
};}};
getSystem.$crtmm$=ex$.$prop$getSystem.$crtmm$;
// runtime

function runtimeClass() {
    var proc = new runtimeClass.$$;
    Basic(proc);
    return proc;
}
runtimeClass.$crtmm$={$nm:'runtimeClass',$mt:'c',$ps:[],$an:function(){return[shared()];},mod:$CCMM$,d:['$','runtime']};
initTypeProto(runtimeClass, "ceylon.language::runtime", $init$Basic());
var runtime$proto = runtimeClass.$$.prototype;

atr$(runtime$proto, 'name', function() {
    if (typeof process !== "undefined" && process.execPath && process.execPath.match(/node(js)?(\.exe)?$/)) {
        return String$("node.js", 7);
    } else if (typeof window === 'object') {
        return String$("Browser", 7);
    }
    return String$("Unknown JavaScript environment", 30);
},undefined,{$t:{t:String$},$cont:runtime$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','name']});
atr$(runtime$proto, 'version', function() {
    if (typeof process !== "undefined" && typeof process.version === 'string') {
        return String$(process.version);
    }
    return String$("Unknown");
},undefined,{$t:{t:String$},$cont:runtime$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','version']});
atr$(runtime$proto, 'integerSize', function() {
    return 53;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','integerSize']});
atr$(runtime$proto, 'integerAddressableSize', function() {
    return 32;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','integerAddressableSize']});
var $minIntegerValue = Integer(-9007199254740991); //-(2^53-1)
atr$(runtime$proto, 'minIntegerValue', function() {
    return $minIntegerValue;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','minIntegerValue']});
var $maxIntegerValue = Integer(9007199254740989); //(2^53-3) => ((2^53)-2 is NaN)
atr$(runtime$proto, 'maxIntegerValue', function() {
    return $maxIntegerValue;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','maxIntegerValue']});
var $maxArraySize = Integer(4294967295); //(2^32-1)
atr$(runtime$proto, 'maxArraySize', function() {
    return $maxArraySize;
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','runtime','$at','maxArraySize']});
var runtimeString = String$("runtime", 7);
atr$(runtime$proto, 'string', function() {
    return runtimeString;
},undefined,{$t:{t:String$},$cont:runtime$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});

var runtime$ = runtimeClass();
function getRuntime() { return runtime$; }
ex$.getRuntime=getRuntime;
ex$.$prop$getRuntime={get:getRuntime,$crtmm$:function(){
  return{mod:$CCMM$,d:['$','runtime'],$t:{t:runtimeClass}};}};
getRuntime.$crtmm$=ex$.$prop$getRuntime.$crtmm$;
// operatingSystem

function operatingSystemClass() {
    var proc = new operatingSystemClass.$$;
    Basic(proc);
    return proc;
}
operatingSystemClass.$crtmm$={$nm:'operatingSystemClass',$mt:'c',$ps:[],$an:function(){return[shared()];},mod:$CCMM$,d:['$','operatingSystem']};
initTypeProto(operatingSystemClass, "ceylon.language::operatingSystem", $init$Basic());
var operatingSystem$proto = operatingSystemClass.$$.prototype;

atr$(operatingSystem$proto, 'name',function() {
    if (typeof process !== "undefined" && typeof process.platform === 'string') {
        return String$(process.platform);
    }
    return String$("Unknown");
},undefined,{$t:{t:String$},$cont:operatingSystem$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','operatingSystem','$at','name']});
atr$(operatingSystem$proto, 'version', function() {
    return String$("Unknown");
},undefined,{$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','operatingSystem','$at','version']});
atr$(operatingSystem$proto, 'newline', function(){ return linesep; },undefined,
  {$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','operatingSystem','$at','newline']});
atr$(operatingSystem$proto, 'fileSeparator', function(){ return filesep; },undefined,
  {$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','operatingSystem','$at','fileSeparator']});
atr$(operatingSystem$proto, 'pathSeparator', function(){ return pathsep; },undefined,
  {$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','operatingSystem','$at','pathSeparator']});
var operatingSystemString = String$("operatingSystem", 7);
atr$(operatingSystem$proto, 'string', function() {
    return operatingSystemString;
},undefined,{$t:{t:String$},$cont:operatingSystem$proto,$an:function(){return[shared(),actual()]},mod:$CCMM$,d:['$','Object','$at','string']});

var operatingSystem$ = operatingSystemClass();
function getOperatingSystem() { return operatingSystem$; }
ex$.getOperatingSystem=getOperatingSystem;
ex$.$prop$getOperatingSystem={get:getOperatingSystem,$crtmm$:function(){
  return {mod:$CCMM$,d:['$','operatingSystem'],$t:{t:operatingSystemClass}};
}};
getOperatingSystem.$crtmm$=ex$.$prop$getOperatingSystem.$crtmm$;

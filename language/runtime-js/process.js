//Some native stuff for process, runtime, operatingSystem, language
var properties = {};
var properties_ = properties;
var nodeprocess_;
if (run$isBrowser()) {
    if (navigator.language !== undefined) {
        properties["user.language"] = $_String(navigator.language);
    }
    if (navigator.platform !== undefined) {
        properties["os.name"] = $_String(navigator.platform);
    }
    if (navigator.languages||navigator.userLanguage||navigator.browserLanguage||navigator.language) {
        // work around a cordova bug https://github.com/ceylon/ceylon/issues/6182
        var $lang=navigator.languages && navigator.languages[0];
        if(!$lang) {
            $lang = navigator.userLanguage || navigator.browserLanguage || navigator.language;
        }
        properties["user.locale"]=$_String($lang);
    }
    if (navigator.appVersion !== undefined) {
        properties["browser.version"] = $_String(navigator.appVersion);
    }
}
if (run$isNode()) {
    if (process.platform !== undefined) {
        properties["os.name"] = $_String(process.platform);
    }
    if (process.arch !== undefined) {
        properties["os.arch"] = $_String(process.arch);
    }
    if (process.versions !== undefined && process.versions.node != undefined) {
        properties["node.version"] = $_String(process.versions.node);
    }
    nodeprocess_ = process;
}
if (typeof document !== "undefined") {
    if (document.defaultCharset !== undefined) {
        properties["file.encoding"] = $_String(document.defaultCharset);
    }
}

var linesep = '\n';
var filesep = '/';
var pathsep = ':';
var osname = properties["os.name"];
if ((osname !== undefined) && (osname.search(/win/i) >= 0 && osname.search(/darwin/i)<0)) {
    linesep = "\r\n";
    filesep = '\\';
    pathsep = ';';
}
properties["line.separator"] = linesep;
properties["file.separator"] = filesep;
properties["path.separator"] = pathsep;

processargv_ = empty();
if ((typeof process !== "undefined") && (process.argv !== undefined)) {
    // parse command line arguments
    if (process.argv.length > 2) {
        // Ignore the first two arguments (see https://github.com/ceylon/ceylon.language/issues/503)
        var args = process.argv.slice(2);
        for (var i=0; i<args.length; ++i) {
            var arg = args[i];
            args[i] = $_String(args[i]);
        }
        processargv_ = args.length===0?empty():ArraySequence(args, {Element$ArraySequence:{t:$_String}});
    }
} else if (typeof window !== "undefined") {
    // parse URL parameters
    var parts = window.location.search.substr(1).replace('+', ' ').split('&');
    if ((parts.length > 1) || ((parts.length > 0) && (parts[0].length > 0))) {
        var argStrings = new Array(parts.length);
        //can't do "for (i in parts)" anymore because of the added stuff to arrays
        var i;
        for (i=0; i<parts.length; i++) { argStrings[i] = decodeURIComponent(parts[i]); }
        processargv_ = parts.length===0?empty():ArraySequence(argStrings, {Element$ArraySequence:{t:$_String}});
    }
}

function _process_pick_writeLine() {
  if (run$isNode() && (process.stdout !== undefined)) {
    return function(line) {
      if(line)this.write(line.valueOf());
      this.write(linesep.valueOf());
    }
  } else if ((typeof console !== "undefined") && (console.log !== undefined)) {
    return function(line) {
        console.log(line?line.valueOf():'');
    }
  }
  return function(){};
}

function _process_pick_writeErrorLine() {
  if (run$isNode() && (process.stderr !== undefined)) {
    return function(line) {
      if(line)this.writeError(line.valueOf());
      this.writeError(linesep.valueOf());
    }
  } else if ((typeof console !== "undefined") && (console.error !== undefined)) {
    return function(line) {
        console.error(line?line.valueOf():'');
    }
  }
  return function(x){this.writeLine(x);}
}

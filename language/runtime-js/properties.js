//Some native stuff for process, runtime, operatingSystem, language
_properties = {};
if (run$isBrowser()) {
    if (navigator.language !== undefined) {
        _properties["user.language"] = navigator.language;
    }
    if (navigator.platform !== undefined) {
        _properties["os.name"] = navigator.platform;
    }
    if (navigator.languages||navigator.userLanguage||navigator.browserLanguage||navigator.language) {
        // work around a cordova bug https://github.com/ceylon/ceylon/issues/6182
        var $lang=navigator.languages && navigator.languages[0];
        if(!$lang) {
            $lang = navigator.userLanguage || navigator.browserLanguage || navigator.language;
        }
        _properties["user.locale"] = $lang;
    }
    if (navigator.appVersion !== undefined) {
        _properties["browser.version"] = navigator.appVersion;
    }
}
if (run$isNode()) {
    if (process.platform !== undefined) {
        _properties["os.name"] = process.platform;
    }
    if (process.arch !== undefined) {
        _properties["os.arch"] = process.arch;
    }
    if (process.versions !== undefined && process.versions.node != undefined) {
        _properties["node.version"] = process.versions.node;
    }
    _process = process;
}
if (typeof document !== "undefined") {
    if (document.defaultCharset !== undefined) {
        _properties["file.encoding"] = document.defaultCharset;
    }
}

var linesep = '\n';
var filesep = '/';
var pathsep = ':';
var osname = _properties["os.name"];
if (osname !== undefined 
		&& osname.search(/win/i) >= 0 
		&& osname.search(/darwin/i)<0) {
    linesep = "\r\n";
    filesep = '\\';
    pathsep = ';';
}
_properties["line.separator"] = linesep;
_properties["file.separator"] = filesep;
_properties["path.separator"] = pathsep;

_argv = [];
_namedArgs = {};
if (run$isNode()) {
    // parse command line arguments
    if (process.argv !== undefined && process.argv.length > 2) {
        // Ignore the first two arguments 
    	    //(see https://github.com/ceylon/ceylon.language/issues/503)
        _argv = process.argv.slice(2);
        for (var i=0; i<_argv.length; ++i) {
            var arg = _argv[i];
            if (arg.charAt(0) == '-') {
                var pos = 1;
                if (arg.charAt(1) == '-') { pos = 2; }
                arg = arg.substr(pos);
                pos = arg.indexOf('=');
                if (pos >= 0) {
                    _namedArgs[arg.substr(0, pos)] = arg.substr(pos+1);
                } else {
                    var value = _argv[i+1];
                    if (value !== undefined && value.charAt(0) != '-') {
                        _namedArgs[arg] = value;
                        ++i;
                    } else {
                        _namedArgs[arg] = null;
                    }
                }
            }
        }
    }
}
if (run$isBrowser()) {
    // parse URL parameters
    var parts = window.location.search.substr(1).replace('+', ' ').split('&');
    if (parts.length > 1 || parts.length > 0 && parts[0].length > 0) {
        //can't do "for (i in parts)" anymore because of the added stuff to arrays
        for (var i=0; i < parts.length; i++) {
            var part = parts[i];
        	    _argv[i] = part;
            var pos = part.indexOf('=');
            if (pos >= 0) {
                var value = decodeURIComponent(part.substr(pos+1));
                _namedArgs[part.substr(0, pos)] = value;
            } else {
                _namedArgs[part] = null;
            }
        }
    }
}


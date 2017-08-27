//Some native stuff for process, runtime, operatingSystem, language

vmType = "unknown";

if (typeof process !== "undefined") {
	nodeProcess = process;
	vmType = "node";
}

if (typeof navigator !== "undefined" 
	&& typeof window !== "undefined") {
	vmType = "browser";
	browser = {};
	
    if (navigator.languages
    		|| navigator.userLanguage
    		|| navigator.browserLanguage
    		|| navigator.language) {
        // work around a cordova bug 
    	    // https://github.com/ceylon/ceylon/issues/6182
    		browser.locale 
    				= navigator.languages 
    		       && navigator.languages[0];
    		if (!browser.locale) {
    			browser.locale 
    					= navigator.userLanguage 
    			       || navigator.browserLanguage 
    			       || navigator.language;
        }
    }
    
}

//workaround for https://github.com/ceylon/ceylon/issues/7232
if (typeof console == "undefined") {
	console = {};
}


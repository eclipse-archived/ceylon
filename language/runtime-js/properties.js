//Some native stuff for process, runtime, operatingSystem, language

_process = process;

if (typeof navigator !== "undefined" 
	&& typeof window !== "undefined") {
	
    if (navigator.languages
    		|| navigator.userLanguage
    		|| navigator.browserLanguage
    		|| navigator.language) {
        // work around a cordova bug 
    	    // https://github.com/ceylon/ceylon/issues/6182
    		_locale = navigator.languages 
    		       && navigator.languages[0];
    		if (!_locale) {
    			_locale = navigator.userLanguage 
    			       || navigator.browserLanguage 
    			       || navigator.language;
        }
    }
    
}


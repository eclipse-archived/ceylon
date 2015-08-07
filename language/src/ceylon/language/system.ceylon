import java.lang {
    System { currentTimeMillis, nanoTime }
}
import java.util {
    TimeZone { defaultTimeZone=default },
    Locale { defaultLocale=default }
}
import java.nio.charset {
    Charset { defaultCharset }
}

"Represents the system on which the current process is 
 executing.
 
 Holds information about system time and locale."
see (`value process`, `value runtime`, `value language`,
     `value operatingSystem`)
shared native object system {
    
    "The elapsed time in milliseconds since midnight, 
     1 January 1970."
    shared native Integer milliseconds;
    
    "The elapsed time in nanoseconds since an arbitrary 
     starting point."
    shared native Integer nanoseconds;
    
    "Returns the offset, in milliseconds, to add to UTC to 
     get the local time for default timezone for this system."
    shared native Integer timezoneOffset;
    
    "Returns the IETF language tag representing the default 
     locale for this system."
    shared native String locale;
    
    "Returns the IANA character set name representing the default 
     character encoding for this system."
    shared native String characterEncoding;
    
    string => "system";
}

shared native("jvm") object system {
    
    shared native("jvm") Integer milliseconds =>
            currentTimeMillis();
    
    shared native("jvm") Integer nanoseconds =>
            nanoTime();
    
    shared native("jvm") Integer timezoneOffset =>
            defaultTimeZone.getOffset(milliseconds);
    
    shared native("jvm") String locale =>
            defaultLocale.toLanguageTag();
    
    shared native("jvm") String characterEncoding =>
            defaultCharset().name();
    
}

shared native("js") object system {
    
    shared native("js") Integer milliseconds {
        dynamic {
            return \iDate.now();
        }
    }
    
    shared native("js") Integer nanoseconds {
        dynamic {
            return \iDate.now() * 1000000;
        }
    }
    
    shared native("js") Integer timezoneOffset {
        dynamic {
            return Date().getTimezoneOffset() * -60000;
        }
    }
    
    shared native("js") String locale {
        return process.propertyValue("user.locale")
            else normalizeLocaleTag(process.environmentVariableValue("LANG"))
            else "en";
    }
    
    String? normalizeLocaleTag(String? tag) {
        if (exists tag, !tag.empty) {
            Integer? p = tag.firstOccurrence('.');
            String t = if (exists p) then tag[0:p] else tag;
            return t.replace("_", "-");
        } else {
            return null;
        }
    }
    
    shared native("js") String characterEncoding =>
            "UTF-16"; //JavaScript always uses UTF-16
    
}

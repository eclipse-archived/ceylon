/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Represents the system on which the current process is 
 executing.
 
 Holds information about system time and locale."
see (value process, value runtime, value language,
     value operatingSystem)
tagged("Environment")
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
    
    "Returns the IANA character set name representing the 
     default character encoding for this system."
    since("1.1.0")
    shared native String characterEncoding;
    
    "Encode the given string as a byte array using the given
     [[encoding|characterEncoding]]. The `UTF-8` and `UTF-16`
     character encodings are supported on all platforms. 
     Additional platform-specific character encodings may also
     be supported."
    shared native Array<Byte> encode(String string,
        "The character encoding to use, defaulting to the
         [[default character encoding|characterEncoding]] 
         for this system."
        String encoding = characterEncoding);
    
    string => "system";
}

shared native("jvm") object system {
    
    import java.lang {
        System,
        Str=String
    }
    import java.util {
        TimeZone,
        Locale
    }
    import java.nio.charset {
        Charset {
            defaultCharset
        }
    }
    
    shared native("jvm") Integer milliseconds 
            => System.currentTimeMillis();
    
    shared native("jvm") Integer nanoseconds 
            => System.nanoTime();
    
    shared native("jvm") Integer timezoneOffset 
            => TimeZone.default.getOffset(milliseconds);
    
    shared native("jvm") String locale 
            => Locale.default.toLanguageTag();
    
    shared native("jvm") String characterEncoding 
            => defaultCharset().name();
    
    shared native("jvm") Array<Byte> encode(String string, 
        String encoding)
            => Array<Byte> { *Str(string).getBytes(encoding) };
    
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
            value p = tag.firstOccurrence('.');
            String t = if (exists p) then tag[0:p] else tag;
            return t.replace("_", "-");
        } else {
            return null;
        }
    }
    
    shared native("js") String characterEncoding 
            //TODO: would it be better to change this to UTF-8?
            => "UTF-16";
    
    shared native("js") Array<Byte> encode(String string,
        String encoding) {
        dynamic {
            dynamic bytes;
            switch (encoding)
            case ("UTF-8") {
                bytes = stringToUtf8(string);
            }
            case ("UTF-16") {
                bytes = stringToUtf16(string);
            }
            else {
                throw Exception("unknown character encoding: "
                    + encoding);
            }
            value array = Array.ofSize(bytes.length, 0.byte);
            for (i in 0:bytes.length) {
                array[i] = Byte(bytes[i]);
            }
            return array;
        }
    }
    
}

"Represents failure of an attempt to parse a string
 representation."
since("1.3.1")
see(`function Integer.parse`, 
    `function Float.parse`, 
    `function Boolean.parse`)
shared class ParseException(String message)
    extends Exception(message) {}
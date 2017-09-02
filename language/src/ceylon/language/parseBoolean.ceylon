"The `Boolean` value of the given string representation of a 
 boolean value, or `null` if the string does not represent a 
 boolean value.
 
 Recognized values are `\"true\"`, `\"false\"`."
tagged("Basic types")
see (function Boolean.parse)
shared Boolean? parseBoolean(String? string) 
        => if (exists string,
               is Boolean result
                   = parseBooleanInternal(string))
        then result
        else null;

Boolean|ParseException parseBooleanInternal(String string)
        => switch (string)
        case ("true") true
        case ("false") false
        else ParseException("illegal format for Boolean");
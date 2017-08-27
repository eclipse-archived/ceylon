"The `Boolean` value of the given string representation of a 
 boolean value, or `null` if the string does not represent a 
 boolean value.
 
 Recognized values are `\"true\"`, `\"false\"`."
tagged("Basic types")
see (function Boolean.parse)
shared Boolean? parseBoolean(String? string) 
        => if (exists string,
               is Boolean result
                   = Boolean.parse(string))
        then result
        else null;

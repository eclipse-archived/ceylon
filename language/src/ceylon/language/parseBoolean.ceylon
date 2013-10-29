"The `Boolean` value of the given string representation of a boolean value,
 or `null` if the string does not represent a boolean value.
 
 Recognized values are \"true\", \"false\"."
shared Boolean? parseBoolean(String string) {
    switch (string)
    case ("true") { return true; }
    case ("false") { return false; }
    else { return null; }
}

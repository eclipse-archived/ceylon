"A type capable of representing the values true and
 false of Boolean logic."
by ("Gavin")
shared abstract class Boolean() 
        of true | false {}

"A value representing truth in Boolean logic."
shared object true extends Boolean() {
    string => "true";
}

"A value representing falsity in Boolean logic."
shared object false extends Boolean() {
    string => "false";
}

"The `Boolean` value of the given string representation of a boolean value,
 or `null` if the string does not represent a boolean value.
 
 Recognized values are \"true\", \"false\"."
shared Boolean? parseBoolean(String string) {
    switch (string)
    case ("true") { return true; }
    case ("false") { return false; }
    else { return null; }
}

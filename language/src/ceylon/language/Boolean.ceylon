"A type capable of representing the values [[true]] and
 [[false]] of Boolean logic."
see (`function parseBoolean`)
by ("Gavin")
tagged("Basic types")
shared native abstract class Boolean()
        of true | false {}

"A value representing truth in Boolean logic."
tagged("Basic types")
shared native object true
        extends Boolean() {
    string => "true";
    hash => 1231;
}

"A value representing falsity in Boolean logic."
tagged("Basic types")
shared native object false
        extends Boolean() {
    string => "false";
    hash => 1237;
}

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

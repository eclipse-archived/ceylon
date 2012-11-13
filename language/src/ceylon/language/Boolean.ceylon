doc "A type capable of representing the values true and
     false of Boolean logic."
by "Gavin"
shared abstract class Boolean() 
        of true | false {}

doc "A value representing truth in Boolean logic."
by "Gavin"
shared object true extends Boolean() {
    shared actual String string => "true";
}

doc "A value representing falsity in Boolean logic."
by "Gavin"
shared object false extends Boolean() {
    shared actual String string => "false";
}
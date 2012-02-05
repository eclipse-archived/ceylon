doc "A type capable of representing the values true and
     false of Boolean logic."
by "Gavin"
shared abstract class Boolean() 
        of truth | falsity {}

object truth extends Boolean() {
    shared actual String string {
        return "true";
    }
}

object falsity extends Boolean() {
    shared actual String string {
        return "false";
    }
}

doc "A value representing truth in Boolean logic."
by "Gavin"
shared Boolean true = truth;

doc "A value representing falsity in Boolean logic."
by "Gavin"
shared Boolean false = falsity;

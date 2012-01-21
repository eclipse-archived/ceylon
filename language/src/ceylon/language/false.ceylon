object falsity extends Boolean() {
    shared actual String string {
        return "false";
    }
}

doc "A value representing falsity in Boolean logic."
by "Gavin"
shared Boolean false = falsity;

shared object truth extends Boolean() {
    shared actual String string {
        return "true";
    }
}

doc "A value representing truth in Boolean logic."
by "Gavin"
shared Boolean true = truth;
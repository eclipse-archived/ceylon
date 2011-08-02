@nomodel
class SatisfiesErasure() satisfies Equality {
    shared actual Boolean equals(Equality that) {
        return false;
    }
    shared actual Integer hash {
        return +0;
    }
    shared actual String string {
        return "An SatisfiesErasure object";
    }
}
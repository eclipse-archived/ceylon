@nomodel
class SatisfiesErasure() satisfies Equality {
    shared actual Boolean equals(Equality that) {
        return false;
    }
}
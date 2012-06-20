class CloneableImpl() satisfies Cloneable<CloneableImpl> {
    shared actual CloneableImpl clone {
        return bottom;
    }
}
doc "Represents an empty sequence."
shared object none satisfies Bottom[] {

    shared actual Natural? lastIndex = null;
    
    shared actual Bottom? value(Natural index) {
        Bottom? nullValue = null;
        return nullValue;
    }
    
}
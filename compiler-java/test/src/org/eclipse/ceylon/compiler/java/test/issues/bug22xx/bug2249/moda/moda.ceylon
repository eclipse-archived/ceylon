shared interface InterfaceWithInitializedAttribute {
    shared variable formal String? internalValue;
    shared default String attribute {
        if (exists a=internalValue) {
            return a;
        } else {
            internalValue = initialValue();
            assert(exists n=internalValue);
            return n;
        }
    }
    assign attribute {
        internalValue = attribute;
    }
    shared formal String initialValue();
}

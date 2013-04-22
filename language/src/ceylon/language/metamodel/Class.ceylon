shared interface Class 
        satisfies ClassOrInterface & Declaration {
    shared formal actual AppliedClassType<Anything, Anything[]> apply(AppliedProducedType* types);
}
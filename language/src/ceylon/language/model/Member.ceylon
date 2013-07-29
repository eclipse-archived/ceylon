shared interface Member<in Type, out Kind> 
        satisfies Kind(Type)
        given Kind satisfies Model {
    
    shared formal ClassOrInterface<Anything> declaringClassOrInterface;
}
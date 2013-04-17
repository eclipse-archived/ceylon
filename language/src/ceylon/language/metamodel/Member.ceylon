shared interface Member<Type,Kind> 
        satisfies Kind(Type)
        given Kind satisfies Declaration {
    
    shared formal ClassOrInterface<Type> declaringClassOrInterface;
}
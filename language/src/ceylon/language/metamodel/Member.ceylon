shared interface Member<Type, Kind> 
        satisfies Kind(Type)
        given Kind satisfies DeclarationType {
    
//    shared formal ClassOrInterface<Type> declaringClassOrInterface;
}
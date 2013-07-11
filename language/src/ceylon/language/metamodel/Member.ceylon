shared interface Member<in Type, out Kind> 
        satisfies Kind(Type)
        given Kind satisfies DeclarationType {
    
//    shared formal ClassOrInterface<Type> declaringClassOrInterface;
}
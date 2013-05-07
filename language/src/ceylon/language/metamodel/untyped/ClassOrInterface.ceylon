import ceylon.language.metamodel {
    AppliedClassOrInterface = ClassOrInterface,
    AppliedType
}

shared interface ClassOrInterface 
        of Class | Interface 
        satisfies Declaration & Parameterised {
    
    shared formal ParameterisedType<Class>? superclass;
    
    shared formal ParameterisedType<Interface>[] interfaces;
    
    shared formal Member<Kind>[] members<Kind>() 
            given Kind satisfies Declaration;
    
    shared formal Member<Kind>[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies Declaration;
    
    shared formal AppliedClassOrInterface<Anything> apply(AppliedType* types);
}
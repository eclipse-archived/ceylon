import ceylon.language.metamodel {
    AppliedClassOrInterface = ClassOrInterface,
    AppliedMember = Member,
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
    
    shared formal AppliedClassOrInterface<Anything> bindAndApply(Object instance, AppliedType* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(AppliedType* types)
        given Kind satisfies AppliedClassOrInterface<Anything>;

}
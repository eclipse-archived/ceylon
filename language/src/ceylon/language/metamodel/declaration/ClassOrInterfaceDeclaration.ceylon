import ceylon.language.metamodel {
    AppliedClassOrInterface = ClassOrInterface,
    AppliedMember = Member,
    AppliedType
}

shared interface ClassOrInterfaceDeclaration 
        of ClassDeclaration | InterfaceDeclaration 
        satisfies Declaration & Parameterised {
    
    shared formal OpenParameterisedType<ClassDeclaration>? superclass;
    
    shared formal OpenParameterisedType<InterfaceDeclaration>[] interfaces;
    
    shared formal Kind[] members<Kind>() 
            given Kind satisfies Declaration;
    
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies Declaration;
    
    shared formal AppliedClassOrInterface<Anything> apply(AppliedType* types);
    
    shared formal AppliedClassOrInterface<Anything> bindAndApply(Object instance, AppliedType* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(AppliedType* types)
        given Kind satisfies AppliedClassOrInterface<Anything>;

}
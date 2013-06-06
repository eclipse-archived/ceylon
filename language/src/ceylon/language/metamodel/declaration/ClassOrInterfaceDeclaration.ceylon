import ceylon.language.metamodel {
    AppliedClassOrInterface = ClassOrInterface,
    AppliedMember = Member,
    Type
}

shared interface ClassOrInterfaceDeclaration 
        of ClassDeclaration | InterfaceDeclaration 
        satisfies TopLevelOrMemberDeclaration & GenericDeclaration {
    
    shared formal OpenParameterisedType<ClassDeclaration>? superclass;
    
    shared formal OpenParameterisedType<InterfaceDeclaration>[] interfaces;
    
    shared formal Kind[] members<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    "Looks up a member of this package by name and type."
    shared formal Kind? getMember<Kind>(String name) 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    shared formal AppliedClassOrInterface<Anything> apply(Type* types);
    
    shared formal AppliedClassOrInterface<Anything> bindAndApply(Object instance, Type* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(Type* types)
        given Kind satisfies AppliedClassOrInterface<Anything>;

}
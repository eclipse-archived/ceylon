import ceylon.language.metamodel {
    AppliedClassOrInterface = ClassOrInterface,
    AppliedMember = Member,
    Type
}

shared interface ClassOrInterfaceDeclaration 
        of ClassDeclaration | InterfaceDeclaration 
        satisfies TopLevelOrMemberDeclaration & GenericDeclaration {
    
    // FIXME: names are not great and not in line with OpenParameterisedType, perhaps declarationSuperClassType or superClassOpenType?
    // or extendedOpenType?
    shared formal OpenParameterisedType<ClassDeclaration>? superclassDeclaration;
    
    shared formal OpenParameterisedType<InterfaceDeclaration>[] interfaceDeclarations;
    
    // FIXME: should Kind default to TopLevelOrMemberDeclaration?
    shared formal Kind[] memberDeclarations<Kind>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    shared formal Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    "Looks up a member of this package by name and type."
    shared formal Kind? getMemberDeclaration<Kind>(String name) 
            given Kind satisfies TopLevelOrMemberDeclaration;
    
    shared formal AppliedClassOrInterface<Anything> apply(Type* types);
    
    shared formal AppliedClassOrInterface<Anything> bindAndApply(Object instance, Type* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(Type* types)
        given Kind satisfies AppliedClassOrInterface<Anything>;

}
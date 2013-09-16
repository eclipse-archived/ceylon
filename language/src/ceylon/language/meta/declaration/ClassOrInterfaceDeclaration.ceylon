import ceylon.language.meta.model {
    ClassOrInterface,
    Member,
    AppliedType = Type
}

shared interface ClassOrInterfaceDeclaration 
        of ClassDeclaration | InterfaceDeclaration 
        satisfies NestableDeclaration & GenericDeclaration {
    
    shared formal OpenClassType? extendedType;
    
    shared formal OpenInterfaceType[] satisfiedTypes;
    
    shared formal OpenType[] caseTypes;
    
    shared formal Boolean isAlias;
    
    // FIXME: should Kind default to NestableDeclaration?
    shared formal Kind[] memberDeclarations<Kind>() 
            given Kind satisfies NestableDeclaration;
    
    shared formal Kind[] annotatedMemberDeclarations<Kind, Annotation>() 
            given Kind satisfies NestableDeclaration;
    
    "Looks up a member of this package by name and type."
    shared formal Kind? getMemberDeclaration<Kind>(String name) 
            given Kind satisfies NestableDeclaration;
    
    shared formal ClassOrInterface<Type> apply<Type>(AppliedType<Anything>* typeArguments);
    
    shared formal Member<Container, ClassOrInterface<Type>> & ClassOrInterface<Type> 
        memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType, AppliedType<Anything>* typeArguments);

}
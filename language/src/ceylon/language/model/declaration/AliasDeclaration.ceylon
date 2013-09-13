import ceylon.language.model {
    Member,
    Model,
    AppliedType = Type
}

shared interface AliasDeclaration 
    satisfies NestableDeclaration & GenericDeclaration {

    shared formal OpenType extendedType;

    /*
    FIXME: this is too shaky WRT member types, we'll figure it out later    
    shared formal AppliedType<Type> apply<Type=Anything>(AppliedType<Anything>* typeArguments);
    
    shared formal AppliedType<Type> & Member<Container, AppliedType<Type> & Model> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType, AppliedType<Anything>* typeArguments);
*/
}

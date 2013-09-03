import ceylon.language.model {
    Type
}

shared interface AliasDeclaration 
    satisfies TopLevelOrMemberDeclaration & GenericDeclaration {
    
    shared formal Type<Anything> apply(Type<Anything>* types);
    
    shared formal Type<Anything> bindAndApply(Object instance, Type<Anything>* types);
}

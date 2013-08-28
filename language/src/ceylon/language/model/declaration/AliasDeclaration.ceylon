import ceylon.language.model {
    Type
}

shared interface AliasDeclaration satisfies TopLevelOrMemberDeclaration {
    
    shared formal Type apply(Type* types);
    
    shared formal Type bindAndApply(Object instance, Type* types);
}

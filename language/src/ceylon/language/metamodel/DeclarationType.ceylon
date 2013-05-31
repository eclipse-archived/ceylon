import ceylon.language.metamodel.declaration {
    TopLevelOrMemberDeclaration
}

// FIXME: change to DeclaredType?
shared interface DeclarationType of ClassOrInterface<Anything>
                                  | Function<Anything, Nothing> 
                                  | Attribute<Anything> {
    
    shared formal TopLevelOrMemberDeclaration declaration;
}

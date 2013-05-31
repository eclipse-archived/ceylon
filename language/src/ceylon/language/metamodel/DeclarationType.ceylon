import ceylon.language.metamodel.declaration {
    Declaration
}

// FIXME: change to DeclaredType?
shared interface DeclarationType of ClassOrInterface<Anything>
                                  | Function<Anything, Nothing> 
                                  | Attribute<Anything> {
    
    shared formal Declaration declaration;
}

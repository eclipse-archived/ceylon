import ceylon.language.metamodel.declaration {
    TopLevelOrMemberDeclaration
}

// FIXME it's not a type so let's find a better name
// FIXME: change to AppliedDeclaration or ClosedDeclaration?
shared interface DeclarationType of ClassOrInterface<Anything>
                                  | FunctionType<Anything, Nothing> 
                                  | AttributeType<Anything> {
    
    shared formal TopLevelOrMemberDeclaration declaration;
}

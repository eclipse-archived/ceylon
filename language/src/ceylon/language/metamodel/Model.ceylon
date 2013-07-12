import ceylon.language.metamodel.declaration {
    TopLevelOrMemberDeclaration
}

shared interface Model of ClassOrInterface<Anything>
                        | FunctionType<Anything, Nothing> 
                        | AttributeType<Anything> {
    
    shared formal TopLevelOrMemberDeclaration declaration;
}

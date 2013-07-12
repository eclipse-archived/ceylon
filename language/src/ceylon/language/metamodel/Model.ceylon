import ceylon.language.metamodel.declaration {
    TopLevelOrMemberDeclaration
}

shared interface Model of ClassOrInterface<Anything>
                        | FunctionModel<Anything, Nothing> 
                        | AttributeModel<Anything> {
    
    shared formal TopLevelOrMemberDeclaration declaration;
}

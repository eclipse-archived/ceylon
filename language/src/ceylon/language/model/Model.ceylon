import ceylon.language.model.declaration {
    TopLevelOrMemberDeclaration
}

shared interface Model of ClassOrInterface<Anything>
                        | FunctionModel<Anything, Nothing> 
                        | ValueModel<Anything> {
    
    shared formal TopLevelOrMemberDeclaration declaration;
}

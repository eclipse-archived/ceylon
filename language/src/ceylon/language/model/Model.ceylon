import ceylon.language.model.declaration {
    NestableDeclaration
}

shared interface Model of ClassOrInterface<Anything>
                        | FunctionModel<Anything, Nothing> 
                        | ValueModel<Anything> {
    
    shared formal ClassOrInterface<Anything>? container;
    
    shared formal NestableDeclaration declaration;
}

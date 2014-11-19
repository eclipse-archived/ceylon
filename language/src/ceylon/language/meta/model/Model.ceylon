import ceylon.language.meta.declaration {
    NestableDeclaration,
    ConstructorDeclaration
}

"The root of all models. There are several types of models:
 
 - [[ClassOrInterface]]
 - [[FunctionModel]]
 - [[ValueModel]]
 "
shared sealed interface Model of ClassOrInterface<Anything>
                        | FunctionModel<Anything, Nothing> 
                        | ValueModel<Anything> 
        satisfies Declared {
    
    "The container type of this model, or `null` if this is a toplevel model."
    shared actual formal Type<Anything>? container;
    
    "The declaration for this model."
    shared actual formal NestableDeclaration declaration;
}

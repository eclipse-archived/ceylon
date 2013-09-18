import ceylon.language.meta.declaration {
    NestableDeclaration
}

"The root of all models. There are several types of models:
 
 - [[ClassOrInterface]]
 - [[FunctionModel]]
 - [[ValueModel]]
 "
shared interface Model of ClassOrInterface<Anything>
                        | FunctionModel<Anything, Nothing> 
                        | ValueModel<Anything> {
    
    "The container type of this model, or `null` if this is a toplevel model."
    shared formal Type<Anything>? container;
    
    "The declaration for this model."
    shared formal NestableDeclaration declaration;
}

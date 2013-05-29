import ceylon.language.metamodel.declaration {
    UntypedDeclaration = Declaration
}

shared interface Declaration of ClassOrInterface<Anything>
                              | Function<Anything, Nothing> 
                              | Value<Anything> {
    
    shared formal UntypedDeclaration declaration;
}

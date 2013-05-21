import ceylon.language.metamodel.untyped {
    UntypedDeclaration = Declaration
}

shared interface Declaration of ClassOrInterface<Anything>
                              | Function<Anything, Nothing> 
                              | Value<Anything> {
    
    shared formal UntypedDeclaration declaration;
}

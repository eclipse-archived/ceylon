import ceylon.language.metamodel.declaration {
    UntypedDeclaration = Declaration
}

shared interface DeclarationType of ClassOrInterface<Anything>
                                  | Function<Anything, Nothing> 
                                  | Value<Anything> {
    
    shared formal UntypedDeclaration declaration;
}

import ceylon.language.metamodel { Value }

shared interface AttributeDeclaration
        satisfies FunctionOrAttributeDeclaration {
    
    shared formal Value<Anything> apply(Anything instance = null);
    // TODO: add memberApply?
}
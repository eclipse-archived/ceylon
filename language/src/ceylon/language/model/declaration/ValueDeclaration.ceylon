import ceylon.language.model { Value }

shared interface ValueDeclaration
        satisfies FunctionOrValueDeclaration {
    
    shared formal Value<Anything> apply(Anything instance = null);
    // TODO: add memberApply?
}
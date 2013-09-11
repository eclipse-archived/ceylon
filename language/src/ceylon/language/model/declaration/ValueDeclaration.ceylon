import ceylon.language.model { Value }

shared interface ValueDeclaration
        satisfies FunctionOrValueDeclaration {
    
    // why is this not parameterised for the return type?
    shared formal Value<Anything> apply(Anything instance = null);
    // TODO: add memberApply?
}
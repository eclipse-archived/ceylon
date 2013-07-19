import ceylon.language.metamodel { Value }

shared interface AttributeDeclaration
        satisfies TopLevelOrMemberDeclaration {
    
    shared formal Value<Anything> apply(Anything instance = null);
    // TODO: add memberApply?
}
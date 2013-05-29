import ceylon.language.metamodel { AppliedValue = Value }

shared interface AttributeDeclaration
        satisfies Declaration {
    
    shared formal AppliedValue<Anything> apply(Anything instance = null);
    
    shared formal OpenType type;
}
import ceylon.language.metamodel { AppliedValue = Value }

shared interface Value
        satisfies Declaration {
    
    shared formal AppliedValue<Anything> applied;
    
    shared formal Type type;
}
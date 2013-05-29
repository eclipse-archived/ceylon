import ceylon.language.metamodel { AppliedValue = Value }

shared interface Value
        satisfies Declaration {
    
    shared formal AppliedValue<Anything> apply(Anything instance = null);
    
    shared formal Type type;
}
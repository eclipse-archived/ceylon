import ceylon.language.metamodel { AppliedFunction = Function, AppliedType }

shared interface Function 
        satisfies Declaration & Parameterised {

    shared formal Type type;

    shared formal AppliedFunction<Anything, Nothing> apply(AppliedType* types);
}
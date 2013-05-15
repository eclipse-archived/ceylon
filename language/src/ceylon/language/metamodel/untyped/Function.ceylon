import ceylon.language.metamodel { AppliedFunction = Function, AppliedType }

shared interface Function 
        satisfies Declaration & Parameterised {

    shared formal Type type;

    shared formal Parameter[] parameters;

    shared formal Sequence<Parameter[]> parameterLists;

    shared formal AppliedFunction<Anything, Nothing> apply(AppliedType* types);
}
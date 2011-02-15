shared interface CallableDeclaration<R>
        satisfies Declaration {

    shared formal Type<R> returnType;

    shared formal Parameter<Object>[] parameters;

    shared formal R unsafeCall(Object obj);

}
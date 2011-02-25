shared interface ValueDeclaration<out T>
        satisfies Declaration {

     shared formal Boolean variable;

     shared formal Type<T> valueType;

     shared formal T unsafeGet(Object obj);

}

shared interface Value<out Type>
        satisfies AttributeType<Type> {

    shared formal Type get();
}
